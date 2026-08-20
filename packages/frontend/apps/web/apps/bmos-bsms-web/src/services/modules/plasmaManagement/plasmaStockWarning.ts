import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------血浆库存预警---------------

/**
 * @description: 血浆库存预警列表 /plasma-inventory-warning/page
 */
export const getPlasmaStockInventoryWarningList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-inventory-warning/page`,
    method: 'POST',
    data,
  });
}