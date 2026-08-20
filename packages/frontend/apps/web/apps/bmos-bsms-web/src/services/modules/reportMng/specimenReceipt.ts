import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 标本入库单 ---------------

/**
 * @description: 分页列表 /sample-receipt/page-list
 */
export const getSampleReceiptList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-receipt/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本入库单打印 /sample-receipt/print
 */
export const getSampleReceiptPrint = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-receipt/print`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
