import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 标本管理
const SpecimenManagementEnum: Record<string, log> = {
  '170020001': {
    // 标本数据同步
    [`${BASE_URL}/sample-data-sync/receive`]: {
      type: OperationType.edit,
      business: '确认接收',
    },
    [`${BASE_URL}/sample-data-sync/revocation`]: {
      type: OperationType.edit,
      business: '撤销同步',
    },
    [`${BASE_URL}/sample-data-sync/update`]: {
      type: OperationType.edit,
      business: '修改信息',
    },
    [`${BASE_URL}/sample-data-sync/import`]: {
      type: OperationType.add,
      business: '手动导入',
    },
  },
  '170020002': {
    // 待入库标本管理
    [`${BASE_URL}/sample-wait-in-storage/transport`]: {
      type: OperationType.edit,
      business: '获取运输信息',
    },
    [`${BASE_URL}/sample-wait-in-storage/in-stock-acceptance`]: {
      type: OperationType.edit,
      business: '入库验收',
    },
  },
  '170020003': {
    // 入库前外观检验
    [`${BASE_URL}/sample-appearance-check/submit`]: {
      type: OperationType.edit,
      business: '外观检验提交',
    },
  },
  '170020004': {
    // 标本入库
    [`${BASE_URL}/sample-in-storage/batch/in`]: {
      type: OperationType.edit,
      business: '整批入库',
    },
  },
  '170020005': {
    // 入库标本核对
    [`${BASE_URL}/sample-in-warehouse-verify/submit`]: {
      type: OperationType.edit,
      business: '完成核对',
    },
  },
  '170020006': {
    // 已入库标本查询
    [`${BASE_URL}/sample-in-warehouse-query/maintain`]: {
      type: OperationType.edit,
      business: '标本维护',
    },
  },
  '170020007': {
    // 验收审核
    [`${BASE_URL}/sample-acceptance-audit/audit`]: {
      type: OperationType.edit,
      business: '验收审核',
    },
  },
  '170020009': {
    // 标本出库计划
    [`${BASE_URL}/sample-out-plan/create`]: {
      type: OperationType.add,
      business: '新增计划',
    },
    [`${BASE_URL}/sample-out-plan/updateNo`]: {
      type: OperationType.edit,
      business: '更改出库计划批次',
    },
    [`${BASE_URL}/sample-out-plan/update/info`]: {
      type: OperationType.edit,
      business: '更改出库计划信息',
    },
    [`${BASE_URL}/sample-out-plan/attention`]: {
      type: OperationType.edit,
      business: '出库计划申请',
    },
    [`${BASE_URL}/sample-out-plan`]: {
      type: OperationType.delete,
      business: '删除出库计划',
    },
    [`${BASE_URL}/sample-out-plan/batch/insert`]: {
      type: OperationType.edit,
      business: '批量添加',
    },
    [`${BASE_URL}/sample-out-plan/batch/back`]: {
      type: OperationType.edit,
      business: '批量退回',
    },
  },
  '170020010': {
    // 标本出库审核
    [`${BASE_URL}/sample-out-plan-audit/audit`]: {
      type: OperationType.edit,
      business: '标本出库计划审核',
    },
  },
  '170020011': {
    // 标本出库
    [`${BASE_URL}/sample-out-warehouse/out`]: {
      type: OperationType.relevance,
      business: '合并出库/整盘出库',
    },
  },
  '170020012': {
    // 出库标本核对
    // [`${BASE_URL}/sample-out-verify/recheck`]: {
    //   type: OperationType.edit,
    //   business: '重新核对',
    // },
    [`${BASE_URL}/sample-out-verify/out`]: {
      type: OperationType.edit,
      business: '标本出库',
    },
  },
  '170020013': {
    // 外观不合格审核
    [`${BASE_URL}/sample-appearance-audit/audit`]: {
      type: OperationType.audit,
      business: '外观不合格审核',
    },
  },
};

export default SpecimenManagementEnum;
