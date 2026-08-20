import request from '../../service';

/**
 * @description: 取消发料 /api/app/wms/sendOut/cancel
 * @param {string} id 发料单工单id
 */
export const reqSendOutCancel = (id: string) => {
  return request({
    url: `/app/wms/sendOut/cancel?id=${id}`,
    method: 'PUT',
  });
};

/**
 * @description: 根据发料工单id查询申请发料列表 /api/app/wms/sendOut/queryDetail
 * @param {string} id 发料单工单id
 */
export const reqSendOutQueryDetail = (id: string) => {
  return request({
    url: `/app/wms/sendOut/queryDetail`,
    method: 'GET',
    params: { id },
  });
};

/**
 * @description: 查询仓库发料分页 /api/app/wms/sendOut/queryPage
 */
export const reqSendOutQueryPage = (params: any) => {
  return request({
    url: `/app/wms/sendOut/queryPage`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 发料 /api/app/wms/sendOut/sendout
 * @param {any} data 发料单
 */
export const reqSendOutSendout = (data: any) => {
  return request({
    url: `/app/wms/sendOut/sendout`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 发料 /api/app/wms/sendOut/submit
 * @param {any} data 发料单
 */
export const reqSendOutSubmit = (data: any) => {
  return request({
    url: `/app/wms/sendOut/submit`,
    method: 'POST',
    data,
  });
};
