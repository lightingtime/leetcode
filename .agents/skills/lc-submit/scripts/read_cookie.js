// 从本机 Edge/Chrome 的登录态中读取 leetcode.cn 的 cookie，写入 .lc/config.json
// 用法: node read_cookie.js
// 原理: 复制浏览器 Cookies SQLite 与 Local State → DPAPI 解密（v10 AES-GCM）→
//       提取 LEETCODE_SESSION / csrftoken。cookie 值不会打印到控制台。
// 注意: 若浏览器新版 app-bound 加密（cookie 值 v20 前缀），本脚本无法解密，
//       需要换用浏览器内手动复制，或让用户重新登录一次。
const { DatabaseSync } = require('node:sqlite');
const fs = require('fs');
const os = require('os');
const path = require('path');
const crypto = require('crypto');
const { execFileSync } = require('child_process');

const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');
const CONFIG_PATH = path.join(LC_DIR, 'config.json');
const PW = 'C:/Program Files/PowerShell/7/pwsh.exe';

const BROWSERS = [
  { name: 'Edge', root: path.join(process.env.LOCALAPPDATA || '', 'Microsoft', 'Edge', 'User Data') },
  { name: 'Chrome', root: path.join(process.env.LOCALAPPDATA || '', 'Google', 'Chrome', 'User Data') },
];

function dpapiUnprotectBase64(b64) {
  const script =
    "Add-Type -AssemblyName System.Security.Cryptography.ProtectedData; " +
    "$b=[Convert]::FromBase64String('" + b64 + "'); " +
    "[Convert]::ToBase64String([System.Security.Cryptography.ProtectedData]::Unprotect($b, $null, 'CurrentUser'))";
  const out = execFileSync(PW, ['-NoProfile', '-Command', script], { encoding: 'utf8', timeout: 15000 });
  const lines = out.split(/\r?\n/).filter(l => l.trim());
  if (lines.length === 0) throw new Error('DPAPI 解密无输出');
  return Buffer.from(lines[lines.length - 1].trim(), 'base64');
}

function decryptCookieValue(value, key) {
  if (value.startsWith('v20')) return { ok: false, reason: 'app-bound 加密（v20），无法用 DPAPI 解密' };
  if (!value.startsWith('v10')) return { ok: true, value }; // 未加密
  const buf = Buffer.from(value, 'base64');
  const nonce = buf.subarray(3, 3 + 12);
  const tag = buf.subarray(buf.length - 16);
  const ct = buf.subarray(3 + 12, buf.length - 16);
  try {
    const d = crypto.createDecipheriv('aes-256-gcm', key, nonce);
    d.setAuthTag(tag);
    return { ok: true, value: Buffer.concat([d.update(ct), d.final()]).toString('utf8') };
  } catch (e) {
    return { ok: false, reason: '解密失败: ' + e.message };
  }
}

function profilesOf(root) {
  if (!fs.existsSync(root)) return [];
  return fs.readdirSync(root, { withFileTypes: true })
    .filter(d => d.isDirectory() && /^(Default|Profile \d+)$/.test(d.name))
    .map(d => d.name)
    .filter(name => fs.existsSync(path.join(root, name, 'Network', 'Cookies')))
    .sort((a, b) => (a === 'Default' ? -1 : a.localeCompare(b)) - (b === 'Default' ? -1 : b.localeCompare(a)));
}

function findBest(rows, key, wantName) {
  const nowUs = Date.now() * 1000;
  const candidates = rows
    .filter(r => r.name === wantName && (r.expires_utc === 0 || r.expires_utc > nowUs))
    .map(r => {
      const dec = decryptCookieValue(r.value, key);
      return { host: r.host_key, expires: r.expires_utc, dec };
    })
    .filter(x => x.dec.ok);
  if (candidates.length === 0) return null;
  candidates.sort((a, b) => b.expires - a.expires);
  return candidates[0].dec.value;
}

function main() {
  const tmp = fs.mkdtempSync(path.join(os.tmpdir(), 'lc-cookie-'));
  let found = null;
  const problems = [];

  for (const br of BROWSERS) {
    for (const profile of profilesOf(br.root)) {
      const base = path.join(br.root, profile);
      const lsPath = path.join(br.root, 'Local State'); // Local State 在浏览器根目录，不在 profile 内
      const dbPath = path.join(base, 'Network', 'Cookies');
      if (!fs.existsSync(lsPath)) { problems.push(`${br.name}/${profile}: 无 Local State`); continue; }
      let key;
      try {
        const ls = JSON.parse(fs.readFileSync(lsPath, 'utf8'));
        const ek = ls && ls.os_crypt && ls.os_crypt.encrypted_key;
        if (!ek) { problems.push(`${br.name}/${profile}: 无 os_crypt.encrypted_key`); continue; }
        const raw = Buffer.from(ek, 'base64');
        if (raw.subarray(0, 5).toString() !== 'DPAPI') throw new Error('encrypted_key 前缀不是 DPAPI');
        key = dpapiUnprotectBase64(raw.subarray(5).toString('base64'));
      } catch (e) {
        problems.push(`${br.name}/${profile}: 密钥解密失败 - ${e.message}`);
        continue;
      }
      let rows;
      try {
        fs.copyFileSync(dbPath, path.join(tmp, 'cookies.db'));
        const db = new DatabaseSync(path.join(tmp, 'cookies.db'), { readOnly: true });
        rows = db.prepare(
          "SELECT host_key, name, value, expires_utc FROM cookies WHERE name IN ('LEETCODE_SESSION','csrftoken') AND host_key LIKE '%leetcode%'"
        ).all();
        db.close();
      } catch (e) {
        problems.push(`${br.name}/${profile}: 读取 Cookies 失败 - ${e.message}`);
        continue;
      }
      const session = findBest(rows, key, 'LEETCODE_SESSION');
      const csrf = findBest(rows, key, 'csrftoken');
      if (!session && !csrf) {
        problems.push(`${br.name}/${profile}: 未找到 leetcode.cn 的登录 cookie（可能未登录或已过期）`);
        continue;
      }
      console.log(`${br.name}/${profile}: LEETCODE_SESSION 长度=${session ? session.length : 0}，csrftoken 长度=${csrf ? csrf.length : 0}`);
      if (session && !found) {
        found = { source: `${br.name}/${profile}`, session, csrf: csrf || '' };
      }
    }
  }

  if (!found) {
    console.error('未找到可用的登录 cookie。');
    problems.forEach(p => console.error('  - ' + p));
    console.error('请确认浏览器已登录 leetcode.cn；若 cookie 为 v20 加密，请改用浏览器手动复制，或重新登录后重试。');
    process.exitCode = 1;
    return;
  }

  const config = fs.existsSync(CONFIG_PATH) ? JSON.parse(fs.readFileSync(CONFIG_PATH, 'utf8')) : {};
  config.leetcode_session = found.session;
  config.csrf_token = found.csrf;
  fs.writeFileSync(CONFIG_PATH, JSON.stringify(config, null, 2), 'utf8');
  console.log(`已写入 .lc/config.json（来源：${found.source}）`);
  if (!found.csrf) console.log('警告：未找到 csrftoken，提交时可能需要手动补充。');
  fs.rmSync(tmp, { recursive: true, force: true });
}

main();
