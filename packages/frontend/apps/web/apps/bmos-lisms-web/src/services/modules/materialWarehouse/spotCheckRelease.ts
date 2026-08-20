import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料抽检放行---------------

/**
 * @description: 分页列表 /material/use/spot-check-pass/page
 */
export const getMaterialUseSpotCheckPassPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check-pass/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 抽检放行 /material/use/spot-check-pass/submit
 */
export const materialUseSpotCheckPassSubmit = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check-pass/submit`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 文件上传 /material/use/spot-check-pass/upload
 */
export const materialUseFileUpload = (data: FormData) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check-pass/upload`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description: 文件下载 /material/use/download
 */
export const materialUseDownload = (params: any) => {
  return request({
    url: `${BASE_URL}/material/use/download`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 获取编辑详情 /material/use/spot-check-pass/detail/{userFormIdentify}
 */
export const getMaterialUseSpotCheckPassDetail = (userFormIdentify: string) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check-pass/detail/${userFormIdentify}`,
    method: 'GET',
  });
};

/**
 * @description: 编辑提交 /material/use/spot-check-pass/update
 */
export const materialUseSpotCheckPassUpdate = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check-pass/update`,
    method: 'PUT',
    data,
  });
};
