import { getPictureRange } from '@/api';
import { formatTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';

const isOverLimit = (configValue, value, symbol = 'LESS_THAN', isDown = true) => {
  if (isDown) {
    // 判断下限
    if (!Number.isNaN(configValue)) {
      // 值小于下限,必定超限
      if (value < configValue) {
        return true;
      }
      else {
        // 判断是小于号,并相等超限, 小于等于号不超限
        return symbol !== 'LESS_THAN' && value === configValue;
      }
    }
  }
  else {
    // 判断上限
    if (!Number.isNaN(configValue)) {
      // 值大于上限,必定超限
      if (value > configValue) {
        return true;
      }
      else {
        // 判断是小于号,并相等超限, 小于等于号不超限
        return symbol !== 'LESS_THAN' && value === configValue;
      }
    }
  }
  return false;
};

// 获取采集超限label
export const getFormatterOverLimit = (lineData, gatherValue) => {
  let overLimit = '  ';
  // 纠偏线下限
  overLimit += isOverLimit(lineData.correctionLineConfig.scopeConfig.lowerValue * 1, gatherValue * 1, lineData.correctionLineConfig.scopeConfig.lowerSymbol) ? t('超纠偏线') : '';
  // 纠偏线上限
  overLimit += isOverLimit(lineData.correctionLineConfig.scopeConfig.upperValue * 1, gatherValue * 1, lineData.correctionLineConfig.scopeConfig.upperSymbol, false) ? t('超纠偏线') : '';
  // 警戒线下限
  overLimit += isOverLimit(lineData.warningLineConfig.scopeConfig.lowerValue * 1, gatherValue * 1, lineData.warningLineConfig.scopeConfig.lowerSymbol) ? t('超警戒线') : '';
  // 警戒线上限
  overLimit += isOverLimit(lineData.warningLineConfig.scopeConfig.upperValue * 1, gatherValue * 1, lineData.warningLineConfig.scopeConfig.upperSymbol, false) ? t('超警戒线') : '';
  // 标准线下限
  overLimit += isOverLimit(lineData.standardLineConfig.scopeConfig.lowerValue * 1, gatherValue * 1, lineData.standardLineConfig.scopeConfig.lowerSymbol) ? t('超标准线') : '';
  // 标准线上限
  overLimit += isOverLimit(lineData.standardLineConfig.scopeConfig.upperValue * 1, gatherValue * 1, lineData.standardLineConfig.scopeConfig.upperSymbol, false) ? t('超标准线') : '';
  return overLimit;
};

const lineStyle = {
  correctionLineConfig: '#FF4C26', // 纠偏线
  warningLineConfig: '#FF9933', // 警戒线
  standardLineConfig: '#59BF78', // 标准线
};

export const getAllNumSeries = (lineData) => {
  const series = [];
  const allNum = [];
  for (const key in lineData) {
    const { limitType, scopeConfig, fixedValue } = lineData[key];
    const { lowerValue, upperValue } = scopeConfig;
    // limitType: 是否是范围,最小值,最大值,等于值
    if (limitType === 0) {
      // 配置的范围
      // 最小值
      if (lowerValue) {
        allNum.push(lowerValue); // 存入数组中,判断最大/小值是多少
        series.push({
          lineStyle: {
            color: lineStyle[key],
          },
          yAxis: lowerValue,
        });
      }
      // 最大值
      if (upperValue) {
        allNum.push(upperValue); // 存入数组中,判断最大/小值是多少
        series.push({
          lineStyle: {
            color: lineStyle[key],
          },
          yAxis: upperValue,
        });
      }
    }
    else {
      if (fixedValue) {
        allNum.push(fixedValue); // 存入数组中,判断最大/小值是多少
        series.push({
          lineStyle: {
            color: lineStyle[key],
          },
          yAxis: fixedValue,
        });
      }
    }
  }
  return {
    series,
    allNum,
  };
};

export const collectionTimeType = {
  day: 24 * 60 * 60 * 1000,
  hour: 60 * 60 * 1000,
  minute: 60 * 1000,
  second: 1 * 1000,
};
export const getXData = (clearanceTime, collectionTime) => {
  const allTime = clearanceTime[1] - clearanceTime[0];
  const interval = collectionTime.value * collectionTimeType[collectionTime.type];
  const scale = Math.floor(allTime / interval);
  const XData = [];

  if (allTime > interval) {
    // 采集范围大于采集间隔
    for (let index = 0; index < scale; index++) {
      XData.push(formatTime(clearanceTime[0] + interval * index, 'datetime'));
    }
  }
  return XData;
};

const findSmallerNumber = (arr, num) => {
  // 筛选出小于给定数字的元素
  const smallerNumbers = arr.filter(n => n <= num);
  // 返回找到的最大数字，如果没有找到，则返回undefined
  return smallerNumbers.length ? Math.max(...smallerNumbers) : undefined;
};

export const getYData = async (data, clearanceTime, collectionTime, equipmentData, allNum, procedureStepModelId, id) => {
  const allTime = clearanceTime[1] - clearanceTime[0];
  const interval = collectionTime.value * collectionTimeType[collectionTime.type];
  // const scale = Math.floor(allTime/interval)
  const yData = [];
  const allTimeNum = [];
  const allVal = [];
  const allTimeData = {};
  data.forEach((item) => {
    allTimeNum.push(item.time);
    allVal.push(item.val);
    allTimeData[item.time] = item.val;
  });
  if (allTime > interval) {
    // 采集范围大于采集间隔
    for (let index = clearanceTime[0]; index < clearanceTime[1]; index += interval) {
      const time = findSmallerNumber(allTimeNum, index);
      yData.push(!time ? '' : allTimeData[time]);
    }
  }

  const res = await getPictureRange({
    acquisitionDataCode: equipmentData,
    componentId: id,
    maxValue: Math.max(...allVal, ...allNum),
    minValue: Math.min(...allVal, ...allNum),
    procedureStepModelId,
  });
  return {
    yData,
    min: res.data.lowerValue,
    max: res.data.upperValue,
  };
};
