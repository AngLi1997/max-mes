import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------单采血浆站管理---------------

/**
 * @description: 分页查询 /plasma-station/page
 */
export const getPlasmaStationList = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-station/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 新增 /plasma-station/create
 */
export const createPlasmaStation = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-station/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 删除 /plasma-station/{id}
 */
export const deletePlasmaStation = (id: string) => {
  return request({
    url: `${BASE_URL}/plasma-station/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description: 编辑 /plasma-station/update
 */
export const updatePlasmaStation = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-station/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 启用状态修改 /plasma-station/enableOrDisable
 */
export const enableOrDisablePlasmaStation = (data: any) => {
  return request({
    url: `${BASE_URL}/plasma-station/enableOrDisable`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 查询浆站id和名称对应关系 /plasma-station/list
 */
export const getPlasmaStationAllList = () => {
  return request({
    url: `${BASE_URL}/plasma-station/list`,
    method: 'GET',
  });
};

/**
 * @description: 根据id查询单条浆站信息 /plasma-station/{id}
 */
export const getPlasmaStationById = (id: string) => {
  return request({
    url: `${BASE_URL}/plasma-station/${id}`,
    method: 'GET',
  });
};
