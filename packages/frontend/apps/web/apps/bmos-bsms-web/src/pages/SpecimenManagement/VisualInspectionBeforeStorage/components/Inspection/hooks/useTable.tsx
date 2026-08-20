import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgInfo',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('标本外观'),
      dataIndex: 'appearance',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.appearance?.name}</span>;
      },
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 140,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
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
