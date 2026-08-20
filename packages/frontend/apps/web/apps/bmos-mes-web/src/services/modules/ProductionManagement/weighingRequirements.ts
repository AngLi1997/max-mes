import request from '../../service';

/**
 * @description: 称量需求列表分页 /api/app/mes/weigh/ticket/requirement/group/page
 * @param {any} params
 */
export const reqWeighingRequirementsQueryPage = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/page`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询需求组配料信息物料列表 /api/app/mes/weigh/ticket/requirement/group/queryInfo
 * @param {any} params
 */
export const reqWeighingRequirementsQueryInfo = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/queryInfo`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询配料信息物料列表 /api/app/mes/weigh/ticket/requirement/group/queryMaterialList
 * @param {any} params
 */
export const reqWeighingRequirementsQueryMaterialList = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/queryMaterialList`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 创建称量工单组需求 /api/app/mes/weigh/ticket/requirement/group/create
 * @param {any} data
 */
export const reqWeighingRequirementsCreate = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 保存称量工单组配料信息 /api/app/mes/weigh/ticket/requirement/group/saveRequirement
 * @param {any} data
 */
export const reqWeighingRequirementsSave = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/saveRequirement`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 修改称量工单需求组 /api/app/mes/weigh/ticket/requirement/group/edit
 * @param {any} data
 */
export const reqWeighingRequirementsEdit = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/edit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 取消称量工单需求 /api/app/mes/weigh/ticket/requirement/group/cancel
 * @param {any} params
 */
export const reqWeighingRequirementsCancel = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/cancel`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 确认称量工单需求组 /api/app/mes/weigh/ticket/requirement/group/makeSure
 * @param {any} params
 */
export const reqWeighingRequirementsMakeSure = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/makeSure`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 计算理论量 /api/app/mes/requisition/quantity/calculate
 * @param {any} params
 */
export const reqQuantityCalculate = (params: any) => {
  return request({
    url: `/app/mes/requisition/quantity/calculate`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 计算配料量 /api/app/mes/weigh/ticket/requirement/group/calcFormulaQuantity
 * @param {any} data
 */
export const reqCalcFormulaQuantity = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/calcFormulaQuantity`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据工单id查询称量详情 /api/app/mes/weigh/ticket/requirement/group/getWeighRecord
 * @param {any} params
 */
export const reqWeighingRequirementsGetWeighRecord = (params: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/getWeighRecord`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 保存时校验称量工单组配料信息 /api/app/mes/weigh/ticket/requirement/group/validateSaveRequirement
 * @param {any} data
 */
export const reqWeighingRequirementsValidateSave = (data: any) => {
  return request({
    url: `/app/mes/weigh/ticket/requirement/group/validateSaveRequirement`,
    method: 'POST',
    data,
  });
};
