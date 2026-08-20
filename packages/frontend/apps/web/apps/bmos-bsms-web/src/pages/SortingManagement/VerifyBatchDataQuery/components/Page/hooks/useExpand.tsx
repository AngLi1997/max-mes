import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = (enterDetail: any, openCnt: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('计划批号'),
      dataIndex: 'planBatchNo',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return <a onClick={() => enterDetail(record)}>{record?.planBatchNo}</a>;
      },
    },
    {
      title: t('重量'),
      dataIndex: 'totalWeight',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('数量'),
      dataIndex: 'totalNumber',
      width: 120,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.totalNumber ? <a onClick={() => openCnt(record)}>{record?.totalNumber}</a> : 0;
      },
    },
    {
      title: t('已拣数量'),
      dataIndex: 'sortedNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('待拣数量'),
      dataIndex: 'toSortNumber',
      width: 160,
      sorter: true,
      resizable: true,
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
  };
};
