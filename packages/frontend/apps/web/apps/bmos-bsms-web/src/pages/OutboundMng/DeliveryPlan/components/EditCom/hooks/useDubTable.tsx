import { getDeliveryPlanPlasmaList, getDeliveryPlanSelectedList, getSortingCategoryListOptions } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, Key, RenderCallbackParams, TableColumn } from '@bmos/components';
import { FormItemRest, Input } from 'ant-design-vue';
import { useTable } from './useTable';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  const batchNo = ref<any>('');

  // 获取分拣类型列表
  // const categoryOption = ref<any>([]);
  // const getSortingCategoryOptions = async () => {
  //   const { data } = await getSortingCategoryListOptions({
  //     manageType: 1,
  //   });

  //   categoryOption.value =
  //     data?.map((item: any) => {
  //       return {
  //         label: item.typeDescribe,
  //         value: item.id,
  //       };
  //     }) ?? [];
  //   const leftFormRef = dubTableRef.value?.leftRef?.getQueryFormRef(0);
  //   leftFormRef?.updateSchema({
  //     field: 'type',
  //     componentProps: {
  //       options: categoryOption.value,
  //     },
  //   });
  // };

  // 多选
  const leftSelectedRows = ref<any>([]);
  const leftRowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      getCheckboxProps: (record: any) => {
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
      plasmaOrgNos: [],
      sortingNos: [],
      flag: false,
    } as any;
    let setSortingNos = new Set();

    for (let key in expandedTableMap) {
      expandedTableMap[key].myselectedRows?.forEach((item: any) => {
        setSortingNos.add(item.batchNo);
        ans.plasmaOrgNos.push(item.orgNo);
      });
    }

    // expandedRowKeys.value.forEach((key: any) => {
    //   expandedTableMap[key].myselectedRows?.forEach((item: any) => {
    //     setSortingNos.add(item.batchNo);
    //     ans.plasmaOrgNos.push(item.orgNo);
    //   });
    // });
    leftSelectedRows.value.forEach((item: any) => {
      if (!setSortingNos.has(item.batchNo)) {
        ans.sortingNos.push(item.batchNo);
      }
    });
    ans.flag = ans.plasmaOrgNos.length > 0 || ans.sortingNos.length > 0;
    return ans;
  });

  const leftLoadData = async (params: any, onChangeParams: any): Promise<any> => {
    const datas = {
      ...params,
    };

    if (!(datas.qualityStatus && datas.warehouseId && datas.outboundType)) {
      return {
        data: [],
      };
    }

    const res = await getDeliveryPlanPlasmaList(datas);

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
      },
    ] as Partial<FormProps>[],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    rowKeys: ['batchNo'],
    columns: [
      [
        {
          title: t('分拣批号'),
          dataIndex: 'batchNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('托盘号'),
          dataIndex: 'containerNo',
          width: 170,
          hideInTable: true,
        },
        {
          title: t('分拣类型'),
          dataIndex: 'type',
          width: 120,
          resizable: true,
          formItemProps: {
            component: 'Select',
            componentProps: {
              // options: [],
              request: async () => {
                const { data } = await getSortingCategoryListOptions({
                  manageType: 1,
                });
                return (
                  data?.map((item: any) => {
                    return {
                      label: item.typeDescribe,
                      value: item.id,
                    };
                  }) ?? []
                );
              },
            },
          },
        },
        {
          title: t('数量'),
          dataIndex: 'num',
          width: 100,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 100,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('采浆日期起'),
          dataIndex: 'slurryDateUp',
          width: 170,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('采浆日期止'),
          dataIndex: 'slurryDateDown',
          width: 170,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('箱/托盘号起'),
          dataIndex: 'containerNoUp',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('箱/托盘号止'),
          dataIndex: 'containerNoDown',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('箱号'),
          dataIndex: 'containerNoAfter',
          width: 170,
          hideInTable: true,
          formItemProps: {
            // formItemProps: {
            //   autoLink: false,
            // },
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <FormItemRest>
                  <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <Input v-model:value={formModel.containerNoAfterUp} allowClear placeholder={t('请输入')} />
                    <span style={{ margin: '0 5px' }}>~</span>
                    <Input v-model:value={formModel.containerNoAfterDown} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        },
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          hideInTable: true,
          component: 'Input',
        },
      ] as TableColumn[],
    ],
    expandedRowsChanges: [
      async (expandedKeys: Key[]) => {
        expandedRowKeys.value = expandedKeys;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandedTableMap[newKey]) {
          expandedTableMap[newKey] = useTable(batchNo.value, fetchDubData);
        } else {
          expandedTableMap[newKey].fetchData();
        }
        // innerData.value = res.data;
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
      getCheckboxProps: (record: any) => {
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

  const rightLoadData = async (params: any, onChangeParams: any) => {
    const datas = {
      ...params,
    };
    if (!datas.batchNo) {
      return {
        data: [],
      };
    }

    batchNo.value = datas.batchNo;

    return await getDeliveryPlanSelectedList(datas);
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
    rowKeys: ['plasmaOrgNo'],
    columns: [
      [
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 180,
          resizable: true,
          formItemProps: {
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <FormItemRest>
                  <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <Input v-model:value={formModel.plasmaNoUp} allowClear placeholder={t('请输入')} />
                    <span style={{ margin: '0 5px' }}>~</span>
                    <Input v-model:value={formModel.plasmaNoDown} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        },
        {
          title: t('分拣批号'),
          dataIndex: 'batchNo',
          width: 150,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血浆类型'),
          dataIndex: 'type',
          width: 100,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.type?.name ?? '-'}</span>;
          },
        },
        {
          title: t('效价'),
          dataIndex: 'titer',
          width: 80,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血浆重量'),
          dataIndex: 'weight',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 100,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.bloodType?.name ?? '-'}</span>;
          },
        },
        {
          title: t('大托盘号'),
          dataIndex: 'bigContainerNo',
          width: 150,
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
          title: t('血浆箱/托盘号'),
          dataIndex: 'containerNo',
          width: 170,
          resizable: true,
          formItemProps: {
            label: t('血浆箱号'),
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <FormItemRest>
                  <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <Input v-model:value={formModel.containerNoAfterUp} allowClear placeholder={t('请输入')} />
                    <span style={{ margin: '0 5px' }}>~</span>
                    <Input v-model:value={formModel.containerNoAfterDown} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        },
        {
          title: t('对应编号'),
          dataIndex: 'corrPlasmaNo',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('对应类型'),
          dataIndex: 'corrRelationType',
          width: 150,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.corrRelationType?.name ?? '-'}</span>;
          },
        },
        {
          title: t('对应日期'),
          dataIndex: 'corrSlurryDate',
          width: 150,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('天数'),
          dataIndex: 'corrSlurryDateDiff',
          width: 80,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 150,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('姓名'),
          dataIndex: 'name',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 100,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.sex?.name ?? '-'}</span>;
          },
        },
        {
          title: t('入库批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('入库人'),
          dataIndex: 'inWarehouseBy',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          width: 150,
          sorter: true,
          hideInSearch: true,
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
    // getSortingCategoryOptions,
    fetchDubData,
    clearSelected,
  };
};
