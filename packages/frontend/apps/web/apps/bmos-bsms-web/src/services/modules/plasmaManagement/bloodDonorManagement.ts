import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------献浆者管理---------------

/**
 * @description: 分页查询 /plasma-donor-info/page
 */
export const getBloodDonorList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-donor-info/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 导出 /plasma-donor-info/export
 */
export const plasmaDonorInfoExport = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-donor-info/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
    // headers: {
    //   'Content-Type': 'multipart/form-data',
    // }
  });
};

/**
 * @description: 获取献浆者详情 /plasma-donor-info/{id}
 */
export const getBloodDonorDetail = (id: string) => {
  return request({
    url: `${BASE_URL}/plasma-donor-info/${id}`,
    method: 'GET',
  });
};
