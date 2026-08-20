import request from '../request';
// 审核流追溯相关接口
// 左侧审核树
export const GetExamineList = async (params: any) => {
  return await request({
    url: '/api/app/mes/audit/list/flow/audit/category',
    method: 'get',
    params,
  });
};
// 左侧审核树新接口(节点加上流程)
export const GetExamineList2 = async (params: any) => {
  return await request({
    url: '/api/app/mes/audit/flow/audit/history/category',
    method: 'get',
    params,
  });
};

// 查询右边表格
export const GetAuditHistory = async (params: any) => {
  return await request({
    url: '/api/app/mes/audit/list/audit/history',
    method: 'get',
    params,
  });
};

// 审核流导出
export const ExamineExport = async (params: any) => {
  return await request({
    url: '/api/app/mes/audit/export/audit/history',
    method: 'get',
    params,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111020001',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};
// 在调用审批流追溯日志导出接口前先调一下这个
export const reqLogExportSaveExamineTraceability = async (data: any) => {
  return await request({
    url: '/api/app/platform/log/export/save',
    method: 'post',
    data,
    responseType: 'arraybuffer',
    headers: {
      'Bmos-MenuId': '111020001',
      'Bmos-Operation': 3,
      'Bmos-Operation-Business': encodeURIComponent(t('导出') || ''),
    },
  });
};

// 查看查历史数据表格
export const GetTaskHistoryList = async (params: any) => {
  return await request({
    url: '/api/app/mes/audit/list/task/history',
    method: 'get',
    params,
  });
};

// 审核流查看详情页的导出
export const ExamineDetailExport = async (params: any) => {
  return await request({
    url: '/api/app/mes/audit/export/task/history',
    method: 'get',
    params,
    responseType: 'arraybuffer',
  });
};
