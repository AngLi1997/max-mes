import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------出库血浆核对---------------

/**
 * @description: 二次确认 /outbound/check/user
 */
export const checkUser = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/user`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 在库血浆-一级列表 /outbound/check/page
 */
export const getOutboundCheckList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 在库血浆-二级列表 /outbound/check/plasma/page
 */
export const getOutboundPlasmaCheckList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/plasma/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 核对血浆列表 /outbound/check/batch/detail
 */
export const getOutboundCheckBatchDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/batch/detail`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 重新核对 /outbound/check/back
 */
export const outboundCheckBack = (params: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/back`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 扫描核对 /outbound/check/scan
 */
export const outboundCheckScan = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/scan`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 血浆出库 /outbound/check/delivery
 */
export const outboundCheckDelivery = (params: any) => {
  return request({
    url: `${BASE_URL}/outbound/check/delivery`,
    method: 'GET',
    params
  });
}