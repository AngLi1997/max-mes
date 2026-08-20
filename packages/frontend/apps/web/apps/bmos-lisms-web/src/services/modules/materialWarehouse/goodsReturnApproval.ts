import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------物料退货批准---------------

/**
 * @description: 批准审核分页列表 /material/use/return/approve/page
 */
export const getMaterialUseReturnApprovePage = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/return/approve/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 批准 /material/use/return/approve
 */
export const materialUseReturnApprove = (data: any) => {
  return request({
    url: `${BASE_URL}/material/use/return/approve`,
    method: 'POST',
    data,
  });
};
