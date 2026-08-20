import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格血浆分拣---------------

/**
 * @description: 待分拣列表 /sorting-unqualified-plasma/to-sort/page
 */
export const getSortingUnqualifiedPlasmaList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/to-sort/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 已分拣列表 /sorting-unqualified-plasma/sorted/page
 */
export const getSortingUnqualifiedPlasmaSortedList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/sorted/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 分拣计划类型 /sorting-unqualified-plasma/type
 */
export const getSortingUnqualifiedPlasmaType = () => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/type`,
    method: 'GET',
  });
};

/**
 * @description: 扫描 /sorting-unqualified-plasma/scan/{plasmaOrgNo}
 */
export const sortingUnqualifiedPlasmaScan = (plasmaOrgNo: string) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/scan/${plasmaOrgNo}`,
    method: 'PUT',
  });
};

/**
 * @description: 手动提交 /sorting-unqualified-plasma/submit
 */
export const sortingUnqualifiedPlasmaSubmit = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/submit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销 /sorting-unqualified-plasma/revocation/{plasmaOrgNo}
 */
export const sortingUnqualifiedPlasmaRevocation = (plasmaOrgNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/revocation/${plasmaOrgNo}`,
    method: 'PUT',
  });
};

/**
 * @description: 查询箱号信息 /sorting-unqualified-plasma/box/{boxNo}
 */
export const getSortingUnqualifiedPlasmaBox = (boxNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-plasma/box/${boxNo}`,
    method: 'GET',
  });
};
