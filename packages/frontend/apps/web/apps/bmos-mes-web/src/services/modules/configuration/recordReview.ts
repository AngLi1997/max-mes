import request from '../../service';

/**
 * @description: 查询记录审核页面 /api/app/mes/record/audit/page/record/audit
 * @param {API.MesProcessPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @returns {Promise<any>} 返回一个promise
 */

export const reqRecordAuditPage = (
  params: API.MesProcessPageReq = {
    pageNum: 1,
    pageSize: 10,
  },
) => {
  return request({
    url: '/app/mes/record/audit/page/record/audit',
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询记录审核页面 /api/app/mes/record/audit/start/flow
 * @param {API.MesProcessPageReq} params 默认参数 pageNum: 1, pageSize: 10
 * @returns {Promise<any>} 返回一个promise
 */

export const reqRecordAuditStartflow = (params: { versionId: string }) => {
  return request({
    url: '/app/mes/record/audit/start/flow',
    method: 'GET',
    params,
  });
};
