import request from '../../service';

// 获取有生效版本的工艺
export const getProcessListTreeReq = (params: any) => {
  return request({
    url: `/app/mes/process/list/tree`,
    method: 'GET',
    params,
  });
};
// 根据工艺及其版本获取对应产线列表
export const getFactoryLineListByProcessVersion = async (params: any) => {
  return await request({
    url: `/app/mes/factory/line/listByProcessVersion`,
    method: 'GET',
    params,
  });
};
// 查询生产进度详情
export const postProgressDashboardDetail = async (data: any) => {
  return await request({
    url: `/app/query/progress/dashboard/detail`,
    method: 'POST',
    data,
  });
};
