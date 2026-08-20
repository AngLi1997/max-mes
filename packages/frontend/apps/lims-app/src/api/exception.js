import request from '@/utils/request/request.js';

// 工艺下拉查询接口 /api/app/mes/process/list
export const reqProcessListAll = params =>
  request.get('/api/app/mes/process/list', params);

// 异常管理分页
export const getExceptionPage = params =>
  request.get('/api/app/mes/exception/page', params);

// 根据工艺查询所有工艺版本
export const getVersionList = processId =>
  request.get('/api/app/mes/process/version/list', { processId });

// 根据工艺版本查询所有生产批次
export const getListPlanByProcess = (processId, processVersion) =>
  request.get('/api/app/mes/plan/info/listPlanByProcess', {
    processId,
    processVersion,
  });

// 根据产品id查询所有批次列表
export const listAllPlanByProductId = params =>
  request.get('/api/app/mes/plan/info/listAllPlanByProductId', params);

// 根据工序id查询步骤列表
export const getListByProcedureModelId = procedureModelId =>
  request.get('/api/app/mes/procedure/step/listByProcedureModelId', {
    procedureModelId,
  });

// 手动添加异常信息
export const exceptionSave = data =>
  request.post('/api/app/mes/exception/save', data, {
    header: {
      'Bmos-MenuId': '121040001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': '新增异常记录',
    },
  });

// 编辑异常
export const exceptionEdit = data =>
  request.post('/api/app/mes/exception/edit', data, {
    header: {
      'Bmos-MenuId': '121040001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '编辑异常记录',
    },
  });

// 处理异常
export const exceptionHandle = data =>
  request.post('/api/app/mes/exception/handle', data, {
    header: {
      'Bmos-MenuId': '121040001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '处理异常记录',
    },
  });

// 作废异常
export const exceptionCancel = data =>
  request.post('/api/app/mes/exception/cancel', data, {
    header: {
      'Bmos-MenuId': '121040001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '作废异常记录',
    },
  });

// 异常重新调查
export const exceptionReInvestigate = data =>
  request.post('/api/app/mes/exception/reInvestigate', data, {
    header: {
      'Bmos-MenuId': '121040001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '重新调查异常记录',
    },
  });

// 查询历史接口
export const reqHistoryList = businessId =>
  request.get(`/api/app/mes/operation/history/list/${businessId}`);
