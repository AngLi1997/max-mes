import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 140,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 180,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResultValue',
      width: 120,
      resizable: true,
    },
    {
      title: t('检验次数'),
      dataIndex: 'checkNum',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkNum?.name;
      },
    },
    {
      title: t('试剂批号'),
      dataIndex: 'reagentBatchNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qualityControllerBatchNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('检验人'),
      dataIndex: 'checkBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('检验日期'),
      dataIndex: 'checkDate',
      width: 170,
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
