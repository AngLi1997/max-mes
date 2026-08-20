import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 复核单项检验数据(C4) /inspect/singledata/check
 */
export const postInspectSingledataCheck = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/singledata/check`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 撤销复核单项检验数据(C4) /inspect/singledata/cancelcheck
 */
export const postInspectSingledataCancelcheck = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/singledata/cancelcheck`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询单项检验数据列表(C4) /inspect/singledata/list
 */
export const postInspectSingledataList = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/singledata/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询单项检验数据详情(C4) /inspect/singledata/detail
 */
export const postInspectSingledataDetail = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/singledata/detail`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询指定标本的单项检验数据(C4) /inspect/samplesingledata/list
 */
export const postInspectSamplesingledataList = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/samplesingledata/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 读取酶免四项检测数据(C4-4) /inspect/fourenzyme/read
 */
export const postInspectFourenzymeRead = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/fourenzyme/read`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 文件上传酶免单项检测数据(C4-4) /inspect/four-enzyme/file/read
 */
export const postInspectFourEnzymeFileRead = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/four-enzyme/file/read`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 读取蛋白电泳检测数据(C4-4) /inspect/proteinelec/read
 */
export const postInspectProteinelecRead = (data: any) => {
  return request({
    url: `${BASE_URL}/inspect/proteinelec/read`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 读取蛋白质含量检测数据(C4-1) /inspect/protein/read
 */
export const postInspectProteinRead = () => {
  return request({
    url: `${BASE_URL}/inspect/protein/read`,
    method: 'POST',
  });
};

/**
 * @description: 读取ALT检测数据(C4-2) /inspect/alt/read
 */
export const postInspectAltRead = () => {
  return request({
    url: `${BASE_URL}/inspect/alt/read`,
    method: 'POST',
  });
};

/**
 * @description: 打印蛋白电泳检测报告前置校验(C4-4), 日期格式yyyyMMdd /inspect/file/proteinelec/check
 */
export const getInspectFileProteinelecCheck = (params: any) => {
  return request({
    url: `${BASE_URL}/inspect/file/proteinelec/check`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 查询所有仪器设备（C模块使用） /laboratory/instrument/query/inspect
 */
export const getLaboratoryInstrumentQueryInspect = (params: any) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/query/inspect`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 关键物料下拉框接口（C模块使用） /laboratory/use/query/key
 */
export const postLaboratoryUseQueryKey = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/query/key`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 上一个发布结果的质控值（C模块使用） /laboratory/use/query/last/quality
 */
export const postLaboratoryUseQueryLastQuality = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/use/query/last/quality`,
    method: 'POST',
    data,
  });
};
