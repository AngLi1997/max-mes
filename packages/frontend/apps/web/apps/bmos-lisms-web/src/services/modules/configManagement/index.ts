import { BASE_URL } from '@/services/baseUrl';
import request from '../../service';

/**
 * @description: 获取静态数据下拉列表 /config/list/{menuIdentify}
 */
export const getStaticDataConfigList = (menuIdentify: string) => {
  return request({
    url: `${BASE_URL}/config/list/${menuIdentify}`,
    method: 'GET',
  });
};

/**
 * @description: 获取所有参数 /config/list-all
 */
export const getConfigAll = () => {
  return request({
    url: `${BASE_URL}/config/list-all`,
    method: 'GET',
  });
};

/**
 * @description: 获取浆站下拉列表 /config/station-list
 */
export const getPlasmaStationList = () => {
  return request({
    url: `${BASE_URL}/config/station-list`,
    method: 'GET',
  });
};

/**
 * @description: 配置菜单查询 /config/menu-tree
 */
export const postStaticDataConfigMenuTree = (menuType: string) => {
  return request({
    url: `${BASE_URL}/config/menu-tree`,
    method: 'POST',
    data: {
      menuType,
    },
  });
};

/**
 * @description: 配置分页列表 /config/page
 */
export const postStaticDataConfigPage = (data: any) => {
  return request({
    url: `${BASE_URL}/config/page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 浆站配置分页列表 /config/station-page
 */
export const postStaticDataConfigStationPage = (data: any) => {
  return request({
    url: `${BASE_URL}/config/station-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 检验项分页列表 /config/inspect-page
 */
export const postStaticDataConfigInspectPage = (data: any) => {
  return request({
    url: `${BASE_URL}/config/inspect-page`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 新增配置 /config/create
 */
export const postStaticDataConfigCreate = (data: any) => {
  return request({
    url: `${BASE_URL}/config/create`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 编辑配置 /config/edit
 */
export const postStaticDataConfigEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/config/edit`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 编辑配置排序 /config/sort-edit
 */
export const postStaticDataConfigSortEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/config/sort-edit`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 编辑浆站配置 /config/station-edit
 */
export const postStaticDataConfigStationEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/config/station-edit`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 删除配置 /config/{id}
 * @param {string[]} id 配置id
 */
export const deleteStaticDataConfig = (data: any) => {
  return request({
    url: `${BASE_URL}/config/delete`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 编辑检验项目配置 /config/inspect-edit
 */
export const postStaticDataConfigInspectEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/config/inspect-edit`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 编辑检验标准设置 /config/inspect-rule-edit
 */
export const postStaticDataConfigInspectRuleEdit = (data: any) => {
  return request({
    url: `${BASE_URL}/config/inspect-rule-edit`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 文件一级列表分页 /config/file/template-page
 */
export const postConfigFileTemplatePage = (data: any) => {
  return request({
    url: `${BASE_URL}/config/file/template-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 文件二级列表分页 /config/file/template-child-page
 */
export const postConfigFileTemplateChildPage = (data: any) => {
  return request({
    url: `${BASE_URL}/config/file/template-child-page`,
    method: 'POST',
    data,
  });
};

/**
 * @description: 文件预览 /config/file/content
 */
export const postConfigFileContent = (data: any) => {
  return request({
    url: `${BASE_URL}/config/file/content`,
    method: 'POST',
    data,
    responseType: 'arraybuffer',
    original: true,
  });
};

/**
 * @description: 新增文件 /config/file/create
 */
export const postConfigFileCreate = (data: any) => {
  const formData = new FormData();
  for (const key in data) {
    if (Object.prototype.hasOwnProperty.call(data, key)) {
      formData.append(key, data[key]);
    }
  }
  return request({
    url: `${BASE_URL}/config/file/create`,
    method: 'POST',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * @description: 报告审核 /config/file/audit
 */
export const postConfigFileAudit = (data: any) => {
  return request({
    url: `${BASE_URL}/config/file/audit`,
    method: 'POST',
    data,
  });
};
/**
 * @description: 检验项目列表 /config/inspect-list
 */
export const getConfigInspectList = () => {
  return request({
    url: `${BASE_URL}/config/inspect-list`,
    method: 'GET',
  });
};

/**
 * @description: 查询所有仪器管理（H模块使用） /laboratory/instrument/query/config
 * @param {string} item 项目code
 */
export const getLaboratoryInstrumentQueryConfig = (item: string) => {
  return request({
    url: `${BASE_URL}/laboratory/instrument/query/config`,
    method: 'GET',
    params: {
      item,
    },
  });
};
