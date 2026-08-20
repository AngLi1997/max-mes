export enum MODAL_TYPE {
  RECORD = 'RECORD',
  INST = 'INST',
}

// 1 签名公式
// 2 引用公式
// 3 时间差公式
// 4 求和公式
// 5 最大值公式
// 6 最小值公式
// 7 平均值公式
// 8 关联签名公式
// 9 数值判定公式（文本、单选、多选）
// 10 日期计算公式

export const CHECK_FORMULA = ['NUMBER', 'TEXT', 'DATE', 'TIME', 'SUBMIT_SIGN', 'RADIO', 'CHECKBOX'];

export const COMPUTE_FORMULA = ['NUMBER'];

export const SIGN = '1';

export const QUOTE = '2';

export const NODE_MATH = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11'];

export const BEHAVIOR = 'add';

export const DATE = 'DATE';

export const ERROR_MESSAGE: Record<string, string> = {
  DATE: t('请选择日期、选择组件'),
  TEXT: t('请选择文字、选择组件'),
  NUMBER: t('请选择数值、选择组件'),
  TIME: t('请选择日期组件'),
};
