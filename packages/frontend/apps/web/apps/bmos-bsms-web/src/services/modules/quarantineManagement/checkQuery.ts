import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------核查查询---------------

/**
 * @description: 分页查询 /quarantine/check/page
 */
export const getCheckQueryList = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/check/page`,
    method: 'POST',
    data,
  });
};

// /**
//  * @description: 审核 /quality-guarantee/examination/audit
//  */
// export const auditSpecimenExamination = (data: any) => {
//   return request({
//     url: `${BASE_URL}/quality-guarantee/examination/audit`,
//     method: 'PUT',
//     data,
//   });
// }

/**
 * @description: 检疫期核查详情 /quarantine/check/detail/{id}
 */
export const getCheckQueryDetail = (id: string) => {
  return request({
    url: `${BASE_URL}/quarantine/check/detail/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 核查明细分页 /quarantine/check/detail-page
 */
export const getCheckQueryDetailPage = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/check/detail-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 核查份数详情分页 /quarantine/check/detail/page
 */
export const getCheckQueryDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/check/detail/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 核查份数详情导出 /quarantine/check/detail/export
 */
export const exportCheckQueryDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/check/detail/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
