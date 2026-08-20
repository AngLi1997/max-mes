import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格核查记录审核---------------

/**
 * @description: G3~G4不合格核查记录列表 /unqualified/record/page
 */
export const getUnqualifiedCheckRecordList = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/record/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核/退回 /unqualified/record/audit
 */
export const unqualifiedCheckRecordAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/unqualified/record/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 详情 /unqualified/record/{id}
 */
export const unqualifiedCheckRecordDetail = (id: any) => {
  return request({
    url: `${BASE_URL}/unqualified/record/${id}`,
    method: 'POST',
  });
};

/**
 * @description: 打印不合格核查记录 /unqualified/record/print/{id}
 */
export const unqualifiedCheckRecordPrint = (id: any) => {
  return request({
    url: `${BASE_URL}/unqualified/record/print/${id}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 操作记录列表 /unqualified/log/list
 */
export const unqualifiedCheckRecordLogList = (params: any) => {
  return request({
    url: `${BASE_URL}/unqualified/log/list`,
    method: 'GET',
    params,
  });
};
