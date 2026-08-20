import { readdirSync, readFileSync, statSync, writeFileSync } from 'node:fs';
import { join } from 'node:path';

const tFunctionRegex = /t\((['"`])([^'"`\u4E00-\u9FA5]*[\u4E00-\u9FA5][^'"`]*)\1\)/g;
const chineseDict = {};
const excludes = ['node_modules', 'dist', '.git', 'static'];

function traverseDirectory(dirPath) {
  const files = readdirSync(dirPath);

  files.forEach((file) => {
    const fullPath = join(dirPath, file);
    const stats = statSync(fullPath);
    if (stats.isDirectory() && !excludes.includes(file)) {
      traverseDirectory(fullPath);
    }
    else if (stats.isFile()) {
      const content = readFileSync(fullPath, 'utf-8');
      let matches;

      // eslint-disable-next-line no-cond-assign
      while ((matches = tFunctionRegex.exec(content)) !== null) {
        const chinese = matches[2];
        // 存入 JSON 对象
        chineseDict[chinese] = chinese;
      }
    }
  });
}

// 生成 JSON 文件
// 接口请求 http://172.30.1.160:8848/nacos/v1/auth/users/login username: nacos password: nacos， post请求，获取token accessToken

function saveJson() {
  try {
    // fetch 接口请求 http://172.30.1.160:8848/nacos/v1/auth/users/login username: nacos password: nacos， post请求，获取token accessToken
    fetch('http://172.30.1.160:8848/nacos/v1/auth/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        username: 'nacos',
        password: 'nacos',
      }),
    })
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        const accessToken = data.accessToken;
        console.log('Access Token:', accessToken);
        // get query 参数
        const params = new URLSearchParams({
          dataId: 'bmos-app_zh_CN.json',
          group: 'frontend-i18n',
          show: 'all',
          username: 'nacos',
          accessToken,
        });
        fetch(`http://172.30.1.160:8848/nacos/v1/cs/configs?${params.toString()}`)
          .then(response => response.json())
          .then((configData) => {
            const content = JSON.parse(configData.content);
            // 遍历 chineseDict，如果 content 中不存在，则添加
            const newContent = {};
            for (const key in chineseDict) {
              if (!content[key]) {
                content[key] = key;
                newContent[key] = key;
              }
            }
            const updatedJsonContent = JSON.stringify(content, null, 2);
            writeFileSync('./script/updated-chinese-strings.json', updatedJsonContent, 'utf-8');

            const newJsonContent = JSON.stringify(newContent, null, 2);
            writeFileSync('./script/new-chinese-strings.json', newJsonContent, 'utf-8');
            console.log('中文字符提取完成，已保存为 chinese-strings.json');
          });
      });
  }
  catch (error) {
    console.error('Failed to save JSON file', error);
  }
}

const rootDir = './src'; // 可修改为你的项目路径
traverseDirectory(rootDir);
console.log('中文字符提取中...');
saveJson();
