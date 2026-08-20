import request from '../../service';

/**
 * @description: 查询历史接口 /api/app/wms/log/operation/list/{businessId}
 * @param {string} businessId 业务id
 */
export const reqHistoryList = (businessId: string = '') => {
  return request({
    url: `/app/wms/operation/history/list/${businessId}`,
    method: 'GET',
  });
};

/**
 * @description: 所有部门 /api/app/platform/dept/tree-all
 */

export const reqPlatformDeptTreeAllList = () => {
  return request({
    url: `/app/platform/dept/tree-all`,
    method: 'GET',
  });
};

/**
 * @description: 所有未分配的人 /api/app/platform/dept/tree-unassigned
 */

export const reqPlatformDeptTreeUnassignedList = () => {
  return request({
    url: `/app/platform/dept/tree-unassigned`,
    method: 'GET',
  });
};

/**
 * @description: 根据部门获取人 /api/app/platform/dept/user/tree
 */

export const reqPlatformDeptUserTreeList = () => {
  return request({
    url: `/app/platform/dept/user/tree`,
    method: 'GET',
  });
};

/**
 * @description: 获取所有参数配置(可查锁屏时间) /api/app/platform/business/parameter/detailByCode
 * @param {string} code 参数编码
 */

export const getParameter = (code: string) => {
  return request({
    url: `/app/platform/business/parameter/detailByCode/${code}`,
    method: 'GET',
  });
};

/**
 * @description: 获取所有角色 /api/app/platform/role/aggregate/tree
 */

export const getPlatformRoleAggregateTree = () => {
  return request({
    url: `/app/platform/role/aggregate/tree`,
    method: 'GET',
  });
};

/**
 * @description: 查询审批历史 /api/app/wms/audit/list/flow/audit/history
 * @param {string} processInstanceId 业务id
 * @param {string} deploymentId 业务key
 */

export const getFlowAuditHistory = (processInstanceId: string, deploymentId: string) => {
  return request({
    url: `/app/wms/audit/list/flow/audit/history`,
    method: 'GET',
    params: {
      processInstanceId,
      deploymentId,
    },
  });
};

/**
 * @description: 校验签名 /api/app/platform/signature/validate
 * @param {any} data
 */

export const mesSignatureValidate = (data: any) => {
  return request({
    url: `/app/platform/signature/validate`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 检查当前用户是否配置了签名密码 /api/app/platform/signature/checkSignatureConfig
 */
export const mesSignatureCheckSignatureConfig = (data: { userIds: string[] }) => {
  return request({
    url: `/app/platform/signature/checkSignatureConfig`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取签名图片 /api/app/platform/user/sign/info
 */

export const getSignatureImg = () => {
  return request({
    url: `/app/platform/user/sign/info`,
    method: 'GET',
  });
};

/**
 * @description: 校验签名v2 /api/app/platform/signature/validate
 * @param {any} data
 */

export const mesSignatureValidateV2 = (data: any) => {
  return request({
    url: `/app/platform/signature/validate/v2`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 校验签名(V3校验签名密码) /api/app/platform/signature/validateSignaturePassword
 */

export const mesSignatureValidateSignaturePassword = (data: any) => {
  return request({
    url: `/app/platform/signature/validateSignaturePassword`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 任务处理-审批通过 /api/app/wms/audit/complete
 * @param {any} data 参数
 */

export const mesAuditComplete = (data: any) => {
  return request({
    url: `/app/wms/audit/complete`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 任务处理-审批不通过 /api/app/wms/audit/complete/not/approve
 * @param {any} data 参数
 */

export const mesAuditCompleteNotApprove = (data: any) => {
  return request({
    url: `/app/wms/audit/complete/not/approve`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 任务处理-审批不通过 /api/app/wms/audit/back/to/prev
 * @param {any} data 参数
 */

export const mesAuditBackToPrev = (data: any) => {
  return request({
    url: `/app/wms/audit/back/to/prev`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询抄送人 /api/app/wms/audit/list/make/user
 * @param {string} nodeId 节点id
 * @param {string} deploymentId 业务key
 */

export const getFlowListMakeUser = (nodeId: string, deploymentId: string) => {
  return request({
    url: `/app/wms/audit/list/make/user`,
    method: 'GET',
    params: {
      nodeId,
      deploymentId,
    },
  });
};

/**
 * @description: 成品树 /app/wms/product/material/finishProductTree
 * @param {number} categoryType 分类信息类型
 * @param {boolean} isFinishedProduct 是否成品
 */

export const getProductMaterialFinishProductTree = (categoryType: number = 2, isFinishedProduct: boolean = true) => {
  return request({
    url: `/app/wms/product/material/finishProductTree`,
    method: 'GET',
    params: {
      categoryType,
      isFinishedProduct,
    },
  });
};

/**
 * @description: 保存操作历史 /api/app/wms/operation/history/save
 * @param {string} businessId 业务id
 * @param {string} type 操作类型
 */

export const operationHistorySave = (businessId: string, type: number) => {
  return request({
    url: `/app/wms/operation/history/save`,
    method: 'POST',
    data: {
      type,
      businessId,
    },
  });
};

/**
 * @description 部门权限-部分数据  /api/app/wms/resource/permission/partition/tree
 * @param params
 * @returns
 */
export const getPermissionPartitionTree = () => {
  return request({
    // url: '/app/wms/resource/permission/partition/tree',
    url: '/app/wms/resource/permission/partition/tree', //暂用mes的
    method: 'GET',
  });
};
/**
 * @description 根据Code 获取权限  /api/app/platform/menu/admin/tree
 * @param {any} params 菜单编码
 * @returns
 */
export const getPermissionMenuList = (params: any) => {
  return request({
    url: '/app/platform/menu/auth/all',
    method: 'GET',
    params,
  });
};

/**
 * @description 查询参数配置  /api/app/platform/business/parameter/detailByCode/{code}
 * @param params
 * @returns
 */
export const getParameterDetailByCode = (code: string) => {
  return request({
    url: `/app/platform/business/parameter/detailByCode/${code}`,
    method: 'GET',
  });
};

export const getMenuList = async (params: any) => {
  return await request({
    url: '/app/platform/menu/auth/tree',
    method: 'get',
    params,
  });
};

/**
 * @description 物料配置的单位  /api/app/platform/material/extendUnit/extendUnit/list
 * @param {string} materialId 物料id
 */
export const reqPlatformMaterialExtendUnitList = async (materialId: string) => {
  return await request({
    url: '/app/platform/material/extendUnit/extendUnit/list',
    method: 'get',
    params: {
      materialId,
    },
  });
};

/**
 * @description 根据启用状态查询用户列表  /api/app/platform/user/list
 * @param {string} state 启用状态
 */
export const reqPlatformUserListByState = async (state: string) => {
  return await request({
    url: '/app/platform/user/list',
    method: 'get',
    params: {
      state,
    },
  });
};

/**
 * @description 根据功能权限按钮id查询用户列表  /api/app/platform/user/listByMenuId
 * @param {string} menuId 权限码
 */
export const reqPlatformUserListByMenuId = async (menuId: string) => {
  return await request({
    url: '/app/platform/user/listByMenuId',
    method: 'get',
    params: {
      menuId,
    },
  });
};

/**
 * @description 根据功能权限按钮id查询用户列表  /api/app/wms/unit/calcSumAdapt
 * @param {string} targetUnitId 权限码 求和目标转换单位id
 * @param {Array<{unitId: string, value: string}>} list 求和单位列表
 */
export const reqWmsUnitCalcSumAdapt = async (targetUnitId: string, list: Array<{ unitId: string; value: string }>) => {
  return await request({
    url: '/app/wms/unit/calcSumAdapt',
    method: 'POST',
    data: {
      targetUnitId,
      list,
    },
  });
};

/**
 * @description 免疫类型查询
 * @param data
 * @returns
 */
export const getImmunetypeList = (data: any) => {
  return request({
    url: '/bmos-plasma/immunity/page',
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询浆站id和名称对应关系 /plasma-station/list
 */
export const getPlasmaStationAllList = () => {
  return request({
    url: '/bmos-plasma/plasma-station/list',
    method: 'GET',
  });
};
