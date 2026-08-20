/**
 * Independent time operation tool to facilitate subsequent switch to dayjs
 */
import dayjs from 'dayjs';

const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';
const DATE_FORMAT = 'YYYY-MM-DD';

/**
 * @description 格式化时间
 * @param date 时间
 * @param format 格式化字符串
 * @returns {string} 格式化后的时间
 */
export function formatToDateTime(date: dayjs.Dayjs | undefined = undefined, format = DATE_TIME_FORMAT): string {
  return dayjs(date).format(format);
}
/**
 * @description 格式化日期
 * @param date 日期
 * @param format 格式化字符串
 * @returns {string} 格式化后的日期
 */
export function formatToDate(
  date: dayjs.Dayjs | number | undefined | string = undefined,
  format = DATE_FORMAT,
): string {
  return dayjs(date).format(format);
}

/**
 * @description 添加天数
 * @param date 日期
 * @param num 天数
 * @param format 格式化字符串
 * @returns {string} 添加天数后的日期
 */
export function addDate(
  date: dayjs.Dayjs | number | undefined | string = undefined,
  num = 1,
  format = DATE_FORMAT,
): string {
  return dayjs(date).add(num, 'day').format(format);
}

/**
 * @description: 日期比较
 * @param date1
 * @param date2
 * @returns {number} -1: date1 < date2, 0: date1 = date2, 1: date1 > date2
 */
export function compareDate(
  date1: dayjs.Dayjs | number | undefined | string,
  date2: dayjs.Dayjs | number | undefined | string,
) {
  return dayjs(date1).diff(date2, 'day');
}

export const dateUtil = dayjs;
