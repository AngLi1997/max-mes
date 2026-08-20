import { TableColumn } from '@bmos/components';

// 二级列表
export const useTable = () => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('分拣批次'),
      dataIndex: 'sortingPlanBatchNo',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 190,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 120,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleType?.name}</span>;
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
      title: t('标本状态'),
      dataIndex: 'sampleStatus',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleStatus?.name}</span>;
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
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
