// 通过 Chrome DevTools Protocol 让 Chrome 自己解密 cookie，写入 .lc/config.json
// 用法:
//   node read_cookie_cdp.js --test   # 用全新临时配置验证链路（不读取任何真实 cookie）
//   node read_cookie_cdp.js          # 读取 Chrome Default 配置里的 leetcode.cn cookie
// 前置: 读取真实 cookie 前需要 Chrome 已完全退出（否则 Cookies 文件被锁）。
// 原理: 复制 Chrome 的 cookie 库到临时配置 → 无头模式启动 Chrome（同二进制可解密 app-bound 加密）
//       → 经 CDP Network.getAllCookies 取明文 cookie → 写 config.json。cookie 值不打印到控制台。
const { spawn, execFileSync } = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');
const http = require('http');

const CHROME = 'C:/Program Files/Google/Chrome/Application/chrome.exe';
const SRC_PROFILE = path.join(process.env.LOCALAPPDATA || '', 'Google', 'Chrome', 'User Data', 'Default');
const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');
const CONFIG_PATH = path.join(LC_DIR, 'config.json');

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

function killTree(pid) {
  try { execFileSync('taskkill', ['/PID', String(pid), '/T', '/F'], { stdio: 'ignore' }); } catch {}
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

async function run(tempProfileDir) {
  const portFile = path.join(tempProfileDir, 'DevToolsActivePort');
  const chrome = spawn(CHROME, [
    '--headless=new', '--remote-debugging-port=0', `--user-data-dir=${tempProfileDir}`,
    '--no-first-run', '--no-default-browser-check', '--disable-sync', '--disable-background-networking',
    'about:blank'
  ], { stdio: 'ignore' });
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
  const tmp = fs.mkdtempSync(path.join(os.tmpdir(), 'lc-cdp-'));
  try {
    if (!isTest) {
      const srcDb = path.join(SRC_PROFILE, 'Network', 'Cookies');
      if (!fs.existsSync(srcDb)) throw new Error(`未找到 Chrome cookie 库: ${srcDb}`);
      fs.mkdirSync(path.join(tmp, 'Network'), { recursive: true });
      for (const rel of ['Network/Cookies', 'Network/Cookies-journal', 'Local State', 'Preferences']) {
        const src = path.join(SRC_PROFILE, rel);
        if (fs.existsSync(src)) fs.copyFileSync(src, path.join(tmp, rel));
      }
    }

    const cookies = await run(tmp);
    if (isTest) {
      console.log(`CDP 链路正常，共读到 ${cookies.length} 个 cookie（测试模式，未读取真实配置）`);
      return;
    }

    const session = pick(cookies, 'LEETCODE_SESSION');
    const csrf = pick(cookies, 'csrftoken');
    if (!session) {
      console.error('Chrome Default 中没有 leetcode.cn 的 LEETCODE_SESSION（可能未登录或已过期）');
      process.exitCode = 1;
      return;
    }
    const config = fs.existsSync(CONFIG_PATH) ? JSON.parse(fs.readFileSync(CONFIG_PATH, 'utf8')) : {};
    config.leetcode_session = session;
    config.csrf_token = csrf || '';
    fs.writeFileSync(CONFIG_PATH, JSON.stringify(config, null, 2), 'utf8');
    console.log(`已写入 .lc/config.json（LEETCODE_SESSION 长度=${session.length}，csrftoken 长度=${csrf ? csrf.length : 0}）`);
    if (!csrf) console.log('警告：未找到 csrftoken，提交时可能需要手动补充。');
  } catch (e) {
    console.error('失败：' + e.message);
    if (/EBUSY|locked/.test(e.message)) console.error('请先完全退出 Chrome 再运行本脚本。');
    process.exitCode = 1;
  } finally {
    setTimeout(() => { try { fs.rmSync(tmp, { recursive: true, force: true }); } catch {} }, 800);
  }
}

main();
