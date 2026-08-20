import request from '@/utils/request/request.js';

// 根据工位和批次获取设备列表 /api/app/platform/equipment/app/station/page
export const reqPlatformEquipmentAppStationPageApi = params => request.get(
  '/api/app/platform/equipment/app/station/page',
  params,
);
// 根据根据产线id获取设备列表 /api/app/platform/equipment/app/getConfigByProductionLineId
export const reqPlatformEquipmentAppGetConfigByProductionLineIdApi = params => request.get(
  '/api/app/platform/equipment/app/getConfigByProductionLineId',
  params,
);
// 获取当前工位下的设备列表 /api/app/platform/equipment/app/station/list
export const reqPlatformEquipmentAppStationListApi = data => request.post(
  '/api/app/platform/equipment/app/station/list',
  data,
);

// 获取当前产线工位下的设备列表 /equipment/app/{productionLineId}/getConfigByStationIdList
export const reqGetConfigByStationIdListApi = (productionLineId, stationIdList) => request.get(
  `/api/app/platform/equipment/app/${productionLineId}/getConfigByStationIdList`,
  { stationIdList },
);

// 根据设备id获取设备状态信息 /api/app/platform/equipment/app/info/{id}
export const reqMesEquipmentGetConfigByEquipmentIdApi = id => request.get(
  `/api/app/platform/equipment/app/info/${id}`,
);

// 查询标准单位下拉框
export const getMesUnitListApi = () => request.get(
  `/api/app/platform/unit/getAllUnit`,
);

// 保存设备信息组件信息 /api/app/mes/equipment/equipmentComponentInfo
export const reqMesEquipmentEquipmentComponentInfoApi = data => request.post(
  '/api/app/mes/equipment/equipmentComponentInfo',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': '保存设备信息',
    },
  },
);

// 修改设备信息组件 /api/app/mes/equipment/equipmentComponentInfo
export const reqUpdateMesEquipmentEquipmentComponentInfoApi = data => request.put(
  '/api/app/mes/equipment/equipmentComponentInfo',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '修改设备信息',
    },
  },
);

// 获取设备工位详情 /api/app/platform/equipment/station/info/{id}
export const reqPlatformEquipmentStationInfoApi = id => request.get(
  `/api/app/platform/equipment/station/info/${id}`,
);

// 根据设备id获取设备绑定采集项的所有数据 /api/app/platform/equipment/acquisitionPointData
export const reqPlatformEquipmentAcquisitionPointDataApi = equipmentId => request.get(
  '/api/app/platform/equipment/acquisitionPointData',
  {
    equipmentId,
  },
);

// 保存设备录入组件数据 /api/app/mes/equipment/acquisitionData
export const reqMesEquipmentAcquisitionDataApi = data => request.post(
  '/api/app/mes/equipment/acquisitionData',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 0,
      'Bmos-Operation-Business': '保存设备数采数据',
    },
  },
);

// 修改设备录入组件数据 /api/app/mes/equipment/acquisitionData
export const reqMesUpdateEquipmentAcquisitionDataApi = data => request.put(
  '/api/app/mes/equipment/acquisitionData',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '修改设备数采数据',
    },
  },
);

// 查询设备点位历史数据 /api/app/platform/equipment/acquisitionPointHistoryData
export const reqPlatformEquipmentAcquisitionPointHistoryDataApi = params => request.get(
  '/api/app/platform/equipment/acquisitionPointHistoryData',
  params,
);

// 根据设备编码获取设备id /api/app/platform/equipment/app/query/equipment/id
export const reqPlatformEquipmentAppQueryEquipmentIdApi = equipmentCode => request.get(
  '/api/app/platform/equipment/app/query/equipment/id',
  {
    equipmentCode,
  },
);
