import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------血浆外观检验---------------

/**
 * @description: 分页查询 /plasma-appearance/page
 */
export const getAppearanceList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆外观检验二级列表 /plasma-appearance/detail/page
 */
export const getAppearanceDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance/detail/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 外观检验 /plasma-appearance/execute
 */
export const appearanceExecute = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance/execute`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 整批出库 /plasma-appearance/warehouse/out
 */
export const appearanceWarehouseOut = (params: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance/warehouse/out`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 整批回库 /plasma-appearance/warehouse/in
 */
export const appearanceWarehouseIn = (params: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance/warehouse/in`,
    method: 'POST',
    params,
  });
};

/**
 * @description: 血浆外观检验详情 /plasma-appearance/{plasmaOrgNo}
 */
export const getAppearanceDetail = (plasmaOrgNo: string) => {
  return request({
    url: `${BASE_URL}/plasma-appearance/${plasmaOrgNo}`,
    method: 'GET',
  });
};
