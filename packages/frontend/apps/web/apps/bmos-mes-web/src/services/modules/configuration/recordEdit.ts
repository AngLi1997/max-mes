import request from '../../service';

// /api/app/mes/record/redact/record
export const recordRedactRecord = (params: API.RecordRedactRecordReq) => {
  return request({
    url: '/app/mes/record/redact/record',
    method: 'GET',
    params,
  });
};
// 不记历史
export const recordManageRedactRecord = (params: API.RecordRedactRecordReq) => {
  return request({
    url: '/app/mes/record/manage/redact/record',
    method: 'GET',
    params,
  });
};

// /api/app/mes/record/production/id
export const getRecordProductionId = () => {
  return request({
    url: '/app/mes/record/production/id',
    method: 'GET',
  });
};

// /api/app/mes/record/save/component 最终添加组件内容
export const recordSaveComponent = (data: API.RecordSaveComponentReq) => {
  return request({
    url: '/app/mes/record/save/component',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/update/component 最终编辑组件内容
export const recordUpdateComponent = (data: API.RecordSaveComponentReq) => {
  return request({
    url: '/app/mes/record/update/component',
    method: 'POST',
    data,
  });
};

// 记录管理最终添加组件内容
export const recordManageSaveComponent = (data: API.RecordSaveComponentReq) => {
  return request({
    url: '/app/mes/record/manage/save/component',
    method: 'POST',
    data,
  });
};

// /api/app/platform/dict/list/dict/code 根据code查询二级列表数据
export const dictListDictCode = (params: any) => {
  return request({
    url: '/app/platform/dict/list/dict/code',
    method: 'GET',
    params,
  });
};

// /api/app/mes/record/save/record/item 添加/编辑记录项
export const recordSaveRecordItem = (data: API.SaveRecordItemReq) => {
  return request({
    url: '/app/mes/record/save/record/item',
    method: 'POST',
    data,
  });
};

// /api/app/mes/record/delete/record/item
export const recordDeleteRecordItem = (params: API.DeleteRecordItemReq) => {
  return request({
    url: '/app/mes/record/delete/record/item',
    method: 'GET',
    params,
  });
};

// /api/app/mes/record/copy/record/item
export const recordCopyRecordItem = (params: API.CopyRecordItemReq) => {
  return request({
    url: '/app/mes/record/copy/record/item',
    method: 'GET',
    params,
  });
};

// /api/app/mes/record/list/component
export const recordListComponent = (params: API.RecordListComponentReq) => {
  return request({
    url: '/app/mes/record/list/component',
    method: 'GET',
    params,
  });
};

// 记录信息及记录项列表
export const recordItemDetail = (params: any) => {
  return request({
    url: '/app/mes/record/item/detail',
    method: 'GET',
    params,
  });
};

// 记录项名称修改
export const recordItemChangeName = (data: any) => {
  return request({
    url: '/app/mes/record/item/changeName',
    method: 'POST',
    data,
  });
};

// 单个记录项新增
export const recordItemSingleSave = (data: any) => {
  return request({
    url: '/app/mes/record/item/singleSave',
    method: 'POST',
    data,
  });
};

// 记录项编辑和组件保存
export const recordItemSingleEdit = (data: any) => {
  return request({
    url: '/app/mes/record/item/singleEdit',
    method: 'POST',
    data,
  });
};

// 修改记录项顺序
export const recordItemChangeSort = (data: any) => {
  return request({
    url: '/app/mes/record/item/changeSort',
    method: 'POST',
    data,
  });
};

// 记录编辑历史
export const recordItemEdit = (params: any) => {
  return request({
    url: '/app/mes/record/item/edit',
    method: 'PUT',
    params,
  });
};
