import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 血浆手动分拣 ---------------

/**
 * @description: 校验核查批号 /sorting-plasma/validate
 */
export const sortingPlasmaValidateCheckNo = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/validate`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 待分拣列表 /sorting-plasma/to-sort/page
 */
export const getSortingPlasmaToSortList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/to-sort/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 分拣计划类型 /sorting-plasma/type/{checkNo}
 */
export const getSortingPlasmaType = (checkNo: string) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/type/${checkNo}`,
    method: 'GET',
  });
};


/**
 * @description: 扫描 /sorting-plasma/scan/{plasmaOrgNo}
 */
export const sortingPlasmaScan = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/scan`,
    method: 'PUT',
    params
  });
}

/**
 * @description: 已分拣列表 /sorting-plasma/sorted/page
 */
export const getSortingPlasmaSortedList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/sorted/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 手动提交 /sorting-plasma/submit
 */
export const sortingPlasmaSubmit = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/submit`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 撤销 /sorting-plasma/revocation/{plasmaOrgNo}
 */
export const sortingPlasmaRevocation = (plasmaOrgNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/revocation/${plasmaOrgNo}`,
    method: 'PUT',
  });
}

/**
 * @description: 查询箱号信息 /sorting-plasma/box/{boxNo}
 */
export const getSortingPlasmaBoxInfo = (boxNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-plasma/box/${boxNo}`,
    method: 'GET',
  });
}