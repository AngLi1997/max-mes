import type { FormProps, TableColumn } from '@bmos/components';
import { TableInstance } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { correspondingTypeDict } = getDicts();
  const tableRef = ref<TableInstance>();

  const columnsBase: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'no',
      width: 190,
      resizable: true,
    },
    {
      title: t('对应编号'),
      dataIndex: 'corrPlasmaNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'primeContainerNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆状态'),
      dataIndex: 'plasmaStatus',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaStatus?.name || '-';
      },
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.corrRelationType?.name || '-';
      },
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 100,
      resizable: true,
    },
    {
      title: t('编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      // sorter: true,
      resizable: true,
    },
  ];

  const columns1: TableColumn[] = [
    ...columnsBase,
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 120,
      resizable: true,
      // customRender: ({ record }) => {
      //   return record?.immunityType?.name || '-';
      // }
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      width: 100,
      sorter: true,
      resizable: true,
    },
  ];

  const columns2: TableColumn[] = [
    ...columnsBase,
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItem',
      width: 150,
      resizable: true,
    },
  ];

  const formProps = reactive<Partial<FormProps>>({
    // 是否展示更多
    showAdvancedButton: true,
    baseColProps: {
      span: 12,
    },
    labelWidth: 105,
    labelAlign: 'left',
    schemas: [
      {
        label: t('血浆箱/托盘号'),
        field: 'primeContainerNo',
        component: 'Input',
      },
      {
        label: t('血浆编号'),
        field: 'no',
        component: 'Input',
      },
      {
        label: t('对应类型'),
        field: 'corrRelationType',
        component: 'Select',
        componentProps: {
          options: correspondingTypeDict,
        },
      },
    ],
  });

  return {
    tableRef,
    columnsBase,
    columns1,
    columns2,
    formProps,
  };
};
