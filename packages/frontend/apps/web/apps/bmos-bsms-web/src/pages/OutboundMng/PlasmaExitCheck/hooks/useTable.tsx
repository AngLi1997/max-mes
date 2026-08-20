import { TableColumn } from '@bmos/components';

// 二级列表
export const useTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      sorter: true,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 160,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('免疫类别'),
      dataIndex: 'immunityType',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      width: 100,
      sorter: true,
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
  ];

  const setRef = (el: any) => {
    pageRef.value = el;
  };
  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    columnsFirst,
    setRef,
    fetchData,
  };
};
