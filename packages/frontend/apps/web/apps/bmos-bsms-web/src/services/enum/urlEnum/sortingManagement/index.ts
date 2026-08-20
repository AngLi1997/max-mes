import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 分拣管理
const SortingManagementEnum: Record<string, log> = {
  '170080002': {
    // 分拣计划
    [`${BASE_URL}/sorting-plan/save`]: {
      type: OperationType.add,
      business: '新增分拣计划',
    },
    [`${BASE_URL}/sorting-plan/add`]: {
      type: OperationType.edit,
      business: '分拣计划批量添加',
    },
    [`${BASE_URL}/sorting-plan/back`]: {
      type: OperationType.edit,
      business: '分拣计划批量退回',
    },
    [`${BASE_URL}/sorting-plan/finish`]: {
      type: OperationType.edit,
      business: '结束分拣计划',
    },
    [`${BASE_URL}/sorting-plan`]: {
      type: OperationType.delete,
      business: '删除分拣计划',
    },
  },
  '170080004': {
    // 分拣出库
    [`${BASE_URL}/sorting-out-warehouse/out`]: {
      type: OperationType.edit,
      business: '分拣出库/整盘出库',
    },
    // [`${BASE_URL}/sorting-out-warehouse/out`]: {
    //   type: OperationType.edit,
    //   business: '整盘出库',
    // },
  },
  '170080005': {
    // 血浆手动分拣
    [`${BASE_URL}/sorting-plasma/submit`]: {
      type: OperationType.edit,
      business: '提交',
    },
    [`${BASE_URL}/sorting-plasma/revocation`]: {
      type: OperationType.edit,
      business: '撤销',
    },
  },
  '170080006': {
    // 合并血浆
    [`${BASE_URL}/plasma/merge/merge`]: {
      type: OperationType.edit,
      business: '提交合并',
    },
  },
  '170080007': {
    // 合并标本
    [`${BASE_URL}/sample/merge/merge`]: {
      type: OperationType.edit,
      business: '提交合并',
    },
  },
  '170080008': {
    // 分拣维护
    [`${BASE_URL}/sorting-maintain/plasma/revocation`]: {
      type: OperationType.edit,
      business: '血浆撤销',
    },
    [`${BASE_URL}/sorting-maintain/sample/revocation`]: {
      type: OperationType.edit,
      business: '标本撤销',
    },
  },
  '170080009': {
    // 不合格血浆分拣
    // [`${BASE_URL}/sorting-unqualified-plasma/scan`]: {
    //   type: OperationType.edit,
    //   business: '扫描',
    // },
    [`${BASE_URL}/sorting-unqualified-plasma/submit`]: {
      type: OperationType.edit,
      business: '提交',
    },
    [`${BASE_URL}/sorting-unqualified-plasma/revocation`]: {
      type: OperationType.edit,
      business: '撤销',
    },
  },
  '170080010': {
    // 不合格标本分拣
    // [`${BASE_URL}/sorting-unqualified-sample/scan`]: {
    //   type: OperationType.edit,
    //   business: '扫描',
    // },
    [`${BASE_URL}/sorting-unqualified-sample/submit`]: {
      type: OperationType.edit,
      business: '提交',
    },
    [`${BASE_URL}/sorting-unqualified-sample/revocation`]: {
      type: OperationType.edit,
      business: '撤销',
    },
  },
};

export default SortingManagementEnum;
