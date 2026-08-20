import { isFunction, isObject } from '@bmos/utils';
import { HREF_LANG, LOCAL_LANG } from '../common';

export const __defProp = Object.defineProperty;
export const __defNormalProp = (obj: object, key: any, value?: any) =>
  key in obj
    ? __defProp(obj, key, {
        enumerable: true,
        configurable: true,
        writable: true,
        value,
      })
    : (obj[key] = value);
export const __publicField = (obj: object, key: any, value?: any) => {
  __defNormalProp(obj, typeof key !== 'symbol' ? key + '' : key, value);
  return value;
};

export const maybeFormData = (data: any) => {
  if (!data) return false;
  const { append } = Object.getPrototypeOf(data);
  return isFunction(append);
};

export const getProp = (obj: any, prop: any) => {
  if (isObject(obj)) {
    return obj[prop];
  }
  return null;
};

export const errorNormalize = (err: any) => {
  return err;
};

export function isFinalFault(fault: any) {
  // && fault instanceof classes.Fault && fault[ISC_FINAL_FAULT] === true;
  return typeof fault === 'object';
}

export const getLang = () => {
  const url = new URLSearchParams(location.href);
  const local = localStorage.getItem(LOCAL_LANG);
  const lang = url.get(HREF_LANG) || local || 'zh_CN';
  return lang;
};

export const isOriginal = (config: any) => {
  return config.original;
};
