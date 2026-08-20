import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料抽检---------------

/**
 * @description: 分页列表 /material/use/spot-check/page
 */
export const getMaterialUseSpotCheckPage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 提交抽检 /material/use/spot-check/submit
 */
export const materialUseSpotCheckSubmit = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check/submit`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 撤销抽检 /material/use/spot-check/revert
 */
export const materialUseSpotCheckRevert = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/spot-check/revert`,
    method: 'PUT',
    data,
  });
};
