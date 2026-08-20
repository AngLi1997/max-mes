import request from '../../service';

//称量日志-分页 /api/app/mes/ingredient/weigh/log/page
export const getWeighLogPage = (params: any) => {
  return request({
    url: '/app/mes/ingredient/weigh/log/page',
    method: 'get',
    params,
  });
};
