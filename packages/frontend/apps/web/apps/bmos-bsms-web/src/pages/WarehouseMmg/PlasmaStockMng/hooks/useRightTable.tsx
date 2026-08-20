import { TableColumn } from '@bmos/components';

export const useRightTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 180,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 100,
      sorter: true,
      resizable: true,
    },
    // {
    //   title: t('血浆状态'),
    //   dataIndex: 'status',
    //   width: 100,
    //   resizable: true,
    //   customRender: ({ record }) => {
    //     return record?.status?.name;
    //   },
    // },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('大托盘号'),
      dataIndex: 'bigContainerNo',
      width: 170,
      resizable: true,
    },
  ];

  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  return {
    pageRef,
    columnsFirst,
    setRef,
    fetchData,
  };
};
