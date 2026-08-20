import { getItem } from '@/utils';
import axios from '@bmos/axios';
import request from '../../service';

// /api/app/mes/record/fileUpload
// 文件上传统一接口
export const fileUpload = (data: FormData) => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data',
      token:
        getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
      'bmos-access-token':
        getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
    },
  };
  return axios.post('/api/app/mes/record/fileUpload', data, config);
};

// /api/app/mes/record/record/item/upload
export const recordItemUpload = (data: FormData) => {
  const config = {
    headers: {
      'Content-Type': 'multipart/form-data',
      token:
        getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
      'bmos-access-token':
        getItem('BMOS-ACCESS-TOKEN') || '1433e2d1-d9f8-4481-85a8-b8a8979a285b',
    },
  };
  return axios.post('/api/app/mes/record/record/item/upload', data, config);
};

export const fileDownload = (fileName: string) => {
  const params = {
    fileName,
  };

  return request({
    url: '/common/open/file/download',
    method: 'GET',
    params,
    headers: {
      // 'isc-access-token': getToken(),
      token: getItem('BMOS-ACCESS-TOKEN'),
    },
    responseType: 'arraybuffer',
  });
};
