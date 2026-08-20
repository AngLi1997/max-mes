import {
  BMInfoTable,
  BMTable,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useForm = () => {
  const infoFormRef = ref();

  const setFormModel = (formModel) => {
    infoFormRef.value?.setFormModels(formModel);
  };

  // 检验信息
  const productDetails = reactive([
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
      title: t('物料类型'),
      dataIndex: 'materialType',
    },
    {
      title: t('物料信息'),
      dataIndex: 'materialName',
    },
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
    },
    {
      title: t('请验时间'),
      dataIndex: 'createTime',
    },
  ]);

  // 检验结果
  const inspectDetails = reactive([
    {
      title: t('请验单号'),
      dataIndex: 'inspectNo',
    },
    {
      title: t('汇总检验结果'),
      dataIndex: 'inspectResultLabel',
      styleFunc: (data) => {
        return {
          color: data.inspectResultValue === 'QUALIFIED' ? '#59BF78' : '#FF5633',
        };
      },
    },
  ]);

  const inspectTableProps = reactive({
    pagination: false,
    border: false,
    tableColProps: [
      {
        prop: 'inspectProgramNo',
        label: t('检项代码'),
      },
      {
        prop: 'inspectProgramName',
        label: t('检项名称'),
      },
      {
        prop: 'inspectResult',
        label: t('检项结果'),
      },
      {
        prop: 'inspectConclusion',
        label: t('检项结论'),
        customRender: ({ row }) => {
          return (<span style={{ color: row.inspectConclusion.value === 'UNQUALIFIED' ? '#FF5633' : '#242526' }}>{row.inspectConclusion.label}</span>);
        },
      },
    ],
  });

  const tableRef = ref();

  const setTableData = (data) => {
    tableRef.value.tableData = data;
  };

  const infoFormProps = reactive({
    schemas: [
      {
        field: 'productionInfoTitle',
        component: 'FormTitle',
        label: t('检验信息'),
        colProps: {
          span: 24,
        },
      },
      {
        field: 'productionInfo',
        component: ({ formModel }) => {
          return (
            <BMInfoTable
              details={productDetails}
              data={formModel}
            />
          );
        },
        colProps: {
          span: 24,
        },
      },
      {
        label: t('检验结果'),
        field: 'inspectResultTitle',
        component: 'FormTitle',
        colProps: {
          span: 24,
        },
      },
      {
        field: 'inspectResultInfo',
        component: ({ formModel }) => {
          return (
            <>
              <BMInfoTable
                details={inspectDetails}
                style={{ marginBottom: '9.38rpx' }}
                data={formModel}
              />
              <BMTable
                ref={tableRef}
                {...inspectTableProps}
              />
            </>

          );
        },
        colProps: {
          span: 24,
        },
      },
    ],
  });

  return {
    infoFormRef,
    infoFormProps,
    setTableData,
    setFormModel,
  };
};
