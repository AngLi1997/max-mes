// 匹配所有中文字符的正则表达式
export const getChinese = (str: string) => {
  const chineseRegex = /[\u4e00-\u9fa5]/g;
  return str.match(chineseRegex);
};
