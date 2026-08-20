import { isObject } from './is.js';
/**
 * deepMerge
 * @param obj 目标对象
 * @param args 要合并的对象
 * @returns 合并后的对象
 */
export function deepMerge(obj, target) {
  let key;
  for (key in target) {
    obj[key] = isObject(obj[key]) ? deepMerge(obj[key], target[key]) : (obj[key] = target[key]);
  }
  return obj;
}
