import request from '../../service';

/**
 * @description:  根据模型id获取房间详情 /api/app/platform/factory/room/3D/model
 */
export const getPlatformFactoryRoomModel = (id: any) => {
  return request({
    url: `/app/platform/factory/room/3D/model/${id}`,
    method: 'GET',
  });
};

/**
 * @description:  根据设备id获取设备详情 /api/app/platform/equipment/app/info
 */
export const getPlatformEquipmentAppInfo = (id: any) => {
  return request({
    url: `/app/platform/equipment/app/info/${id}`,
    method: 'GET',
  });
};
