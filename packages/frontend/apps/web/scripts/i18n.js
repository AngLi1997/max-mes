const fs = require('node:fs');
const path = require('node:path');
const readline = require('node:readline');

// 配置项
const CONFIG = {
  serverUrl: process.argv.find(arg => arg.startsWith('--serverUrl='))?.split('=')[1] || 'http://172.30.1.160:8848',
  excludes: ['node_modules', 'dist', '.git', 'bmos-bims-web', 'bmos-bsms-web', 'bmos-lisms-web'],
  nacos: {
    username: 'nacos',
    password: 'nacos',
    dataId: 'bmos-web_zh_CN.json',
    group: 'frontend-i18n'
  }
};

// 优化后的正则表达式
const tFunctionRegex = /t\((['"`])([^'"`\u4E00-\u9FA5]*[\u4E00-\u9FA5][^'"`]*)\1\)/g;

// 进度显示类
class ProgressBar {
  constructor(total) {
    this.total = total;
    this.current = 0;
  }

  update() {
    this.current++;
    const percentage = Math.floor((this.current / this.total) * 100);
    readline.clearLine(process.stdout, 0);
    readline.cursorTo(process.stdout, 0);
    process.stdout.write(`中文字符提取中... ${percentage}% (${this.current}/${this.total})`);
  }
}

// 文件操作工具类
class FileUtils {
  static async readFile(filePath) {
    try {
      return await fs.promises.readFile(filePath, 'utf-8');
    } catch (error) {
      console.error(`读取文件失败: ${filePath}`, error);
      return '';
    }
  }

  static async writeFile(filePath, content) {
    try {
      await fs.promises.writeFile(filePath, content, 'utf-8');
    } catch (error) {
      console.error(`写入文件失败: ${filePath}`, error);
      throw error;
    }
  }
}

// Nacos API 工具类
class NacosAPI {
  static async login() {
    try {
      const response = await fetch(`${CONFIG.serverUrl}/nacos/v1/auth/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          username: CONFIG.nacos.username,
          password: CONFIG.nacos.password
        })
      });
      const data = await response.json();
      return data.accessToken;
    } catch (error) {
      console.error('Nacos登录失败:', error);
      throw error;
    }
  }

  static async getConfig(accessToken) {
    try {
      const params = new URLSearchParams({
        dataId: CONFIG.nacos.dataId,
        group: CONFIG.nacos.group,
        show: 'all',
        username: CONFIG.nacos.username,
        accessToken
      });

      const response = await fetch(`${CONFIG.serverUrl}/nacos/v1/cs/configs?${params.toString()}`);
      const data = await response.json();
      return JSON.parse(data.content);
    } catch (error) {
      console.error('获取配置失败:', error);
      throw error;
    }
  }
}

// 中文字符提取类
class ChineseExtractor {
  constructor() {
    this.chineseDict = {};
    this.progressBar = null;
  }

  async countTotalFiles(dirPath) {
    let count = 0;
    const files = await fs.promises.readdir(dirPath);

    for (const file of files) {
      const fullPath = path.join(dirPath, file);
      const stats = await fs.promises.stat(fullPath);

      if (stats.isDirectory() && !CONFIG.excludes.includes(file)) {
        count += await this.countTotalFiles(fullPath);
      } else if (stats.isFile()) {
        count++;
      }
    }

    return count;
  }

  async traverseDirectory(dirPath) {
    const files = await fs.promises.readdir(dirPath);

    for (const file of files) {
      const fullPath = path.join(dirPath, file);
      const stats = await fs.promises.stat(fullPath);

      if (stats.isDirectory() && !CONFIG.excludes.includes(file)) {
        await this.traverseDirectory(fullPath);
      } else if (stats.isFile()) {
        const content = await FileUtils.readFile(fullPath);
        let matches;

        while ((matches = tFunctionRegex.exec(content)) !== null) {
          const chinese = matches[2];
          this.chineseDict[chinese] = chinese;
        }

        this.progressBar.update();
      }
    }
  }

  async saveToNacos() {
    try {
      const accessToken = await NacosAPI.login();
      const existingContent = await NacosAPI.getConfig(accessToken);

      const newContent = {};
      for (const key in this.chineseDict) {
        if (!existingContent[key]) {
          existingContent[key] = key;
          newContent[key] = key;
        }
      }

      await FileUtils.writeFile(
        'scripts/updated-chinese-strings.json',
        JSON.stringify(existingContent, null, 2)
      );

      await FileUtils.writeFile(
        'scripts/new-chinese-strings.json',
        JSON.stringify(newContent, null, 2)
      );

      console.log('\n中文字符提取完成，已保存到指定文件');
    } catch (error) {
      console.error('保存到Nacos失败:', error);
      throw error;
    }
  }
}

// 主函数
async function main() {
  try {
    console.log('中文字符提取中...');
    const extractor = new ChineseExtractor();

    console.log('正在计算文件总数...');
    const totalFiles = await extractor.countTotalFiles('./');
    console.log(`总共找到 ${totalFiles} 个文件`);

    extractor.progressBar = new ProgressBar(totalFiles);
    console.log('中文字符提取中...');

    await extractor.traverseDirectory('./');
    console.log('\n提取完成，正在保存...');

    await extractor.saveToNacos();
  } catch (error) {
    console.error('程序执行失败:', error);
    process.exit(1);
  }
}

main();
