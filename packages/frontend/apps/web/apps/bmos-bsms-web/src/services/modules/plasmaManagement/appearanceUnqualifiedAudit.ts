import { BASE_URL } from '@/services/baseUrl';
import { getItem } from '@/utils';
import axios from '@bmos/axios';
import request from '../../service';

// ---------------外观不合格审核 -- 血浆---------------

/**
 * @description: 分页查询 /plasma-appearance-audit/page
 */
export const getAppearanceAuditList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-audit/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /plasma-appearance-audit/execute
 */
export const appearanceAuditExecute = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-audit/execute`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 血浆外观检验详情 /plasma-appearance-audit/{plasmaOrgNo}
 */
export const getAppearanceAuditDetail = (plasmaOrgNo: string) => {
  return request({
    url: `${BASE_URL}/plasma-appearance-audit/${plasmaOrgNo}`,
    method: 'GET',
  });
}