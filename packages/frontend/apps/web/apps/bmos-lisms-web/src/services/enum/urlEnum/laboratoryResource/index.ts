import { BASE_URL } from '@/services/baseUrl';
import { RecordSourceAuditTypeEnum } from '@/types';
import { t } from '@bmos/i18n';
import { OperationType } from '../../const';
import { log } from '../../type';

// 实验室资源管理
const LaboratoryResourceEnum: Record<string, log> = {
  '210050001': {
    // 领用库库存管理
    [`${BASE_URL}/laboratory/use/apply`]: {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.recordSource === RecordSourceAuditTypeEnum.OUT_CONSUME ? t('物料消耗') : t('物料报废'),
        };
      },
    },
  },
  '210050003': {
    // 领用库消耗审核
    [`${BASE_URL}/laboratory/use/deplete/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
  },
  '210050005': {
    // 领用库报废审核
    [`${BASE_URL}/laboratory/use/scrap/audit`]: {
      type: OperationType.audit,
      business: t('审核'),
    },
  },
  '210050007': {
    // 仪器设备管理
    [`${BASE_URL}/laboratory/instrument/save`]: {
      type: OperationType.add,
      business: t('新增'),
    },
    [`${BASE_URL}/laboratory/instrument/update`]: {
      type: OperationType.edit,
      business: t('编辑'),
    },
    [`${BASE_URL}/laboratory/instrument/delete`]: {
      type: OperationType.delete,
      business: t('删除'),
    },
  },
};

export default LaboratoryResourceEnum;
