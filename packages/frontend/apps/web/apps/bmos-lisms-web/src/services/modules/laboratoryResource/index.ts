import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 查询领用下拉框接口 /laboratory/use/inventory/list
 */
export const getLaboratoryUseInventoryList = (params: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/inventory/list`,
    method: 'GET',
    params,
  });
};

/**
 * @description: E1-3、E1-4、E1-5、E1-6:页面查询 /laboratory/use/page/audit
 */
export const getLaboratoryUseAuditPage = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/page/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 领用库消耗审核接口 /laboratory/use/deplete/audit
 */
export const laboratoryUseDepleteAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/deplete/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 领用库报废审核接口 /laboratory/use/scrap/audit
 */
export const laboratoryUseScrapAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/scrap/audit`,
    method: 'POST',
    data,
  });
};

export * from './equipment';
export * from './inventoryManagement';
export * from './inventoryQuery';
