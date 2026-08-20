import request from '../../service';

// 皮重管理相关接口

// 创建皮重配置
export const reqTareWeighConfigCreate = (data: any) => {
  return request({
    url: '/app/mes/tareWeigh/config/create',
    method: 'POST',
    data,
  });
};

// 编辑皮重配置
export const reqTareWeighConfigEdit = (data: any) => {
  return request({
    url: `/app/mes/tareWeigh/config/edit`,
    method: 'PUT',
    data,
  });
};

// 删除皮重配置
export const reqTareWeighConfigDelete = (params: any) => {
  return request({
    url: `/app/mes/tareWeigh/config/delete`,
    method: 'DELETE',
    params,
  });
};

// 分页查询皮重配置
export const reqTareWeighConfigPage = (params: any) => {
  return request({
    url: '/app/mes/tareWeigh/config/page',
    method: 'GET',
    params,
  });
};

// 根据id查询皮重配置(查详情)
export const reqTareWeighConfigQueryById = (params: any) => {
  return request({
    url: '/app/mes/tareWeigh/config/queryById',
    method: 'GET',
    params,
  });
};
