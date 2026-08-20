import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'plasmaStationName',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearanceResult',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.appearanceResult?.name}</span>;
      },
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆批号'),
      dataIndex: 'syncBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
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
