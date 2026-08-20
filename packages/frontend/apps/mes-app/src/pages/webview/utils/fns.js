import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { t } from '@/utils/useBmosI18n.js';
import { isUndefined } from 'lodash-es';

// 检查阀值，返回状态（时间组件、数值组件规则相同）
export const checkThreshold = (
  componentType,
  configInfo,
  value,
  defaultState,
) => {
  let state = defaultState;
  // 0 范围限制
  // 1 数值相等
  try {
    if (configInfo.limit === 1) {
      if (componentType === 'TIME') {
        state
          = value === Number.parseFloat(configInfo?.numericalValue)
            ? defaultState
            : 'unusual';
      }
      else {
        if (configInfo?.numericalValue) {
          state
            = value === Number.parseFloat(configInfo?.numericalValue)
              ? defaultState
              : 'unusual';
        }
        else {
          state = defaultState;
        }
      }
    }
    else if (configInfo?.limit === 0) {
      if (configInfo.scope) {
        const { scopeMin, scopeMax, lowerLimit, upperLimit } = configInfo.scope;
        const min = Number.parseFloat(scopeMin);
        const max = Number.parseFloat(scopeMax);
        const newLowerLimit = isUndefined(lowerLimit) ? 1 : lowerLimit;
        const newUpperLimit = isUndefined(upperLimit) ? 1 : upperLimit;
        /*
                    下限 0 上限 0 大于下限 小于上限
                    下限 0 上限 1 大于下限 小于等于上限
                    下限 1 上限 0 大于等于下限 小于上限
                    下限 1 上限 1 大于等于下限 小于等于上限
                */
        const conditions = {
          '00': (!isNaN(min) && value <= min) || (!isNaN(max) && value >= max),
          '01': (!isNaN(min) && value <= min) || (!isNaN(max) && value > max),
          '10': (!isNaN(min) && value < min) || (!isNaN(max) && value >= max),
          '11': (!isNaN(min) && value < min) || (!isNaN(max) && value > max),
        };
        const key = `${newLowerLimit}${newUpperLimit}`;
        state = conditions[key] ? 'unusual' : defaultState;
      }
      else {
        state = defaultState;
      }
    }
  }
  catch (error) {
    state = defaultState;
  }
  return state;
};

// 根据str和提取的数组将结果转换成秒
function convertStrToSeconds(str, numbers, timeComponentFormat) {
  let result = 0;
  if (str.includes(timeComponentFormat?.find(item => item.label === 'second')?.value || t('秒'))) {
    result = numbers.pop();
  }
  if (str.includes(timeComponentFormat?.find(item => item.label === 'minute')?.value || t('分'))) {
    result += numbers.pop() * 60;
  }
  if (str.includes(timeComponentFormat?.find(item => item.label === 'hour')?.value || t('时'))) {
    result += numbers.pop() * 60 * 60;
  }
  if (str.includes(timeComponentFormat?.find(item => item.label === 'day')?.value || t('日'))) {
    result += numbers.pop() * 60 * 60 * 24;
  }
  return result;
}
// 从字符串中提取数字成数组 并转换成数字秒
export function extractNumbersFromString(str, timeComponentFormat) {
  const numbers = str.match(/\d+/g).map(Number);
  return convertStrToSeconds(str, numbers, timeComponentFormat);
}

export const getSignFormat = async () => {
  const systemInfoStore = useSystemInfoStore();
  const { getParameterByCode } = systemInfoStore;
  const data = getParameterByCode('platform.sys.signature.time-format');
  return data.value || 'yyyy-MM-dd HH:mm:ss';
};
