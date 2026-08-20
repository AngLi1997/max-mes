import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------预警管理---------------

/**
 * @description: 物料到期预警 /material/warn/expired/page
 */
export const getMaterialWarnExpiredPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/warn/expired/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 物料最低库存预警 /material/warn/inventory/page
 */
export const getMaterialWarnInventoryPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/warn/inventory/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 供应商到期预警 /material/warn/supplier/page
 */
export const getMaterialWarnSupplierPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/warn/supplier/page`,
    method: 'POST',
    data,
  });
};
