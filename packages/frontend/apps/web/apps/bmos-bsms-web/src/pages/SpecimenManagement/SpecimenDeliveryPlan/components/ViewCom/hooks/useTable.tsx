import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

export const useTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 180,
      resizable: true,
      formItemProps: {
        order: 1,
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
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 180,
      resizable: true,
      formItemProps: {
        order: 2,
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
      title: t('大托盘号'),
      dataIndex: 'palletNo',
      width: 150,
      hideInTable: true,
      resizable: true,
      formItemProps: {
        order: 3,
      },
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name ?? '-';
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      sorter: true,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
      sorter: true,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfoVO?.name ?? '-';
      },
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfoVO?.sex?.name ?? '-';
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfoVO?.bloodType?.name ?? '-';
      },
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
