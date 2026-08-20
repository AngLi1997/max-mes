import { RelatedItem } from '@/pages/ProductConfig/ProcessConfig/types';
import request from '../../service';

/**
 * @description: 工艺列表查询接口 /api/app/mes/process/page
 * @param {API.MesProcessPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessList = (
  params: API.MesProcessPageReq = {
    pageNum: 1,
    pageSize: 10,
  },
) => {
  return request({
    url: '/app/mes/process/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 工艺下拉查询接口 /api/app/mes/process/list
 * @param {API.MesProcessListReq} params
 */

export const reqProcessListAll = (params?: API.MesProcessListReq) => {
  return request({
    url: '/app/mes/process/list',
    method: 'GET',
    params,
  });
};

/**
 * @description: 工艺版本列表查询接口 /api/app/mes/process/version/page
 * @param {API.ProcessVersionPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessVersionList = (params: API.ProcessVersionPageReq) => {
  return request({
    url: '/app/mes/process/version/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 修改版本启停状态 /api/app/mes/process/version/changeState
 * [edit:编辑,
  approval:审核,
  confirm:确认,
  valid:生效,
  invalid:失效,
  wait_valid:待生效]
 * @param {string} actionState 操作状态
 * @param {string} id 工艺版本id
 */

export const reqVersionChangeState = (data: any) => {
  return request({
    url: '/app/mes/process/version/changeState',
    method: 'PUT',
    data,
  });
};

/**
 * @description: 保存完整工艺 /api/app/mes/process/save
 * @param {API.MesProcessSaveReq} data
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessSave = (data: API.MesProcessSaveReq) => {
  return request({
    url: '/app/mes/process/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 工艺完整详情查询 /api/app/mes/process/detail
 * @param {API.MesProcessDetailReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqGetDetailUsingGET = (params: API.MesProcessDetailReq) => {
  return request({
    url: '/app/mes/process/detail',
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询历史工序集合 /api/app/mes/procedure/historic/list
 * @param {string} name 工序名称
 * @param {string} processId 工艺id
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureHistoricListGET = (processId: string, name?: string) => {
  return request({
    url: '/app/mes/procedure/historic/list',
    method: 'GET',
    params: {
      processId,
      ...(name && { name }),
    },
  });
};

/**
 * @description: 校验工序名称是否重复 /api/app/mes/procedure/validate/name
 * @param {string} name 工序名称
 * @param {string} processId 工艺id
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureValidateNameGET = (processId: string, name: string, id?: string) => {
  return request({
    url: '/app/mes/procedure/validate/name',
    method: 'GET',
    params: {
      processId,
      name,
      ...(id && { id }),
    },
  });
};
/**
 * @description: 查询流程模型 /api/app/mes/flow/model
 * @param {API.MesFlowModelReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqGetProcessModelUsingGET = (params: API.MesFlowModelReq) => {
  return request({
    url: '/app/mes/flow/model',
    method: 'GET',
    params,
  });
};

/**
 * @description: 编辑版本 /api/app/mes/process/modify
 * @param {API.MesProcessModifyReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessModify = (data: API.MesProcessModifyReq) => {
  return request({
    url: '/app/mes/process/modify',
    method: 'POST',
    data,
  });
};

/**
 * @description: 复制工艺 /api/app/mes/process/version/copy
 * @param {API.ProcessVersionCopyReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessVersionCopy = (data: API.ProcessVersionCopyReq) => {
  return request({
    url: '/app/mes/process/version/copy',
    method: 'POST',
    data,
  });
};

/**
 * @description: 新增版本 /api/app/mes/process/version/save
 * @param {API.ProcessVersionSaveReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcessVersionSave = (data: API.ProcessVersionSaveReq) => {
  return request({
    url: '/app/mes/process/version/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 保存工序流程 /api/app/mes/procedure/detail/save
 * @param {API.ProcedureDetailSaveReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureDetailSave = (data: API.ProcedureDetailSaveReq) => {
  return request({
    url: '/app/mes/procedure/detail/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 保存工序流程 /api/app/mes/procedure/detail/modify
 * @param {API.ProcedureDetailModifyReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureDetailModify = (data: API.ProcedureDetailModifyReq) => {
  return request({
    url: '/app/mes/procedure/detail/modify',
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询历史工序步骤 /api/app/mes/procedure/step/historic/list
 * @param {string} name 工序名称
 * @param {string} procedureId 工序id
 * @param {string} procedureModelId 工序模型id
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureStepHistoricListGET = (procedureId: string, procedureModelId: string, name?: string) => {
  return request({
    url: '/app/mes/procedure/step/historic/list',
    method: 'GET',
    params: {
      procedureId,
      procedureModelId,
      ...(name && { name }),
    },
  });
};

/**
 * @description: 校验工序步骤名称是否重复 /api/app/mes/procedure/validate/name
 * @param {string} name 工序名称
 * @param {string} procedureId 工序id
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureStepValidateNameGET = (procedureId: string, name: string, id?: string) => {
  return request({
    url: '/app/mes/procedure/step/validate/name',
    method: 'GET',
    params: {
      procedureId,
      name,
      ...(id && { id }),
    },
  });
};

/**
 * @description: 查询工序步骤集合 /api/app/mes/procedure/step/list
 * @param {API.ProcedureStepListReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqProcedureStepListReq = (params: API.ProcedureStepListReq) => {
  return request({
    url: '/app/mes/procedure/step/list',
    method: 'GET',
    params,
  });
};
/**
 * @description: 保存工序步骤记录项配置 /api/app/mes/procedure/step/config/save
 * @param {API.StepConfigSaveReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqStepConfigSaveReq = (data: API.StepConfigSaveReq) => {
  return request({
    url: '/app/mes/procedure/step/config/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询配置集合 /api/app/mes/procedure/step/config/list
 * @param {API.StepConfigListReq} params
 * @returns {Promise<any>} 返回一个promise
 */

export const reqStepConfigListReq = (params: API.StepConfigListReq) => {
  return request({
    url: '/app/mes/procedure/step/config/list',
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询产品数 /api/app/mes/product/material/productTree
 * @param {API.StepConfigListReq} params
 */

export const reqProductMaterialProductTreeReq = (categoryType = 2) => {
  return request({
    url: `/app/mes/product/material/productTree?categoryType=${categoryType}`,
    method: 'GET',
  });
};

/**
 * @description: 已选择部门 /api/app/mes/resource/permission/list/dept
 * @param {API.StepConfigListReq} params
 */

export const reqResourcePermissionListDeptReq = (resourceId: string) => {
  return request({
    url: `/app/mes/resource/permission/list/dept?resourceId=${resourceId}`,
    method: 'GET',
  });
};

/**
 * @description: 保存数据权限 /api/app/mes/resource/permission/save
 * @param {API.StepConfigListReq} params
 */

export const reqResourcePermissionSaveReq = (data: any) => {
  return request({
    url: `/app/mes/resource/permission/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询工艺记录项顺序 /api/app/mes/process/version/record/order
 * @param {string} processId 工艺id
 * @param {string} processVersion 工艺版本
 */

export const reqProcessVersionRecordOrderReq = (processId: string, processVersion: string) => {
  return request({
    url: `/app/mes/process/version/record/order`,
    method: 'GET',
    params: {
      processId,
      processVersion,
    },
  });
};

/**
 * @description: 保存工艺记录项顺序 /api/app/mes/process/version/save/order
 * @param {any} data
 */

export const reqProcessVersionSaveOrderReq = (data: any) => {
  return request({
    url: `/app/mes/process/version/save/order`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询物料list /app/platform/material/page
 */

export const reqPlatformMaterialPageReq = (
  params: any = {
    pageNum: 1,
    pageSize: 100,
  },
) => {
  return request({
    url: `/app/platform/material/page`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询工序负责人角色下拉框 /api/app/mes/platform/query/role/list
 */

export const reqPlatformQueryRoleListReq = () => {
  return request({
    url: `/app/mes/platform/query/role/list`,
    method: 'GET',
  });
};

/**
 * @description: 查询工艺下的工序步骤 /api/app/mes/procedure/step/listByProcess
 * @param {string} processId 工艺id
 * @param {string} processVersion 工艺版本
 */

export const getProcedureStepListByProcessReq = (processId: string, processVersion: string) => {
  return request({
    url: `/app/mes/procedure/step/listByProcess`,
    method: 'GET',
    params: {
      processId,
      processVersion,
    },
  });
};

/**
 * @description: 根据产线list获取班组 /app/mes/plan/team/listByProductionLineIds
 */

export const getPlanTeamListByLineIdListReq = (params?: any) => {
  return request({
    url: `/app/mes/plan/team/listByProductionLineIds`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 根据工艺版本id获取班组 /app/mes/plan/team/listByProcessVersionId
 */

export const getPlanTeamListByProcessVersionIdReq = (params?: any) => {
  return request({
    url: `/app/mes/plan/team/listByProcessVersionId`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 审核待办列表 /api/app/mes/process/audit/todo/page
 * * @param {any} params
 */

export const getProcessAuditTodoReq = (params: any) => {
  return request({
    url: `/app/mes/process/audit/todo/page`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 提交审核 /api/app/mes/process/version/audit
 * @param {string} id 工艺版本id
 * @param {string} effectDate 生效时间,以年月日方式传递,立即生效时不传入时间
 */

export const processVersionAuditReq = (id: string, effectDate?: string) => {
  return request({
    url: `/app/mes/process/version/audit`,
    method: 'POST',
    data: {
      id,
      effectDate,
    },
  });
};

/**
 * @description: 获取工艺关联关系 /api/app/mes/process/relation/processes/materials
 * @param {string} processId 工艺id
 */

export const getProcessRelationProcessesMaterialsReq = (processId: string) => {
  return request({
    url: `app/mes/process/relation/processes/materials`,
    method: 'GET',
    params: {
      processId,
    },
  });
};

/**
 * @description:  保存工艺关联关系 /api/app/mes/process/save/relations
 * @param {string} processId 工艺id
 * @param {RelatedItem[]} relations 关联关系
 */

export const getProcessSaveRelationsReq = (processId: string, relations: RelatedItem[]) => {
  return request({
    url: `/app/mes/process/save/relations`,
    method: 'POST',
    data: {
      processId,
      relations,
    },
  });
};

/**
 * @description:  查询工序绑定房间下的工位集合 /api/app/mes/procedure/rooms
 * @param {string} procedureModelId 工序id
 */

export const getProcedureRooms = (procedureModelId: string) => {
  return request({
    url: `/app/mes/procedure/rooms`,
    method: 'GET',
    params: {
      procedureModelId,
    },
  });
};

/**
 * @description:  工位设备属性 /api/app/platform/equipment/app/list/equipment/property
 * @param {string} stationId 工位id
 */

export const getListEquipmentPropertyReq = (stationId: string) => {
  return request({
    url: `/app/platform/equipment/app/list/equipment/property`,
    method: 'GET',
    params: {
      stationId,
    },
  });
};

/**
 * @description:  获取产线 /api/app/mes/product/line
 */

export const getProcessProductLineReq = () => {
  return request({
    url: `/app/mes/factory/line/list`,
    method: 'GET',
  });
};

/**
 * @description:  根据产线获取房间 /api/app/mes/process/product/line/room/{lineId}
 * @param {string[]} lineIds 产线id
 */

export const getProcessProductLineRoomReq = (lineIds: string[]) => {
  return request({
    url: `/app/mes/factory/line/room`,
    method: 'GET',
    params: {
      lineIds,
    },
  });
};

/**
 * @description:  查询工序绑定房间 /api/app/mes/procedure/rooms/list
 * @param {string} procedureModelId 工序id
 */

export const getProcedureRoomsListReq = (procedureModelId: string) => {
  return request({
    url: `/app/mes/procedure/rooms/list`,
    method: 'GET',
    params: {
      procedureModelId,
    },
  });
};

/**
 * @description:  查询工序绑定房间 /api/app/mes/procedure/rooms/list
 * @param {string} procedureModelId 工序id
 */

export const getProcedureRoomsListAllReq = (procedureModelId: string) => {
  return request({
    url: `/app/mes/procedure/rooms/list/all`,
    method: 'GET',
    params: {
      procedureModelId,
    },
  });
};

/**
 * @description:  查询节点集合 /api/app/mes/procedure/node/list
 * @param {string} id 工艺版本id/工序id
 * @param {boolean} type true:步骤节点/false:任务节点
 * @param {string} stepModelId 工步模型id
 */

export const getProcedureNodeListReq = (id: string, type: boolean = false, stepModelId?: string) => {
  return request({
    url: `/app/mes/procedure/node/list`,
    method: 'GET',
    params: {
      id,
      type: type ? 'true' : 'false',
      stepModelId,
    },
  });
};

/**
 * @description:  查询设备集合 /api/app/mes/procedure/equipment/list
 */

export const getProcedureEquipmentListReq = () => {
  return request({
    url: `/app/mes/procedure/equipment/list`,
    method: 'GET',
  });
};

/**
 * @description:  校验表达式 /api/app/mes/procedure/expression/checkout/expression
 */

export const postProcedureExpressionCheckoutExpressionReq = (data: any) => {
  return request({
    url: `/app/mes/procedure/expression/checkout/expression`,
    method: 'POST',
    data,
  });
};

/**
 * @description:  查询操作规程集合 /api/app/mes/operate/list/sop
 */

export const getOperateListReq = () => {
  return request({
    url: `/app/mes/operate/list/sop`,
    method: 'GET',
  });
};

/**
 * @description:  【生产物料】根据生产物料的自定义字段信息 /api/app/mes/material/field/info/{materialId}
 * @param {string} materialId 物料id
 */

export const getMaterialFieldInfoByMaterialIdReq = (materialId: string) => {
  return request({
    url: `/app/mes/material/field/info/${materialId}`,
    method: 'GET',
  });
};
/**
 * @description:  工艺树 /api/app/mes//process/list/tree
 */

export const getProcessListTreeReq = () => {
  return request({
    url: `/app/mes//process/list/tree`,
    method: 'GET',
  });
};

/**
 * @description:  查询字典下拉框 /api/app/mes/platform/query/list/dict/down
 */

export const getPlatformQueryListDictDownReq = () => {
  return request({
    url: `/app/mes/platform/query/list/dict/down`,
    method: 'GET',
  });
};
/**
 * @description:  查询工艺排序 /api/app/mes/procedure/list/process/sort
 */

export const reqProcedureListProcessSortReq = (params: any) => {
  return request({
    url: `/app/mes/procedure/list/process/sort`,
    method: 'GET',
    params,
  });
};
/**
 * @description:  添加工序排序信息 /api/app/mes/procedure/save/process/sort
 */

export const reqProcedureSaveProcessSortReq = (data: any) => {
  return request({
    url: `/app/mes/procedure/save/process/sort`,
    method: 'POST',
    data,
  });
};

/**
 * @description:  查询产线包含停用的 /api/app/mes/factory/process/line/list
 */

export const reqAllFactoryProcessLineList = (params?: any) => {
  return request({
    url: `/app/mes/factory/process/line/list`,
    method: 'GET',
    params,
  });
};
/**
 * @description:  查询生产BOM包含停用的 /api/app/mes/product/formula/process/enableList
 * @param {string} productId 产品id
 * @param {string} processVersionId 工艺版本id
 */

export const reqAllProductFormulaProcessEnableList = (productId: string, processVersionId?: string) => {
  return request({
    url: `/app/mes/product/formula/process/enableList`,
    method: 'GET',
    params: {
      productId,
      processVersionId,
    },
  });
};

/**
 * @description:  查询生产记录版本包含停用的 /api/app/mes/record/query/process/record/version
 * @param {string} recordId 记录id
 * @param {string} processVersionId 工艺版本id
 */

export const reqAllRecordQueryProcessRecordVersion = (recordId: string, processVersionId?: string) => {
  return request({
    url: `/app/mes/record/query/process/record/version`,
    method: 'GET',
    params: {
      recordId,
      processVersionId,
    },
  });
};

/**
 * @description:  查询工序配置负责人包含停用的 /api/app/mes/procedure/model/role/list
 * @param {string} procedureModelId 工序模型id
 */

export const reqAllProcedureModelRoleList = (procedureModelId?: string) => {
  return request({
    url: `/app/mes/procedure/model/role/list`,
    method: 'GET',
    params: {
      procedureModelId,
    },
  });
};

/**
 * @description:  查询工序配置的班组包含停用的 /api/app/mes/plan/team/list/process/team
 * @param {string[]} lineIds 产线id 集合
 * @param {string} processVersionId 工艺版本id
 */

export const reqAllPlanTeamListProcessTeam = (lineIds: string[], processVersionId?: string) => {
  return request({
    url: `/app/mes/plan/team/list/process/team`,
    method: 'GET',
    params: {
      lineIds,
      processVersionId,
    },
  });
};
/**
 * @description:  查询房间包含停用的 /api/app/mes/factory/process/line/room
 * @param {string[]} lineIds 产线id 集合
 * @param {string} processVersionId 工艺版本id
 */

export const reqAllFactoryProcessLineRoom = (lineIds: string[], procedureModelId?: string) => {
  return request({
    url: `/app/mes/factory/process/line/room`,
    method: 'GET',
    params: {
      lineIds,
      procedureModelId,
    },
  });
};

/**
 * @description:  查询工步班组包含停用的 /api/app/mes/plan/team/procedure/step/listByProcessVersionId
 * @param {string} processVersionId 工艺版本id
 * @param {string} procedureModelId 工步模型id
 */

export const reqAllPlanTeamProcedureStepListByProcessVersionId = (
  processVersionId: string,
  procedureModelId?: string,
) => {
  return request({
    url: `/app/mes/plan/team/procedure/step/listByProcessVersionId`,
    method: 'GET',
    params: {
      processVersionId,
      procedureModelId,
    },
  });
};

/**
 * @description:  查询任务包含停用的 /api/app/mes/procedure/complete/node/list
 * @param {string} procedureModelId 工步模型id
 */
export const reqAllProcedureCompleteNodeList = (procedureModelId: string) => {
  return request({
    url: `/app/mes/procedure/complete/node/list`,
    method: 'GET',
    params: {
      procedureModelId,
    },
  });
};

/**
 * @description:  工步完成条件执行条件查询设备包含停用的 /api/app/mes/procedure/step/equipment/list
 * @param {string} stepModelId 工步模型id
 */
export const reqAllProcedureStepEquipmentList = (stepModelId: string) => {
  return request({
    url: `/app/mes/procedure/step/equipment/list`,
    method: 'GET',
    params: {
      stepModelId,
    },
  });
};
/**
 * @description:  工步查询房间包含停用的 /api/app/mes/procedure/step/rooms/list/all
 * @param {string} procedureModelId 工序模型id
 */
export const reqAllProcedureStepRoomsListAll = (params: any) => {
  return request({
    url: `/app/mes/procedure/step/rooms/list/all`,
    method: 'GET',
    params,
  });
};

/**
 * @description:  工步查询房间包含停用的 /api/app/mes/procedure/get/procedure/model
 * @param {string} versionId 工艺版本id
 * @param {string} stepModelId 工步模型id
 */
export const reqAllProcedureGetProcedureModel = (versionId: string, stepModelId?: string) => {
  return request({
    url: `/app/mes/procedure/get/procedure/model`,
    method: 'GET',
    params: {
      versionId,
      stepModelId,
    },
  });
};

// 查询工艺大屏显示配置数据
export const reqAllProcessGetDashboardConfig = (params: any) => {
  return request({
    url: `/app/mes/process/getDashboardConfig`,
    method: 'GET',
    params,
  });
};

// 保存工艺大屏显示配置数据
export const reqAllProcessSaveDashboardConfig = (data: any) => {
  return request({
    url: `/app/mes/process/saveDashboardConfig`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据工艺版本id获取配方物料列表 /api/app/mes/product/formula/material/listByProcess
 * @param {any} params
 */

export const reqProductFormulaMaterialListByProcessReq = (params?: any) => {
  return request({
    url: `/app/mes/product/formula/material/listByProcess`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 【生产物料】获取生产物料的自定义字段信息 /api/app/mes/material/field/info/list
 * @param {string} materialId 物料id
 * @param {string} fieldType 类型
 */

export const reqMaterialFieldInfoListReq = (materialId: string, fieldType: string) => {
  return request({
    url: `/app/mes/material/field/info/list`,
    method: 'GET',
    params: { materialId, fieldType },
  });
};

/**
 * @description: 根据记录id列表以及工艺id查询版本列表 /api/app/mes/record/query/process/record/version/list
 * @param {string} processVersionId 工艺版本id
 * @param {string} recordIdStr 记录id列表
 */
export const reqRecordQueryProcessRecordVersionList = (processVersionId: string, recordIdStr: string) => {
  return request({
    url: `/app/mes/record/query/process/record/version/list`,
    method: 'GET',
    params: { processVersionId, recordIdStr },
  });
};
