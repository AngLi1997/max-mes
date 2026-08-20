import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgInfo',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号起'),
      dataIndex: 'boxIdUp',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号止'),
      dataIndex: 'boxIdDown',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本验收'),
      dataIndex: 'acceptanceResult',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.acceptanceResult?.name}</span>;
      },
    },
    {
      title: t('验收人'),
      dataIndex: 'acceptanceBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('验收日期'),
      dataIndex: 'acceptanceDate',
      width: 170,
      resizable: true,
    },
    {
      title: t('验收备注'),
      dataIndex: 'remark',
      width: 100,
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
