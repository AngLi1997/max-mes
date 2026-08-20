import { getAppearanceBeforeList } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  const syncBatchNo = ref('');

  const containerNo = ref('');

  const totalObj = ref({
    totalNum: 0,
    toBeDetectedNum: 0,
  });
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
      syncBatchNo.value = '';
      containerNo.value = '';
      totalObj.value = {
        totalNum: 0,
        toBeDetectedNum: 0,
      };
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
    containerNo.value = datas.containerNo;

    if (pageObj.pageNum === datas.pageNum && pageObj.pageSize === datas.pageSize) {
      fetchDubData('right');
    }

    const { data } = await getAppearanceBeforeList(datas);

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
        labelAlign: 'left',
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
          title: t('血浆批号'),
          dataIndex: 'syncBatchNo',
          hideInTable: true,
        },
        {
          title: t('来源单位'),
          dataIndex: 'plasmaStationName',
          hideInSearch: true,
          width: 220,
          resizable: true,
        },
        {
          title: t('血浆批号'),
          dataIndex: 'syncBatchNo',
          width: 140,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          hideInSearch: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血浆箱/托盘号'),
          dataIndex: 'containerNo',
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
          title: t('血浆外观'),
          dataIndex: 'appearanceResult',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.appearanceResult?.name}</span>;
          },
        },
        {
          title: t('提交人'),
          dataIndex: 'applyByName',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('提交日期'),
          dataIndex: 'applyDate',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('待审血浆外观'),
          dataIndex: 'applyAppearance',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.applyAppearance?.name}</span>;
          },
        },
        {
          title: t('审核状态'),
          dataIndex: 'auditStatus',
          width: 140,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.auditStatus?.name}</span>;
          },
        },
        {
          title: t('审核人'),
          dataIndex: 'auditByName',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditDate',
          width: 150,
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

  const rightLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,

      syncBatchNo: syncBatchNo.value,
      containerNo: containerNo.value,
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

    const { data } = await getAppearanceBeforeList(datas);

    return {
      data: data.detailList,
    };
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('检验血浆列表')],
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
    search: [false],
    columns: [
      [
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 100,
          resizable: true,
        },
        {
          title: t('血浆外观'),
          dataIndex: 'applyAppearance',
          width: 140,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.applyAppearance?.name}</span>;
          },
        },
        {
          title: t('来源单位'),
          dataIndex: 'plasmaStationName',
          width: 170,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          resizable: true,
        },
        // {
        //   title: t('入库日期'),
        //   dataIndex: 'outBoxNoStart',
        //   width: 170,
        //   resizable: true,
        // },
      ] as TableColumn[],
    ],
  });
  const clearSelected = () => {
    if (!rowSelections[0]?.selectedRowKeys) return;
    rowSelections[0].selectedRowKeys = [];
    operationSelectedRows.value = [];
  };

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
    syncBatchNo,
    containerNo,
    dubTableRef,
    leftTableProps,
    rightTableProps,
    rowSelections,
    operationSelectedRows,
    fetchDubData,
    totalObj,
    clearSelected,
  };
};
