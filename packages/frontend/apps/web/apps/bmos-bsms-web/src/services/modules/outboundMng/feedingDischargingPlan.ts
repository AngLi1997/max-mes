import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 投料出库审核 -----------------

/**
 * @description: 分页查询(K2 ~ K4) /outbound-process/page
 */
export const getOutboundProcessPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 分页查询(K5) /outbound-process/quality/page
 */
export const getOutboundProcessQualityPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/quality/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: K2、K3、K4、K5 二级列表 /outbound-process/plasma/page
 */
export const getOutboundProcessPlasmaPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/plasma/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 审核 /outbound-process/process
 */
export const auditOutboundProcess = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/process`,
    method: 'PUT',
    data,
  });
}

/**
 * @description: 流程进度 /outbound-process/process/info
 */
export const getOutboundProcessInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/process/info`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 操作记录 /outbound-process/process/log
 */
export const getOutboundProcessLog = (params: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/process/log`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 审核明细 /outbound-process/process/detail
 */
export const getOutboundProcessDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound-process/process/detail`,
    method: 'POST',
    data,
  });
}