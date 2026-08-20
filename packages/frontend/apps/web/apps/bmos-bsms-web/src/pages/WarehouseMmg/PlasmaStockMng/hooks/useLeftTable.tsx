import { TableColumn } from '@bmos/components';

export const useLeftTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('血浆数量'),
      dataIndex: 'plasmaNum',
      width: 80,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 150,
      sorter: true,
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
