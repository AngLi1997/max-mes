import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------标本请验审核---------------

/**
 * @description: 分页查询 /quality-guarantee/examination/page
 */
export const getSpecimenExaminationList = (data: any) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/examination/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 审核 /quality-guarantee/examination/audit
 */
export const auditSpecimenExamination = (data: any) => {
  return request({
    url: `${BASE_URL}/quality-guarantee/examination/audit`,
    method: 'PUT',
    data,
  });
};
