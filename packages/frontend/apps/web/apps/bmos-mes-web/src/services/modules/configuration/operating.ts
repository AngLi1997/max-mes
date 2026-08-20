import { getItem } from '@/utils';
import request from '../../service';
//查询操作规程分类树列表 /api/app/mes/operate/list/category
export const getOperateListCategory = () => {
  return request({
    url: '/app/mes/operate/list/category',
    method: 'GET',
  });
};

//新增操作规程分类 /api/app/mes/operate/save/category
export const postOperateSaveCategory = (data: any) => {
  return request({
    url: '/app/mes/operate/save/category',
    method: 'POST',
    data,
  });
};

//编辑操作规程分类 /api/app/mes/operate/update/category
export const postOperateUpdateCategory = (data: any) => {
  return request({
    url: '/app/mes/operate/update/category',
    method: 'POST',
    data,
  });
};

//删除操作规程分类 /api/app/mes/operate/delete/category
export const deleteOperateDeleteCategory = (params: any) => {
  return request({
    url: '/app/mes/operate/delete/category',
    method: 'DELETE',
    params,
  });
};

//查询操作规程管理列表 /api/app/mes/operate/rule/page/list
export const getOperateRulePageList = (params: any) => {
  return request({
    url: '/app/mes/operate/rule/page/list',
    method: 'GET',
    params,
  });
};

//查询操作规程版本管理列表 /api/app/mes/operate/rule/version/page/list
export const getOperateRuleVersionPageList = (params: any) => {
  return request({
    url: '/app/mes/operate/rule/version/page/list',
    method: 'GET',
    params,
  });
};

//新增文件 /api/app/mes/operate/rule/save
export const postOperateRuleSave = (data: any) => {
  return request({
    url: '/app/mes/operate/rule/save',
    method: 'POST',
    data,
  });
};

//文件上传 /api/app/mes/operate/rule/upload
export const postOperateRuleUpload = (data: any) => {
  return request({
    url: '/app/mes/operate/rule/upload',
    method: 'POST',
    data,
  });
};

//根据主键id查询版本详情 /api/app/mes/operate/rule/version/details
export const getOperateRuleVersionDetails = (params: any) => {
  return request({
    url: '/app/mes/operate/rule/version/details',
    method: 'GET',
    params,
  });
};
//编辑操作规程版本详情 /api/app/mes/operate/rule/version/update
export const postOperateRuleVersionUpdate = (data: any) => {
  return request({
    url: '/app/mes/operate/rule/version/update',
    method: 'POST',
    data,
  });
};
//编辑状态 /api/app/mes/operate/rule/version/update/state
export const postOperateRuleVersionUpdateState = (data: any) => {
  return request({
    url: '/app/mes/operate/rule/version/update/state',
    method: 'POST',
    data,
  });
};
//立即生效 /api/app/mes/operate/rule/version/update/effect

export const getOperateRuleVersionUpdateEffect = (params: any) => {
  return request({
    url: '/app/mes/operate/rule/version/update/effect',
    method: 'GET',
    params,
  });
};

// 直接生效
export const putRuleVersionUpdateValid = (versionId: any) => {
  return request({
    url: `/app/mes/operate/rule/version/update/valid/${versionId}`,
    method: 'PUT',
  });
};

//发起流程审核：启用or停用 /api/app/mes/operate/rule/version/start/flow
export const postOperateRuleVersionStartFlow = (data: any) => {
  return request({
    url: '/app/mes/operate/rule/version/start/flow',
    method: 'POST',
    data,
  });
};

//新增版本 /api/app/mes/operate/rule/version/save
export const postOperateRuleVersionSave = (data: any) => {
  return request({
    url: '/app/mes/operate/rule/version/save',
    method: 'POST',
    data,
  });
};

//pdf加载 /operate/rule/version/download
export const getOperateRuleVersionDownload = (params: any) => {
  return request({
    url: '/app/mes/operate/rule/version/download',
    method: 'GET',
    params,
    headers: {
      'Bmos-Access-Token': getItem('BMOS-ACCESS-TOKEN'),
    },
    responseType: 'arraybuffer',
  });
};

//查询代办流程分页 /api/app/mes/operate/rule/version/page/todo/flow
export const getOperateRuleVersionPageTodoFlow = (params: any) => {
  return request({
    url: '/app/mes/operate/rule/version/page/todo/flow',
    method: 'GET',
    params,
  });
};

//获取服务器时间
export const getServerTimeApi = () => {
  return request({
    url: '/app/mes/execute/server/time',
    method: 'GET',
  });
};
