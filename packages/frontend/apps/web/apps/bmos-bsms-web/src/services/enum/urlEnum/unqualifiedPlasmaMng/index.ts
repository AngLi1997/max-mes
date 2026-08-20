import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 不合格血浆管理
const UnqualifiedPlasmaMngEnum: Record<string, log> = {
  '170070001': {
    // 浆站不合格血浆管理
    [`${BASE_URL}/unqualified/plasma/handle`]: {
      type: OperationType.edit,
      business: '处理不合格浆站信息',
    },
    [`${BASE_URL}/unqualified/plasma/issue`]: {
      type: OperationType.add,
      business: '出具不合格记录',
    },
    [`${BASE_URL}/unqualified/plasma/register`]: {
      type: OperationType.edit,
      business: '登记不合格信息',
    },
    [`${BASE_URL}/unqualified/plasma`]: {
      type: OperationType.delete,
      business: '删除不合格登记',
    },
    [`${BASE_URL}/unqualified/plasma/report/save`]: {
      type: OperationType.add,
      business: '出具不合格血浆核查报告',
    },
    [`${BASE_URL}/unqualified/plasma/report/edit`]: {
      type: OperationType.edit,
      business: '编辑不合格血浆核查报告',
    },
    [`${BASE_URL}/unqualified/plasma/station/export`]: {
      type: OperationType.export,
      business: '导出',
    },
  },
  '170070002': {
    // 企业不合格血浆管理
    [`${BASE_URL}/unqualified/plasma/issue`]: {
      type: OperationType.add,
      business: '出具不合格记录',
    },
    [`${BASE_URL}/unqualified/plasma/report/save`]: {
      type: OperationType.add,
      business: '出具不合格血浆核查报告',
    },
    [`${BASE_URL}/unqualified/plasma/report/edit`]: {
      type: OperationType.edit,
      business: '编辑不合格血浆核查报告',
    },
    [`${BASE_URL}/unqualified/plasma/company/export`]: {
      type: OperationType.export,
      business: '导出',
    },
  },
  '170070003': {
    // 不合格核查记录审核
    [`${BASE_URL}/unqualified/record/audit`]: {
      type: OperationType.audit,
      business: '不合格核查记录审核',
    },
  },
  '170070005': {
    // 不合格核查报告送审
    [`${BASE_URL}/unqualified/plasma/report/send-to-audit`]: {
      type: OperationType.edit,
      business: '不合格核查报告送审',
    },
    [`${BASE_URL}/unqualified/plasma/report/revocation`]: {
      type: OperationType.edit,
      business: '撤销',
    },
  },
  '170070006': {
    // 不合格核查报告审核
    [`${BASE_URL}/unqualified/plasma/report/audit/audit`]: {
      type: OperationType.audit,
      business: '不合格核查报告审核',
    },
  },
};

export default UnqualifiedPlasmaMngEnum;
