import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('检品批号'),
      dataIndex: 'sampleBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('检品数量'),
      dataIndex: 'totalNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('合格数量'),
      dataIndex: 'qualifiedNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('不合格数量'),
      dataIndex: 'unQualifiedNum',
      width: 170,
      resizable: true,
    },
    {
      title: t('报告人'),
      dataIndex: 'reportBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('收检日期'),
      dataIndex: 'acceptanceDate',
      width: 170,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 200,
      resizable: true,
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
