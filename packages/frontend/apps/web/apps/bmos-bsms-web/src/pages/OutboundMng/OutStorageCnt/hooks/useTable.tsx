import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, FormProps, Input } from 'ant-design-vue';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
      formItemProps: {
        order: 2,
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.plasmaNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.plasmaNoDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('分拣批次'),
      dataIndex: 'batchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆类型'),
      dataIndex: 'type',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name ?? '-';
      },
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('血浆重量'),
      dataIndex: 'weight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name ?? '-';
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
      formItemProps: {
        order: 1,
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.containerNoAfterUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.containerNoAfterDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('对应编号'),
      dataIndex: 'corrPlasmaNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.corrRelationType?.name ?? '-';
      },
    },
    {
      title: t('对应日期'),
      dataIndex: 'corrSlurryDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('天数'),
      dataIndex: 'corrSlurryDateDiff',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sex?.name ?? '-';
      },
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('入库人'),
      dataIndex: 'inWarehouseBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
  ];

  const formProps: Partial<FormProps> = {
    initialValues: {},
    showAdvancedButton: false,
    baseColProps: {
      span: 8,
    },
    actionColOptions: {
      span: 8,
    },
  };

  return {
    tableRef,
    formProps,
    columns,
  };
};
