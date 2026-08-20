import request from '../../src/utils/request';

export const getProcessListTreeReq = () => {
  return request({
    url: `/api/app/mes//process/list/tree`,
    method: 'GET',
  });
};

export const reqProcedureHistoricListGET = (processId: string, name?: string) => {
  return request({
    url: '/api/app/mes/procedure/historic/list',
    method: 'GET',
    params: {
      processId,
      ...(name && { name }),
    },
  });
};

// 查询生产进度展示工序-康盛科泰 /api/app/mes/product/schedule/config/config/product/schedule/procedure
export const reqProductScheduleProcedureList = () => {
  return request({
    url: '/api/app/mes/product/schedule/config/config/product/schedule/procedure',
    method: 'GET',
  });
};

// 配置生产进度展示工序-康盛科泰 /api/app/mes/product/schedule/config/config/product/schedule/procedure
export const reqProductScheduleProcedureConfig = (data: any) => {
  return request({
    url: '/api/app/mes/product/schedule/config/config/product/schedule/procedure',
    method: 'POST',
    data,
  });
};

// 查询生产进度-康盛科泰 /api/app/mes/product/schedule
export const reqProductScheduleList = (data: any) => {
  return request({
    url: '/api/app/mes/product/schedule',
    method: 'GET',
    params: data,
  });
};
