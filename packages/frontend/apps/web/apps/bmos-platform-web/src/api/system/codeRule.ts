import request from '@/utils/request';

/**
 * @description: 编码规则列表 /api/app/platform/codeRule/page
 * @param {any} params 入参
 */
export const reqPlatformCodeRuleGET = (
  params?: any,
) => {
  return request({
    url: '/api/app/platform/codeRule/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 编码规则版本列表 /api/app/platform/codeRuleVersion/page
 * @param {any} params 入参
 */
export const reqPlatformCodeRuleVersionGET = (
  params?: any,
) => {
  return request({
    url: '/api/app/platform/codeRuleVersion/page',
    method: 'GET',
    params,
  });
};

/**
 * @description: 保存规则版本 /api/app/platform/codeRule/save
 * @param {any} data 入参
 */
export const reqPlatformCodeRuleSavePOST = (
  data?: any,
) => {
  return request({
    url: '/api/app/platform/codeRule/save',
    method: 'POST',
    data,
  });
};

/**
 * @description: 确认规则版本 /api/app/platform/codeRuleVersion/confirm/{id}
 * @param {string} id 入参
 */
export const reqPlatformCodeRuleVersionConfirmPUT = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/codeRuleVersion/confirm/${id}`,
    method: 'PUT',
  });
};

/**
 * @description: 停用规则版本 /api/app/platform/codeRuleVersion/disabled/{id}
 * @param {string} id 入参
 */
export const reqPlatformCodeRuleVersionDisabledPUT = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/codeRuleVersion/disabled/${id}`,
    method: 'PUT',
  });
};

/**
 * @description: 启用规则版本 /api/app/platform/codeRuleVersion/enabled/{id}
 * @param {string} id 入参
 */
export const reqPlatformCodeRuleVersionEnabledPUT = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/codeRuleVersion/enabled/${id}`,
    method: 'PUT',
  });
};

/**
 * @description: 删除规则版本 /api/app/platform/codeRuleVersion/delete/{id}
 * @param {string} id 入参
 */
export const reqPlatformCodeRuleVersionDELETE = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/codeRuleVersion/delete/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description: 编码规则详情 /api/app/platform/codeRule/detail/{id}
 * @param {any} params 入参
 */
export const reqPlatformCodeRuleDetailGET = (
  id: string,
) => {
  return request({
    url: `/api/app/platform/codeRule/detail/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 规则版本编辑 /api/app/platform/codeRuleVersion/update
 * @param {any} data 入参
 */
export const reqPlatformCodeRuleUpdatePUT = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/codeRuleVersion/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 新增规则版本 /api/app/platform/codeRuleVersion/save
 * @param {any} data 入参
 */
export const reqPlatformCodeRuleVersionSavePOST = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/codeRuleVersion/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 部门树 /api/app/mes/resource/permission/dept/tree
 * @param {API.StepConfigListReq} params
 */

export const reqResourcePermissionDeptTreeReq = () => {
  return request({
    url: `/api/app/mes/resource/permission/dept/tree`,
    method: 'GET',
  });
};

/**
 * @description: 已选择部门 /api/app/platform/codeRule/permission/detail/{id}
 * @param {API.StepConfigListReq} params
 */

export const reqResourcePermissionListDeptReq = (resourceId: string) => {
  return request({
    url: `/api/app/platform/codeRule/permission/detail/${resourceId}`,
    method: 'POST',
  });
};

/**
 * @description: 保存数据权限 /api/app/platform/codeRule/permission
 * @param {any} data 入参
 */
export const reqPlatformCodeRulePermissionSavePOST = (
  data: any,
) => {
  return request({
    url: `/api/app/platform/codeRule/permission`,
    method: 'POST',
    data,
  });
};