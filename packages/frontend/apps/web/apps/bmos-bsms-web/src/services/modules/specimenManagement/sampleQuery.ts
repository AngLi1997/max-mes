import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------已入库标本查询---------------

/**
 * @description: 分页查询 /sample-in-warehouse-query/page-list
 */
export const getSampleInWarehouseQueryList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-warehouse-query/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 样本状态维护 /sample-in-warehouse-query/maintain
 */
export const sampleInWarehouseQueryMaintain = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-in-warehouse-query/maintain`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 已入库样本查询详情 /sample-detail/in-warehouse/detail
 */
export const getSampleInWarehouseDetail = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-detail/in-warehouse/detail`,
    method: 'GET',
    params,
  });
};
