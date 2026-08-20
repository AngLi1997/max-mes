export interface NumericalJudgmentItem {
  field: string; // 选项值
  limitType?: number; //限制类型
  numericalValue?: number | null; // 等于值
  upperLimit?: number; // 上限值比较
  lowerLimit?: number; // 下限值比较
  scopeMax?: string; // 最大值
  scopeMin?: string; // 最小值
  satisfiedValue?: string; // 满足值
  unsatisfiedValue?: string; // 不满足值
  scope?: any; // 范围
}
