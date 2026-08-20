import { ref, reactive } from 'vue'
import { t } from '@bmos/i18n';

export const useDescriptions = () => {
  // 基础信息
  const basicData = ref<any>({})
  const basicItems = reactive<any>([
    {
      label: t('请验时间'),
      field: 'verifyTime',
    },
    {
      label: t('检品编码'),
      field: 'productsCode',
    },
    {
      label: t('检品名称'),
      field: 'productsName',
    },
    {
      label: t('规格'),
      field: 'specification',
    },
    {
      label: t('批号'),
      field: 'batchNo',
    },
    {
      label: t('数量'),
      field: 'inspectNumber',
    },
    {
      label: t('级别'),
      field: 'level',
    },
    {
      label: t('生产单位'),
      field: 'productionUnit',
    },
    {
      label: t('供货单位'),
      field: 'supplier',
    },
    {
      label: t('实验包'),
      field: 'packageName',
    },
    {
      label: t('请验部门'),
      field: 'verifyDept',
    },
    {
      label: t('请验人'),
      field: 'verifier',
    },
    {
      label: t('备注'),
      field: 'remark',
    }
  ])

  // 取样信息
  const sampleData = ref<any>({})
  const sampleItems = reactive<any>([
    {
      label: t('取样量'),
      field: 'result',
    },
    {
      label: t('取样人'),
      field: 'operatorName',
    },
    {
      label: t('取样时间'),
      field: 'operateTime',
    }
  ])
  return {
    basicData,
    basicItems,
    sampleData,
    sampleItems
  }
}