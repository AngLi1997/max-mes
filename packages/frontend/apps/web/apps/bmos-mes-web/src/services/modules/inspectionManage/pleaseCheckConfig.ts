import request from '../../service';

/**
 * @description /api/app/mes/inspect/config/bind/material 请验单绑定物料
 * @param {Object} data
 */
export const reqInspectConfigBindMaterial = (data: Record<string, any>) => {
  return request({
    url: '/app/mes/inspect/config/bind/material',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/inspect/config/delete/{id} 删除请验单
 * @param {string} id 请验单id
 */
export const reqInspectConfigDelete = (id: string) => {
  return request({
    url: `/app/mes/inspect/config/delete/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description /api/app/mes/inspect/config/disable/{id} 停用请验单
 * @param {string} id 请验单id
 */
export const reqInspectConfigDisable = (id: string) => {
  return request({
    url: `/app/mes/inspect/config/disable/${id}`,
    method: 'PUT',
  });
};

/**
 * @description /api/app/mes/inspect/config/enable/{id} 启用请验单
 * @param {string} id 请验单id
 */
export const reqInspectConfigEnable = (id: string) => {
  return request({
    url: `/app/mes/inspect/config/enable/${id}`,
    method: 'PUT',
  });
};

/**
 * @description /api/app/mes/inspect/config/queryDetail/{id} 获取请验单详情
 * @param {string} id 请验单id
 */
export const reqInspectConfigQueryDetail = (id: string) => {
  return request({
    url: `/app/mes/inspect/config/queryDetail/${id}`,
    method: 'GET',
  });
};

/**
 * @description /api/app/mes/inspect/config/queryList 获取请验单列表
 * @param {Object} params 查询参数
 */
export const reqInspectConfigQueryList = (params: Record<string, any>) => {
  return request({
    url: '/app/mes/inspect/config/queryList',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/inspect/config/save 请验单配置保存
 * @param {Object} data 参数
 */
export const reqInspectConfigSave = (data: Record<string, any>) => {
  return request({
    url: '/app/mes/inspect/config/save',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/inspect/config/update 请验单配置修改
 * @param {Object} data 参数
 */
export const reqInspectConfigUpdate = (data: Record<string, any>) => {
  return request({
    url: '/app/mes/inspect/config/update',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/inspect/config/query/material/{id} 获取请验单已经绑定的物料
 * @param {string} id 请验单id
 */
export const reqInspectConfigQueryMaterial = (id: string) => {
  return request({
    url: `/app/mes/inspect/config/query/material/${id}`,
    method: 'GET',
  });
};
