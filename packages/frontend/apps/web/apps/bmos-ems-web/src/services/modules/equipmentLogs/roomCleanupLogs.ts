import request from '../../service';

//房间状态清洁日志分页查询 /api/app/platform/factory/room/log/page
export const getPlatformFactoryRoomLogPage = (params: any) => {
  return request({
    url: '/app/platform/factory/room/log/page',
    method: 'get',
    params,
  });
};

//导出房间清场日志 /api/app/platform/factory/room/log/export
export const getPlatformFactoryRoomLogExport = (params: any) => {
  return request({
    url: '/app/platform/factory/room/log/export',
    method: 'get',
    params,
    responseType: 'arraybuffer',
  });
};

/**
 * @description /api/app/mes/product/material/productTree 获取所有产品树，包括半成品
 */
export const datasetAllProductTree = () => {
  return request({
    url: `/app/mes/product/material/productTree`,
    method: 'GET',
    params: {
      categoryType: 2,
    },
  });
};
