import { TableColumn } from '@bmos/components';

export const useTable = (addNos: any) => {
  const pageRef = ref<any>(null);
  // 多选
  const myselectedRows = ref<any>([]);
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
          myselectedRows.value = selectedRows;
        }
        // operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      hideInSearch: true,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('标本状态'),
      dataIndex: 'sampleStatus',
      width: 170,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleStatus?.name;
      },
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 150,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name;
      },
    },
    {
      title: t('大托盘号'),
      dataIndex: 'palletNo',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('添加'),
          // ifShow: hasPermission('111020001000002'),
          onClick: () => {
            addNos({ orgSampleNoList: [record?.orgSampleNo] });
          },
        },
      ],
    },
  ];

  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  return {
    pageRef,
    myselectedRows,
    rowSelections,
    columnsFirst,
    setRef,
    fetchData,
  };
};
