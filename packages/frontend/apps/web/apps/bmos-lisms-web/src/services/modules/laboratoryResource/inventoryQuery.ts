import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------领用库入库查询---------------

/**
 * @description: 分页查询 /laboratory/use/page/in
 */
export const getLaboratoryUseInPage = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/page/in`,
    method: 'POST',
    data,
  });
};
