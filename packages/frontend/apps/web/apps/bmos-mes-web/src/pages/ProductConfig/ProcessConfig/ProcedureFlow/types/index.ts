import { Recordable } from '@bmos/components';

export interface BatchRecordItems {
  batchRecordId: string;
  batchRecordName: string;
  batchRecordVersionId: string;
}

export enum SegmentedType {
  // 功能配置
  FunctionConfig = 'FunctionConfig',
  // 执行条件
  ExecutionCondition = 'ExecutionCondition',
  // 完成条件
  CompletionCondition = 'CompletionCondition',
}

export type SegmentedTypeValue = `${SegmentedType}`;

export interface RightDrawerProps {
  open: boolean;
  settingNodeId: string;
  settingNodeFormData: Recordable;
  isView: boolean;
  batchRecordItems: BatchRecordItems[];
  procedureId: string;
  procedureIdOther: string;
  flowDataForDrawer: Recordable;
  detailProceduresSteps: Recordable;
  processDetail: Recordable;
  currentNodeType: NodeTypeValue;
  versionId: string;
}

export interface ConditionItem {
  code: string; // 条件编码
  name: string; // 条件名称
  conditionType: string; // 条件类型
  procedureId?: string; // 工序节点
  taskNodeId?: string; // 任务节点
  stepId?: string; //步骤节点
  equipmentId?: string; // 设备信息
  equipmentState?: string; // 使用状态
  roomId?: string; // 房间信息
  roomState?: string; // 房间状态
  materialId?: string; // 物料信息
  checkRule?: string; // 校验规则
  number?: string; // 物料量
  defaultResult?: boolean; // 默认测试条件 true false
}

// { label: t('任务节点完成'), value: 'task_node_complete' },
// { label: t('设备使用状态'), value: 'equipment_use_state' },
// { label: t('房间状态'), value: 'room_state' },
// { label: t('物料预定量'), value: 'material_reserve_number' },
// { label: t('配料称量签名'), value: 'dosing_signature' },
// { label: t('中间品产出签名'), value: 'output_signature' },
export enum ConditionType {
  StepNodeComplete = 'step_node_complete',
  TaskNodeComplete = 'task_node_complete',
  EquipmentUseState = 'equipment_use_state',
  RoomState = 'room_state',
  MaterialReserveNumber = 'material_reserve_number',
  DosingSignature = 'dosing_signature',
  OutputSignature = 'output_signature',
}

export type ConditionTypeValue = `${ConditionType}`;

// 任务节点 步骤节点
export enum NodeType {
  TaskNode = 'taskNode',
  StepNode = 'stepNode',
}

export type NodeTypeValue = `${NodeType}`;

// {
//   label: t('记录作业'),
//   value: '0',
// },
// {
//   label: t('工序审核'),
//   value: '1',
// },
// {
//   label: t('工艺审核'),
//   value: '2',
// },
// {
//   label: t('工序换班'),
//   value: '3',
// },
// {
//   label: t('工艺换班'),
//   value: '4',
// },
// {
//   label: t('辅助记录'),
//   value: '5',
// },
export enum NodeFunctionEnum {
  RecordOperation = '0',
  ProcedureAudit = '1',
  ProcessAudit = '2',
  ProcedureShift = '3',
  ProcessShift = '4',
  AuxiliaryRecord = '5',
  Inspection = '6',
}

export const NodeFunctionEnumMap = new Map<NodeFunctionEnum, string>([
  [NodeFunctionEnum.RecordOperation, t('记录作业')],
  [NodeFunctionEnum.ProcedureAudit, t('工序审核')],
  [NodeFunctionEnum.ProcessAudit, t('工艺审核')],
  [NodeFunctionEnum.ProcedureShift, t('工序换班')],
  [NodeFunctionEnum.ProcessShift, t('工艺换班')],
  [NodeFunctionEnum.AuxiliaryRecord, t('辅助记录')],
  [NodeFunctionEnum.Inspection, t('发起请验')],
]);
