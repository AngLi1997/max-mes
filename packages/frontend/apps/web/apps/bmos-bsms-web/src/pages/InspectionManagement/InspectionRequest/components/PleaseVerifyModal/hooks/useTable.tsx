import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('入库批号'),
      dataIndex: 'inspectionBatchNo',
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
      title: t('标本编号起'),
      dataIndex: 'sampleNoUp',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本编号止'),
      dataIndex: 'sampleNoDown',
      width: 170,
      resizable: true,
    },
    {
      title: t('请验数量'),
      dataIndex: 'inspectionNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 140,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 140,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 190,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
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
  //   pageRef.value?.fetchData(0, params);
  // };

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
