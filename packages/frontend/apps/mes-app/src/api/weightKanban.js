// 称量看板接口
import request from '@/utils/request/request.js';

// 获取工单概览数据 /api/app/mes/weigh/dashboard/ticket/overview
export const reqWeighDashboardTicketOverview = params =>
  request.get('/api/app/mes/weigh/dashboard/ticket/overview', params);

// 获取今日工单数据 /api/app/mes/weigh/dashboard/ticket/today
export const reqWeighDashboardTicketToday = params =>
  request.get('/api/app/mes/weigh/dashboard/ticket/today', params);

// 获取生产批次配料完成情况 /api/app/mes/weigh/dashboard/production/completion
export const reqWeighDashboardProductionCompletion = params =>
  request.get('/api/app/mes/weigh/dashboard/production/completion', params);

// 获取称量工单趋势 /api/app/mes/weigh/dashboard/ticket/trend
export const reqWeighDashboardTicketTrend = params =>
  request.get('/api/app/mes/weigh/dashboard/ticket/trend', params);

// 获取称量需求趋势 /api/app/mes/weigh/dashboard/requirement/trend
export const reqWeighDashboardRequirementTrend = params =>
  request.get('/api/app/mes/weigh/dashboard/requirement/trend', params);

// 获取称量工单完成情况 /api/app/mes/weigh/dashboard/ticket/completion
export const reqWeighDashboardTicketCompletion = params =>
  request.get('/api/app/mes/weigh/dashboard/ticket/completion', params);
