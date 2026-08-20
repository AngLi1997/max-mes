import { getWmsQualifiedSampleList, qualifiedSampleBackWarehouseOut } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { DataRequestFn, FormProps, Key, TableColumn } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';
import { useLeftTable } from './useLeftTable';
import { useRightTable } from './useRightTable';

const { hasPermission } = usePermissionStore();

export const useDubTable = (openOutputModal: any) => {
  const { sampleTypeDict, warehouseDict } = getDicts();
  const dubTableRef = ref<any>(null);

  // const storageStatusMap = {
  //   1: t('回库中'),
  //   2: t('在库'),
  //   3: t('出库中'),
  //   4: t('已出库'),
  // };

  // 展开项的key
  const expandedLeftRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandedLeftTableMap = reactive<any>({});

  // 删除指定展开的key
  const removeExpandedLeftRowKeys = (key: any) => {
    expandedLeftRowKeys.value = expandedLeftRowKeys.value.filter((k: any) => k !== key);
  };

  const leftLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,
    };

    const res = await getWmsQualifiedSampleList(datas);

    const keys = res?.data?.list?.map((item: any) => item.uniqueValue) || [];

    // 查询二级列表（如果展开了的话）
    expandedLeftRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandedLeftTableMap[key].fetchData();
      }
    });

    return res;
  };

  const leftTableProps = reactive({
    requests: [leftLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('待入库标本')],
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
    tableFields: [
      {
        default: {
          currentInventoryStatus: 2,
          sampleStatus: 1,
        },
      },
    ],
    expandedRowsChanges: [
      async (expandedKeys: Key[]) => {
        expandedLeftRowKeys.value = expandedKeys;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandedLeftTableMap[newKey]) {
          expandedLeftTableMap[newKey] = useLeftTable();
        } else {
          expandedLeftTableMap[newKey].fetchData();
        }
      },
    ],
    rowKeys: ['uniqueValue'],
    columns: [
      [
        {
          title: t('标本批号'),
          dataIndex: 'batchNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('所在仓库'),
          dataIndex: 'warehouseId',
          hideInSearch: !getWarehouseConfigByCode.value,
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.warehouse?.name ?? '-';
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: warehouseDict,
            },
          },
        },
        {
          title: t('合格数'),
          dataIndex: 'qualifiedNum',
          width: 100,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('箱数'),
          dataIndex: 'boxNum',
          width: 100,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          hideInTable: true,
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: sampleTypeDict,
            },
          },
        },
        {
          title: t('总数'),
          dataIndex: 'totalNum',
          width: 100,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('仓储状态'),
          dataIndex: ['currentInventoryStatus', 'label'],
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('操作'),
          key: 'ACTION',
          fixed: 'right',
          width: 100,
          actions: ({ record }) => [
            {
              label: t('标本回库'),
              ifShow: hasPermission('170090003000002') && record?.currentInventoryStatus !== 1,
              onClick: () => {
                Modal.confirm({
                  title: t('是否回库当前批次标本?'),
                  icon: h(ExclamationCircleOutlined),
                  async onOk() {
                    try {
                      await qualifiedSampleBackWarehouseOut({
                        batchNo: record.batchNo,
                        qualified: 1,
                        currentInventoryStatus: record.currentInventoryStatus,
                        warehouseId: record.warehouse?.value,
                      });
                      message.success(t('操作成功'));
                      removeExpandedLeftRowKeys(record.batchNo);
                      fetchDubData();
                      return Promise.resolve();
                    } catch (error: any) {
                      error.message && message.error(error.message);
                      return Promise.reject();
                    }
                  },
                });
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
  });

  const rightLoadData = async (params: any): Promise<any> => {
    const datas = {
      ...params,
    };

    const res = await getWmsQualifiedSampleList(datas);

    const keys = res?.data?.list?.map((item: any) => item.uniqueValue) || [];

    // 查询二级列表（如果展开了的话）
    expandedLeftRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandedLeftTableMap[key].fetchData();
      }
    });

    return res;
  };

  const expandedRightRowKeys = ref<any>([]);
  const expandedRightTableMap = reactive<any>({});

  const removeExpandedRightRowKeys = (key: any) => {
    expandedRightRowKeys.value = expandedRightRowKeys.value.filter((item: any) => item !== key);
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('已入库标本')],
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
    scrolls: [{ x: 750, y: 220 }],
    expandedRowsChanges: [
      async (expandedKeys: Key[]) => {
        expandedRightRowKeys.value = expandedKeys;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandedRightTableMap[newKey]) {
          expandedRightTableMap[newKey] = useRightTable();
        } else {
          expandedRightTableMap[newKey].fetchData();
        }
      },
    ],
    tableFields: [
      {
        default: {
          currentInventoryStatus: 1,
          sampleStatus: 1,
        },
      },
    ],
    rowKeys: ['uniqueValue'],
    columns: [
      [
        {
          title: t('标本批号'),
          dataIndex: 'batchNo',
          width: 150,
          resizable: true,
        },
        {
          title: t('所在仓库'),
          dataIndex: 'warehouseId',
          hideInSearch: !getWarehouseConfigByCode.value,
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.warehouse?.name ?? '-';
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: warehouseDict,
            },
          },
        },
        {
          title: t('合格数'),
          dataIndex: 'qualifiedNum',
          width: 100,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('大托盘号'),
          dataIndex: 'palletNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          hideInTable: true,
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: sampleTypeDict,
            },
          },
        },
        {
          title: t('总数'),
          dataIndex: 'totalNum',
          width: 90,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('仓储状态'),
          dataIndex: ['currentInventoryStatus', 'label'],
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('操作'),
          key: 'ACTION',
          fixed: 'right',
          width: 80,
          actions: ({ record }) => [
            {
              label: t('标本出库'),
              ifShow: hasPermission('170090003000001') && record?.currentInventoryStatus === 2,
              onClick: () => {
                openOutputModal(record);
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
  });

  // 刷新列表
  const fetchDubData = (type?: 'left' | 'right') => {
    if (type != 'right') {
      dubTableRef.value?.leftRef.fetchData();
    }
    if (type != 'left') {
      dubTableRef.value?.rightRef.fetchData();
    }
  };

  return {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    expandedLeftRowKeys,
    expandedRightRowKeys,
    expandedLeftTableMap,
    expandedRightTableMap,
    removeExpandedLeftRowKeys,
    removeExpandedRightRowKeys,
    fetchDubData,
  };
};
