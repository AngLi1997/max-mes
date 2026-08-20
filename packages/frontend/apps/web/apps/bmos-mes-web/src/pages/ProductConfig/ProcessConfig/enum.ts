// 工艺配置状态 addProcess addVersion editVersion copyVersion viewVersion 五种状态
export enum PROCESS_STATE {
  ADD_PROCESS = 'addProcess',
  ADD_VERSION = 'addVersion',
  EDIT_VERSION = 'editVersion',
  COPY_VERSION = 'copyVersion',
  VIEW_VERSION = 'viewVersion',
}

// 开始节点	START_EVENT
// 结束节点	END_EVENT
// 连线	SEQUENCE_FLOW
// 用户任务	USER_TASK
// 子流程任务节点	CALL_ACTIVITY_TASK
export enum FlowNodeType {
  START_EVENT = 'START_EVENT',
  END_EVENT = 'END_EVENT',
  SEQUENCE_FLOW = 'SEQUENCE_FLOW',
  USER_TASK = 'USER_TASK',
  CALL_ACTIVITY_TASK = 'CALL_ACTIVITY_TASK',
}

export enum EDIT_VIEW {
  EDIT = 'edit',
  VIEW = 'view',
}

// 网关类型
export enum GatewayType {
  // 并行网关
  PARALLEL_GATEWAY = 'PARALLEL_GATEWAY',
  // 选做网关
  OPTIONAL_GATEWAY = 'OPTIONAL_GATEWAY',
}

// 版本状态
export enum VersionStatus {
  // 编辑
  EDIT = 'edit',
  // 审核
  APPROVAL = 'approval',
  // 确认
  CONFIRM = 'confirm',
  // 生效
  VALID = 'valid',
  // 失效
  INVALID = 'invalid',
  // 待生效
  WAIT_VALID = 'wait_valid',
  // 重新编辑
  FRESH_EDIT = 'fresh_edit',
}

export type VersionStatusType = `${VersionStatus}`;
