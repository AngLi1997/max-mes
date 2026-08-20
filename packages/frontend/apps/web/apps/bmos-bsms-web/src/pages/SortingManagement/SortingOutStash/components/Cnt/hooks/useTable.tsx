import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆重量'),
      dataIndex: 'weight',
      width: 110,
      sorter: true,
      resizable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return record?.corrRelationType?.name;
      },
    },
    {
      title: t('血浆状态'),
      dataIndex: 'plasmaStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaStatus?.name;
      },
    },
    {
      title: t('血浆外观'),
      dataIndex: 'applyAppearance',
      width: 150,
      resizable: true,
      customRender: ({ record }) => {
        return record?.applyAppearance?.name;
      },
    },
    {
      title: t('供浆类型'),
      dataIndex: 'plasmaType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaType?.name;
      },
    },
    {
      title: t('免疫类别'),
      dataIndex: 'immunityType',
      width: 100,
      resizable: true,
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
