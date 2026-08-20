import request from '../../service';

// ems设备管理-设备管理

//占位接口
export const storageQueryAllTree222 = () => {
  return request({
    url: `/app/ems/storage/config/queryTree`,
    method: 'GET',
  });
};

//新建设备分类 /api/app/platform/equipment/category/save
export const postEquipmentCategorySave = (data: any) => {
  return request({
    url: `/app/platform/equipment/category/save`,
    method: 'POST',
    data,
  });
};

//修改设备分类 /api/app/platform/equipment/category/update
export const putEquipmentCategoryUpdate = (data: any) => {
  return request({
    url: `/app/platform/equipment/category/update`,
    method: 'PUT',
    data,
  });
};

//删除设备分类 /api/app/platform/equipment/category/delete/{id}
export const deleteEquipmentCategoryApi = (id: string) => {
  return request({
    url: `/app/platform/equipment/category/delete/${id}`,
    method: 'DELETE',
  });
};
//获取设备分类树 /api/app/platform/equipment/category/list
export const getEquipmentCategoryList = () => {
  return request({
    url: `/app/platform/equipment/category/list`,
    method: 'GET',
  });
};

//获取设备列表 /api/app/platform/equipment/page
export const getPlatformEquipmentPage = (params: any) => {
  return request({
    url: `/app/platform/equipment/page`,
    method: 'GET',
    params,
  });
};

//获取所有设备tag /api/app/platform/equipment/all/tag
export const getEquipmentAllTag = () => {
  return request({
    url: `/app/platform/equipment/all/tag`,
    method: 'GET',
  });
};

//启停设备 /api/app/platform/equipment/enable
export const postEquipmentEnable = (data: any) => {
  return request({
    url: `/app/platform/equipment/enable`,
    method: 'POST',
    data,
  });
};

//获取tag下所有内置属性 /api/app/platform/equipment/tag/property
export const getEquipmentTagProperty = (params: any) => {
  return request({
    url: `/app/platform/equipment/tag/property`,
    method: 'GET',
    params,
  });
};
//新建设备 /api/app/platform/equipment/save
export const postEquipmentSave = (data: any) => {
  return request({
    url: `/app/platform/equipment/save`,
    method: 'POST',
    data,
  });
};

//获取点位列表 /api/app/platform/equipment/list/acquisition
export const getEquipmentListAcquisition = () => {
  return request({
    url: `/app/platform/equipment/list/acquisition`,
    method: 'GET',
  });
};

//获取设备详情 /api/app/platform/equipment/info/{id}
export const getEquipmentInfo = (id: string) => {
  return request({
    url: `/app/platform/equipment/info/${id}`,
    method: 'GET',
  });
};

//删除设备 /api/app/platform/equipment/delete/{id}
export const deleteEquipmentApi = (id: string) => {
  return request({
    url: `/app/platform/equipment/delete/${id}`,
    method: 'DELETE',
  });
};

//编辑设备 /api/app/platform/equipment/update

export const putEquipmentUpdate = (data: any) => {
  return request({
    url: `/app/platform/equipment/update`,
    method: 'PUT',
    data,
  });
};

//打印标签 /api/app/platform/tag/instance/printBatch
export const postTagInstancePrintBatch = (data: Object[]) => {
  return request({
    url: `/app/platform/tag/instance/printBatch`,
    method: 'POST',
    data,
  });
};

// 获取匹配采集点时的下拉框(待加)

// 匹配采集点保存按钮
export const postEquipmentEquipmentIdAcquisitionPoint = (equipmentId: any, data: Object) => {
  return request({
    url: `/app/platform/equipment/${equipmentId}/acquisitionPoint`,
    method: 'POST',
    data,
  });
};

// 查询字典下拉框
export const getQueryListDictDown = (params: any) => {
  return request({
    url: '/app/mes/platform/query/list/dict/down',
    method: 'GET',
    params,
  });
};

// 根据设备数据获取这些数据可用的采集点列表
export const getAcquisitionPointEnableByEquipmentDataProperty = (data: any) => {
  return request({
    url: '/app/platform/acquisitionPoint/enableByEquipmentDataProperty',
    method: 'POST',
    data,
  });
};
// 查询拓展单位下拉框
export const getMesUnitExtendListApi = (unitId: string) => {
  return request({
    url: '/app/platform/unit/list/extendUnit',
    method: 'get',
    params: { unitId },
  });
};

// 查询标准单位下拉框
export const getMesUnitListApi = () => {
  return request({
    url: '/app/mes/unit/list/down/box',
    method: 'get',
  });
};
// 扩展单位查其标准单位(回显级联时所需)
export const reqExtendGetStandard = (params: any) => {
  return request({
    url: '/app/platform/unit/getUnitById',
    method: 'get',
    params,
  });
};
