// 通过 Chrome DevTools Protocol 让 Chrome 自己解密 cookie，写入 .lc/config.json（macOS 版）
// 用法:
//   node read_cookie_macos.js --test   # 用全新临时配置验证链路（不读取任何真实 cookie）
//   node read_cookie_macos.js          # 读取 Chrome/Edge 配置里的 leetcode.cn cookie
// 前置: 读取真实 cookie 前建议先完全退出 Chrome / Edge（否则 cookie 库可能被锁定）。
// 原理: 把浏览器 cookie 库复制到临时配置 → 无头模式启动同款 Chrome（由 Chrome 自行解密，
//       兼容新旧加密格式，不依赖钥匙串手动授权）→ 经 CDP Network.getAllCookies 取明文
//       cookie → 写 config.json。cookie 值不打印到控制台。
const { spawn } = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');
const http = require('http');

const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');
const CONFIG_PATH = path.join(LC_DIR, 'config.json');

const BROWSERS = [
  {
    name: 'Chrome',
    exe: '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
    root: path.join(os.homedir(), 'Library', 'Application Support', 'Google', 'Chrome')
  },
  {
    name: 'Edge',
    exe: '/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge',
    root: path.join(os.homedir(), 'Library', 'Application Support', 'Microsoft Edge')
  }
];

function getJson(url) {
  return new Promise((resolve, reject) => {
    http.get(url, res => {
      let d = '';
      res.on('data', c => (d += c));
      res.on('end', () => { try { resolve(JSON.parse(d)); } catch (e) { reject(e); } });
    }).on('error', reject);
  });
}

class CDP {
  constructor(ws) { this.ws = ws; this.id = 0; this.pending = new Map(); }
  static async connect(url) {
    const ws = new WebSocket(url);
    await new Promise((res, rej) => { ws.onopen = res; ws.onerror = rej; });
    const c = new CDP(ws);
    ws.onmessage = ev => {
      const msg = JSON.parse(ev.data);
      if (msg.id && c.pending.has(msg.id)) {
        const p = c.pending.get(msg.id);
        c.pending.delete(msg.id);
        msg.error ? p.reject(new Error(msg.error.message)) : p.resolve(msg.result);
      }
    };
    return c;
  }
  send(method, params = {}) {
    const id = ++this.id;
    return new Promise((resolve, reject) => {
      this.pending.set(id, { resolve, reject });
      this.ws.send(JSON.stringify({ id, method, params }));
    });
  }
  close() { try { this.ws.close(); } catch {} }
}

function waitForFile(p, ms) {
  const t0 = Date.now();
  return new Promise((resolve, reject) => {
    const tick = () => {
      if (fs.existsSync(p)) {
        try { return resolve(fs.readFileSync(p, 'utf8')); } catch {}
      }
      if (Date.now() - t0 > ms) return reject(new Error('等待 DevToolsActivePort 超时'));
      setTimeout(tick, 200);
    };
    tick();
  });
}

function sleepSync(ms) {
  Atomics.wait(new Int32Array(new SharedArrayBuffer(4)), 0, 0, ms);
}

// 结束整棵 Chrome 进程树（detached 启动，进程组号 = 主进程 pid）
function killTree(pid) {
  try { process.kill(-pid, 'SIGTERM'); } catch {}
  for (let i = 0; i < 20; i++) {
    try { process.kill(pid, 0); } catch { return; } // 主进程已退出
    sleepSync(200);
  }
  try { process.kill(-pid, 'SIGKILL'); } catch {}
}

function pick(cookies, name) {
  const now = Math.floor(Date.now() / 1000);
  const hits = cookies
    .filter(c => c.name === name && c.domain.includes('leetcode'))
    .filter(c => c.expires === -1 || c.expires > now)
    .sort((a, b) => {
      const aScore = (a.domain === 'leetcode.cn' ? 2 : 0) + Math.min(a.value.length, 500);
      const bScore = (b.domain === 'leetcode.cn' ? 2 : 0) + Math.min(b.value.length, 500);
      return bScore - aScore;
    });
  return hits.length ? hits[0].value : null;
}

function profilesOf(browser) {
  if (!fs.existsSync(browser.root)) return [];
  return fs.readdirSync(browser.root, { withFileTypes: true })
    .filter(d => d.isDirectory() && /^(Default|Profile \d+)$/.test(d.name))
    .map(d => d.name)
    .filter(name => ['Cookies', 'Network/Cookies'].some(rel => fs.existsSync(path.join(browser.root, name, rel))))
    .sort((a, b) => (a === 'Default' ? -1 : a.localeCompare(b)) - (b === 'Default' ? -1 : b.localeCompare(a)));
}

// 把 profile 里的 cookie 库与最小配置复制到临时配置，保持标准目录布局：
//   <tmp>/<ProfileName>/Cookies 等（macOS 的 cookie 库在 profile 根目录）
//   <tmp>/Local State            位于浏览器数据根目录
function prepareProfile(tmp, browser, profileName) {
  const profileDir = path.join(browser.root, profileName);
  const destProfile = path.join(tmp, profileName);
  let copied = 0;
  for (const rel of ['Cookies', 'Cookies-journal', 'Network/Cookies', 'Network/Cookies-journal', 'Preferences', 'Secure Preferences']) {
    const src = path.join(profileDir, rel);
    if (!fs.existsSync(src)) continue;
    const dest = path.join(destProfile, rel);
    fs.mkdirSync(path.dirname(dest), { recursive: true });
    fs.copyFileSync(src, dest);
    copied++;
  }
  const localState = path.join(browser.root, 'Local State');
  if (fs.existsSync(localState)) {
    fs.copyFileSync(localState, path.join(tmp, 'Local State'));
    copied++;
  }
  if (copied === 0) throw new Error('profile 中没有可复制的 cookie 库文件');
}

async function run(tempProfileDir, chromeExe) {
  const portFile = path.join(tempProfileDir, 'DevToolsActivePort');
  const chrome = spawn(chromeExe, [
    '--headless=new', '--remote-debugging-port=0', `--user-data-dir=${tempProfileDir}`,
    '--no-first-run', '--no-default-browser-check', '--disable-sync', '--disable-background-networking',
    'about:blank'
  ], { stdio: 'ignore', detached: true });
  try {
    const data = await waitForFile(portFile, 20000);
    const port = data.split(/\r?\n/)[0].trim();
    let targets = [];
    for (let i = 0; i < 30 && targets.length === 0; i++) {
      try { targets = await getJson(`http://127.0.0.1:${port}/json/list`); } catch {}
      if (targets.length === 0) await new Promise(r => setTimeout(r, 300));
    }
    const page = targets.find(t => t.type === 'page') || targets[0];
    if (!page) throw new Error('没有可用页面 target');
    const cdp = await CDP.connect(page.webSocketDebuggerUrl);
    await cdp.send('Network.enable');
    const { cookies } = await cdp.send('Network.getAllCookies');
    cdp.close();
    return cookies;
  } finally {
    killTree(chrome.pid);
  }
}

async function main() {
  const isTest = process.argv.includes('--test');
  const tmpDirs = [];
  try {
    if (isTest) {
      const browser = BROWSERS.find(b => fs.existsSync(b.exe)) || BROWSERS[0];
      const tmp = fs.mkdtempSync(path.join(os.tmpdir(), 'lc-macos-cookie-'));
      tmpDirs.push(tmp);
      const cookies = await run(tmp, browser.exe);
      console.log(`CDP 链路正常（${browser.name}），共读到 ${cookies.length} 个 cookie（测试模式，未读取真实配置）`);
      return;
    }

    let chosen = null;
    const tried = [];
    for (const browser of BROWSERS) {
      if (!fs.existsSync(browser.exe)) { tried.push(`${browser.name}: 未安装`); continue; }
      for (const profile of profilesOf(browser)) {
        const profileDir = path.join(browser.root, profile);
        const tmp = fs.mkdtempSync(path.join(os.tmpdir(), 'lc-macos-cookie-'));
        tmpDirs.push(tmp);
        try {
          prepareProfile(tmp, browser, profile);
        } catch (e) {
          tried.push(`${browser.name}/${profile}: 复制失败 - ${e.message}`);
          continue;
        }
        let cookies;
        try {
          cookies = await run(tmp, browser.exe);
        } catch (e) {
          tried.push(`${browser.name}/${profile}: CDP 读取失败 - ${e.message}`);
          continue;
        }
        const session = pick(cookies, 'LEETCODE_SESSION');
        const csrf = pick(cookies, 'csrftoken');
        if (session) {
          chosen = { browser: browser.name, profile, session, csrf };
          break;
        }
        tried.push(`${browser.name}/${profile}: 无 leetcode.cn 登录 cookie（共读到 ${cookies.length} 个 cookie）`);
      }
      if (chosen) break;
    }

    if (!chosen) {
      console.error('未找到可用的登录 cookie。');
      tried.forEach(t => console.error('  - ' + t));
      console.error('请先在浏览器登录 leetcode.cn，退出浏览器后重试。');
      process.exitCode = 1;
      return;
    }

    const config = fs.existsSync(CONFIG_PATH) ? JSON.parse(fs.readFileSync(CONFIG_PATH, 'utf8')) : {};
    config.leetcode_session = chosen.session;
    config.csrf_token = chosen.csrf || '';
    fs.writeFileSync(CONFIG_PATH, JSON.stringify(config, null, 2), 'utf8');
    console.log(`已写入 .lc/config.json（来源：${chosen.browser}/${chosen.profile}，LEETCODE_SESSION 长度=${chosen.session.length}，csrftoken 长度=${chosen.csrf ? chosen.csrf.length : 0}）`);
    if (!chosen.csrf) console.log('警告：未找到 csrftoken，提交时可能需要手动补充。');
  } catch (e) {
    console.error('失败：' + e.message);
    if (/EBUSY|locked/.test(e.message)) console.error('请先完全退出 Chrome / Edge 再运行本脚本。');
    process.exitCode = 1;
  } finally {
    for (const d of tmpDirs) {
      try { fs.rmSync(d, { recursive: true, force: true }); } catch {}
    }
  }
}

main();
