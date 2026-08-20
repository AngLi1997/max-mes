/* eslint-disable no-undef */
import {
  getAppVersionApi,
} from '@/api/systemApi.js';
import {
  IP_CONFIG,
} from '@/utils/uniStorage/const.js';
import {
  getStorageSync,
} from '@/utils/uniStorage/uniStorage.js';
import {
  t,
} from '@/utils/useBmosI18n';
import {
  ref,
} from 'vue';

// eslint-disable-next-line no-unused-vars, unused-imports/no-unused-vars
const callback = function (task, status) {
  const downing = ref(0);
  const percentage = ref(0);
  percentage.value = Number.parseInt((task.downloadedSize / task.totalSize) * 100);

  switch (task.state) {
    case 1: // 开始
      downing.value = true;
      break;
    case 2: // 已连接到服务器
      break;
    case 3: // 已接收到数据
      break;
    case 4: // 下载完成
      downing.value = false;
      break;
  }
  // 下载完成
  if (status === 200) {
    plus.runtime.install(
      plus.io.convertLocalFileSystemURL(task.filename),
      {
        force: true,
      },
      () => {
        plus.runtime.restart();
      },
      () => {
        uni.showToast({
          title: t('安装失败'),
          mask: false,
          duration: 1500,
        });
      },
    );
  }
  else {
    uni.showToast({
      title: t('更新失败'),
      mask: false,
      duration: 1500,
    });
  }
};

export function getAppVersion() {
  const systemInfo = uni.getSystemInfoSync();
  let version = '';
  // #ifdef APP-PLUS
  console.log(`当前版本：${systemInfo.appWgtVersion}`);
  version = systemInfo.appWgtVersion;
  // #endif
  // #ifdef H5
  console.log(`当前版本：${systemInfo.appVersion}`);
  version = systemInfo.appVersion;
  // #endif
  return version;
}

export function dtask(callback) {
  const url = `http://${
    getStorageSync(IP_CONFIG)
  }/front-end/download/packages/Bmos-Mes.apk`;
  console.log(`下载地址：${url}`);
  const downloadTask = plus.downloader.createDownload(
    url,
    {
      method: 'GET',
    },
    callback,
  );
  downloadTask.start();
}

// 获取manifest.json里的配置信息
export async function checkUpdateApp() {
  // eslint-disable-next-line node/prefer-global/process
  if (process.env.NODE_ENV === 'development') {
    return true;
  }
  const appVersion = getAppVersion();
  const res = await getAppVersionApi();
  return res.data === appVersion;
}

export function download() {
  // const url
  //   = `http://${
  //     getStorageSync(IP_CONFIG)
  //   }/app/bmos-platform/download/index.html`;
  let url = `http://${
    getStorageSync(IP_CONFIG)
  }/front-end/download/packages/Bmos-Mes.apk`;
  // #ifdef APP-PLUS
  //   dtask(callback);
  // plus.runtime.openURL(url);
  const task = plus.downloader.createDownload(url, {}, (d, status) => {
    console.log(status, d);
    if (status === 200) {
      plus.runtime.openFile(d.filename);
    }
    else { // 下载失败
      console.log(`Download failed:${status}`);
    }
  });
  task.start();
  // #endif
  // #ifdef H5
  // window?.openBrowser?.openExternalLink(url);
  url = `http://${
    getStorageSync(IP_CONFIG)
  }/front-end/download/packages/Bmos-Mes.exe`;
  window?.autoUpdate?.update(url);
  // #endif
}
