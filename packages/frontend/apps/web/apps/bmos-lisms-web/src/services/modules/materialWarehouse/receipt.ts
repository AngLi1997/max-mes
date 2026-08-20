import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料接收---------------

/**
 * @description: 分页查询 /material/receive/page
 */
export const getMaterialReceivePage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/receive/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 物料接收 /material/receive
 */
export const materialReceive = (data: any) => {
  return request({
    url: `${BASE_URL}/material/receive`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 物料抽检历史 -- 返回最后抽检时间 /material/outSpotCheck/history
 */
export const getMaterialOutSpotCheckHistory = (data: any) => {
  return request({
    url: `${BASE_URL}/material/outSpotCheck/history`,
    method: 'POST',
    data,
  });
};
