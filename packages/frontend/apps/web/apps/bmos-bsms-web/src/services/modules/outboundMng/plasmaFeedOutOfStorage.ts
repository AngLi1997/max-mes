import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 血浆投料出库 -----------------

/**
 * @description: K6、K7、K8、K9 一级列表 /outbound/page
 */
export const getOutboundPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: K6、K7、K8、K9 二级列表 /outbound/sorting/page
 */
export const getOutboundSortingPage = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/sorting/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: K6、K7、K8、K9 合并出库与整盘出库 /outbound/canDelivery
 */
export const outboundCanDelivery = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/canDelivery`,
    method: 'POST',
    data,
  });
}