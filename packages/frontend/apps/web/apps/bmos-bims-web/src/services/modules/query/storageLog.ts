import request from '../../service';

/**
 * @description: 仓库查询 - 分页查询货位日志 /api/app/wms/log/position/page
 */
export const reqLogPositionPage = (params: any) => {
  return request({
    url: `/app/wms/log/position/page`,
    method: 'GET',
    params,
  });
};
