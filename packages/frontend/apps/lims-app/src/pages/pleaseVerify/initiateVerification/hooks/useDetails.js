import { getProductionInfoApi } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useDetails = () => {
  const { showNotify } = useNotify();

  const bmosPrinterInstance = ref(null);
  // 详情api数据
  const detailData = ref({});
  // 物料详情列表
  const details = reactive([
    {
      title: t('产品名称'),
      dataIndex: 'productName',
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
    },
    {
      title: t('指令单编号'),
      dataIndex: 'planNo',
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
    },
    {
      title: t('批量'),
      dataIndex: 'batchQuantity',
    },
    {
      title: t('单位'),
      dataIndex: 'unitName',
    },
  ]);

  // 物料详情Api
  const detailsApi = async (planId) => {
    try {
      const { data } = await getProductionInfoApi(planId);
      detailData.value = data;
    }
    catch (error) {
      error.message && showNotify({ type: 'warning', message: error.message });
    }
  };

  return {
    details,
    detailsApi,
    detailData,
    bmosPrinterInstance,
  };
};
