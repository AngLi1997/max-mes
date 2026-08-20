import { BASE_URL } from '@/services/baseUrl';
import { getItem } from '@/utils';
import axios from '@bmos/axios';
import request from '../../service';

// /api/app/wms/record/fileUpload
// 文件上传统一接口
export const fileUpload = (data: FormData) => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data',
      token: getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
      'bmos-access-token': getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
    },
  };
  return axios.post('/api/app/wms/record/fileUpload', data, config);
};

// 根据code下载模板文件
export const downloadTemplateFile = (fileName: string) => {
  return request({
    url: `${BASE_URL}/file/download/template`,
    method: 'GET',
    params: {
      fileName,
    },
    responseType: 'arraybuffer',
    original: true,
  });
};

// /api/app/wms/record/record/item/upload
export const recordItemUpload = (data: FormData) => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data',
      token: getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
      'bmos-access-token': getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
    },
  };
  return axios.post('/api/app/wms/record/record/item/upload', data, config);
};

// /api/app/platform/file/upload
export const commonFileUpload = (data: FormData) => {
  return request({
    url: `/app/platform/file/upload`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
      'Bmos-Access-Token': getItem('BMOS-ACCESS-TOKEN'),
    },
  });
};

// 文件下载统一接口
export const fileDownload = (path: string) => {
  return request({
    url: '/app/platform/file/download',
    method: 'GET',
    params: {
      path,
    },
    headers: {
      'Bmos-Access-Token': getItem('BMOS-ACCESS-TOKEN'),
    },
    responseType: 'arraybuffer',
  });
};
