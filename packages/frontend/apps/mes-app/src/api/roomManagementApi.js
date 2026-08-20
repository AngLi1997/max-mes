import request from '@/utils/request/request.js';

// 获取房间列表 /api/app/mes/mobile/factory/room/page
export const getRoomListApi = params => request.get('/api/app/mes/mobile/factory/room/page', params);

// 获取生产前确认中的房间列表
export const newgetRoomListApiByLineId = params => request.get('/api/app/platform/feign/factory/room/pageByLineId', params);

// 根据房间id获取房间信息 /api/app/mes/mobile/factory/room/info/{id}
export const getRoomInfoApi = params => request.get(`/api/app/mes/mobile/factory/room/info/${params}`);

// 改变房间状态  /api/app/mes/mobile/factory/room/status
export const getRoomStatusApi = data => request.put('/api/app/mes/mobile/factory/room/status', data);

// 查询所有正在执行中的生产计划 /api/app/mes/plan/info/start/plan/list
export const getRoomPlanInfoStartPlanList = params => request.get('/api/app/mes/plan/info/start/plan/list', params);

// 查询工序集合 /api/app/mes/procedure/list
export const getRoomProcedureList = params => request.get('/api/app/mes/procedure/list', params);

// 获取具有权限的清场人/复核人 /api/app/mes/mobile/factory/room/auth/user
export const getRoomAuthUserApi = params => request.get('/api/app/mes/mobile/factory/room/auth/user', params);

// 根据房间code获取房间信息 /api/app/mes/mobile/factory/room/infoByCode/{code}
export const getRoomInfoByCodeApi = params => request.get(`/api/app/mes/mobile/factory/room/infoByCode/${params}`);
