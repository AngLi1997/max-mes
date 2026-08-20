import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 检验数据查询 ---------------

/**
 * @description: 分页查询 /check/data/page
 */
export const getInspectionDataList = (data: any) => {
  return request({
    url: `${BASE_URL}/check/data/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 导出 /check/data/export
 */
export const exportInspectionData = (data: any) => {
  return request({
    url: `${BASE_URL}/check/data/export`,
    method: 'POST',
    responseType: 'arraybuffer',
    original: true,
    data,
  });
};
