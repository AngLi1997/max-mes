import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

export * from './proteinContent';
export * from './alt';
export * from './pcr';


// -----------------Y2-3、4、5、6-酶免四项管理------------------

/**
 * @description: 分页查询 /monoidal/page
 */
export const getMonoidalList = (data: any) => {
  return request({
    url: `${BASE_URL}/monoidal/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 二级列表 /monoidal/second-page
 */
export const getMonoidalSecondList = (data: any) => {
  return request({
    url: `${BASE_URL}/monoidal/second-page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 发布 /monoidal/publish
 */
export const publishMonoidal = (data: any) => {
  return request({
    url: `${BASE_URL}/monoidal/publish`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批次号发布 /monoidal/batch-publish
 */
export const batchPublishMonoidal = (params: any) => {
  return request({
    url: `${BASE_URL}/monoidal/batch-publish`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 核对 /monoidal/check
 */
export const checkMonoidal = (data: any) => {
  return request({
    url: `${BASE_URL}/monoidal/check`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批次号核对 /monoidal/batch-check
 */
export const batchCheckMonoidal = (params: any) => {
  return request({
    url: `${BASE_URL}/monoidal/batch-check`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 读取 /monoidal/data/read
 */
export const readMonoidal = (data: FormData) => {
  return request({
    url: `${BASE_URL}/monoidal/data/read`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    }
  });
}

// -----------------Y2-8、9、10、11-效价管理------------------

/**
 * @description: 分页查询 /titer/page
 */
export const getTiterList = (data: any) => {
  return request({
    url: `${BASE_URL}/titer/page`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 发布 /titer/publish
 */
export const publishTiter = (data: any) => {
  return request({
    url: `${BASE_URL}/titer/publish`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批次号发布 /titer/batch-publish
 */
export const batchPublishTiter = (params: any) => {
  return request({
    url: `${BASE_URL}/titer/batch-publish`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 核对 /titer/check
 */
export const checkTiter = (data: any) => {
  return request({
    url: `${BASE_URL}/titer/check`,
    method: 'POST',
    data,
  });
}

/**
 * @description: 批次号核对 /titer/batch-check
 */
export const batchCheckTiter = (params: any) => {
  return request({
    url: `${BASE_URL}/titer/batch-check`,
    method: 'GET',
    params,
  });
}

/**
 * @description: 读取 /titer/data/read
 */
export const readTiter = (data: FormData) => {
  return request({
    url: `${BASE_URL}/titer/data/read`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    }
  });
}