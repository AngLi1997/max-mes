import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------放行单审核---------------

/**
 * @description: F3-F4_放行单审核/查询列表 /quality-guarantee/release/page
 */
export const getQualityGuaranteeReleaseList = (data: any) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /quality-guarantee/note/audit
 */
export const auditQualityGuaranteeRelease = (data: any) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/note/audit`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 打印放行单 /quality-guarantee/release-note/print/{id}
 */
export const printQualityGuaranteeRelease = (id: string) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release-note/print/${id}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 放行单详情 /quality-guarantee/release/detail/{id}
 */
export const getQualityGuaranteeReleaseDetail = (id: string) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release/detail/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 放行单操作日志 /quality-guarantee/release/operation/log/{id}
 */
export const getQualityGuaranteeReleaseOperationLog = (id: string) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release/operation/log/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 放行单文件编号 /quality-guarantee/release-permit/number
 */
export const getQualityGuaranteeReleasePermitNumber = () => {
  return request({
    url: `${BASE_URL}/quality-guarantee/release-permit/number`,
    method: 'GET',
  });
};
