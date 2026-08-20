export * from './acceptanceAudit';
export * from './appearanceUnqualifiedAudit';
export * from './sampleDataSync';
export * from './sampleInStoredMng';
export * from './sampleQuery';
export * from './specimenDeliveryPlan';
export * from './specimenRelease';
export * from './specimenStorage';
export * from './specimenVerification';
export * from './visualInspectionBeforeStorage';

import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: B8_标本库存预警 分页列表 /sample-inventory-warning/warning/list
 */
export const getSampleInventoryWarningList = (data: any) => {
  return request({
    url: `${BASE_URL}/sample-inventory-warning/warning/list`,
    method: 'POST',
    data,
  });
};
