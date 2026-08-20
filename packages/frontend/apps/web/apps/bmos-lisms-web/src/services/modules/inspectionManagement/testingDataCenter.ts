import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 校验是否能发布汇总检验数据(C2) /inspect/alldata/check
 */
export const postInspectAlldataCheck = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/alldata/check`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 发布汇总检验数据(C2) /inspect/alldata/publish
 */
export const postInspectAlldataPublish = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/alldata/publish`,
    method: 'POST',
    data,
  });
};
