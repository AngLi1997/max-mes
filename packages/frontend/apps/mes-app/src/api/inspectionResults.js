import request from '@/utils/request/request.js';

// 根据组件实例id获取组件实例详情
export const postInspectionInstanceByProps = (data) =>
  request.post('/api/app/mes/components/getInstanceByProps', data);

// 根据组件实例id获取组件实例详情
export const getInspectionInstanceByProps = (params) =>
  request.get('/api/app/mes/components/getInstanceByProps', params);

// 请验结果组件:获取非退回请验单分页
export const getNotRejectPage = (params) =>
  request.get('/api/app/mes/inspect/component/notRejectPage', params);

// 获取请验单详情
export const getInspectInfo = (params) =>
  request.get('/api/app/mes/inspect/info', params);

// 获取请验结果
export const getInspectProgramResult = (params) =>
  request.get('/api/app/mes/inspect/program/result', params);

// 请验结果组件:确定回填表单数据
export const inspectComponentConfirm = (data) =>
  request.post('/api/app/mes/inspect/component/confirm', data);