/**
 * 防抖函数
 */
export function debounce(fn, delay) {
  let timer = null;
  return function () {
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      // eslint-disable-next-line prefer-rest-params
      fn.apply(this, arguments);
      timer = null;
    }, delay);
  };
}

/**
 * 节流函数
 */
export function throttle(fn, delay) {
  let timer = null;
  return function () {
    if (timer) {
      return;
    }
    timer = setTimeout(() => {
      // eslint-disable-next-line prefer-rest-params
      fn.apply(this, arguments);
      timer = null;
    }, delay);
  };
}

// 判断是否空对象
export function isEmptyObject(obj) {
  if (!obj)
    return true;
  // eslint-disable-next-line no-unreachable-loop
  for (const key in obj) {
    return false;
  }
  return true;
}

// 返回到指定他页面(默认返回webview)
export function goBackToTargetPath(targetPath = 'pages/webview/index') {
  const pages = getCurrentPages();
  const index = pages.findIndex(item => item.route === targetPath);
  if (index > -1) {
    uni.navigateBack({
      delta: pages.length - index - 1,
    });
  }
  else {
    uni.navigateBack();
  }
}

/**
 * 链式获取对象属性
 * @param obj 对象
 * @param path 路径 string a.b.c
 * @returns 值
 */
export function getNestedValue(obj, path) {
  if (!obj || typeof path !== 'string')
    return undefined;
  const keys = path.split('.');
  return keys.reduce((acc, key) => (acc && acc[key] !== undefined ? acc[key] : undefined), obj);
}

/**
 * @description rpx 转 px 工具函数
 * @param px px 值
 */
export function px2rpx(px) {
  const screenWidth = uni.getSystemInfoSync().windowWidth; // 屏幕宽度
  return (screenWidth / 1280) * px; // 750 是设计稿宽度
}
