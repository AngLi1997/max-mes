import request from '../../service';

/**
 * @description /api/app/mes/lotRelease/template/category/tree 查询批签发模板分类树
 */
export const reqLotReleaseTemplateCategoryTree = () => {
  return request({
    url: '/app/mes/lotRelease/template/category/tree',
    method: 'GET',
  });
};

/**
 * @description /api/app/mes/lotRelease/template/category/createCategory 新增批签发模板分类
 */
export const reqLotReleaseTemplateCategoryCreateCategory = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/template/category/createCategory',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/category/delete 删除批签发模板分类
 * @param {String} id 分类id
 */
export const reqLotReleaseTemplateCategoryDelete = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/category/delete',
    method: 'DELETE',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/category/editCategory 修改批签发模板分类
 */
export const reqLotReleaseTemplateCategoryEditCategory = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/template/category/editCategory',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/queryPage 分页查询批签发模板数据
 */
export const reqLotReleaseTemplateQueryPage = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/template/queryPage',
    method: 'GET',
    params,
  });
};
/**
 * @description /api/app/mes/lotRelease/template/queryVersionPage 分页查询批签发模板版本数据
 */
export const reqLotReleaseTemplateQueryVersionPage = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/template/queryVersionPage',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/bindProcess 绑定工艺
 */
export const reqLotReleaseTemplateBindProcess = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/template/bindProcess',
    method: 'PUT',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/history 查看批签发模板版本操作历史
 * @param {String} id 模板id
 */
export const reqLotReleaseTemplateQueryHistory = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/history',
    method: 'GET',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/makeDefault 设为默认
 * @param {String} id 模板id
 */
export const reqLotReleaseTemplateMakeDefault = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/makeDefault',
    method: 'PUT',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/makeDefault 确认
 * @param {String} id 模板id
 */
export const reqLotReleaseTemplateMakeSure = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/makeSure',
    method: 'PUT',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/scrap 作废
 * @param {String} id 模板id
 */
export const reqLotReleaseTemplateScrap = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/scrap',
    method: 'PUT',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/uploadTemplate 上传批签发文件模板
 */
export const reqLotReleaseTemplateUploadTemplate = (file: any) => {
  return request({
    url: '/app/mes/lotRelease/template/uploadTemplate',
    method: 'POST',
    data: file,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/createTemplate 新增批签发模板
 */
export const reqLotReleaseTemplateCreateTemplate = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/template/createTemplate',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/listProcessIdByTemplateId 根据模板id查询配置的批签发模板关联的工艺id
 * @param {String} id 模板id
 */
export const reqLotReleaseTemplateQueryListProcessIdByTemplateId = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/listProcessIdByTemplateId',
    method: 'GET',
    params: {
      templateId: id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/createTemplateVersion 新增批签发模板版本
 */
export const reqLotReleaseTemplateCreateTemplateVersion = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/template/createTemplateVersion',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/uploadTemplateFile 上传批签发文件模板
 */
export const reqLotReleaseTemplateUploadTemplateFile = (data: any) => {
  return request({
    url: '/app/mes/lotRelease/template/updateTemplateFile',
    method: 'POST',
    data,
  });
};

/**
 * @description /api/app/mes/lotRelease/template/downloadTemplate 下载批签发文件模板
 * @param {String} id 模板id
 */
export const reqLotReleaseTemplateDownloadTemplate = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/downloadTemplate',
    method: 'POST',
    params: {
      id,
    },
    responseType: 'arraybuffer',
  });
};
/**
 * @description /api/app/mes/lotRelease/template/history 根据模板id查询配置的批签发模板关联的工艺id
 * @param {String} id 模板id
 */
export const reqLotReleaseHistory = (id: string) => {
  return request({
    url: '/app/mes/lotRelease/template/history',
    method: 'GET',
    params: {
      id,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/listByProcessId 根据工艺id查询配置的批签发模板
 * @param {String} processId 工艺id
 */
export const reqLotReleaseListByProcessId = (processId: string) => {
  return request({
    url: '/app/mes/lotRelease/template/listByProcessId',
    method: 'GET',
    params: {
      processId,
    },
  });
};

/**
 * @description /api/app/mes/lotRelease/template/listVersionByTemplateId 根据模版id查询的批签发模板版本列表
 * @param {String} templateId 模版id
 */
export const reqLotReleaseListVersionByTemplateId = (templateId: string) => {
  return request({
    url: '/app/mes/lotRelease/template/listVersionByTemplateId',
    method: 'GET',
    params: {
      templateId,
    },
  });
};
/**
 * @description /api/app/mes/lotRelease/manage/downloadByUrl 下载批签发文件模板
 * @param {String} id 模板id
 */
export const reqLotReleaseMangeDownloadByUrl = (url: string) => {
  return request({
    url: '/app/mes/lotRelease/manage/downloadByUrl',
    method: 'POST',
    params: {
      url,
    },
    responseType: 'arraybuffer',
  });
};
