import {
  getBatchDetailApi,
  getBoundMaterialBatchApi,
} from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useTable = (opDetail, activeInfo) => {
  const tableRef = ref();
  const showDetailData = ref({
    materialName: '',
    materialBatchNo: '',
    produceDate: '',
    expiredDate: '',
    targetConcentration: '',
  });
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

  const tableProps = reactive({
    pagination: false,
    data: [],
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
                opDetail(row);
              },
            },
          ];
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
        width: 220,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'licenceNo',
        label: t('放行单编号'),
        width: 220,
        thProps: {
          align: 'left',
        },
      },
    ],
  });

  const getTableList = async (req) => {
    const { data } = await getBoundMaterialBatchApi(req);
    tableProps.data = [...data];
  };

  return {
    tableRef,
    tableProps,
    getTableList,
    showDetailData,
    basicItemsData,
  };
};
