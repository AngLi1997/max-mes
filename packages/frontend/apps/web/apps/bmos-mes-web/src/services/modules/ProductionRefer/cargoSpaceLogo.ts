import request from '../../service';

// 获取货位日志列表
export const getCargoSpaceLogPageApi = (params: any) => {
  return request({
    url: '/app/mes/storage/log/page',
    method: 'get',
    params,
  });
};

// 获取产品树
export const getProductTreeApi = () => {
  return request({
    url: '/app/mes/product/material/productTree',
    method: 'get',
    params: {
      categoryType: 2,
    },
  });
};
