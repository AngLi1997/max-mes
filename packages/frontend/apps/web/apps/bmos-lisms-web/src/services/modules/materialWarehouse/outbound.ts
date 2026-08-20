import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料出库---------------

/**
 * @description: 出库分页列表 /material/use/out/page
 */
export const getMaterialUseOutPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/out/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 出库 /material/use/out
 */
export const materialUseOut = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/out`,
    method: 'PUT',
    data,
  });
};
