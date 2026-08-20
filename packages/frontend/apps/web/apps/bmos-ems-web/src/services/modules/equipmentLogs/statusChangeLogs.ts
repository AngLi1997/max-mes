import request from '../../service';

// ems设备日志-状态变更日志

//分页接口
export const reqEquipmentLogStatusPage = (params: any) => {
  return request({
    url: `/app/platform/equipment/log/status/page`,
    method: 'GET',
    params,
  });
};
// 导出设备状态日志
export const reqEquipmentLogStatusExport = async (params: any) => {
  return await request({
    url: `/app/platform/equipment/log/status/export`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
