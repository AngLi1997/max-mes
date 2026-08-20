import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------浆站不合格血浆管理---------------

/**
 * @description: 浆站不合格血浆管理分页 /unqualified/plasma/page
 */
export const getUnqualifiedPlasmaList = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 浆站不合格血浆信息导出 /unqualified/plasma/station/export
 */
export const exportUnqualifiedPlasma = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/station/export`,
    method: 'POST',
    responseType: 'arraybuffer',
    original: true,
    data,
  });
};

/**
 * @description: 企业不合格血浆管理分页 /unqualified/plasma/enterprise/page
 */
export const getEnterpriseUnqualifiedPlasmaList = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/enterprise/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 企业不合格血浆信息导出 /unqualified/plasma/company/export
 */
export const exportEnterpriseUnqualifiedPlasma = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/company/export`,
    method: 'POST',
    responseType: 'arraybuffer',
    original: true,
    data,
  });
};

/**
 * @description: 登记不合格信息 /unqualified/plasma/register
 */
export const unqualifiedPlasmaRegister = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/register`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 删除不合格登记 /unqualified/plasma/{id}
 */
export const deleteUnqualifiedPlasma = (id: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description: 生成报告单号 /unqualified/plasma/report/generate
 */
export const generateUnqualifiedPlasmaReportNo = () => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/generate`,
    method: 'POST',
  });
};

/**
 * @description: 处理不合格浆站信息 /unqualified/plasma/handle/{id}
 */
export const unqualifiedPlasmaHandle = (id: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/handle/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 出具不合格记录 /unqualified/plasma/issue
 */
export const unqualifiedPlasmaIssue = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/issue`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 出具不合格血浆核查报告 /unqualified/plasma/report/save
 */
export const unqualifiedPlasmaReportSave = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 获取不合格核查报告基础信息 /unqualified/plasma/report/{unqualifiedPlasmaInfoId}
 */
export const getUnqualifiedPlasmaReportInfo = (unqualifiedPlasmaInfoId: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/${unqualifiedPlasmaInfoId}`,
    method: 'GET',
  });
};

/**
 * @description: 编辑不合格血浆核查报告 /unqualified/plasma/report/edit
 */
export const unqualifiedPlasmaReportEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/report/edit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查看影响血浆 /unqualified/plasma/affected/plasma
 */
export const getUnqualifiedPlasmaAffectedPlasma = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/affected/plasma`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 导出受影响血浆记录 /unqualified/plasma/affected/export
 */
export const exportUnqualifiedPlasmaAffectedPlasma = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/plasma/affected/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
