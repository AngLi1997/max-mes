import request from '../../service';

//获取物料日志数据 /api/app/mes/material/log/page
export const reqMaterialLogPage = (params: any) => {
  return request({
    url: `/app/mes/material/log/page`,
    method: 'GET',
    params,
  });
};

// 获取产品树 /api/app/mes/product/material/productTree
export const getMaterialLogTreeApi = (params: any) => {
  return request({
    url: '/app/mes/product/material/productTree',
    method: 'get',
    params,
  });
};

// 获取物料批号树 /api/app/mes/storage/material/batch/listByMaterialId
export const MaterialBatchListByMaterialId = (params: any) => {
  return request({
    url: `/app/mes/storage/material/batch/listByMaterialId`,
    method: 'GET',
    params,
  });
};

//获取物料件号 /api/app/mes/storage/material/list
export const getMaterialListApi = (params: any) => {
  return request({
    url: `/app/mes/storage/material/list`,
    method: 'GET',
    params,
  });
};
