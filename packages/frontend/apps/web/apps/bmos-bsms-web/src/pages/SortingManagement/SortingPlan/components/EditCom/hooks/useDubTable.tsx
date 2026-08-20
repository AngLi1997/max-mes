import {
  getSampleSortingPlanDetailList,
  getSampleSortingPlanSelectableList,
  getSortingPlanDetailList,
  getSortingPlanSelectableList,
  sortingPlanBatchAdd,
  sortingPlanBatchBack,
} from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { DataRequestFn, FormProps, Key, TableColumn } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';
import { useTable } from './useTable';

export const useDubTable = (planBatchNo: string, type: 1 | 2) => {
  const { sortingStatusDict } = getDicts();
  const dubTableRef = ref<any>(null);

  const leftRequests = {
    1: getSortingPlanSelectableList,
    2: getSampleSortingPlanSelectableList,
  };

  // 多选
  const leftSelectedRows = ref<any>([]);
  const leftRowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (leftRowSelections[0]) {
          leftRowSelections[0].selectedRowKeys = selectedRowKeys;
          leftSelectedRows.value = selectedRows;
        }
        // operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandedTableMap = reactive<any>({});

  // 左列表一二级选中的所有数据
  const leftSelectedAllRows = computed(() => {
    let ans = {
      itemOrgNoList: [],
      batchNoList: [],
      flag: false,
    } as any;
    let setCheckNoList = new Set();

    for (let key in expandedTableMap) {
      expandedTableMap[key].myselectedRows?.forEach((item: any) => {
        setCheckNoList.add(item.batchNo);
        ans.itemOrgNoList.push(item.itemOrgNo);
      });
    }

    // expandedRowKeys.value.forEach((key: any) => {
    //   expandedTableMap[key].myselectedRows?.forEach((item: any) => {
    //     setCheckNoList.add(item.batchNo);
    //     ans.itemOrgNoList.push(item.itemOrgNo);
    //   });
    // });
    leftSelectedRows.value.forEach((item: any) => {
      if (!setCheckNoList.has(item.batchNo)) {
        ans.batchNoList.push(item.batchNo);
      }
    });
    ans.flag = ans.itemOrgNoList.length > 0 || ans.batchNoList.length > 0;
    return ans;
  });

  const leftLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,
    };

    if (!datas.planBatchNo) {
      return {
        data: [],
      };
    }

    const res = await leftRequests[type](datas);

    const keys = res?.data?.list?.map((item: any) => item.batchNo) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandedTableMap[key].fetchData();
      }
    });

    return res;
  };

  const leftTableProps = reactive({
    rowSelections: leftRowSelections,
    requests: [leftLoadData as DataRequestFn],
    formProps: [
      {
        showAdvancedButton: true,
        baseColProps: {
          span: 12,
        },
        schemas: [
          {
            label: type === 1 ? t('血浆箱号') : t('标本箱号'),
            field: 'containerNo',
            component: 'Input',
          },
          {
            label: t('采浆日期'),
            field: 'slurryDate',
            component: 'RangePicker',
            componentProps: {
              format: 'YYYY-MM-DD',
              picker: 'date',
              valueFormat: 'YYYY-MM-DD',
            },
          },
        ],
        fieldMapToTime: [['slurryDate', ['slurryDateBegin', 'slurryDateEnd'], 'YYYY-MM-DD']],
      },
    ] as Partial<FormProps>[],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    scrolls: [{ x: 700, y: 220 }],
    rowKeys: ['batchNo'],
    columns: [
      [
        {
          title: type === 1 ? t('核查批号') : t('标本批号'),
          dataIndex: 'batchNo',
          width: 180,
          resizable: true,
        },
        {
          title: t('来源单位'),
          dataIndex: 'originOrg',
          resizable: true,
        },
        {
          title: t('数量'),
          dataIndex: 'num',
          width: 80,
          sorter: true,
          resizable: true,
        },
        {
          title: t('操作'),
          key: 'ACTION',
          fixed: 'right',
          width: 80,
          actions: ({ record }) => [
            {
              label: t('添加'),
              // ifShow: hasPermission('111020001000002'),
              onClick: () => {
                Modal.confirm({
                  title: t('是否将这些数据加入计划?'),
                  icon: h(ExclamationCircleOutlined),
                  async onOk() {
                    try {
                      const data = {
                        planBatchNo,
                        batchNoList: [record.batchNo],
                      };
                      await sortingPlanBatchAdd(data);
                      message.success(t('操作成功'));
                      fetchDubData();
                    } catch (error: any) {
                      error.message && message.error(error.message);
                      return Promise.reject();
                    }
                  },
                  onCancel() {},
                });
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
    expandedRowsChanges: [
      async (expandedKeys: Key[]) => {
        expandedRowKeys.value = expandedKeys;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandedTableMap[newKey]) {
          expandedTableMap[newKey] = useTable(planBatchNo, type, fetchDubData);
        } else {
          expandedTableMap[newKey].fetchData();
        }
      },
    ],
  });

  // 多选
  const rightSelectedRows = ref<any[]>([]);
  const rightRowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rightRowSelections[0]) {
          rightRowSelections[0].selectedRowKeys = selectedRowKeys;
          rightSelectedRows.value = selectedRows;
        }
        // operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);

  const rightRequests = {
    1: getSortingPlanDetailList,
    2: getSampleSortingPlanDetailList,
  };

  const rightLoadData = async (params: any) => {
    const datas = {
      ...params,
    };
    if (!datas.planBatchNo) {
      return {
        data: [],
      };
    }
    return await rightRequests[type](datas);
  };

  const rightTableProps = reactive({
    rowSelections: rightRowSelections,
    requests: [rightLoadData as DataRequestFn],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        showAdvancedButton: true,
        baseColProps: {
          span: 12,
        },
      },
    ] as Partial<FormProps>[],
    scrolls: [{ x: 700, y: 220 }],
    titles: [''],
    rowKeys: ['itemOrgNo'],
    columns: [
      [
        {
          title: type === 1 ? t('血浆编号') : t('标本编号'),
          dataIndex: 'itemNo',
          width: 200,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 150,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 150,
          hideInSearch: true,
          hideInTable: type === 2,
          resizable: true,
        },
        {
          title: t('效价'),
          dataIndex: 'titer',
          width: 80,
          sorter: true,
          hideInSearch: true,
          hideInTable: type === 2,
          resizable: true,
        },
        {
          title: t('分拣状态'),
          dataIndex: 'sortingStatus',
          width: 100,
          resizable: true,
          // customRender: ({ record }) => {
          //   return <span>{record?.sortingStatus?.name}</span>;
          // },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: sortingStatusDict,
            },
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 80,
          hideInSearch: true,
          hideInTable: type === 2,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.bloodType?.name}</span>;
          },
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 80,
          hideInSearch: true,
          hideInTable: type === 2,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.sex?.name}</span>;
          },
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 80,
          sorter: true,
          hideInSearch: true,
          hideInTable: type === 2,
          resizable: true,
        },
        {
          title: t('操作'),
          key: 'ACTION',
          fixed: 'right',
          width: 80,
          actions: ({ record }) => [
            {
              label: t('撤销'),
              // ifShow: !record.confirmStatus.value && hasPermission('100020006000008'),
              onClick: () => {
                Modal.confirm({
                  title: t('是否将这些数据退回?'),
                  icon: h(ExclamationCircleOutlined),
                  async onOk() {
                    try {
                      const data = {
                        planBatchNo,
                        itemOrgNoList: [record.itemOrgNo],
                      };
                      await sortingPlanBatchBack(data);

                      message.success(t('操作成功'));
                      fetchDubData();
                    } catch (error: any) {
                      error.message && message.error(error.message);
                      return Promise.reject();
                    }
                  },
                  onCancel() {},
                });
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
  });

  // 清空选中
  const clearSelected = () => {
    // 子列表清空
    expandedRowKeys.value.forEach((key: any) => {
      if (expandedTableMap[key].rowSelections[0]) {
        expandedTableMap[key].rowSelections[0].selectedRowKeys = [];
      }
      expandedTableMap[key].myselectedRows = [];
    });
    // 左列表清空
    if (leftRowSelections[0]) {
      leftRowSelections[0].selectedRowKeys = [];
    }
    leftSelectedRows.value = [];
    // 右列表清空
    if (rightRowSelections[0]) {
      rightRowSelections[0].selectedRowKeys = [];
    }
    rightSelectedRows.value = [];
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
    dubTableRef,
    leftSelectedRows,
    expandedRowKeys,
    expandedTableMap,
    leftSelectedAllRows,
    rightSelectedRows,
    leftTableProps,
    rightTableProps,
    clearSelected,
    fetchDubData,
  };
};
