import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// --------------- 分拣维护 ---------------

/**
 * @description: 获取分拣人 /sorting-maintain/person
 */
export const getSortingMaintainPerson = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/person`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 血浆维护分页列表 /sorting-maintain/plasma/page
 */
export const getSortingMaintainPlasmaList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/plasma/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本维护分页列表 /sorting-maintain/sample/page
 */
export const getSortingMaintainSampleList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/sample/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆撤销 /sorting-maintain/plasma/revocation
 */
export const sortingMaintainPlasmaRevocation = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/plasma/revocation`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 标本撤销 /sorting-maintain/sample/revocation
 */
export const sortingMaintainSampleRevocation = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/sample/revocation`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询箱号信息 -- 血浆 /sorting-maintain/plasma/box/{boxNo}
 */
export const getSortingMaintainPlasmaBox = (boxNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/plasma/box/${boxNo}`,
    method: 'GET',
  });
};

/**
 * @description: 打印箱号 -- 血浆 /sorting-maintain/plasma/print
 */
export const printSortingMaintainPlasma = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/plasma/print`,
    method: 'POST',
    params,
    responseType: 'blob',
    original: true,
  });
};

/**
 * @description: 查询箱号信息 -- 标本 /sorting-maintain/sample/box/{boxNo}
 */
export const getSortingMaintainSampleBox = (boxNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/sample/box/${boxNo}`,
    method: 'GET',
  });
};

/**
 * @description: 打印箱号 -- 标本 /sorting-maintain/sample/print
 */
export const printSortingMaintainSample = (params: any) => {
  return request({
    url: `${BASE_URL}/sorting-maintain/sample/print`,
    method: 'POST',
    params,
    responseType: 'blob',
    original: true,
  });
};
