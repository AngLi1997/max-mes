import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkItem?.name;
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('配板编号'),
      dataIndex: 'equipmentNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('孔位号'),
      dataIndex: 'holeNo',
      width: 100,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResult',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkResult?.name;
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
