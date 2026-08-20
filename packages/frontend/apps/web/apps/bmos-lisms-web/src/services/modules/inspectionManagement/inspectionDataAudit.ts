import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 查询发布单列表(C3) /inspect/datapub/list
 */
export const postInspectDatapubCheck = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/datapub/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核汇总检验数据(C3) /inspect/alldata/audit
 */
export const postInspectAlldataAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/alldata/audit`,
    method: 'POST',
    data,
  });
};
