import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 200,
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
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('检验次数'),
      dataIndex: 'checkNum',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkNum?.name;
      },
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  // const setRef = (el: any) => {
  //   pageRef.value = el;
  // };

  // const fetchData = async (params: any) => {
  //   pageRef.value.fetchData(0, params);
  // };

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
