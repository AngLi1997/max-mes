import request from '../../service';
// 批次摘要接口

// 新增批次摘要
export const lotSummaryCreate = (data: any) => {
  return request({
    url: '/app/mes/lotSummary/create',
    method: 'POST',
    data,
  });
};

// 删除批次摘要
export const lotSummaryDelete = (params: any) => {
  return request({
    url: '/app/mes/lotSummary/delete',
    method: 'DELETE',
    params,
  });
};

// 编辑批次摘要
export const lotSummaryEdit = (data: any) => {
  return request({
    url: '/app/mes/lotSummary/edit',
    method: 'PUT',
    data,
  });
};

// 导出批次摘要生产数据分页
export const exportProductDataPage = (data: any) => {
  return request({
    url: '/app/mes/lotSummary/exportProductDataPage',
    method: 'POST',
    data,
    responseType: 'arraybuffer',
  });
};

// 查询批次摘要详情
export const lotSummaryQueryDetail = (params: any) => {
  return request({
    url: '/app/mes/lotSummary/queryDetail',
    method: 'GET',
    params,
  });
};

// 分页查询批次摘要
export const lotSummaryQueryPage = (params: any) => {
  return request({
    url: '/app/mes/lotSummary/queryPage',
    method: 'GET',
    params,
  });
};

// 查询批次摘要生产数据分页
export const queryProductDataPage = (params: any) => {
  return request({
    url: '/app/mes/lotSummary/queryProductDataPage',
    method: 'GET',
    params,
  });
};

// 查询数据集详情
export const queryDatasetDetailApi = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetDetail',
    method: 'GET',
    params,
  });
};

// 根据工艺id查询数据集列表
export const queryDatasetListByProcessIdApi = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetListByProcessId',
    method: 'GET',
    params,
  });
};
