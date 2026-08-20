import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本拒收---------------

/**
 * @description: 分页查询 /sample/reject/apply/page
 */
export const getSampleRejectPage = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/reject/apply/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本接收/标本拒收：通过编号查询拒收详情 /sample/reject/get/sampleNo/{batchNo}
 */
export const getSampleRejectDetail = (batchNo: string, params: { sampleNo: string } | {} = {}) => {
  return request({
    url: `${BASE_URL}/sample/reject/get/sampleNo/${batchNo}`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 标本拒收：标本拒收申请 /sample/reject/apply
 */
export const getSampleRejectApply = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/reject/apply`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本拒收：备注查询 /sample/reject/get/remark
 */
export const getSampleRejectRemark = (data: any) => {
  return request({
    url: `${BASE_URL}/sample/reject/get/remark`,
    method: 'POST',
    data,
  });
};
