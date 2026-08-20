import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 出库血浆统计 -----------------

/**
 * @description: 一级列表 /outbound/statistics/page
 */
export const getOutboundStatisticsPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/statistics/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 二级列表 /outbound/statistics/outbound/page
 */
export const getOutboundStatisticsOutboundPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/statistics/outbound/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 出库计划血浆导出 /outbound/statistics/export
 */
export const exportOutboundStatistics = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/statistics/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
}