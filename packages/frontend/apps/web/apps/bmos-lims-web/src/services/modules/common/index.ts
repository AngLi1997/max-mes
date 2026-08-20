import { getItem } from '@/utils';
import request from '../../service';

/**
 * @description: 查询历史接口 /api/app/mes/log/operation/list/{businessId}
 * @param {string} businessId 业务id
 */
export const reqHistoryList = (businessId: string = '') => {
  return request({
    url: `/app/mes/operation/history/list/${businessId}`,
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
 * @description: 查询审批历史 /api/app/mes/audit/list/flow/audit/history
 * @param {string} processInstanceId 业务id
 * @param {string} deploymentId 业务key
 */

export const getFlowAuditHistory = (
  processInstanceId: string,
  deploymentId: string,
) => {
  return request({
    url: `/app/mes/audit/list/flow/audit/history`,
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
 * @description: 任务处理-审批通过 /api/app/mes/audit/complete
 * @param {any} data 参数
 */

export const mesAuditComplete = (data: any) => {
  return request({
    url: `/app/mes/audit/complete`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 任务处理-审批不通过 /api/app/mes/audit/complete/not/approve
 * @param {any} data 参数
 */

export const mesAuditCompleteNotApprove = (data: any) => {
  return request({
    url: `/app/mes/audit/complete/not/approve`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 任务处理-审批不通过 /api/app/mes/audit/back/to/prev
 * @param {any} data 参数
 */

export const mesAuditBackToPrev = (data: any) => {
  return request({
    url: `/app/mes/audit/back/to/prev`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询抄送人 /api/app/mes/audit/list/make/user
 * @param {string} nodeId 节点id
 * @param {string} deploymentId 业务key
 */

export const getFlowListMakeUser = (nodeId: string, deploymentId: string) => {
  return request({
    url: `/app/mes/audit/list/make/user`,
    method: 'GET',
    params: {
      nodeId,
      deploymentId,
    },
  });
};

// /api/common/open/file/upload
export const commonFileUpload = (data: FormData) => {
  return request({
    url: `/common/open/file/upload`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
      token: getItem('BMOS-ACCESS-TOKEN'),
    },
  });
};

/**
 * @description: 成品树 /app/mes/product/material/finishProductTree
 * @param {number} categoryType 分类信息类型
 * @param {boolean} isFinishedProduct 是否成品
 */

export const getProductMaterialFinishProductTree = (
  categoryType: number = 2,
  isFinishedProduct: boolean = true,
) => {
  return request({
    url: `/app/mes/product/material/finishProductTree`,
    method: 'GET',
    params: {
      categoryType,
      isFinishedProduct,
    },
  });
};

/**
 * @description: 保存操作历史 /api/app/mes/operation/history/save
 * @param {string} businessId 业务id
 * @param {string} type 操作类型
 */

export const operationHistorySave = (businessId: string, type: number) => {
  return request({
    url: `/app/mes/operation/history/save`,
    method: 'POST',
    data: {
      type,
      businessId,
    },
  });
};

/**
 * @description 部门权限-部分数据  /api/app/mes/resource/permission/partition/tree
 * @param params 
 * @returns 
 */
export const getPermissionPartitionTree = ()=>{
  return request({
    url: '/app/mes/resource/permission/partition/tree',
    method: 'GET',
  });
}
