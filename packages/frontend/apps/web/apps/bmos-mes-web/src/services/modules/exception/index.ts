import request from '../../service';

/**
 * @description: 批次异常信息分页 /api/app/mes/exception/batch/page
 */

export const getFinishProductTree = (params: any) => {
  return request({
    url: `/app/mes/product/material/finishProductTree`,
    method: 'GET',
    params,
  });
};

// 异常管理分页
export const getExceptionPage = (params: any) => {
  return request({
    url: `/app/mes/exception/page`,
    method: 'GET',
    params,
  });
};

// 根据工艺查询所有工艺版本
export const getVersionList = (processId: string) => {
  return request({
    url: `/app/mes/process/version/list`,
    method: 'GET',
    params: {
      processId,
    },
  });
};

// 根据工艺版本查询所有生产批次
export const getListPlanByProcess = (processId: any, processVersion: string) => {
  return request({
    url: `/app/mes/plan/info/listPlanByProcess`,
    method: 'GET',
    params: {
      processId,
      processVersion,
    },
  });
};

// 根据工序id查询步骤列表
export const getListByProcedureModelId = (procedureModelId: string) => {
  return request({
    url: `/app/mes/procedure/step/listByProcedureModelId`,
    method: 'GET',
    params: {
      procedureModelId,
    },
  });
};

// 手动添加异常信息/api/app/mes/exception/save
export const exceptionSave = (data: any) => {
  return request({
    url: `/app/mes/exception/save`,
    method: 'POST',
    data,
  });
};

// 编辑异常
export const exceptionEdit = (data: any) => {
  return request({
    url: `/app/mes/exception/edit`,
    method: 'POST',
    data,
  });
};

// 处理异常
export const exceptionHandle = (data: any) => {
  return request({
    url: `/app/mes/exception/handle`,
    method: 'POST',
    data,
  });
};

// 作废异常
export const exceptionCancel = (data: any) => {
  return request({
    url: `/app/mes/exception/cancel`,
    method: 'POST',
    data,
  });
};

// 异常重新调查
export const exceptionReInvestigate = (data: any) => {
  return request({
    url: `/app/mes/exception/reInvestigate`,
    method: 'POST',
    data,
  });
};

// 批次异常信息分页
export const getExceptionBatchPage = (params: any) => {
  return request({
    url: `/app/mes/exception/batch/page`,
    method: 'GET',
    params,
  });
};

// 批次辅助记录列表查询
export const getSubRecordList = (params: any) => {
  return request({
    url: `/app/mes/flow/subRecordList`,
    method: 'GET',
    params,
  });
};

// 批次辅助记录列表查询
export const getSubsidiaryList = (params: any) => {
  return request({
    url: `/app/mes/execute/subsidiary/list`,
    method: 'GET',
    params,
  });
};
