import {
  calculateApi,
  getAvailableBoundMaterialBatchApi,
  getBatchDetailApi,
} from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { nextTick, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const addUseTable = (openDetailFlag, infoData, activeInfo, satisfied) => {
  const { showNotify } = useNotify();

  const tableRef = ref();
  const selectData = ref([]);
  const showDetailData = ref({});
  const initBasicItemsData = [
    {
      label: t('物料信息'),
      field: 'materialName',
    },
    {
      label: t('物料批号'),
      field: 'materialBatchNo',
    },
    {
      label: t('生产日期'),
      field: 'produceDate',
    },
    {
      label: t('有效期至'),
      field: 'expiredDate',
    },
  ];
  const basicItemsData = ref([...initBasicItemsData]);
  const isInit = ref(false);

  const tableProps = reactive({
    pagination: false,
    data: [],
    type: 'selection',
    border: false,
    tableColProps: [
      {
        prop: 'materialBatchNo',
        label: t('物料批号'),
        width: 240,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'ACTION',
        label: t('批次详情'),
        width: 120,
        thProps: {
          align: 'left',
        },
        actions: ({ row }) => {
          return [
            {
              label: t('查看'),
              onClick: async () => {
                const { data } = await getBatchDetailApi({ materialBatchId: row.materialBatchId });
                showDetailData.value = {
                  materialName: `${data.materialMergeCode}-${data.materialName}`,
                  materialBatchNo: data.materialBatchNo,
                  produceDate: data.produceDate,
                  expiredDate: data.expiredDate,
                };
                basicItemsData.value = [...initBasicItemsData];
                data.fieldList.forEach((item, index) => {
                  if (activeInfo.value.consistenceParamCode === item.field) {
                    basicItemsData.value.push({
                      label: item.fieldName,
                      field: `fieldType_${index}`,
                    });
                    showDetailData.value[`fieldType_${index}`] = item.fieldValue;
                  }
                });
                openDetailFlag.value = true;
              },
            },
          ];
        },
      },
      {
        prop: 'materialQuantity',
        label: t('物料量'),
        width: 150,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'preparationQuantity',
        label: t('配液量'),
        width: 150,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'surplus',
        label: t('剩余量'),
        width: 150,
        thProps: {
          align: 'left',
        },
        customRender: ({ row }) => {
          return <span>{row.materialQuantity - row.preparationQuantity}</span>;
        },
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 110,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'expiredDate',
        label: t('有效期至'),
        width: 240,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'supplier',
        label: t('供应商'),
        width: 300,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'producer',
        label: t('生产商'),
        width: 300,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'originalBatchNo',
        label: t('原厂批号'),
        width: 200,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'originalCode',
        label: t('原始编码'),
        width: 200,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'reportNo',
        label: t('报告单编号'),
        width: 240,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'licenceNo',
        label: t('放行单编号'),
        width: 240,
        thProps: {
          align: 'left',
        },
      },
    ],
  });

  const selectionChange = async (val) => {
    if (isInit.value) {
      isInit.value = false;
      return;
    }
    if (!val.length) {
      tableProps.data.forEach((item) => {
        item.preparationQuantity = '';
      });
    }
    selectData.value = [...val];
    const params = {
      formulaMaterialId: activeInfo.value.id,
      materialBatchIdList: selectData.value.map(item => item.materialBatchId) || [],
      preparationPlanId: infoData.value.id,
    };
    try {
      const { data } = await calculateApi(params);
      const allSelectObj = {};
      data.preparationQuantityList.forEach((item) => {
        allSelectObj[item.materialBatchId] = item.preparationQuantity;
      });
      tableProps.data.forEach((item) => {
        item.preparationQuantity = allSelectObj[item.materialBatchId] || '';
      });
      satisfied.value = data.satisfied;
      activeInfo.value.targetConcentration.waring = !satisfied.value;
      activeInfo.value.targetConcentration.success = satisfied.value;
    }
    catch (error) {
      error.message && showNotify({ type: 'danger', message: error.message });
    }
  };

  // 获取起称量任务详情
  const getTableData = async () => {
    const { data } = await getAvailableBoundMaterialBatchApi({
      formulaMaterialId: activeInfo.value.id,
      preparationPlanId: infoData.value.id,
    });
    tableProps.data = data;
    const changeList = [];
    data.forEach((item, index) => {
      if (item.bound) {
        changeList.push(index);
      }
    });
    isInit.value = true;
    nextTick(() => {
      tableRef.value.tableRef?.toggleRowSelection(changeList, true);
    });
  };

  return {
    tableRef,
    tableProps,
    getTableData,
    selectionChange,
    selectData,
    satisfied,
    showDetailData,
    basicItemsData,
  };
};
