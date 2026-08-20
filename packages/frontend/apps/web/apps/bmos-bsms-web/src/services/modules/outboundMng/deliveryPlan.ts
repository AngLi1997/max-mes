import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

// ---------------出库计划---------------

/**
 * @description: 分页查询 /outbound/plan/page
 */
export const getDeliveryPlanList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 血浆出库单打印 /outbound/plan/print/{batchNo}
 */
export const printDeliveryPlan = (batchNo: string) => {
  return request({
    url: `${BASE_URL}/outbound/plan/print/${batchNo}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 血浆出库单附页打印 /outbound/plan/print/detail/{batchNo}
 */
export const printDeliveryPlanDetail = (batchNo: string) => {
  return request({
    url: `${BASE_URL}/outbound/plan/print/detail/${batchNo}`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 导出 /outbound/plan/export
 */
export const exportDeliveryPlanList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 根据id查询 /outbound/plan/{id}
 */
export const getDeliveryPlanById = (id: string) => {
  return request({
    url: `${BASE_URL}/outbound/plan/${id}`,
    method: 'GET',
  });
};

/**
 * @description: 新增 /outbound/plan/create
 */
export const createDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑 /outbound/plan/update
 */
export const updateDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/update`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 删除 /outbound/plan/{batchNo}
 */
export const deleteDeliveryPlan = (batchNo: string) => {
  return request({
    url: `${BASE_URL}/outbound/plan/${batchNo}`,
    method: 'DELETE',
  });
};

/**
 * @description: 查询在库血浆-一级列表 /outbound/plan/plasma/page
 */
export const getDeliveryPlanPlasmaList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/plasma/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询在库血浆-二级列表 /outbound/plan/plasma-info/page
 */
export const getDeliveryPlanPlasmaInfoList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/plasma-info/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 查询已选择的血浆 /outbound/plan/selected/page
 */
export const getDeliveryPlanSelectedList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/selected/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 已选择血浆导出 /outbound/plan/detail/export
 */
export const exportDeliveryPlanDetailList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/detail/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 编辑出库计划导入 /outbound/plan/import
 */
export const updateDeliveryPlanImport = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/import`,
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description: 已选择血浆导出 /outbound/plan/selected/export
 */
export const exportDeliveryPlanSelectedList = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/selected/export`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 批量添加血浆 /outbound/plan/choose
 */
export const batchAddOutboundPlasma = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/choose`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 批量/按托盘/按分拣批次退回血浆 /outbound/plan/back
 */
export const batchBackOutboundPlasma = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/back`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 计划申请 /outbound/plan/apply
 */
export const applyDeliveryPlan = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/apply`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 更改计划批次 /outbound/plan/edit/lot-no
 */
export const editDeliveryPlanLotNo = (data: any) => {
  return request({
    url: `${BASE_URL}/outbound/plan/edit/lot-no`,
    method: 'PUT',
    data,
  });
};
