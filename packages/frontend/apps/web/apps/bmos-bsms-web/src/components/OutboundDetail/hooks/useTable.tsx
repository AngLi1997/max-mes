import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

export const useTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 180,
      resizable: true,
      formItemProps: {
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
      title: t('分拣批号'),
      dataIndex: 'batchNo',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('血浆类型'),
      dataIndex: 'type',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.type?.name}</span>;
      },
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      width: 80,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('血浆重量'),
      dataIndex: 'weight',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.bloodType?.name}</span>;
      },
    },
    {
      title: t('大托盘号'),
      dataIndex: 'bigContainerNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      width: 150,
      resizable: true,
      formItemProps: {
        label: t('血浆箱号'),
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
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      width: 150,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.corrRelationType?.name}</span>;
      },
    },
    {
      title: t('对应日期'),
      dataIndex: 'corrSlurryDate',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('天数'),
      dataIndex: 'corrSlurryDateDiff',
      width: 80,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sex?.name}</span>;
      },
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('入库人'),
      dataIndex: 'inWarehouseBy',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    // fieldMapToTime: [['outPlanDate', ['startDate', 'endDate'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
  };
};
