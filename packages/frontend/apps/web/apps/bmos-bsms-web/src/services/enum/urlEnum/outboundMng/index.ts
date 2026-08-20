import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 出库管理
const OutboundMngEnum: Record<string, log> = {
  '170100001': {
    // 出库计划
    [`${BASE_URL}/outbound/plan/create`]: {
      type: OperationType.add,
      business: '新增出库计划',
    },
    [`${BASE_URL}/outbound/plan/update`]: {
      type: OperationType.edit,
      business: '编辑计划',
    },
    [`${BASE_URL}/outbound/plan`]: {
      type: OperationType.delete,
      business: '删除计划',
    },
    // [`${BASE_URL}/outbound/plan/choose`]: {
    //   type: OperationType.edit,
    //   business: '批量选择的血浆',
    // },
    // [`${BASE_URL}/outbound/plan/back`]: {
    //   type: OperationType.edit,
    //   business: '批量/按托盘/按分拣批次退回血浆',
    // },
    [`${BASE_URL}/outbound/plan/apply`]: {
      type: OperationType.edit,
      business: '计划申请',
    },
    [`${BASE_URL}/outbound/plan/edit/lot-no`]: {
      type: OperationType.edit,
      business: '更改计划批次',
    },
  },
  '170100002': {
    // 投料出库审核
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.edit,
      business: '计划审核',
    },
  },
  '170100003': {
    // 科研调用出库审核
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.edit,
      business: '计划审核',
    },
  },
  '170100004': {
    // 销毁出库审核
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.edit,
      business: '计划审核',
    },
  },
  '170100005': {
    // 质量授权人批准
    [`${BASE_URL}/outbound-process/process`]: {
      type: OperationType.edit,
      business: '计划审核',
    },
  },
  '170100006': {
    // 血浆投料出库
    [`${BASE_URL}/outbound/canDelivery`]: {
      type: OperationType.edit,
      business: '合并出库/整盘出库',
    },
  },
  '170100007': {
    // 血浆销毁出库
    [`${BASE_URL}/outbound/canDelivery`]: {
      type: OperationType.edit,
      business: '合并出库/整盘出库',
    },
  },
  '170100008': {
    // 血浆科研出库
    [`${BASE_URL}/outbound/canDelivery`]: {
      type: OperationType.edit,
      business: '合并出库/整盘出库',
    },
  },
  '170100009': {
    // 血浆调用出库
    [`${BASE_URL}/outbound/canDelivery`]: {
      type: OperationType.edit,
      business: '合并出库/整盘出库',
    },
  },
  '170100010': {
    // 出库血浆核对
    [`${BASE_URL}/outbound/check/delivery`]: {
      type: OperationType.edit,
      business: '血浆出库',
    },
  },
};

export default OutboundMngEnum;
