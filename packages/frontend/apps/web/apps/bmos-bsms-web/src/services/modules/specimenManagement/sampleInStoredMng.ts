import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------待入库标本管理---------------

/**
 * @description: 分页查询 /sample-wait-in-storage/page-list
 */
export const getWaitInStorageList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-wait-in-storage/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表分页 /sample-wait-in-storage/detail-list/{batchNo}
 */
export const getWaitInStorageDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-wait-in-storage/detail-list/${data.batchNo}`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取运输信息 /sample-wait-in-storage/transport
 */
export const getWaitInStorageTransport = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-wait-in-storage/transport`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 数据回显 /sample-wait-in-storage/show
 *
 */
export const getWaitInStorageEcho = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-wait-in-storage/show`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库验收 /sample-wait-in-storage/in-stock-acceptance
 */
export const waitInStorageAcceptance = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-wait-in-storage/in-stock-acceptance`,
    method: 'POST',
    data,
  });
};

/**
 * @description: B2、B7_样本验收详情 /sample-detail/acceptance/detail
 */
export const getSampleAcceptanceDetail = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-detail/acceptance/detail`,
    method: 'GET',
    params,
  });
};
