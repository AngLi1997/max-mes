import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleType?.name}</span>;
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 120,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sex?.name}</span>;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.bloodType?.name}</span>;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearance',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.appearance?.name}</span>;
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.immunityType?.name}</span>;
      },
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params: any) => {
    pageRef.value.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
  };
};
