import { EmitFn } from '@bmos/components';

// props 对象
export type DataRequestFn = () => Promise<any>;

export const approvalProps = {
  /** 表格数据请求函数 */
  dataRequest: {
    // 获取列表数据函数API
    type: Function as PropType<DataRequestFn>,
  },
};

// props 类型
export type BMApprovalProps = Partial<ExtractPropTypes<typeof approvalProps>>;

export const approvalEmits = {
  back: () => true,
};

export type ApprovalEmits = typeof approvalEmits;

export type ApprovalEmitFn = EmitFn<ApprovalEmits>;