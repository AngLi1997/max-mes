import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------检验报告管理---------------

/**
 * @description: 分页查询 /report/page
 */
export const getInspectionReportManagementList = (data: any) => {
  return request({
    url: `${BASE_URL}/report/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表 /report/second-page
 */
export const getInspectionReportManagementSecondList = (data: any) => {
  return request({
    url: `${BASE_URL}/report/second-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 检验报告管理-详情-基础信息 /report/{id}
 */
export const getInspectionReportManagementInfo = (id: string) => {
  return request({
    url: `${BASE_URL}/report/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 检验报告管理-详情-不合格信息 /report/negative/{id}
 */
export const getInspectionReportManagementNegativeInfo = (data: any) => {
  return request({
    url: `${BASE_URL}/report/negative`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 检验报告管理-详情-检验报告 /report/report/{id}
 */
export const getInspectionReportManagementReportInfo = (id: string) => {
  return request({
    url: `${BASE_URL}/report/report/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 创建检验报告 /report/create
 */
export const createInspectionReportManagement = (data: any) => {
  return request({
    url: `${BASE_URL}/report/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /report/audit
 */
export const auditInspectionReportManagement = (data: any) => {
  return request({
    url: `${BASE_URL}/report/audit`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 打印检验报告 /report/print/{id}
 */
export const printInspectionReportManagement = (id: string) => {
  return request({
    url: `${BASE_URL}/report/print/${id}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};
