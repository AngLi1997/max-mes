import request from '../../service';

/**
 * @description /api/app/mes/lotRelease/manage/queryPlanPage 分页查询批签发生产计划数据
 */
export const reqLotReleaseManageQueryPlanPage = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/queryPlanPage',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/queryPage 分页查询批签发数据
 */
export const reqLotReleaseManageQueryPage = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/queryPage',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/generate 生成批签发
 */
export const reqLotReleaseManageGenerate = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/generate',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/history 查看历史
 * @param {String} id 批签发id
 */
export const reqLotReleaseManageHistory = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/manage/history',
    method: 'GET',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/scrap 作废
 * @param {String} id 批签发id
 */
export const reqLotReleaseManageScrap = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/manage/scrap',
    method: 'PUT',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/submit 提交审核
 */
export const reqLotReleaseManageSubmit = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/submit',
    method: 'PUT',
    params,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/uploadExcel 上传批签发文件
 */
export const reqLotReleaseManageUploadExcel = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/uploadExcel',
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};
/**
 * @description /api/app/mes/lotRelease/manage/productTree 获取批记录产品树
 */
export const reqLotReleaseManageProductTree = (templateId?: string) => {
  return request({
    url: '/app/mes/lotRelease/manage/productTree',
    method: 'GET',
    params: {
      categoryType: 2,
      ...(templateId ? { templateId } : {}),
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/queryVersionPage 分页查询批签发版本数据
 */
export const reqLotReleaseManageQueryVersionPage = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/queryVersionPage',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/updateExcelFile 提交审核
 */
export const reqLotReleaseManageUpdateExcelFile = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/updateExcelFile',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/generate 生成批签发
 */
export const reqLotReleaseGenerate = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/generate',
    method: 'POST',
    data,
  });
};
/**
 * @description /api/app/mes/plan/info/relation/plan/list 批签发查询批次引用列表
 * @param {String} planId 生产计划id
 */
export const reqPlanInfoRelationPlanList = (planId: string) => {
  return request({
    url: '/app/mes/plan/info/relation/plan/list',
    method: 'GET',
    params: {
      planId,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/download 下载批签发
 * @param {String} id 批签发的id
 */
export const reqLotReleaseManageDownload = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/manage/download',
    method: 'POST',
    params: {
      id,
    },
    responseType: 'arraybuffer',
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/getDynamicReportItem 查询生成批签发需要动态填报的数据
 */
export const reqLotReleaseManageGetDynamicReportItem = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/getDynamicReportItem',
    method: 'POST',
    data,
  });
};
/**
 * @description /api/app/mes/plan/info/listUnTerminatePlanByProcessId 根据工艺id 获取生产批号列表
 * @param {String} processId 工艺id
 */
export const reqPlanInfoListUnTerminatePlanByProcessId = (processId: string) => {
  return request({
    url: '/app/mes/plan/info/listUnTerminatePlanByProcessId',
    method: 'GET',
    params: {
      processId,
    },
  });
};
