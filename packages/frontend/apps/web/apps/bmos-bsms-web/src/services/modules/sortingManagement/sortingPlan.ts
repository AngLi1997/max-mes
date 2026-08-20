import { BASE_URL } from '@/services/baseUrl';
import { getItem } from '@/utils';
import axios from '@bmos/axios';
import request from '../../service';

// ---------------分拣计划---------------

/**
 * @description: 分页查询 /sorting-plan/page
 */
export const getSortingPlanList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取分拣相关信息 /sorting-plan/manage
 */
export const getSortingPlanManage = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/manage`,
    method: 'GET',
    params
  });
};

/**
 * @description: 生成分拣批号 /sorting-plan/no
 */
export const generateSortingPlanNo = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/no`,
    method: 'PUT',
    params,
  });
};

/**
 * @description: 创建分拣计划 /sorting-plan/save
 */
export const createSortingPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取分拣计划详情 /sorting-plan/{planBatchNo}
 */
export const getSortingPlanDetail = (planBatchNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/${planBatchNo}`,
    method: 'GET',
  });
};

/**
 * @description: 结束分拣计划 /sorting-plan/finish
 */
export const sortingPlanFinish = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/finish`,
    method: 'PUT',
    params,
  });
};

/**
 * @description: 删除分拣计划 /sorting-plan/{planBatchNo}
 */
export const deleteSortingPlan = (planBatchNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/${planBatchNo}`,
    method: 'DELETE',
  });
};

/**
 * @description: 库存血浆一级列表  /sorting-plan/selectable/plasma/page
 */
export const getSortingPlanSelectableList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/selectable/plasma/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 库存血浆二级列表 /sorting-plan/selectable/detail/plasma/page
 */
export const getSortingPlanSelectableDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/selectable/detail/plasma/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 分拣计划已选择血浆列表 /sorting-plan/detail/plasma/page
 */
export const getSortingPlanDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/detail/plasma/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 库存标本一级列表  /sorting-plan/selectable/sample/page
 */
export const getSampleSortingPlanSelectableList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/selectable/sample/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 库存标本二级列表 /sorting-plan/selectable/detail/sample/page
 */
export const getSampleSortingPlanSelectableDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/selectable/detail/sample/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 分拣计划已选择标本列表 /sorting-plan/detail/sample/page
 */
export const getSampleSortingPlanDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/detail/sample/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批量添加 /sorting-plan/add
 */
export const sortingPlanBatchAdd = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/add`,
    method: 'PUT',
    data,
  });
}

/**
 * @description: 批量退回 /sorting-plan/back
 */
export const sortingPlanBatchBack = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plan/back`,
    method: 'PUT',
    data,
  });
}

