import request from '../../service';

/**
 * @description:  获取产线 /api/app/mes/product/line
 */
export const getProcessProductLineReq = () => {
  return request({
    url: `/app/mes/factory/line/list`,
    method: 'GET',
  });
};

/**
 * @description:  获取楼栋树 /api/app/platform/tenement/tree
 */
export const getPlatformTenementTree = () => {
  return request({
    url: `/app/platform/tenement/tree`,
    method: 'GET',
  });
};
/**
 * @description:  获取楼层列表 /api/app/platform/tenement/floor/list
 */
export const postPlatformTenementFloorTree = (data: any) => {
  return request({
    url: `/app/platform/tenement/floor/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description:  大屏获取房间列表下拉 /api/app/platform/factory/room/dashboard/list
 */
export const postPlatformFactoryRoomDashboardList = (data: any) => {
  return request({
    url: `/app/platform/factory/room/dashboard/list`,
    method: 'POST',
    data,
  });
};

/**
 * @description:  大屏获取房间列表 /api/app/platform/factory/room/dashboard/page
 */
export const postPlatformFactoryRoomDashboardPage = (data: any) => {
  return request({
    url: `/app/platform/factory/room/dashboard/page`,
    method: 'POST',
    data,
  });
};
/**
 * @description:  获取HUB鉴权信息 /api/app/platform/equipment/mqttAccredit
 */
export const getPlatformEquipmentMqttAccredit = () => {
  return request({
    url: `/app/platform/equipment/mqttAccredit`,
    method: 'GET',
  });
};
