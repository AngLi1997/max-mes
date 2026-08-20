import request from '../../service';

/**
 * @description /api/app/mes/plan/archive/template/category/tree 查询批记录模板分类树
 */
export const reqBatchRecordsTemplateCategoryTree = () => {
  return request({
    url: '/app/mes/plan/archive/template/category/tree',
    method: 'GET',
  });
};

/**
 * @description /api/app/mes/plan/archive/template/category/save 新增批记录模板分类
 */
export const reqBatchRecordsTemplateCategorySave = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/template/category/save',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/category/delete/{id} 删除批记录模板分类
 * @param {String} id 分类id
 */
export const reqBatchRecordsTemplateCategoryDelete = (id: string) => {
  return request({
    url: `/app/mes/plan/archive/template/category/delete/${id}`,
    method: 'DELETE',
  });
};

/**
 * @description /api/app/mes/plan/archive/template/category/update 修改批记录模板分类
 */
export const reqBatchRecordsTemplateCategoryUpdate = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/template/category/update',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/page 分页查询批记录模板数据
 */
export const reqBatchRecordsTemplatePage = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/template/page',
    method: 'GET',
    params,
  });
};
/**
 * @description /api/app/mes/plan/archive/template/version/page 分页查询批记录模板版本数据
 */
export const reqBatchRecordsTemplateVersionPage = (params: any) => {
  return request({
    url: '/app/mes/plan/archive/template/version/page',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/bind/process 绑定工艺
 */
export const reqBatchRecordsTemplateBindProcess = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/template/bind/process',
    method: 'POST',
    data,
  });
};
/**
 * @description /api/app/mes/plan/archive/template/history 查看批记录模板版本操作历史
 * @param {String} id 模板id
 */
export const reqBatchRecordsTemplateHistory = (id: string) => {
  return request({
    url: '/app/mes/plan/archive/template/history',
    method: 'GET',
    params: {
      templateVersionId: id,
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/template/version/normal 设为默认
 * @param {String} id 模板id
 */
export const reqBatchRecordsTemplateVersionNormal = (id: string) => {
  return request({
    url: '/app/mes/plan/archive/template/version/normal',
    method: 'PUT',
    data: {
      templateVersionId: id,
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/template/version/confirm 确认
 * @param {String} id 模板id
 */
export const reqBatchRecordsTemplateVersionConfirm = (id: string) => {
  return request({
    url: '/app/mes/plan/archive/template/version/confirm',
    method: 'PUT',
    data: {
      templateVersionId: id,
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/template/scrap 作废
 * @param {String} id 模板id
 */
export const reqBatchRecordsTemplateVersionScrap = (id: string) => {
  return request({
    url: '/app/mes/plan/archive/template/scrap',
    method: 'PUT',
    data: {
      templateVersionId: id,
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/template/fileUpload 上传批记录文件模板
 */
export const reqBatchRecordsTemplateFileUpload = (file: any) => {
  return request({
    url: '/app/mes/plan/archive/template/fileUpload',
    method: 'POST',
    data: file,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description /api/app/mes/plan/archive/template/save 新增批记录模板
 */
export const reqBatchRecordsTemplateSave = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/template/save',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/version/save 新增批记录模板版本
 */
export const reqBatchRecordsTemplateVersionSave = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/template/version/save',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/version/upload 重新上传批记录文件模板
 */
export const reqBatchRecordsTemplateVersionUpload = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/template/version/upload',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/version/download 下载批记录文件模板
 * @param {String} id 模板id
 */
export const reqBatchRecordsTemplateVersionDownload = (id: string) => {
  return request({
    url: '/app/mes/plan/archive/template/version/download',
    method: 'POST',
    data: {
      templateVersionId: id,
    },
    responseType: 'arraybuffer',
  });
};

/**
 * @description /api/app/mes/plan/archive/version/verify  批记录模版验证
 * @param {String} id 模板id
 */
export const reqBatchRecordsTemplateVersionVerify = (data: any) => {
  return request({
    url: '/app/mes/plan/archive/version/verify',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/plan/archive/template/path/download 下载批记录文件模板（path）
 * @param {String} path 文件路径
 */
export const reqBatchRecordsTemplatePathDownload = (path: string) => {
  return request({
    url: '/app/mes/plan/archive/template/path/download',
    method: 'GET',
    params: {
      path,
    },
    responseType: 'arraybuffer',
  });
};
