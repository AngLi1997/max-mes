import request from '@/utils/request/request.js';

// 领料单详情 /api/app/mes/requisition/detail
export const requisitionDetailApi = params => request.get('/api/app/mes/requisition/detail', params);

// 可预订及已预订的暂存物料件 /api/app/mes/requisition/reservedAvailableMaterial
export const requisitionReservedAvailableMaterialApi = params => request.get(
  '/api/app/mes/requisition/reservedAvailableMaterial',
  params,
);

// 暂存物料批量预定 /api/app/mes/requisition/storage/reserve
export const requisitionStorageReserve = data => request.post('/api/app/mes/requisition/storage/reserve', data, {
  header: {
    'Bmos-MenuId': '121010001002001',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '物料预定',
  },
});

// 仓库领料:获取已预订仓库物料批次 /api/app/mes/requisition/receive/repository/reservedBatch
export const repositoryReservedBatch = params => request.get(
  '/api/app/mes/requisition/receive/repository/reservedBatch',
  params,
);

// 仓库领料:获取批次下已经预定的暂存物料件 /api/app/mes/requisition/reservedMaterial
export const requisitionReservedMaterial = params => request.get('/api/app/mes/requisition/reservedMaterial', params);

// 暂存领料:暂存物料单个取消预定 /api/app/mes/requisition/storage/cancel
export const requisitionStorageCancel = data => request.post('/api/app/mes/requisition/storage/cancel', data);

// 仓库领料:获取可选批次列表 /api/app/mes/requisition/receive/repository/batchList
export const requisitionReceiveRepositoryBatchList = params => request.get(
  '/api/app/mes/requisition/receive/repository/batchList',
  params,
);

// 仓库领料:完成领料 /api/app/mes/requisition/receive/repository/complete
export const requisitionReceiveRepositoryComplete = data => request.post(
  '/api/app/mes/requisition/receive/repository/complete',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001002001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '完成领料计划',
    },
  },
);
// 仓库领料:预定批次 /api/app/mes/requisition/receive/repository/reserveBatch

export const requisitionReceiveRepositoryReserveBatch = data => request.post(
  '/api/app/mes/requisition/receive/repository/reserveBatch',
  data,
);
// 计算理论量 /api/app/mes/requisition/quantity/calculate
export const getRequisitionQuantityCalculate = params => request.get('/api/app/mes/requisition/quantity/calculate', params);
// 仓库领料:完成前取消 /api/app/mes/requisition/receive/repository/cancelReserved
export const requisitionReceiveRepositoryCancelReserved = data => request.post(
  '/api/app/mes/requisition/receive/repository/cancelReserved',
  data,
);

// 仓库领料:获取可选物料量列表  /api/app/mes/requisition/receive/repository/availableQuantityList
export const requisitionRepositoryAvailableQuantityList = params => request.get(
  '/api/app/mes/requisition/receive/repository/availableQuantityList',
  params,
);
// 获取组件配料单详情 /api/app/mes/ingredient/detail
export const ingredientDetail = params => request.get('/api/app/mes/ingredient/detail', params);
// 获取物料批次 /api/app/mes/ingredient/availableBoundMaterialBatch
export const ingredientAvailableBoundMaterialBatch = params => request.get(
  '/api/app/mes/ingredient/availableBoundMaterialBatch',
  params,
);

// 获取已添加到配料单的批次列表 /api/app/mes/ingredient/boundMaterialBatch
export const ingredientBoundMaterialBatch = params => request.get('/api/app/mes/ingredient/boundMaterialBatch', params);
// 配料量计算 /api/app/mes/ingredient/calculate/batch
export const postingredientcalCulateBatch = data => request.post('/api/app/mes/ingredient/calculate/batch', data);

// 勾选批次配料量计算 /api/app/mes/ingredient/calculate
export const ingredientCalculate = params => request.get('/api/app/mes/ingredient/calculate', params);

// 添加物料批次 /api/app/mes/ingredient/bindMaterialBatch
export const ingredientBindMaterialBatch = data => request.post('/api/app/mes/ingredient/bindMaterialBatch', data);
// 完成配料计划 /api/app/mes/ingredient/complete
export const ingredientComplete = data => request.post('/api/app/mes/ingredient/complete', data, {
  header: {
    'Bmos-MenuId': '121010001002003',
    'Bmos-Operation': 0,
    'Bmos-Operation-Business': '完成配料计划',
  },
});

// 领料接收:获取领料单列表 /api/app/mes/requisition/list
export const getRequisitionList = params => request.get('/api/app/mes/requisition/list', params);

// 领料接收:获取组件绑定的领料单信息 /api/app/mes/requisition/receive/boundRequisition
export const getReceiveBoundRequisition = params => request.get('/api/app/mes/requisition/receive/boundRequisition', params);

// 领料接收:获取领料单下物料批次信息  /api/app/mes/requisition/receive/repository/materialBatch
export const getReceiveRepositoryMaterialBatch = params => request.get(
  '/api/app/mes/requisition/receive/repository/materialBatch',
  params,
);

// 根据功能权限按钮id查询用户列表 /api/app/platform/user/listByMenuId
export const getPlatformUserListByMenuId = params => request.get('/api/app/platform/user/listByMenuId', params);

// 领料接收:按批次 /api/app/mes/requisition/receive/repository/batch
export const postRequisitionReceiveRepositoryBatch = data => request.post(
  '/api/app/mes/requisition/receive/repository/batch',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001002002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '领料接收',
    },
  },
);
// 领料接收:物料件列表 /api/app/mes/requisition/receive/material/list
export const getRequisitionReceiveMaterialList = params => request.get(
  '/api/app/mes/requisition/receive/material/list',
  params,
);

// 领料接收:按物料件 /api/app/mes/requisition/receive/repository/material
export const postRequisitionReceiveRepositoryMaterial = data => request.post(
  '/api/app/mes/requisition/receive/repository/material',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001002002',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '领料接收',
    },
  },
);
// 领料接收:完成接收 /api/app/mes/requisition/receive/complete
export const postRequisitionRequisitionReceiveComplete = data => request.post(
  '/api/app/mes/requisition/receive/complete',
  data,
  {
    header: {
      'Bmos-MenuId': '121010001002001',
      'Bmos-Operation': 1,
      'Bmos-Operation-Business': '物料预定',
    },
  },
);
// 成品产出:获取成品产出详情 /api/app/mes/output/finished/detail
export const getFinishedDetail = params => request.get('/api/app/mes/output/finished/detail', params);

// 成品产出:获取组件产出列表 /api/app/mes/output/finished/list
export const getOutputFinishedList = params => request.get('/api/app/mes/output/finished/list', params);

// 成品产出:查询物料绑定拓展单位 /api/app/mes/unit/list/down/extend/bound
export const getMesUnitListDownExtendBound = params => request.get('/api/app/mes/unit/list/down/extend/bound', params);

// 成品产出:产出成品 /api/app/mes/output/finished/save
export const reqOutputFinishedSave = data => request.post('/api/app/mes/output/finished/save', data, {
  header: {
    'Bmos-MenuId': '121010001',
    'Bmos-Operation': 0,
    'Bmos-Operation-Business': '成品产出',
  },
});

// 物料预定组件:获取实例相关信息 /api/app/mes/requisition/reserve/instance
export const getRequisitionReserveInstance = params => request.get('/api/app/mes/requisition/reserve/instance', params);
// 物料预定组件:获取可预定物料件    /api/app/mes/requisition/reserve/availableList
export const getRequisitionReserveAvailableList = params => request.get('/api/app/mes/requisition/reserve/availableList', params);
// 物料预定组件:物料批量预定 /api/app/mes/requisition/reserve
export const postRequisitionReserve = data => request.post('/api/app/mes/requisition/reserve', data, {
  header: {
    'Bmos-MenuId': '121010001001001',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '物料预定',
  },
});
// 物料预定组件:取消预定 /api/app/mes/requisition/reserve/cancel
export const postRequisitionReserveCancel = data => request.post('/api/app/mes/requisition/reserve/cancel', data, {
  header: {
    'Bmos-MenuId': '121010001001001',
    'Bmos-Operation': 1,
    'Bmos-Operation-Business': '取消预定',
  },
});

// 生产前确认-物料预定相关接口
export const getPlanInfoFormulaMaterialList = params => request.get('/api/app/mes/plan/info/formula/material/list', params);// 左侧列表
export const postStorageMaterialConfirmBatchReserve = data => request.post('/api/app/mes/storage/material/confirm/batchReserve', data);// 物料批量预定

// 查询工序绑定的房间信息 /api/app/mes/mobile/factory/room/plan/step/component/room
export const getProcedureRoomsList = params => request.get('/api/app/mes/mobile/factory/room/plan/step/component/room', params);

// 根据房间id获取房间基础信息  /api/app/mes/factory/getRoomCleanInfo/{roomId}
export const getFactoryGetRoomCleanInfo = params => request.get(`/api/app/mes/factory/getRoomCleanInfo/${params}`);
// 清场相关组件扫描二维码时获取房间详情/api/app/mes/mobile/factory/room/component/room/info
export const getMobileFactoryGetRoomInfo = params => request.get(`/api/app/mes/mobile/factory/room/component/room/info`, params);
// 根据房间id进行房间清场操作 /api/app/mes/factory/clean/room
export const postFactoryCleanRoom = data => request.post('/api/app/mes/mobile/factory/room/clean/room', data);
// 根据房间id进行房间清场操作,保存房间执行组件信息/api/app/mes/mobile/factory/room/clean/room
export const postMobileFactoryRoomCleanRoom = data => request.post('/api/app/mes/mobile/factory/room/clean/room', data);
// 保存房间清场检测组件信息 /api/app/mes/factory/room/clean/check/component/save
export const postFactoryRoomCleanCheckComponentSave = data => request.post('/api/app/mes/mobile/factory/room/room/clean/check/component/save', data);

// 保存房间清场信息组件信息 /api/app/mes/mobile/factory/room/room/clean/info/component/save
export const postFactoryRoomRoomCleanInfoComponentSave = data => request.post('/api/app/mes/mobile/factory/room/room/clean/info/component/save', data);
