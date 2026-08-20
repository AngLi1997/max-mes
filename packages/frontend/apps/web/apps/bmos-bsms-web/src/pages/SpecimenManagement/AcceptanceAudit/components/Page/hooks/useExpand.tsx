import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // const operationSelectedRows = ref<any>([]);

  // // 多选
  // const rowSelections = reactive([
  //   {
  //     type: 'checkbox',
  //     hideSelectAll: false,
  //     columnWidth: 50,
  //     fixed: true,
  //     selectedRowKeys: [] as any[],
  //     getCheckboxProps: (record: any) => {
  //       return {
  //         disabled: false,
  //       };
  //     },
  //     onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
  //       if (rowSelections[0]?.selectedRowKeys) {
  //         rowSelections[0].selectedRowKeys = selectedRowKeys;
  //         operationSelectedRows.value = selectedRows;
  //       }
  //     },
  //   },
  //   null,
  // ]);

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleType?.name}</span>;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'no',
      width: 140,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.no}</span>;
      },
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.name}</span>;
      },
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.sex?.name}</span>;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.bloodType?.name}</span>;
      },
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
    // rowSelections,
    // operationSelectedRows,
  };
};
