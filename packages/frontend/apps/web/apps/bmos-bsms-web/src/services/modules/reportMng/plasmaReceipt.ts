import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 血浆入库单 ---------------

/**
 * @description: 分页列表 /plasma-receipt/page-list
 */
export const getPlasmaReceiptList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-receipt/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆入库单打印 /plasma-receipt/print
 */
export const getPlasmaReceiptPrint = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-receipt/print`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
