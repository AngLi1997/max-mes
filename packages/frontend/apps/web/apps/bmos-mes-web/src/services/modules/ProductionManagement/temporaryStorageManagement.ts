import request from '../../service';

/**
 * @description: 暂存间数据树带货位日志 /api/app/mes/storage/config/queryTreeWithCargoPosition
 */
export const reqStorageConfigQueryAllTreeWithCargoPosition = () => {
  return request({
    url: `/app/mes/storage/config/queryTreeWithCargoPosition`,
    method: 'GET',
  });
};

/**
 * @description: 分页查询暂存物料批次 /api/app/mes/storage/material/batch/page
 * @param {any} params
 */
export const reqStorageMaterialBatchPage = (params: any) => {
  return request({
    url: `/app/mes/storage/material/batch/page`,
    method: 'get',
    params,
  });
};

/**
 * @description: 根据物料批次号查询物料批次信息 /api/app/mes/storage/material/batch/queryInfoByMaterialBatchNo
 * @param {any} params
 */
export const reqStorageMaterialBatchQueryInfoByMaterialBatchNo = (params: any) => {
  return request({
    url: `/app/mes/storage/material/batch/queryInfoByMaterialBatchNo`,
    method: 'get',
    params,
  });
};

/**
 * @description: 盘库 /api/app/mes/storage/material/check
 * @param {any} data
 */
export const reqStorageMaterialCheck = (data: any) => {
  return request({
    url: `/app/mes/storage/material/check`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 物料入库 /api/app/mes/storage/material/sendBackMobile
 * @param {any} data
 */
export const reqStorageMaterialInbound = (data: any) => {
  return request({
    url: `/app/mes/storage/material/sendBackMobile`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 根据物料件id查询物料件详情 /api/app/mes/storage/material/info
 * @param {any} params
 */
export const reqStorageMaterialInfo = (params: any) => {
  return request({
    url: `/app/mes/storage/material/info`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 根据物料件号查询物料件详情 /api/app/mes/storage/material/infoByNo
 * @param {any} params
 */
export const reqStorageMaterialInfoByNo = (materialNo: string) => {
  return request({
    url: `/app/mes/storage/material/infoByNo`,
    method: 'GET',
    params: {
      materialNo,
    },
  });
}; /**
 * @description: 根据物料批次id查询详情 /api/app/mes/storage/material/batch/batchDetail
 * @param {string} materialBatchId 物料批次id
 */
export const reqStorageMaterialBatchBatchDetail = (materialBatchId: string) => {
  return request({
    url: `/app/mes/storage/material/batch/batchDetail`,
    method: 'GET',
    params: {
      materialBatchId,
    },
  });
};
/**
 * @description: 移库 /api/app/mes/storage/material/move
 * @param {any} data
 */
export const reqStorageMaterialMove = (data: any) => {
  return request({
    url: `/app/mes/storage/material/move`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 出库 /api/app/mes/storage/material/outbound
 * @param {any} data
 */
export const reqStorageMaterialOutbound = (data: any) => {
  return request({
    url: `/app/mes/storage/material/outbound`,
    method: 'PUT',
    data,
  });
};

/**
 * @description: 分页查询物料件 /api/app/mes/storage/material/page
 * @param {any} params
 */
export const reqStorageMaterialPage = (params: any) => {
  return request({
    url: `/app/mes/storage/material/page`,
    method: 'GET',
    params,
  });
};

/**
 * @description: 退库 /api/app/mes/storage/material/sendBack
 * @param {any} data
 */
export const reqStorageMaterialSendBack = (data: any) => {
  return request({
    url: `/app/mes/storage/material/sendBack`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 分页查询物料件 /api/app/mes/material/position/queryPositionBoundUserListByPermissionCode
 * @param {string} positionId 货位id
 * @param {string} code 权限code
 */
export const reqMaterialPositionListBoundUser = (positionId: string, code?: string) => {
  return request({
    url: `/app/mes/material/position/queryPositionBoundUserListByPermissionCode`,
    method: 'GET',
    params: {
      positionId,
      ...(code ? { permissionCode: code } : {}),
    },
  });
};

/**
 * @description: 根据物料id查询物料批次列表 /api/app/mes/storage/material/batch/listByMaterialId
 * @param {string} materialId 物料id
 * @param {string} batchNo 批次号
 */
export const reqMaterialBatchListByMaterialId = (materialId: string, batchNo?: string) => {
  return request({
    url: `/app/mes/storage/material/batch/listByMaterialId`,
    method: 'GET',
    params: {
      materialId,
      batchNo,
    },
  });
};

/**
 * @description: 获取生产批次详情
 */
export const reqStorageMaterialMangeQueryBatchDetail = (params: any) => {
  return request({
    url: `/app/mes/storage/material/manage/queryBatchDetail`,
    method: 'GET',
    params,
  });
};

//物料件详情信息 /api/storage/material/infoList
export const reqMaterialInfoListByMaterialId = (params: any) => {
  return request({
    url: `/app/mes/storage/material/infoList`,
    method: 'GET',
    params,
  });
};

//合计 /app/mes/unit/calcSumAdapt
export const postUnitCalcSumAdapt = (data: any) => {
  return request({
    url: `/app/mes/unit/calcSumAdapt`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 物料接收
 */
export const reqStorageMaterialReceiveMobile = (data: any) => {
  return request({
    url: `/app/mes/storage/material/receiveMobile`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 根据产品id和工艺id查询未终止的生产批次信息 /api/app/mes/plan/info/queryNotTerminatedBatchListByProductIdAndProcessId
 * @param {string} productId 产品id
 * @param {string} processId 工艺id
 */
export const reqPlanInfoQueryNotTerminatedBatchListByProductIdAndProcessId = (productId: string, processId: string) => {
  return request({
    url: `/app/mes/plan/info/queryNotTerminatedBatchListByProductIdAndProcessId`,
    method: 'GET',
    params: {
      productId,
      processId,
    },
  });
};
/**
 * @description: 物料预定 /api/app/mes/storage/material/reserve
 */
export const reqStorageMaterialReserve = (data: any) => {
  return request({
    url: `/app/mes/storage/material/reserve`,
    method: 'PUT',
    data,
  });
};
/**
 * @description: 取消预定(移动端) /api/app/mes/storage/material/cancelReserve
 */
export const reqStorageMaterialCancelReserve = (data: any) => {
  return request({
    url: `/app/mes/storage/material/cancelReserve`,
    method: 'PUT',
    data,
  });
};
/**
 * @description: 退库并消耗 /api/app/mes/storage/material/sendBackAndConsumeMobile
 */
export const reqStorageMaterialSendBackAndConsumeMobile = (data: any) => {
  return request({
    url: `/app/mes/storage/material/sendBackAndConsumeMobile`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 销毁并消耗 /api/app/mes/storage/material/destroyAndConsumeMobile
 */
export const reqStorageMaterialDestroyAndConsumeMobile = (data: any) => {
  return request({
    url: `/app/mes/storage/material/destroyAndConsumeMobile`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 使用并消耗 /api/app/mes/storage/material/useAndConsumeMobile
 */
export const reqStorageMaterialUseAndConsumeMobile = (data: any) => {
  return request({
    url: `/app/mes/storage/material/useAndConsumeMobile`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 拆包出库 /api/app/mes/storage/material/splitPackage
 */
export const reqStorageMaterialSplitPackage = (data: any) => {
  return request({
    url: `/app/mes/storage/material/splitPackage`,
    method: 'PUT',
    data,
  });
};
/**
 * @description: 扫描称量组件容器信息(带校验) /api/app/mes/tag/scan/scanWeighContainerCode
 * @param {string} code 容器编码
 */
export const reqTagScanScanWeighContainerCode = (code: string) => {
  return request({
    url: `/app/mes/tag/scan/scanWeighContainerCode`,
    method: 'GET',
    params: {
      code,
    },
  });
};
