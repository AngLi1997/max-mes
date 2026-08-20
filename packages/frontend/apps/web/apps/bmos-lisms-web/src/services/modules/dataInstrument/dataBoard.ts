import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 物料库库存统计 /statistics/material
 */
export const getStatisticsMaterial = () => {
  return request({
    url: `${BASE_URL}/statistics/material`,
    method: 'GET',
  });
};

/**
 * @description: 统计信息 /statistics/count
 */
export const postStatisticsCount = (data?: any) => {
  return request({
    url: `${BASE_URL}/statistics/count`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 查询未检验完成的检验项目数据 /statistics/unchecked
 */
export const postStatisticsUnchecked = (data: any) => {
  return request({
    url: `${BASE_URL}/statistics/unchecked`,
    method: 'POST',
    data,
  });
};
