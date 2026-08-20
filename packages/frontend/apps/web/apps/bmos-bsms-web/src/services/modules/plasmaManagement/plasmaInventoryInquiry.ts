import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------血浆库存查询---------------

/**
 * @description: 分页查询 /plasma-inventory/page
 */
export const getPlasmaInventoryList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-inventory/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆维护 /plasma-inventory/maintain
 */
export const plasmaInventoryMaintain = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-inventory/maintain`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆库存详情 /plasma-inventory/{plasmaOrgNo}
 */
export const getPlasmaInventoryDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-inventory/${data.plasmaOrgNo}`,
    method: 'GET',
  });
};

/**
 * @description: 血浆详情操作记录查询 /plasma-inventory/operation/{plasmaOrgNo}
 */
export const getPlasmaInventoryOperation = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-inventory/operation/${data.plasmaOrgNo}`,
    method: 'GET',
  });
};
