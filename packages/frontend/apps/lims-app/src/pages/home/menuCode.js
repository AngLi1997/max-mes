export const codeEnum = {
  // 生产管理
  121010001: {
    path: '/pages/production/productionManagement/index',
    icon: '/static/homeIcon/productionManage.svg',
  },
  // 生产前确认
  121010002: {
    path: '/pages/production/beforeProductionConfirm/index',
    icon: '/static/homeIcon/confirmBeforeProduction.svg',
  },
  // 生产历史
  121010003: {
    path: '/pages/production/productionManagement/index',
    icon: '/static/homeIcon/productionHistory.svg',
    query: {
      productionHistory: true,
    },
  },
  // 生产修订
  121010004: {
    path: '/pages/production/productionManagement/index',
    icon: '/static/homeIcon/productionRevision.svg',
    query: {
      productionHistory: true,
      productionRevision: true,
    },
  },
  // 称量中心
  121020001: {
    path: '/pages/weighingCenter/list/index',
    icon: '/static/homeIcon/weightingCenter.svg',
  },
  // 称量历史
  121020003: {
    path: '/pages/weighingHistory/list/index',
    icon: '/static/homeIcon/weightingHistory.svg',
  },
  // 物料接收
  121020004: {
    path: '/pages/materialReceiving/index',
    icon: '/static/homeIcon/materialReceiving.svg',
  },
  // 物料称量
  121020005: {
    path: '/pages/materialWeighing/confirmOutputBatch/index',
    icon: '/static/homeIcon/materialWeighing.svg',
  },
  // 物料称量历史
  121020006: {
    path: '/pages/materialWeighingHistory/index',
    icon: '/static/homeIcon/materialWeighingHistory.svg',
  },
  // 称量工单执行
  121020007: {
    path: '/pages/weighingWorkOrder/list/index',
    icon: '/static/homeIcon/weighingOrder.svg',
  },
  // 称量工单历史
  121020008: {
    path: '/pages/weighingWorkOrderHistory/list/index',
    icon: '/static/homeIcon/weighingWorkOrderHistory.svg',
  },
  // 库存管理
  121020002: {
    path: '/pages/inventoryManagement/inventoryInfo/index',
    icon: '/static/homeIcon/stockControl.svg',
  },
  // 设备状态
  121030001: {
    path: '/pages/equipment/useInfo/index',
    icon: '/static/homeIcon/deviceStewardship.svg',
    query: {
      deviceStatus: true,
    },
  },
  // 房间管理
  121030002: {
    path: '/pages/roomManagement/managementPage/index',
    icon: '/static/homeIcon/roomManage.svg',
  },
  // 异常管理
  121040001: {
    path: '/pages/exception/management/index',
    icon: '/static/homeIcon/exceptionManagement.svg',
  },
  // 设备使用信息填报
  121030003: {
    path: '/pages/equipment/useInfo/index',
    icon: '/static/homeIcon/equipmentUseInfo.svg',
  },
};
