/**
 * @description: 过滤空值
 * @param obj  对象
 * @returns  过滤后的对象
 */
export const filterEmpty = (obj: any) => {
  return Object.keys(obj)
    .filter(
      key => obj[key] !== null && obj[key] !== undefined && obj[key] !== '',
    )
    .reduce((acc, key) => ({ ...acc, [key]: obj[key] }), {});
};
