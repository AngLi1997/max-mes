import { t } from '@/utils/useBmosI18n.js';

// 最多10位整数，最多9位小数,参数val为输入的值, isPositive 为是否为正数,默认为true,为false时为负数 返回Promise.reject()提示错误信息
export function numberValidator(val, isPositive = true) {
  if (Number(val) <= 0 && isPositive) {
    return Promise.reject(t('请输入正数'));
  }
  if (!/^\d{1,10}(\.\d{1,9})?$/.test(val)) {
    return Promise.reject(t('整数部分最多为10位，小数部分最多9位'));
  }
  return Promise.resolve();
}
