import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------核查批次数据查询---------------

/**
 * @description: 分页查询 /sorting-check/page
 */
export const getSortingCheckList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-check/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 核查批次分拣计划列表(二级列表) /sorting-check/plan/page
 */
export const getSortingCheckPlanList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-check/plan/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 核查批次分拣计划详情列表 /sorting-check/plan/detail/page
 */
export const getSortingCheckPlanDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-check/plan/detail/page`,
    method: 'POST',
    data,
  });
};