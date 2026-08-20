import { isObject } from 'lodash-es';

// 构建URL的query参数
export const buildUrlQuery = (params) => {
  if (!isObject(params)) {
    return '';
  }
  return Object.keys(params)
    .map(
      key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`,
    )
    .join('&');
};

// 解析URL的参数
export const parseUrlQuery = (obj) => {
  if (!isObject(obj)) {
    return {};
  }
  // #ifdef APP-PLUS
  return Object.fromEntries(
    Object.keys(obj).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(obj[key]),
    ]),
  );
  // #endif
  // #ifdef H5
  return obj;
  // #endif
};
