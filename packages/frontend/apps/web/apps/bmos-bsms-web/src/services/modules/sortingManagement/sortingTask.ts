import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------分拣任务---------------

/**
 * @description: 分页查询 /sorting-task/page
 */
export const getSortingTaskList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-task/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 分拣任务详情-基础信息 /sorting-task/detail/{sortingTaskId}
 */
export const getSortingTaskDetail = (sortingTaskId: string) => {
  return request({
    url: `${BASE_URL}/sorting-task/detail/${sortingTaskId}`,
    method: 'GET',
  });
}

/**
 * @description: 分拣任务详情-血浆明细 /sorting-task/detail/page
 */
export const getSortingTaskDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-task/detail/page`,
    method: 'POST',
    data,
  });
}