import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------外观不合格审核 -- 标本---------------

/**
 * @description: 分页查询 /sample-appearance-audit/page-list
 */
export const getSpecimenAppearanceUnqualifiedList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-appearance-audit/page-list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 外观审核 /sample-appearance-audit/audit
 */
export const specimenAppearanceUnqualifiedAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-appearance-audit/audit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: B14_外观不合格审核详情 /sample-detail/appearance/detail
 */
export const getAppearanceUnqualifiedDetail = (params: any) => {
  return request({
    url: `${BASE_URL}/sample-detail/appearance/detail`,
    method: 'GET',
    params,
  });
};
