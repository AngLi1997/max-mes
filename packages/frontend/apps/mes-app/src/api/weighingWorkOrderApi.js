import request from '@/utils/request/request.js';

// 查询称量工单列表数据 /api/app/mes/weigh/centre2/execute/ticket/page
export const queryWeighCenterExecuteTicketPage = params =>
  request.get('/api/app/mes/weigh/centre2/execute/ticket/page', params);

// 根据称量工单id查工单详情 /api/app/mes/weigh/centre2/execute/ticket/detail/{ticketId}
export const queryWeighCenterExecuteTicketDetail = ticketId =>
  request.get(`/api/app/mes/weigh/centre2/execute/ticket/detail/${ticketId}`);

// 扫描物料件/设备号查询物料件信息 /api/app/mes/tag/scan/lh-storage-material
export const weighCenterExecuteScanLhStorageMaterial = params =>
  request.get('/api/app/mes/tag/scan/lh-storage-material', params);

// 绑定操作人和复核人 /api/app/mes/weigh/centre2/execute/ticket/bind-operator
export const weighCenterExecuteTicketBindOperator = params =>
  request.post('/api/app/mes/weigh/centre2/execute/ticket/bind-operator', params);

// 执行称量需求 /api/app/mes/weigh/centre2/execute/requirement/execute
export const weighCenterExecuteRequirementExecute = params =>
  request.post('/api/app/mes/weigh/centre2/execute/requirement/execute', params);

// 根据工单需求id查询工单需求详情 /api/app/mes/weigh/centre2/execute/requirement/detail/{requirementId}
export const queryWeighCenterExecuteRequirementDetail = requirementId =>
  request.get(
    `/api/app/mes/weigh/centre2/execute/requirement/detail/${requirementId}`,
  );

// 获取余料称量详情 /api/app/mes/weigh/centre2/execute/ticket/{ticketId}/oddment-info
export const queryWeighCenterExecuteTicketOddmentInfo = ticketId =>
  request.get(`/api/app/mes/weigh/centre2/execute/ticket/${ticketId}/oddment-info`);

// 完成物料称量 /api/app/mes/weigh/centre2/execute/requirement/finish
export const weighCenterExecuteRequirementFinish = params =>
  request.post(
    '/api/app/mes/weigh/centre2/execute/requirement/finish',
    params,
  );

// 添加物料件 /api/app/mes/weigh/centre2/execute/requirement/bind-storage-material
export const weighCenterExecuteRequirementBindStorageMaterial = params =>
  request.post(
    '/api/app/mes/weigh/centre2/execute/requirement/bind-storage-material',
    params,
    {
      header: {
        'Bmos-MenuId': '121020007',
        'Bmos-Operation': 1,
        'Bmos-Operation-Business': '称量工单执行-消耗',
      },
    },
  );

// 保存称量记录 /api/app/mes/weigh/centre2/execute/requirement/record
export const weighCenterExecuteRequirementRecord = params =>
  request.post(
    '/api/app/mes/weigh/centre2/execute/requirement/record',
    params,
    {
      header: {
        'Bmos-MenuId': '121020007',
        'Bmos-Operation': 0,
        'Bmos-Operation-Business': '称量工单执行',
      },
    },
  );

// 保存余料称量记录 /api/app/mes/weigh/centre2/execute/requirement/oddment
export const weighCenterExecuteRequirementOddment = params =>
  request.post(
    '/api/app/mes/weigh/centre2/execute/requirement/oddment',
    params,
    {
      header: {
        'Bmos-MenuId': '121020007',
        'Bmos-Operation': 0,
        'Bmos-Operation-Business': '称量工单余料称量',
      },
    },
  );

// 称量结果签名 /api/app/mes/weigh/centre2/execute/requirement/sign
export const weighCenterExecuteRequirementSign = params =>
  request.post(
    '/api/app/mes/weigh/centre2/execute/requirement/sign',
    params,
  );

// 分页查询工单历史 /api/app/mes/weigh/centre2/execute/ticket/history/page
export const queryWeighCenterExecuteTicketHistoryPage = params =>
  request.get('/api/app/mes/weigh/centre2/execute/ticket/history/page', params);

// 获取工单称量记录 /api/app/mes/weigh/centre2/execute/ticket/{ticketId}/weigh-records
export const queryWeighCenterExecuteTicketWeighRecords = ticketId =>
  request.get(`/api/app/mes/weigh/centre2/execute/ticket/${ticketId}/weigh-records`);
