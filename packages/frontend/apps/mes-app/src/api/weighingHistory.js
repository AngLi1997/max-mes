// 称量历史相关接口
import request from '@/utils/request/request.js';

// 查询称量历史列表数据
export const reqWeighCenterExecuteQueryHistoryTaskPage = (params) =>
  request.get('/api/app/mes/weigh/centre/execute/queryHistoryTaskPage', params);

// 根据任务id查询称量结果列表
export const reqWeighCenterExecuteQueryRecordResultByTaskId = (params) =>
  request.get(
    '/api/app/mes/weigh/centre/execute/queryRecordResultByTaskId',
    params
  );
