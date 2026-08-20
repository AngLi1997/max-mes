const toString = Object.prototype.toString;
const AsyncFunction = Object.getPrototypeOf(async function() {}).constructor;

const isType =
  (type) =>
  (obj) =>
    obj != null && (Array.isArray(type) ? type : [type]).some(t => getType(obj) === `[object ${t}]`);
export const getType = (obj) => Object.prototype.toString.call(obj);

export const isFn = isType(['Function', 'AsyncFunction', 'GeneratorFunction']);

export const isWindow = isType('Window');

export const isHTMLElement = (obj) => {
  return obj?.['nodeName'] || obj?.['tagName'];
};

export const isArray = Array.isArray;

export const isPlainObj = isType('Object');

export const isString = isType('String');

export const isBoolean = isType('Boolean');

export const isNumber = isType('Number');

export const isObject = (val) => {
  return Object.prototype.toString.call(val) === '[object Object]';
};

export const isRegExp = isType('RegExp');

export const isValid = (val) => val !== null && val !== undefined;

export const isValidNumber = (val) => !isNaN(val) && isNumber(val);

export function is(val, type) {
  return toString.call(val) === `[object ${type}]`;
}

export function isDef(val) {
  return typeof val !== 'undefined';
}

export function isUnDef(val) {
  return !isDef(val);
}

export function isEmpty(val) {
  if (isArray(val) || isString(val)) {
    return val.length === 0;
  }

  if (val instanceof Map || val instanceof Set) {
    return val.size === 0;
  }

  if (isPlainObj(val)) {
    return Object.keys(val).length === 0;
  }

  // 判断普通变量是否为空值
  if (val === null || val === undefined) {
    return true;
  }
  if (is(val, 'Number') && isNaN(val)) {
    return true;
  }

  return false;
}

export function isDate(val) {
  return is(val, 'Date');
}

export function isNull(val) {
  return val === null;
}

export function isNullAndUnDef(val) {
  return isUnDef(val) && isNull(val);
}

export function isNullOrUnDef(val) {
  return isUnDef(val) || isNull(val);
}

export function isPromise(val) {
  return is(val, 'Promise') && val instanceof Promise && [val.then, val.catch, val.finally].every(isFunction);
}

export function isFunction(val) {
  return typeof val === 'function';
}

export function isAsyncFunction(val) {
  return val instanceof AsyncFunction;
}

export function isElement(val) {
  // @ts-ignore
  return isObject(val) && !!val.tagName;
}

export function isMap(val) {
  return is(val, 'Map');
}

export const isServer = typeof window === 'undefined';

export const isClient = !isServer;

export function isUrl(path) {
  const reg =
    /(((^https?:(?:\/\/)?)(?:[-;:&=+$,\w]+@)?[A-Za-z0-9.-]+(?::\d+)?|(?:www.|[-;:&=+$,\w]+@)[A-Za-z0-9.-]+)((?:\/[+~%/.\w-_]*)?\??(?:[-+=&;%@.\w_]*)#?(?:[\w]*))?)$/;
  return reg.test(path);
}

// 定义一个类型检查函数，判断是否是对象或数组
export function isObjectOrArray(value) {
  return Object.prototype.toString.call(value) === '[object Object]' || Array.isArray(value);
}

export function isUndefined(target) {
  return typeof target === 'undefined';
}

/**
 * 递归地比较两个对象的每个属性和值
 * @param obj1 对象1
 * @param obj2 对象2
 * @returns {boolean} 是否相等
 */

export function isDeepEqual(obj1, obj2) {
  if (obj1 === obj2) {
    return true;
  }
  if (typeof obj1 !== 'object' || typeof obj2 !== 'object' || obj1 == null || obj2 == null) {
    return false;
  }
  const keysA = Object.keys(obj1);
    const keysB = Object.keys(obj2);
  if (keysA.length !== keysB.length) {
    return false;
  }
  for (const key of keysA) {
    if (!keysB.includes(key) || !isDeepEqual(obj1[key], obj2[key])) {
      return false;
    }
  }
  return true;
}
