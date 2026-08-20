import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('分拣后批号'),
      dataIndex: 'sortingPlanBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('大托盘编号'),
      dataIndex: 'bigContainerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('数量'),
      dataIndex: 'num',
      width: 100,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 120,
      resizable: true,
    },
    {
      title: t('箱/托盘号起'),
      dataIndex: 'containerNoUp',
      width: 170,
      resizable: true,
    },
    {
      title: t('箱/托盘号止'),
      dataIndex: 'containerNoDown',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 150,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 150,
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
