import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.boxIdUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.boxIdDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
      resizable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.sampleNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.sampleNoDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('分拣批次'),
      dataIndex: 'sortingPlanBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInSearch: true,
      width: 150,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      resizable: true,
    },
    {
      title: t('标本状态'),
      dataIndex: 'sampleStatus',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleStatus?.name;
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 150,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.name;
      },
    },
    {
      title: t('性别'),
      dataIndex: 'plasmaDonorSex',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.sex?.name;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'plasmaDonorBloodType',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.bloodType?.name;
      },
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
      hideInSearch: true,
      width: 150,
      resizable: true,
    },
  ];

  const formProps: Partial<FormProps> = {
    showAdvancedButton: true,
    baseColProps: {
      span: 12,
    },
  };

  // const setRef = (el: any) => {
  //   pageRef.value = el;
  // };

  // const fetchData = async (params: any) => {
  //   pageRef.value.fetchData(0, params);
  // };

  return {
    tableRef,
    columns,
    formProps,
    // setRef,
    // fetchData,
  };
};
