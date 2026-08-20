import { ComponentNode } from '@/components/Record';

export interface FormulaType {
  /**
   * 公式表达式
   */
  formulaExpression?: string;
  /**
   * 公式实际参数字段JSON
   */
  formulaField?: string;
  /**
   * 公式id
   */
  formulaId?: string;
  /**
   * 精度
   */
  formulaPrecision?: number;
  /**
   * 组件id
   */
  id?: number;
  /**
   * 标记该组件是否是一个计算结果（0否1是，默认0）
   */
  isResult?: number;
}

export type EmitFn<E = EmitsOptions> = SetupContext<E>['emit'];

export interface FormulaParsesType {
  key: string;
  value: string | null;
  target?: ComponentNode;
}
