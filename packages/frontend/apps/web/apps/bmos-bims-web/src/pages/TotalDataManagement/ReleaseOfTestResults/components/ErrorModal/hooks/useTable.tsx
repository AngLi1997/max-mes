import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('错误项目'),
      dataIndex: 'errorList',
      width: 300,
      resizable: true,
      customRender: ({ record }) => {
        return record?.errorList?.join(',');
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
