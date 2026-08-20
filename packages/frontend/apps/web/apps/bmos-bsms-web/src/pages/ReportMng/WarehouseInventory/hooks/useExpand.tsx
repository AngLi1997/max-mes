import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('货位编号'),
      dataIndex: 'cargoSpaceNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('大托盘号'),
      dataIndex: 'bigContainerNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('数量'),
      dataIndex: 'statisticsQuantity',
      width: 120,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'statisticsWeight',
      width: 120,
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
