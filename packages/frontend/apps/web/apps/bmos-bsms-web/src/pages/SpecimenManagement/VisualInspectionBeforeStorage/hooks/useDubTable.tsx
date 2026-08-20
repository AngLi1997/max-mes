import { getSpecimenAppearanceBeforeList } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  const totalObj = ref({
    totalNum: 0,
    toBeDetectedNum: 0,
  });

  // 批号
  const syncBatchNo = ref('');
  // 箱号
  const boxId = ref('');

  const pageObj = reactive({
    pageNum: 1,
    pageSize: 50,
  });

  const leftLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,

      verifyStatus: 0,
    };
    if (!datas.syncBatchNo) {
      totalObj.value = {
        totalNum: 0,
        toBeDetectedNum: 0,
      };
      syncBatchNo.value = '';
      boxId.value = '';
      return new Promise(resolve => {
        setTimeout(() => {
          resolve({
            data: [],
            total: 0,
          });
        }, 0);
      });
    }

    syncBatchNo.value = datas.syncBatchNo;
    boxId.value = datas.boxId;
    if (pageObj.pageNum === datas.pageNum && pageObj.pageSize === datas.pageSize) {
      fetchDubData('right');
    }
    const { data } = await getSpecimenAppearanceBeforeList(datas);

    pageObj.pageNum = datas.pageNum;
    pageObj.pageSize = datas.pageSize;

    totalObj.value = {
      totalNum: data.totalNum,
      toBeDetectedNum: data.waitCheckNum,
    };

    return {
      data: data.detailList,
    };
  };

  const leftTableProps = reactive({
    requests: [leftLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    // titles: [t('血浆列表')],
    formProps: [
      {
        showAdvancedButton: false,
        labelWidth: 100,
        baseColProps: {
          span: 9,
        },
      },
    ] as Partial<FormProps>[],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    columns: [
      [
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          hideInSearch: true,
          width: 170,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.originOrgInfo?.originOrg}</span>;
          },
        },
        {
          title: t('标本批号'),
          dataIndex: 'sampleBatchNo',
          formItemProps: {
            field: 'syncBatchNo',
          },
          width: 170,
          resizable: true,
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          hideInSearch: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('标本箱号'),
          dataIndex: 'boxId',
          width: 170,
          resizable: true,
        },
        {
          title: t('检测状态'),
          dataIndex: 'appearanceFlag',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.appearanceFlag?.name}</span>;
          },
        },
        {
          title: t('标本外观'),
          dataIndex: 'appearance',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.appearance?.name}</span>;
          },
        },
        {
          title: t('提交人'),
          dataIndex: 'appearanceCheckBy',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('提交日期'),
          dataIndex: 'appearanceCheckDate',
          width: 180,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('待审标本外观'),
          dataIndex: 'waitAuditAppearance',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.waitAuditAppearance?.name}</span>;
          },
        },
        {
          title: t('审核状态'),
          dataIndex: 'auditStatus',
          width: 120,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.auditStatus?.name}</span>;
          },
        },
        {
          title: t('审核人'),
          dataIndex: 'auditBy',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditDate',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
      ] as TableColumn[],
    ],
  });

  const operationSelectedRows = ref<any>([]);

  // 多选
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
          disabled: record?.acceptanceStatus?.value == 0,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
          operationSelectedRows.value = selectedRows;
        }
      },
    },
    null,
  ]);

  const clearSelected = () => {
    if (!rowSelections[0]?.selectedRowKeys) return;
    rowSelections[0].selectedRowKeys = [];
    operationSelectedRows.value = [];
  };
  const rightLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,

      syncBatchNo: syncBatchNo.value,
      boxId: boxId.value,
      verifyStatus: 1,
    };
    if (!syncBatchNo.value) {
      return new Promise(resolve => {
        setTimeout(() => {
          resolve({
            data: [],
            total: 0,
          });
        }, 0);
      });
    }

    const { data } = await getSpecimenAppearanceBeforeList(datas);

    return {
      data: data.detailList,
    };
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('检验标本列表')],
    rowSelections,
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        showAdvancedButton: true,
        baseColProps: {
          span: 8,
        },
      },
    ] as Partial<FormProps>[],
    scrolls: [{ x: 800, y: 220 }],
    search: [false],
    columns: [
      [
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 120,
          resizable: true,
        },
        // {
        //   title: t('重量'),
        //   dataIndex: 'outType',
        //   width: 100,
        //   resizable: true,
        // },
        {
          title: t('标本外观'),
          dataIndex: 'appearance',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.appearance?.name}</span>;
          },
        },
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 120,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.originOrgInfo?.originOrg}</span>;
          },
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 120,
          resizable: true,
        },
        // {
        //   title: t('入库日期'),
        //   dataIndex: 'outBoxNoStart',
        //   width: 150,
        //   resizable: true,
        // },
      ] as TableColumn[],
    ],
  });

  // const scanList = ref<any[]>([]);

  // const clearScanList = () => {
  //   scanList.value = [];
  // };

  // const scanNoFn = (data: any) => {
  //   scanList.value = data;
  // };

  // 刷新列表
  const fetchDubData = (type?: 'left' | 'right') => {
    if (type != 'right') {
      dubTableRef.value?.leftRef.fetchData();
    }
    if (type != 'left') {
      clearSelected();
      dubTableRef.value?.rightRef.fetchData();
    }
  };

  return {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    rowSelections,
    operationSelectedRows,
    fetchDubData,
    clearSelected,
    syncBatchNo,
    // clearScanList,
    boxId,
    totalObj,
  };
};
