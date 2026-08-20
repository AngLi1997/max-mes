import { getMesUnitListApi, reqMesEquipmentGetConfigByEquipmentIdApi } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { nextTick, reactive, ref } from 'vue';

export const useDetailsInfo = () => {
  const queryInfo = ref({});
  // 详情字段
  const detailsFields = ref([]);
  const equipmentUseList = ref([]);
  const equipmentStatusDetail = ref({});
  const equipmentInfoList = ref([]);
  const tableInfoRef = ref();
  const tableUseRef = ref();
  const tableStatusRef = ref();
  const states = [
    {
      type: 'success',
      label: t('可用'),
    },
    {
      type: 'success',
      label: t('可用'),
    },
    {
      type: 'warning',
      label: t('不可用'),
    },
    {
      type: 'primary',
      label: t('占用'),
    },
    {
      type: 'danger',
      label: t('故障'),
    },
  ];

  const getMesUnitList = async () => {
    const { data } = await getMesUnitListApi();
    return Object.assign(data.existUnit, data.totalUnit, data.unit);
  };

  const getDetail = async (query) => {
    queryInfo.value = query;
    try {
      const allUnit = await getMesUnitList();
      const { data } = await reqMesEquipmentGetConfigByEquipmentIdApi(query.equipmentId);
      equipmentStatusDetail.value = data;
      detailsFields.value = [
        {
          label: t('设备编号'),
          key: data.code,
        },
        {
          label: t('设备标签'),
          key: data.tagVOList?.map(item => item.name).join('、'),
        },
      ];
      equipmentUseList.value = [{
        status: data.status,
        statusName: data.statusName,
        productName: data.productName,
        batchNo: data.batchNo,
      }] || [];
      equipmentInfoList.value = [...(data.infoPropertyList || [])];
      nextTick(() => {
        tableInfoRef.value.tableData = equipmentInfoList.value.map((item) => {
          if (item.code.includes('UNIT')) {
            item.value = allUnit[item.value]?.unitName || '-';
          }
          return item;
        });
        tableUseRef.value.tableData = equipmentUseList.value;
        tableStatusRef.value.tableData = data.equipmentStatusAppVOList || [];
      });
    }
    catch (error) {
      console.log(error);
    }
  };
  const tableProps = reactive({
    pagination: false,
    border: false,
    tableColProps: [
      {
        prop: 'name',
        label: t('属性数据'),
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'value',
        label: t('属性值'),
        thProps: {
          align: 'left',
        },
      },
    ],
  });
  const tableUseProps = reactive({
    pagination: false,
    border: false,
    tableColProps: [
      {
        prop: 'statusName',
        label: t('使用状态'),
        thProps: {
          align: 'left',
        },
        customRender: ({ row }) => {
          if (states[row.status]) {
            return <WdTag type={states[row.status].type} plain>{ row.statusName }</WdTag>;
          }
          else {
            return <view></view>;
          }
        },
      },
      {
        prop: 'productName',
        label: t('产品名称'),
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'batchNo',
        label: t('占用批次'),
        thProps: {
          align: 'left',
        },
      },
    ],
  });
  const tableStatusProps = reactive({
    pagination: false,
    border: false,
    tableColProps: [
      {
        prop: 'finishStatus',
        label: t('设备状态'),
        thProps: {
          align: 'left',
        },
        customRender: ({ row }) => {
          if (row.finishStatus) {
            return <WdTag type="success" plain>{ `${t('已')}${row.name}` }</WdTag>;
          }
          else {
            return <WdTag type="warning" plain>{ row.name }</WdTag>;
          }
        },
      },
      {
        prop: 'expireDateTime',
        label: t('状态有效期至'),
        thProps: {
          align: 'left',
        },
      },
    ],
  });
  return {
    detailsFields,
    equipmentUseList,
    equipmentInfoList,
    getDetail,
    queryInfo,
    equipmentStatusDetail,
    tableProps,
    tableUseProps,
    tableStatusProps,
    tableInfoRef,
    tableUseRef,
    tableStatusRef,
  };
};
