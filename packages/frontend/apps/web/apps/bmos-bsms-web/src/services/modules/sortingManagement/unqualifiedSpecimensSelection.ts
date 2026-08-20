import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------不合格标本分拣---------------

/**
 * @description: 待分拣列表 /sorting-unqualified-sample/to-sort/page
 */
export const getSortingUnqualifiedSampleList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/to-sort/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 已分拣列表 /sorting-unqualified-sample/sorted/page
 */
export const getSortingUnqualifiedSampleSortedList = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/sorted/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 分拣计划类型 /sorting-unqualified-sample/type
 */
export const getSortingUnqualifiedSampleType = () => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/type`,
    method: 'GET',
  });
};

/**
 * @description: 扫描 /sorting-unqualified-sample/scan/{sampleOrgNo}
 */
export const sortingUnqualifiedSampleScan = (sampleOrgNo: string) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/scan/${sampleOrgNo}`,
    method: 'PUT',
  });
};

/**
 * @description: 手动提交 /sorting-unqualified-sample/submit
 */
export const sortingUnqualifiedSampleSubmit = (data: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/submit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销 /sorting-unqualified-sample/revocation/{sampleOrgNo}
 */
export const sortingUnqualifiedSampleRevocation = (sampleOrgNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/revocation/${sampleOrgNo}`,
    method: 'PUT',
  });
};

/**
 * @description: 查询箱号信息 /sorting-unqualified-sample/box/{boxNo}
 */
export const sortingUnqualifiedSampleBox = (boxNo: any) => {
  return request({
    url: `${BASE_URL}/sorting-unqualified-sample/box/${boxNo}`,
    method: 'GET',
  });
};
