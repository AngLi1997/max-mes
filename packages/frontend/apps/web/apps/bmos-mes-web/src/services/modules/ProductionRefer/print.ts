import request from '../../service';

/**
 * @description /api/app/mes/execute/intact/merge/list 查完整的批记录
 * @param {API.MesDatasetPageReq} params
 */
export const getIntactMergeList = (params: any) => {
  return request({
    url: '/app/mes/execute/intact/merge/list',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/relation/detailWithSelf/{planId} 查关联工艺
 * @param {API.MesDatasetPageReq} params
 */
export const getRelationDetail = (planId: string) => {
  return request({
    url: `/app/mes/plan/relation/detailWithSelf/${planId}`,
    method: 'GET',
  });
};

/**
 * @description /api/app/mes/operation/history/list/{businessId} 查关联工艺
 * @param {API.MesDatasetPageReq} params
 */
export const getHistoryList = (businessId: string) => {
  return request({
    url: `/app/mes/operation/history/list/${businessId}`,
    method: 'GET',
  });
};
