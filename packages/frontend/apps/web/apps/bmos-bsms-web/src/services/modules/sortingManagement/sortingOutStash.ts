import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 分拣出库 ---------------

/**
 * @description: 待出库血浆列表 /sorting-out-warehouse/page
 */
export const getSortingOutWarehouseList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-out-warehouse/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 待出库血浆详情列表 /sorting-out-warehouse/detail/page
 */
export const getSortingOutWarehouseDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-out-warehouse/detail/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 待出库血浆详情数量列表 /sorting-out-warehouse/quantity/page
 */
export const getSortingOutWarehouseQuantityList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-out-warehouse/quantity/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 分拣出库 /sorting-out-warehouse/out/{checkNo}
 */
export const sortingOutWarehouse = (checkNo: string) => {
  return request({
    url: `${BASE_URL}/sorting-out-warehouse/out/${checkNo}`,
    method: 'PUT',
  });
};

/**
 * @description: 整盘出库 /sorting-out-warehouse/out/{checkNo}/{bigContainerNo}
 */
export const sortingOutBigContainer = (checkNo: string, bigContainerNo: string) => {
  return request({
    url: `${BASE_URL}/sorting-out-warehouse/out/${checkNo}/${bigContainerNo}`,
    method: 'PUT',
  });
};
