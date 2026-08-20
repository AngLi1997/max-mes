import request from '../../service';
// 生产查询-批次审核相关接口
// 表格列表分页接口
export const getBatchApprovalPage = (params: any) => {
  return request({
    url: '/app/mes/confirm/page',
    method: 'GET',
    params,
  });
};
// 查询工艺名称下拉框
export const getProcessNameList = () => {
  return request({
    url: '/app/mes/confirm/list',
    method: 'GET',
  });
};

// 工艺填写审核结论
export const postUpdateProcess = (data: any) => {
  return request({
    url: '/app/mes/confirm/update/process',
    method: 'POST',
    data,
  });
};
// 通过工艺结论id查工序列表页
export const getProcedurePage = (params: any) => {
  return request({
    url: '/app/mes/procedure/confirm/page',
    method: 'GET',
    params,
  });
};
// 工序填写审核结论
export const postUpdateProcedure = (data: any) => {
  return request({
    url: '/app/mes/procedure/confirm/update/procedure',
    method: 'POST',
    data,
  });
};
// 生产查询-批次审核查询相关接口
// 表格列表
export const getBatchApprovalQueryPage = (params: any) => {
  return request({
    url: '/app/mes/confirm/list/process/opinion',
    method: 'GET',
    params,
  });
};
// 根据工艺id查询工序名下拉
export const getProcedureNameList = (params: any) => {
  return request({
    url: '/app/mes/procedure/confirm/list/procedure/name',
    method: 'GET',
    params,
  });
};
// 查饼图数据
export const getPieChartData = (params: any) => {
  return request({
    url: '/app/mes/confirm/process/statistics',
    method: 'GET',
    params,
  });
};

// 批次追溯分页
export const getPlanRetracePage = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/page',
    method: 'GET',
    params,
  });
};

// 查批次追溯详情
export const getPlanRetraceInfo = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/info',
    method: 'GET',
    params,
  });
};

// 执行信息
export const getPlanRetraceExecuteTracePage = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/execute/trace/page',
    method: 'GET',
    params,
  });
};
// 物料信息
export const getPlanRetraceMaterialTracePage = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/material/trace/page',
    method: 'GET',
    params,
  });
};

// 设备使用
export const getPlanRetraceEquipmentTracePage = (params: any) => {
  return request({
    url: '/app/platform/equipment/log/operate/page',
    method: 'GET',
    params,
  });
};

// 房间清场信息
export const getPlanRetraceRoomTracePage = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/room/trace/page',
    method: 'GET',
    params,
  });
};

// 检验信息
export const getInspectPage = (params: any) => {
  return request({
    url: '/app/mes/inspect/page',
    method: 'GET',
    params,
  });
};

// 检验信息-检验结果详情
export const getInspectProgramResult = (params: any) => {
  return request({
    url: '/app/mes/inspect/program/result',
    method: 'GET',
    params,
  });
};

// 偏差信息
export const getPlanRetraceDeviationTracePage = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/deviation/trace/page',
    method: 'GET',
    params,
  });
};

// 查批记录左侧列表
export const getPlanRetraceExecuteList = (params: any) => {
  return request({
    url: '/app/mes/plan/retrace/executeList',
    method: 'GET',
    params,
  });
};

// 查询已存在的记录复制版本列表
export const getExecuteCopyVersionExistedList = (params: any) => {
  return request({
    url: '/app/mes/execute/copyVersion/existedList',
    method: 'GET',
    params,
  });
};

// 查询某个记录项最新值(回填记录页里的值)
export const getExecuteItemLatestData = (params: any) => {
  return request({
    url: '/app/mes/execute/item/latest/data',
    method: 'GET',
    params,
  });
};

// 点击每个记录项时回显最右边步骤条信息
export const getExecuteFieldDataList = (params: any) => {
  return request({
    url: '/app/mes/execute/field/data/list',
    method: 'GET',
    params,
  });
};

// 查询关联的工艺集合
export const getProcessRecursionRelationProcesses = (params: any) => {
  return request({
    url: '/app/mes/process/recursion/relation/processes',
    method: 'GET',
    params,
  });
};
// 分页查询物料追溯模板信息
export const reqMaterialTraceTemplateQueryPage = (params: any) => {
  return request({
    url: '/app/mes/material/trace/template/queryPage',
    method: 'GET',
    params,
  });
};
//新增物料追溯配置模板
export const reqMaterialTraceTemplateCreate = (data: any) => {
  return request({
    url: '/app/mes/material/trace/template/create',
    method: 'POST',
    data,
  });
};

//删除模板
export const reqMaterialTraceTemplateDelete = (params: any) => {
  return request({
    url: '/app/mes/material/trace/template/delete',
    method: 'DELETE',
    params,
  });
};

//停用模板
export const reqMaterialTraceTemplateDisable = (params: any) => {
  return request({
    url: '/app/mes/material/trace/template/disable',
    method: 'PUT',
    params,
  });
};

//启用模板
export const reqMaterialTraceTemplateEnable = (params: any) => {
  return request({
    url: '/app/mes/material/trace/template/enable',
    method: 'PUT',
    params,
  });
};

// 编辑模板
export const reqMaterialTraceTemplateEdit = (data: any) => {
  return request({
    url: '/app/mes/material/trace/template/edit',
    method: 'POST',
    data,
  });
};

// 查询物料追溯模板信息(详情接口)
export const reqMaterialTraceTemplateQueryDetail = (params: any) => {
  return request({
    url: '/app/mes/material/trace/template/queryDetail',
    method: 'GET',
    params,
  });
};
