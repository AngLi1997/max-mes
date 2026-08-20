import request from '../../service';
// 生产计划分页列表 共用指令单分解页面 需传递 orderBy=t1.confirm_time&dir=desc
export const productionPlanPage = (params: any) => {
  return request({
    url: '/app/mes/plan/info/page',
    method: 'GET',
    params,
  });
};
// 申请(提交审核)
export const submitApprove = (id: string) => {
  return request({
    url: `/app/mes/plan/info/approve/${id}`,
    method: 'PUT',
  });
};

// 作废
export const planDiscard = (id: string) => {
  return request({
    url: `/app/mes/plan/info/discard/${id}`,
    method: 'PUT',
  });
};

// 生产计划跳转页获取产品下拉列表
export const getPlanProductList = (categoryType = 2) => {
  return request({
    url: `/app/mes/product/material/productList?categoryType=${categoryType}`,
    method: 'GET',
  });
};

// 产品下拉列表改变时获取对应生产工艺下拉列表
export const getPlanProcessList = (params: any) => {
  return request({
    // url: `/app/mes/process/list`,
    url: `/app/mes/process/instruction/process/list`, //0726改成加了数据权限的
    method: 'GET',
    params,
  });
};

// 保存(新增保存)
export const planSave = (data: any) => {
  return request({
    url: `/app/mes/plan/info/save`,
    method: 'POST',
    data,
  });
};
//更新(编辑保存)
export const planEditSave = (data: any) => {
  return request({
    url: `/app/mes/plan/info/update`,
    method: 'PUT',
    data,
  });
};

// 获取下一个编号,未确认使用的编号会重复返回(批号回传编码信息?)
export const planGetNextUseNo = (data: any) => {
  return request({
    url: `/app/mes/platform/query/codeRule/getNextUseNo`,
    method: 'POST',
    data,
  });
};

// 获取下一个编号,未确认使用的编号会重复返回(批号回传编码信息?)
export const planGetBatchNextUseNo = (data: any) => {
  return request({
    url: `/app/mes/platform/query/codeRule/getBatchNextUseNo`,
    method: 'POST',
    data,
  });
};

// 批量保存
export const planBatchSave = (data: any) => {
  return request({
    url: `/app/mes/plan/info/batchSave`,
    method: 'POST',
    data,
  });
};

/**
 * @description // /api/app/mes/plan/info/pageTraceable 生产计划追溯分页列表
 * @param any
 * @returns
 */
export const getPlanPageTraceable = async (data: any) => {
  return await request({
    url: '/app/mes/plan/info/pageTraceable',
    method: 'POST',
    data,
  });
};

/**
 * @description // /api/app/mes/plan/archive/{planId} 重新归档
 * @param any
 * @returns
 */
export const archiveAgain = async (id: any) => {
  return await request({
    url: `/app/mes/plan/archive/${id}`,
    method: 'POST',
  });
};

/**
 * @description  生产计划详情 /api/app/mes/plan/info/detail/{id}
 * @param {string} id 生产计划id
 * @returns
 */
export const reqPlanDetail = async (id: string) => {
  return await request({
    url: `/app/mes/plan/info/detail/${id}`,
    method: 'GET',
  });
};

// 根据工艺版本id获取产线列表
export const reqFactoryLineListByProcessVersion = async (params: any) => {
  return await request({
    url: `/app/mes/factory/line/listByProcessVersion`,
    method: 'GET',
    params,
  });
};

//批量提交审核
export const reqPlanInfoApproveBatch = async (data: any) => {
  return await request({
    url: `/app/mes/plan/info/approveBatch`,
    method: 'POST',
    data,
  });
};

// 根据生产工艺id获取关联的工艺集合
export const reqProcessRelationProcesses = async (params: any) => {
  return await request({
    url: `/app/mes/process/relation/processes`,
    method: 'GET',
    params,
  });
};

// 根据生产工艺id获取关联的工艺集合
export const reqPlanInfoStartPage = async (params: any) => {
  return await request({
    url: `/app/mes/plan/info/startPage`,
    method: 'GET',
    params,
  });
};

// 通过生产计划id回显关联信息表格(编辑和查看时及指令单审核点处理时调用)
export const reqPlanRelationList = (params: any) => {
  return request({
    url: `/app/mes/plan/relation/list`,
    method: 'GET',
    params,
  });
};
