import request from '@/utils/request/request.js';

// 获取步骤列表
export const getTodoListApi = (params) => request.get('/api/app/mes/flow/todoPage', params);

// 获取任务列表 /api/app/mes/flow/task/todoPage
export const getTaskListApi = (params) => request.get('/api/app/mes/flow/task/todoPage', params);

// 获取待办数量  /api/app/mes/flow/todo/count
export const getTodoCountApi = (params) => request.get('/api/app/mes/flow/todo/count', params);

// 获取任务列表 
export const getTodoPageApi = (params) => request.get('/api/app/mes/flow/todoPage/fresh', params);

// 获取任务节点下待办列表 
export const getStepTodoPageApi = (params) => request.get('/api/app/mes/flow/step/todoPage/fresh', params);

// 强制开启任务
export const coerceActiveApi = (data) => request.post('/api/app/mes/flow/coerce/active', data);

// 强制完成任务
export const coerceProcedureCompleteApi = (data) => request.post('/api/app/mes/flow/coerce/procedure/complete', data);
