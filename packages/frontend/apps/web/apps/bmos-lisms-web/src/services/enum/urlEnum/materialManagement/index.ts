import { BASE_URL } from '@/services/baseUrl';
import { t } from '@bmos/i18n';
import { OperationType } from '../../const';
import { log } from '../../type';

// 物料管理
const MaterialManagementEnum: Record<string, log> = {
  '210060001': {
    // 供应商信息
    [`${BASE_URL}/material/supplier/save`]: {
      type: OperationType.add,
      business: t('新增'),
    },
    [`${BASE_URL}/material/supplier/update`]: {
      type: OperationType.edit,
      business: t('编辑'),
    },
    [`${BASE_URL}/material/supplier/remove`]: {
      type: OperationType.delete,
      business: t('删除'),
    },
  },
  '210060002': {
    // 物料基础信息
    [`${BASE_URL}/material/save`]: {
      type: OperationType.add,
      business: t('新增'),
    },
    [`${BASE_URL}/material/update`]: {
      type: OperationType.edit,
      business: t('编辑'),
    },
    [`${BASE_URL}/material/remove`]: {
      type: OperationType.delete,
      business: t('删除'),
    },
  },
  '210060003': {
    // 物料接收
    [`${BASE_URL}/material/receive`]: {
      type: OperationType.edit,
      business: t('物料接收'),
    },
  },
  '210060004': {
    // 物料入库
    [`${BASE_URL}/material/inWarehouse`]: {
      type: OperationType.edit,
      business: t('物料入库'),
    },
    [`${BASE_URL}/material/receive/cancel`]: {
      type: OperationType.edit,
      business: t('撤销接收'),
    },
    [`${BASE_URL}/material/inWarehouse/edit`]: {
      type: OperationType.edit,
      business: t('编辑'),
    },
  },
  '210060005': {
    // 物料库存管理
    [`${BASE_URL}/material/use/receive`]: {
      type: OperationType.edit,
      business: t('物料领用'),
    },
    [`${BASE_URL}/material/use/scrap`]: {
      type: OperationType.edit,
      business: t('物料报废'),
    },
    [`${BASE_URL}/material/use/spot-check`]: {
      type: OperationType.edit,
      business: t('物料抽检'),
    },
    [`${BASE_URL}/material/use/return-material`]: {
      type: OperationType.edit,
      business: t('物料退货'),
    },
  },
  '210060006': {
    // 物料抽检
    [`${BASE_URL}/material/use/spot-check/submit`]: {
      type: OperationType.edit,
      business: t('提交抽检'),
    },
    [`${BASE_URL}/material/use/spot-check/revert`]: {
      type: OperationType.edit,
      business: t('撤销抽检'),
    },
  },
  '210060007': {
    // 抽检申请审核
    [`${BASE_URL}/material/use/spot-check/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
  },
  '210060008': {
    // 物料抽检放行
    [`${BASE_URL}/material/use/spot-check-pass/submit`]: {
      type: OperationType.audit,
      business: t('抽检放行'),
    },
    [`${BASE_URL}/material/use/spot-check-pass/update`]: {
      type: OperationType.edit,
      business: t('编辑'),
    },
    [`${BASE_URL}/material/use/download`]: {
      type: OperationType.export,
      business: t('下载'),
    },
  },
  '210060009': {
    // 物料领用审核
    [`${BASE_URL}/material/use/receive/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
  },
  '210060010': {
    // 物料报废审核
    [`${BASE_URL}/material/use/scrap/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
  },
  '210060011': {
    // 物料退货审核
    [`${BASE_URL}/material/use/return/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
  },
  '210060012': {
    // 物料出库
    [`${BASE_URL}/material/use/out`]: {
      type: OperationType.edit,
      business: t('出库'),
    },
  },
};

export default MaterialManagementEnum;
