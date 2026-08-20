// 流程状态 addFlow updateVersion viewVersion editVersion
export enum flow_STATE {
  addFlow = 'addFlow',
  updateVersion = 'updateVersion',
  viewVersion = 'viewVersion',
  editVersion = 'editVersion',
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

// 人员或者角色类型
export enum USER_ROLE_MESSAGE_MARK_TYPE {
  ALL_ROLE = 'all_role',
  ALL_USER = 'all_user',
  MAKE = 'make',
  MESSAGE = 'message',
}

// 版本状态
export enum VersionStatus {
  // 编辑中
  EDITING = 1,
  // 生效
  USING = 2,
  // 失效
  HISTORY = 3,
}
