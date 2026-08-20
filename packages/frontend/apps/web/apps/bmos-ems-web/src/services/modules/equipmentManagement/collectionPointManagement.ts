import request from '../../service';

// ems设备管理-采集点管理

//获取采集点列表分页
export const reqAcquisitionPointPage = (params: any) => {
  return request({
    url: `/app/platform/acquisitionPoint/page`,
    method: 'GET',
    params,
  });
};
// 添加采集点
export const reqAcquisitionPoint = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint`,
    method: 'POST',
    data,
  });
};
// 修改采集点
export const reqInventoryEditInventoryBatch = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint`,
    method: 'PUT',
    data,
  });
};
// 批量删除采集点
export const reqAcquisitionPointBatch = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint/batch`,
    method: 'DELETE',
    data,
  });
};
// 停用采集点
export const reqAcquisitionPointDisable = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint/disable`,
    method: 'PUT',
    data,
  });
};
// 启用采集点
export const reqAcquisitionPointEnable = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint/enable`,
    method: 'PUT',
    data,
  });
};
// 导出采集点
export const reqAcquisitionPointExport = async (params: any) => {
  return await request({
    url: `/app/platform/acquisitionPoint/export`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
// 导入采集点
export const reqAcquisitionPointImport = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint/import`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};
// 下载导出模板
export const reqAcquisitionPointTemplate = () => {
  return request({
    url: `/app/platform/acquisitionPoint/template`,
    method: 'GET',
    responseType: 'arraybuffer',
    original: true,
  });
};

// 关联设备数据
export const reqAcquisitionPointEquipmentData = (data: any) => {
  return request({
    url: `/app/platform/acquisitionPoint/equipmentData`,
    method: 'POST',
    data,
  });
};
