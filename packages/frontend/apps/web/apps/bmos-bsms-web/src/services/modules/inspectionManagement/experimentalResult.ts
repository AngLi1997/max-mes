import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 检验结果 -----------------

/**
 * @description: C2-C3（子级）_检验结果分页列表查询 /examination/result/page-list
 */
export const getExaminationResultPageList = (data: any) => {
  return request({
    url: `${BASE_URL}/examination/result/page-list`,
    method: 'POST',
    data,
  });
}

/**
 * @description: C2_接收实验室检验结论 /examination/result/result-receive
 */
export const examinationResultReceive = (data: any) => {
  return request({
    url: `${BASE_URL}/examination/result/result-receive`,
    method: 'POST',
    data,
  });
}
