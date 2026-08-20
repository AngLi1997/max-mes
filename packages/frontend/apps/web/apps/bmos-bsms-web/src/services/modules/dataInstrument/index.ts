import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 数据仪表 -----------------

/**
 * @description: 不合格血浆统计 /statistic/unqualified-plasma
 */
export const getUnqualifiedPlasmaStatistic = (params: any) => {
  return request({
    url: `${BASE_URL}/statistic/unqualified-plasma`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 检疫期结果统计 /statistic/quarantine-result
 */
export const getQuarantineResultStatistic = () => {
  return request({
    url: `${BASE_URL}/statistic/quarantine-result`,
    method: 'GET',
  });
};

/**
 * @description: 库存预警统计 /statistic/inventory-warning
 */
export const getStockWarningStatistic = () => {
  return request({
    url: `${BASE_URL}/statistic/inventory-warning`,
    method: 'GET',
  });
};

/**
 * @description: 原料批次、份数统计 /statistic/material
 */
export const getMaterialBatchStatistic = () => {
  return request({
    url: `${BASE_URL}/statistic/material`,
    method: 'GET',
  });
};

// ---------------任务看板 -----------------

/**
 * @description: 获取审核任务列表 /audit/task/list
 */
export const getAuditTaskList = () => {
  return request({
    url: `${BASE_URL}/audit/task/list`,
    method: 'GET',
  });
};
