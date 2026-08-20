import request from '../../service';

/**
 * @description /api/app/mes/plan/info/page 分页查询生产信息
 */
export const reqPlanInfoPage = (params: any) => {
  return request({
    url: '/app/mes/plan/info/page',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/archive/page 分页查询批记录数据
 */
export const reqLotRecordsManageQueryPage = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/page',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/archive/list 版本管理-生产信息
 */
export const reqLotRecordsManagePlanArchiveList = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/list',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/archive/history/list 查看历史
 *
 */
export const reqLotRecordsManageHistoryList = (id: any) => {
  return request({
    url: '/app/mes/plan/archive/history/list',
    method: 'GET',
    params: {
      archiveId: id,
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/productTree 获取批记录产品树
 */
export const reqLotRecordsManageProductTree = (templateId?: string) => {
  return request({
    url: '/app/mes/plan/archive/productTree',
    method: 'GET',
    params: {
      categoryType: 2,
      ...(templateId ? { templateId } : {}),
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/record/page 分页查询生产信息下生成的批记录
 */
export const reqLotRecordsManageRecordPage = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/record/page',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/info/relation/plan/list 批记录查询批次引用列表
 */
export const reqLotRecordsManageGetDynamicReportItem = (planId: string) => {
  return request({
    url: '/app/mes/plan/info/relation/plan/list',
    method: 'GET',
    params: {
      planId,
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/judge/generate 判断记录是否生成完成
 */
export const reqLotRecordsManageJudgeGenerate = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/judge/generate',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/archive/path/download 根据路径下载批记录
 * @param {String} path 路径
 */
export const reqLotRecordsManageDownload = (path: string) => {
  return request({
    url: '/app/mes/plan/archive/path/download',
    method: 'GET',
    params: {
      path,
    },
    responseType: 'arraybuffer',
  });
};

/**
 * @description /api/app/mes//plan/archive/template/version/normal 查询模板下的默认模板版本
 */
export const reqLotRecordsTemplateVersionNormal = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/template/version/normal',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/archive/generate 批记录生成
 */
export const reqLotRecordsManageGenerate = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/generate',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/download 批记录下载
 */
export const reqLotRecordsManageDownloadById = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/download',
    method: 'GET',
    params,
    responseType: 'arraybuffer',
  });
};

/**
 * @description /api/app/mes/plan/archive/audit 提交审批
 */
export const reqLotRecordsManageAudit = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/audit',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/scrap 审批作废
 */
export const reqLotRecordsManageScrap = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/scrap',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/reGenerate 重新生成批记录
 */
export const reqLotRecordsManageReGenerate = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/reGenerate',
    method: 'PUT',
    data,
  });
};

// 批记录管理提供直接确认生效的按钮
export const reqPlanArchiveEffective = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/effective',
    method: 'PUT',
    data,
  });
};
