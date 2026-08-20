import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料报废批准---------------

/**
 * @description: 批准审核分页列表 /material/use/scrap/approve/page
 */
export const getMaterialUseScrapApprovePage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/scrap/approve/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批准 /material/use/scrap/approve
 */
export const materialUseScrapApprove = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/scrap/approve`,
    method: 'POST',
    data,
  });
};
