import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 核查信息汇总 ---------------

/**
 * @description: M6核查信息汇总-表头 /sorting/tableHeader
 */
export const getTableHeader = () => {
  return request({
    url: `${BASE_URL}/sorting/tableHeader`,
    method: 'GET',
  });
};

/**
 * @description: M6核查信息汇总-列表 /check/record/page
 */
export const getVerificationInformationSummaryList = (data: any) => {
  return request({
    url: `${BASE_URL}/check/record/page`,
    method: 'POST',
    data,
  });
};
