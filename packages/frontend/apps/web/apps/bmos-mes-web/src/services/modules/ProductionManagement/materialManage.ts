import request from '../../service';

/**
 * @description: 新增物料件 /api/app/mes/storage/material/manage/add
 */
export const reqStorageMaterialManageAdd = (data: any) => {
  return request({
    url: `/app/mes/storage/material/manage/add`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 新增物料批次 /api/app/mes/storage/material/manage/addBatch
 */
export const reqStorageMaterialManageAddBatch = (data: any) => {
  return request({
    url: `/app/mes/storage/material/manage/addBatch`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑物料批次 /api/app/mes/storage/material/manage/editBatch
 */
export const reqStorageMaterialManageEditBatch = (data: any) => {
  return request({
    url: `/app/mes/storage/material/manage/editBatch`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 分页查询物料管理批次分页 /api/app/mes/storage/material/manage/queryBatchPage
 */
export const reqStorageMaterialManageQueryBatchPage = (params: any) => {
  return request({
    url: `/app/mes/storage/material/manage/queryBatchPage`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 分页查询物料管理物料件分页 /api/app/mes/storage/material/manage/queryPage
 */
export const reqStorageMaterialManageQueryPage = (params: any) => {
  return request({
    url: `/app/mes/storage/material/manage/queryPage`,
    method: 'GET',
    params,
  });
};

// 【物料管理】根据物料批次id查询当前批次的自定义字段信息
export const reqStorageMaterialBatchFieldList = (materialBatchId: any) => {
  return request({
    url: `/app/mes/storage/material/batch/field/list/${materialBatchId}`,
    method: 'GET',
  });
};

