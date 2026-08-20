import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkItem?.name;
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 190,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('配板编号'),
      dataIndex: 'equipmentNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('孔位号'),
      dataIndex: 'holeNo',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('OD值'),
      dataIndex: 'od',
      sorter: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('比值'),
      dataIndex: 'ratio',
      sorter: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResult',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkResult?.name;
      },
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
      width: 170,
      resizable: true,
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qualityControllerBatchNo',
      width: 170,
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
      sorter: true,
      width: 150,
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
