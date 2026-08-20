import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------检疫期核查数据---------------

/**
 * @description: 分页查询 /quarantine/summary/page
 */
export const getVerificationQuarantineList = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/summary/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 保存检疫期报告 /quarantine/report/save
 */
export const saveQuarantineReport = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/save`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 提交检疫期报告 /quarantine/report/submit
 */
export const submitQuarantineReport = (data: any) => {
  return request({
    url: `${BASE_URL}/quarantine/report/submit`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 查询检疫期报告 /quarantine/report/{checkNo}
 */
export const getQuarantineReport = (checkNo: string) => {
  return request({
    url: `${BASE_URL}/quarantine/report/${checkNo}`,
    method: 'GET',
  });
}