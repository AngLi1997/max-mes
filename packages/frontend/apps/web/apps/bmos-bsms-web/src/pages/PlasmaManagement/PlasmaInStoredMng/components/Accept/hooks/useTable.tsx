import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号起'),
      dataIndex: 'containerNoUp',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号止'),
      dataIndex: 'containerNoDown',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆编号起'),
      dataIndex: 'plasmaNoUp',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆编号止'),
      dataIndex: 'plasmaNoDown',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('运输温度'),
      dataIndex: 'temperature',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('运输时间'),
      dataIndex: 'transitTime',
      width: 150,
      sorter: true,
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
  //   pageRef.value?.fetchData(0, params);
  // };

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
