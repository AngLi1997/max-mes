import {
  getSampleDeliveryPlanEditChooseList,
  getSampleDeliveryPlanEditList,
  getSortingCategoryListOptions,
} from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, Key, RenderCallbackParams, TableColumn } from '@bmos/components';
import { FormItemRest, Input } from 'ant-design-vue';
import { useTable } from './useTable';

export const useDubTable = (addNos: any) => {
  const { sampleTypeDict } = getDicts();
  const dubTableRef = ref<any>(null);

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
      orgSampleNoList: [],
      sortingPlanBatchNoList: [],
      flag: false,
    } as any;
    let setSortingPlanBatchNoList = new Set();

    for (let key in expandedTableMap) {
      expandedTableMap[key].myselectedRows?.forEach((item: any) => {
        setSortingPlanBatchNoList.add(item.sortingPlanBatchNo);
        ans.orgSampleNoList.push(item.orgSampleNo);
      });
    }

    // expandedRowKeys.value.forEach((key: any) => {
    //   expandedTableMap[key].myselectedRows?.forEach((item: any) => {
    //     setSortingPlanBatchNoList.add(item.sortingPlanBatchNo);
    //     ans.orgSampleNoList.push(item.orgSampleNo);
    //   });
    // });
    leftSelectedRows.value.forEach((item: any) => {
      if (!setSortingPlanBatchNoList.has(item.sortingPlanBatchNo)) {
        ans.sortingPlanBatchNoList.push(item.sortingPlanBatchNo);
      }
    });
    ans.flag = ans.orgSampleNoList.length > 0 || ans.sortingPlanBatchNoList.length > 0;
    return ans;
  });

  const leftLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,
    };
    if (typeof datas.qualityStatus === 'undefined') {
      return {
        data: [],
      };
    }

    const res = await getSampleDeliveryPlanEditList(datas);

    const keys = res?.data?.list?.map((item: any) => item.sortingPlanBatchNo) || [];

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
      },
    ] as Partial<FormProps>[],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    rowKeys: ['sortingPlanBatchNo'],
    columns: [
      [
        {
          title: t('分拣批号'),
          dataIndex: 'sortingPlanBatchNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('托盘号'),
          dataIndex: 'palletNo',
          hideInTable: true,
          component: 'Input',
        },
        {
          title: t('分拣类型'),
          dataIndex: 'sortingType',
          width: 170,
          resizable: true,
          formItemProps: {
            component: 'Select',
            componentProps: {
              fieldNames: {
                label: 'typeDescribe',
                value: 'id',
              },
              request: async () => {
                const { data } = await getSortingCategoryListOptions({
                  manageType: 2,
                });
                return data;
              },
            },
          },
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          hideInTable: true,
        },
        {
          title: t('数量'),
          dataIndex: 'number',
          width: 170,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('采浆日期起'),
          dataIndex: 'slurryDateUp',
          width: 170,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('采浆日期止'),
          dataIndex: 'slurryDateDown',
          width: 170,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('箱号起'),
          dataIndex: 'boxIdUp',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('箱号止'),
          dataIndex: 'boxIdDown',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          hideInTable: true,
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: sampleTypeDict.filter((item: any) => item.value !== 6),
            },
          },
        },
        {
          title: t('箱号'),
          dataIndex: 'boxId',
          hideInTable: true,
          formItemProps: {
            formItemProps: {
              autoLink: false,
            },
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                  <Input v-model:value={formModel.boxIdUp} allowClear placeholder={t('请输入')} />
                  <span style={{ margin: '0 5px' }}>~</span>
                  <Input v-model:value={formModel.boxIdDown} allowClear placeholder={t('请输入')} />
                </div>
              );
            },
          },
        },
      ] as TableColumn[],
    ],
    expandedRowsChanges: [
      async (expandedKeys: Key[]) => {
        expandedRowKeys.value = expandedKeys;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandedTableMap[newKey]) {
          expandedTableMap[newKey] = useTable(addNos);
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

  const rightLoadData = async (params: any) => {
    const datas = {
      ...params,
    };
    if (!datas.outPlanBatchNo) {
      return {
        data: [],
      };
    }
    return await getSampleDeliveryPlanEditChooseList(datas);
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
    rowKeys: ['orgSampleNo'],
    columns: [
      [
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 170,
          resizable: true,
          formItemProps: {
            order: 1,
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <FormItemRest>
                  <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <Input v-model:value={formModel.sampleNoUp} allowClear placeholder={t('请输入')} />
                    <span style={{ margin: '0 5px' }}>~</span>
                    <Input v-model:value={formModel.sampleNoDown} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        },
        {
          title: t('分拣批号'),
          dataIndex: 'sortingPlanBatchNo',
          hideInSearch: true,
          width: 170,
          resizable: true,
        },
        {
          title: t('标本箱号'),
          dataIndex: 'boxId',
          width: 170,
          resizable: true,
          formItemProps: {
            order: 3,
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <FormItemRest>
                  <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <Input v-model:value={formModel.boxIdUp} allowClear placeholder={t('请输入')} />
                    <span style={{ margin: '0 5px' }}>~</span>
                    <Input v-model:value={formModel.boxIdDown} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        },
        {
          title: t('分拣批号'),
          dataIndex: 'sortingPlanBatchNo',
          hideInTable: true,
          formItemProps: {
            order: 4,
          },
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          hideInSearch: true,
          width: 150,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sampleType?.name;
          },
        },
        {
          title: t('大托盘号'),
          dataIndex: 'palletNo',
          width: 150,
          resizable: true,
          formItemProps: {
            order: 2,
          },
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          hideInSearch: true,
          width: 150,
          sorter: true,
          resizable: true,
        },
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          hideInSearch: true,
          width: 150,
          sorter: true,
          resizable: true,
        },
        {
          title: t('姓名'),
          dataIndex: 'plasmaDonorName',
          hideInSearch: true,
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.name;
          },
        },
        {
          title: t('性别'),
          dataIndex: 'plasmaDonorSex',
          hideInSearch: true,
          width: 80,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.sex?.name;
          },
        },
        {
          title: t('入库批号'),
          dataIndex: 'inWarehouseBatchNo',
          hideInSearch: true,
          width: 150,
          resizable: true,
        },
        {
          title: t('入库人'),
          dataIndex: 'inWarehouseBy',
          hideInSearch: true,
          width: 100,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          hideInSearch: true,
          width: 150,
          sorter: true,
          resizable: true,
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
    fetchDubData,
  };
};
