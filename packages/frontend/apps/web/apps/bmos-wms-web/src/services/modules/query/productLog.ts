import request from '../../service';

/**
 * @description: 仓库查询 - 分页查询货品日志 /api/app/wms/log/cargo/page
 */
export const reqLogCargoPage = (params: any) => {
  return request({
    url: `/app/wms/log/cargo/page`,
    method: 'GET',
    params,
  });
};
