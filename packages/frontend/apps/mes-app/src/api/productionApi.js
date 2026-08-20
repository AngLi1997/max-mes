import request from '@/utils/request/request.js';

// 获取产品树
export const getProductTreeApi = params =>
  request.get('/api/app/mes/product/material/productTree', params);

// 生产前确认列表
export const getBeforeProductionConfirmListApi = params =>
  request.get('/api/app/mes/plan/instruction/start/page', params);

// 获取生产指令单详情
export const getProductionInstructionDetailApi = id =>
  request.get(`/api/app/mes/plan/instruction/detail/${id}`);

// 查询关联的工艺集合
export const getProductionInstructionTechnologyListApi = params =>
  request.get('/api/app/mes/process/relation/processes', params);

// 查询关联的工艺集合及关联批次(之前用的上面这接口)
export const getMesPlanRelationList = params =>
  request.get('/api/app/mes/plan/relation/list', params);

// 查询所有工艺集合
export const getProductionListApi = params =>
  request.get('/api/app/mes/process/list', params);

// 生产前确认关联批次
export const getProductionInstructionBatchListApi = params =>
  request.get('/api/app/mes/plan/info/startPage', params);

// 获取生产计划id获取班组
export const getProductionPlanTeamApi = params =>
  request.get('/api/app/mes/plan/team/listByProductPlanId', params);

// 生产前确认保存
export const saveBeforeProductionConfirmApi = data =>
  request.post('/api/app/mes/plan/instruction/team/start/confirm', data);

// 获取生产管理列表
export const getProductionManagementListApi = params =>
  request.get('/api/app/mes/flow/plan/manage/page', params);

// 查询工序节点
export const getProcessNodeApi = params =>
  request.get(`/api/app/mes/flow/procedures`, params);

// 查询工序步骤节点
export const getProcessStepNodeApi = params =>
  request.get(`/api/app/mes/flow/steps`, params);

// 终止生产
export const stopProductionApi = processInstanceId =>
  request.post(`/api/app/mes/flow/terminate/${processInstanceId}`);

// 工序重做
export const processRedoApi = data =>
  request.post('/api/app/mes/flow/procedure/restart', data);

// 查询工序负责人
export const getProcessPrincipalApi = params =>
  request.get('/api/app/mes/procedure/principal/users', params);

// 暂停生产执行  /api/app/mes/plan/info/pause/{id}
export const pauseProductionApi = id =>
  request.put(`/api/app/mes/plan/info/pause/${id}`);

// 恢复生产执行 /api/app/mes/plan/info/recover/{id}
export const recoverProductionApi = id =>
  request.put(`/api/app/mes/plan/info/recover/${id}`);

// 查询生产历史 /api/app/mes/flow/plan/history/page
export const getProductionHistoryApi = params =>
  request.get('/api/app/mes/flow/plan/history/page', params);

// 查询历史工序节点  /api/app/mes/flow/procedures/history/{processInstanceId}
export const getHistoryProcessNodeApi = params =>
  request.get(`/api/app/mes/flow/procedures/history`, params);

// 查询历史工序步骤节点  /api/app/mes/flow/steps/history
export const getHistoryProcessStepNodeApi = params =>
  request.get(`/api/app/mes/flow/steps/history`, params);

// 查询工步换班信息
export const getListChangeTeamApi = params =>
  request.get(`/api/app/mes/flow/list/change/team`, params);

// 工序换班/工艺换班
export const flowChangeTeamApi = data =>
  request.post('/api/app/mes/flow/change/team', data);

// 生产执行工序换班、工艺换班查询班组信息接口
export const getInstructionTeamApi = params =>
  request.get(`/api/app/mes/plan/instruction/team/detail`, params);

// 查询工序换班可选班组
export const getListByProductionLineIds = params =>
  request.get(`/api/app/mes/plan/team/listByProductionLineIds`, params);

// 完成执行实例任务--生产管理(无需换班)
export const noChangeTeamExecutionApi = data =>
  request.post('/api/app/mes/flow/complete/execution', data);

// 完成任务--待办(无需换班)
export const noChangeTeamTaskApi = data =>
  request.post('/api/app/mes/flow/complete/task', data);

// 数值组件趋势分析
export const getFieldTrendAnalysisApi = params =>
  request.get(`/api/app/mes/execute/field/trend/analysis`, params);

// 更新关联批次 /api/app/mes/plan/info/updateRelation
export const updateRelationApi = data =>
  request.post('/api/app/mes/plan/info/updateRelation', data);

// 查询生产中工步详情信息
export const getProcedureDetailApi = params =>
  request.get(`/api/app/mes/flow/procedure/step/detail`, params);

// 查询生产中工步详情信息
export const getProcedureLineApi = params =>
  request.get(`/api/app/mes/process/product/line/tree`, params);

// 查询工艺绑定的产线
export const getListByProcessVersion = params =>
  request.get(`/api/app/mes/factory/line/listByProcessVersion`, params);

// 根据编号规则生成编号
export const getDirectlyCreateBuildNo = params =>
  request.get(`/api/app/mes/production/directlyCreate/buildNo`, params);

// 根据工艺版本查询
export const getDetailByProcessByVersion = params =>
  request.get(`/api/app/mes/product/formula/version/detailByProcess`, params);

// [直接创建指令单]:保存
export const productionDirectlyCreate = data =>
  request.post('/api/app/mes/production/directlyCreate', data);
