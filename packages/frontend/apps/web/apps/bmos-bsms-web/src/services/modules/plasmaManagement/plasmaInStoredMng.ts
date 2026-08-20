import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------待入库血浆管理---------------

/**
 * @description: 分页查询 /plasma-to-warehouse/page
 */
export const getPlasmaToStorageList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-to-warehouse/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表查询 /plasma-to-warehouse/detail/page
 */
export const getPlasmaToStorageDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-to-warehouse/detail/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取入库验收列表 /plasma-to-warehouse/accept/page
 */
export const getPlasmaToStorageAcceptList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-to-warehouse/accept/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 入库验收 /plasma-to-warehouse/accept
 */
export const plasmaToStorageAccept = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-to-warehouse/accept`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取运输信息 /plasma-to-warehouse/transport
 */
export const getPlasmaToStorageTransport = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-to-warehouse/transport`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆批次详情 /plasma-to-warehouse/{syncBatchNo}
 */
export const getPlasmaToStorageDetail = (syncBatchNo: string) => {
  return request({
    url: `${BASE_URL}/plasma-to-warehouse/${syncBatchNo}`,
    method: 'GET',
  });
}