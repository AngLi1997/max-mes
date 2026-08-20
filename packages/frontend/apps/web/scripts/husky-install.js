/**
 * husky 安装脚本（prepare 生命周期调用）。
 *
 * 背景：husky v8 的 install 只检查「当前工作目录」下的 .git，
 * 不会向上递归查找。本仓库的 .git 在 monorepo 根
 * （D:\MES\bmos-monorepo），而前端 workspace 在其下数层，
 * 直接 `husky install` 必然报 `.git can't be found`。
 *
 * 做法：用 `git rev-parse --show-toplevel` 自动定位 git 仓库根，
 * 在其下执行 husky install，并把 .husky 安装到前端 workspace 内
 * （packages/frontend/apps/web/.husky）。这样既能让 git 通过
 * core.hooksPath 找到 hooks，又让 hook 配置与前端代码同处一目录。
 */
const { execSync } = require('child_process');

// .husky 相对 git 仓库根的安装位置（保持在前端 workspace 内）
const HUSKY_DIR = 'packages/frontend/apps/web/.husky';

// 1. 定位 git 根；非 git 工作区（如 docker 构建上下文）则安全跳过，不阻断 pnpm install
let gitRoot;
try {
  gitRoot = execSync('git rev-parse --show-toplevel', { encoding: 'utf8' }).trim();
} catch (e) {
  console.warn('husky - skip install (not a git worktree)');
  process.exit(0);
}

// 2. 在 git 根执行 husky install（husky v8 只检查 cwd 下的 .git）
//    若 install 内部出错则正常抛出，不被吞掉，便于发现问题
execSync(`husky install ${HUSKY_DIR}`, { stdio: 'inherit', cwd: gitRoot });
