import request from '../../service';

// ems设备日志-设备使用日志

//分页接口
export const reqEquipmentLogOperatePage = (params: any) => {
  return request({
    url: `/app/platform/equipment/log/operate/page`,
    method: 'GET',
    params,
  });
};
// 导出设备操作日志
export const reqEquipmentLogOperateExport = async (params: any) => {
  return await request({
    url: `/app/platform/equipment/log/operate/export`,
    method: 'GET',
    params,
    responseType: 'arraybuffer',
    original: true,
  });
};
