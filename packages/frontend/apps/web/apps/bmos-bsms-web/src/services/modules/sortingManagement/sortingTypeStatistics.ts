import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------分拣类型统计---------------

/**
 * @description: 分页查询 /sorting-type-statistics/page
 */
export const getSortingTypeStatisticsList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-type-statistics/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 分拣类型详情 /sorting-type-statistics/detail
 */
export const getSortingTypeStatisticsDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-type-statistics/detail`,
    method: 'POST',
    data,
  });
}
