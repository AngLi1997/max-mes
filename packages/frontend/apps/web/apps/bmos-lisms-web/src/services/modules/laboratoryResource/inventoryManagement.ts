import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------领用库库存管理---------------

/**
 * @description: 分页查询 /laboratory/use/page/manage
 */
export const getLaboratoryUsePage = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/page/manage`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 二级列表 /laboratory/use/page/manage/item
 */
export const getLaboratoryUseItemPage = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/page/manage/item`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 物料报废、物料消耗接口 /laboratory/use/apply
 */
export const materialUseApply = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/apply`,
    method: 'POST',
    data,
  });
};
