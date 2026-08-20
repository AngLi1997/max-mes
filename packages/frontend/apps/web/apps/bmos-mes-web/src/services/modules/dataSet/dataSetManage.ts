import request from '../../service';

/**
 * @description /api/app/mes/dataset/category/tree 查询数据集分类树
 */
export const reqDatasetCategoryTree = () => {
  return request({
    url: '/app/mes/dataset/category/tree',
    method: 'GET',
  });
};

/**
 * @description /api/app/mes/dataset/category/createCategory 新增数据集分类
 */
export const reqDatasetCategoryCreateCategory = (data: any) => {
  return request({
    url: '/app/mes/dataset/category/createCategory',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/dataset/category/delete 删除数据集分类
 */
export const reqDatasetCategoryDelete = (id: string) => {
  return request({
    url: '/app/mes/dataset/category/delete',
    method: 'DELETE',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/dataset/category/editCategory 修改数据集分类
 */
export const reqDatasetCategoryEditCategory = (data: any) => {
  return request({
    url: '/app/mes/dataset/category/editCategory',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/dataset/queryDatasetPage 分页查询数据集
 */
export const reqDatasetQueryDatasetPage = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetPage',
    method: 'GET',
    params,
  });
};
/**
 * @description /api/app/mes/dataset/queryDatasetLotReleaseLinksPage 分页查询批签发引用
 */
export const reqDatasetQueryDatasetLotReleaseLinksPage = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetLotReleaseLinksPage',
    method: 'GET',
    params,
  });
};
/**
 * @description /api/app/mes/dataset/queryDatasetPointPage 分页查询数据点
 */
export const reqDatasetQueryDatasetPointPage = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetPointPage',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/dataset/previewDatasetLotReleaseLinkData 预览数据点(批签发引用)
 */
export const reqDatasetPreviewDatasetLotReleaseLinkData = (data: any) => {
  return request({
    url: '/app/mes/dataset/previewDatasetLotReleaseLinkData',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/dataset/previewDatasetPointData 预览数据点(批记录数据)
 */
export const reqDatasetPreviewDatasetPointData = (data: any) => {
  return request({
    url: '/app/mes/dataset/previewDatasetPointDataList',
    method: 'POST',
    data,
  });
};
/**
 * @description /api/app/mes/dataset/queryDatasetDetail 查询数据集详情
 */
export const reqDatasetQueryDatasetDetail = (params: any) => {
  return request({
    url: '/app/mes/dataset/queryDatasetDetail',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/dataset/createDataset 创建数据集
 */
export const reqDatasetCreateDataset = (data: any) => {
  return request({
    url: '/app/mes/dataset/createDataset',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/dataset/editDataset 修改数据集
 */
export const reqDatasetEditDataset = (data: any) => {
  return request({
    url: '/app/mes/dataset/editDataset',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/dataset/delete 修改数据集
 * @param id 数据集id
 */
export const reqDatasetDeleteDataset = (id: string) => {
  return request({
    url: '/app/mes/dataset/delete',
    method: 'Delete',
    params: {
      id,
    },
  });
};
