import { ref } from 'vue';
import { t } from '@/utils/useBmosI18n.js';

export const useTable = (data) => {
  const tableConfig = ref([
    {
      label: t('物料名称'),
      filed: 'materialName',
      width: '130'
    },
    {
      label: t('物料编码'),
      filed: 'mergeCode',
      width: '100'
    },
    {
      label: t('物料批号'),
      filed: 'storageMaterialBatchNo',
      width: '150'
    },
    {
      label: t('配料量'),
      filed: 'targetQuantity',
      width: '100'
    },
    {
      label: t('已称量'),
      filed: 'finishedQuantity',
      width: '100'
    },
    {
      label: t('未称量'),
      filed: 'unFinishedQuantity',
      width: '100'
    },
    {
      label: t('单位'),
      filed: 'unit',
      width: '50',
      align: 'center'
    },
    {
      label: t('状态'),
      filed: 'weighStatus',
      width: '70',
      align: 'center'
    }
  ]);
  return {
    tableConfig
  };
};
