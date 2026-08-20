import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------报告模板配置---------------

/**
 * @description: 分页查询 /report/page
 */
export const getReportTemplatePage = (data: any) => {
  return request({
    url: `${BASE_URL}/report/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据id查询 /report/{id}
 */
export const getReportTemplateById = (id: string) => {
  return request({
    url: `${BASE_URL}/report/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 新增 /report/create
 */
export const createReportTemplate = (data: any) => {
  return request({
    url: `${BASE_URL}/report/create`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 删除 /report/{id}
 */
export const deleteReportTemplate = (id: string) => {
  return request({
    url: `${BASE_URL}/report/${id}`,
    method: 'DELETE',
  });
}

/**
 * @description: 编辑 /report/update
 */
export const updateReportTemplate = (data: any) => {
  return request({
    url: `${BASE_URL}/report/update`,
    method: 'PUT',
    data,
  });
}

/**
 * @description: 启用状态修改 /report/enableOrDisable
 */
export const enableOrDisableReportTemplate = (data: any) => {
  return request({
    url: `${BASE_URL}/report/enableOrDisable`,
    method: 'PUT',
    data,
  });
}

/**
 * @description: 报告配置下拉列表 /report/pull
 */
export const getReportTemplatePull = (params: any) => {
  return request({
    url: `${BASE_URL}/report/pull`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 根据id查询模板详情 /report-template/{id}
 */
export const getReportTemplateDetailById = (id: string) => {
  return request({
    url: `${BASE_URL}/report-template/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 保存或更新模板详情 /report-template/saveOrUpdate
 */
export const saveOrUpdateReportTemplateDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/report-template/saveOrUpdate`,
    method: 'POST',
    data,
  });
}
