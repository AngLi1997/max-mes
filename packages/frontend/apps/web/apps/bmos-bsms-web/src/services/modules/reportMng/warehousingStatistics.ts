import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 入库统计 ---------------

/**
 * @description: 获取血浆、标本出入库统计 /warehousing-statistics
 */
export const getWarehouseStatisticsInList = (params: any) => {
  return request({
    url: `${BASE_URL}/warehousing-statistics`,
    method: 'GET',
    params,
  });
};

// --------------- 出库统计 ---------------

/**
 * @description: 获取出库统计 /outbound-statistics
 */
export const getWarehouseStatisticsOutList = (params: any) => {
  return request({
    url: `${BASE_URL}/outbound-statistics`,
    method: 'GET',
    params,
  });
};
