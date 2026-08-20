import { TableColumn } from '@bmos/components';

export const useRightTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 180,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
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
      width: 160,
      sorter: true,
      resizable: true,
    },
    {
      title: t('大托盘号'),
      dataIndex: 'palletNo',
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
