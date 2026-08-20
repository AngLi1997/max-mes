import { reqQuantityCalculate } from '@/services';

/**
 * @description: 计算理论量参数
 * @param {string} formulaMaterialId 配方id
 * @param {number} hydration 水分(无则为0)
 * @param {number} noHydrationContent 含量(无或大于100则为100)
 * @param {number} quantity 物料量
 */
type theoreticalQuantity = {
  /**
   * @description: 配方id
   */
  formulaMaterialId: string;
  /**
   * @description: 水分(无则为0)
   */
  hydration: number;
  /**
   * @description: 含量(无或大于100则为100)
   */
  noHydrationContent: number;
  /**
   * @description: 物料量
   */
  quantity: number;
};

/**
 * @description: 计算理论量
 */
export const computeTheoreticalQuantity = async (params: theoreticalQuantity) => {
  try {
    const { data } = await reqQuantityCalculate(params);
    return data;
  } catch (error) {
    return Promise.reject(error);
  }
};

/**
 * @description: 字符串小数之间的计算
 * @param {string | number} a
 * @param {string | number} b
 * @param {string} operator -运算符号 '+' | '-' | '*' | '/'
 * @return {number}
 */
export const calcWithDecimalFormat = (a: string | number, b: string | number, operator: '+' | '-' | '*' | '/') => {
  const getDecimalPlaces = (numStr: string) => {
    const parts = numStr?.split('.');
    return parts?.[1] ? parts?.[1].length : 0;
  };

  const maxDecimalPlaces = Math.max(getDecimalPlaces(String(a)), getDecimalPlaces(String(b)));

  const numA = typeof a === 'string' ? parseFloat(a) : a;
  const numB = typeof b === 'string' ? parseFloat(b) : b;

  let result;
  switch (operator) {
    case '+':
      result = numA + numB;
      break;
    case '-':
      result = numA - numB;
      break;
    case '*':
      result = numA * numB;
      break;
    case '/':
      if (numB === 0) throw new Error('Division by zero');
      result = numA / numB;
      break;
    default:
      throw new Error('Unsupported operator');
  }

  // 保留最多的小数位
  return result.toFixed(maxDecimalPlaces);
};
