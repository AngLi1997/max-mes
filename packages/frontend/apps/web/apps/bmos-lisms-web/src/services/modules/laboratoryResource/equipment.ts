import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------仪器设备管理---------------

/**
 * @description: 分页查询 /laboratory/instrument/page
 */
export const getLaboratoryInstrumentPage = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 保存仪器管理 /laboratory/instrument/save
 */
export const saveLaboratoryInstrument = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/save`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑 /laboratory/instrument/update
 */
export const updateLaboratoryInstrument = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 批量删除 /laboratory/instrument/delete
 */
export const deleteLaboratoryInstrument = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/delete`,
    method: 'DELETE',
    data,
  });
};

/**
 * @description: 修改启用状态 /laboratory/instrument/update/active
 */
export const updateLaboratoryInstrumentActive = (data: any) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/update/active`,
    method: 'PUT',
    data,
  });
};
