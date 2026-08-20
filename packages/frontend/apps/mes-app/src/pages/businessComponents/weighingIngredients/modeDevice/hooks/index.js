import {
  getBalanceListByStationIdApi,
  getConfigByEquipmentIdApi
} from '@/api/weighingIngredientsApi.js';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, ref, nextTick } from 'vue';
import { useWeighingIngredientsStore } from '@/stores/businessComponents/weighingIngredients/index.js';
import { storeToRefs } from 'pinia';

export const useTable = (autoPermission) => {
  const weighingIngredientsStore = useWeighingIngredientsStore();
  const { weighingIngredientsData, weighingDetailsData } = storeToRefs(
    weighingIngredientsStore
  );
  const {
    setSelectedBalance,
    queryWeighDetailByPlanIdAndBatchId,
    setTableData
  } = weighingIngredientsStore;

  const relocationTable = ref();

  // 选中的表格数据
  const selectData = ref(null);
  // 表格标题
  const tableLabel = ref([
    {
      label: t('秤具名称'),
      align: 'left',
      dataIndex: 'balanceName',
      width: '190'
    },
    {
      label: t('秤具编号'),
      align: 'left',
      dataIndex: 'balanceCode',
      width: '190'
    },
    {
      label: t('设备位置'),
      align: 'left',
      dataIndex: 'equipmentPosition',
      width: '190'
    },
    {
      label: t('最大量程'),
      align: 'left',
      dataIndex: 'maxRange',
      width: '190'
    },
    {
      label: t('最小量程'),
      align: 'left',
      dataIndex: 'minRange',
      width: '190'
    },
    {
      label: t('秤具精度'),
      align: 'left',
      dataIndex: 'precision',
      width: '190'
    },
    {
      label: t('单位'),
      align: 'left',
      dataIndex: 'unit',
      width: '190'
    },
    {
      label: t('校准'),
      align: 'left',
      dataIndex: 'isCalibrated',
      width: '190'
    },
    {
      label: t('校准有效期'),
      align: 'left',
      dataIndex: 'calibrateExpiredDate',
      width: '190'
    }
  ]);
  const tableData = ref([]);

  // 校验秤具是否空闲
  const checkBalanceIsFree = async(equipmentId) => {
    const res = await getConfigByEquipmentIdApi({ equipmentId });
    return res.data.status === 1;
  };
  onMounted(() => {
    getBalanceListByStationIdApi(weighingIngredientsData.value.station).then(
      (res) => {
        tableData.value = res.data;
        nextTick(() => {
          if (tableData.value.length > 0 && autoPermission) {
            relocationTable.value.toggleRowSelection([0], true);
          }
        });
      }
    );
    queryWeighDetailByPlanIdAndBatchId();
    setTableData([]);
  });
  return {
    relocationTable,
    selectData,
    tableLabel,
    tableData,
    setSelectedBalance,
    weighingDetailsData,
    checkBalanceIsFree
  };
};
