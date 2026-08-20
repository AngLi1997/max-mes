import { BASE_URL } from '@/services/baseUrl';
import { t } from '@bmos/i18n';
import { OperationType } from '../../const';
import { log } from '../../type';

// 单项数据管理
const SingleDataManagementEnum: Record<string, log> = {
  '180020001': {
    // 蛋白含量
    [`${BASE_URL}/protein/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/protein/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020002': {
    // ALT
    [`${BASE_URL}/alt/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/alt/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020003': {
    // HBsAg
    [`${BASE_URL}/monoidal/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/monoidal/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
    [`${BASE_URL}/monoidal/data/read`]: {
      type: OperationType.edit,
      business: t('读取'),
    },
  },
  '180020004': {
    // 抗-HCV
    [`${BASE_URL}/monoidal/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/monoidal/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020005': {
    // 抗-HIV
    [`${BASE_URL}/monoidal/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/monoidal/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020006': {
    // 抗-TP
    [`${BASE_URL}/monoidal/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/monoidal/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020007': {
    // PCR
    [`${BASE_URL}/pcr/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/pcr/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020008': {
    // 甲肝抗体效价
    [`${BASE_URL}/titer/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/titer/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020009': {
    // 乙肝抗体效价
    [`${BASE_URL}/titer/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/titer/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020010': {
    // 狂犬病抗体效价
    [`${BASE_URL}/titer/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/titer/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
  '180020011': {
    // 破伤风抗体效价
    [`${BASE_URL}/titer/publish`]: {
      type: OperationType.edit,
      business: t('发布'),
    },
    [`${BASE_URL}/titer/check`]: {
      type: OperationType.edit,
      business: t('核对'),
    },
  },
};

export default SingleDataManagementEnum;
