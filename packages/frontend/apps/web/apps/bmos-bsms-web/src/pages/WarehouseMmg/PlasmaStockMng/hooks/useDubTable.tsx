import { getWmsPlasmaInList, getWmsPlasmaOutList, plasmaInWarehouseIn } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { DataRequestFn, FormProps, Key, TableColumn } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';
import { useLeftTable } from './useLeftTable';
import { useRightTable } from './useRightTable';

const { hasPermission } = usePermissionStore();

export const useDubTable = (openOutputModal: any) => {
  const { warehouseDict } = getDicts();
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

    const res = await getWmsPlasmaOutList(datas);

    const keys = res?.data?.list?.map((item: any) => item.batchNo) || [];

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
    titles: [t('待入库血浆')],
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
    scrolls: [{ x: 800, y: 300 }],
    rowKeys: ['batchNo'],
    columns: [
      [
        {
          title: t('血浆批号'),
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
            return record?.warehouse?.name;
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: warehouseDict,
            },
          },
        },
        {
          title: `${t('检疫期合格')}/${t('未通过数量')}`,
          dataIndex: 'resultNum',
          width: 200,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('箱/托数'),
          dataIndex: 'containerNum',
          width: 110,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('总数'),
          dataIndex: 'amount',
          width: 110,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('仓储状态'),
          dataIndex: ['storageStatus', 'label'],
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
              label: t('血浆回库'),
              // ifShow: !record.confirmStatus.value && hasPermission('100020006000008'),
              ifShow: hasPermission('170090001000002') && record?.storageStatus != 1,
              onClick: () => {
                Modal.confirm({
                  title: t('是否回库当前批次血浆?'),
                  icon: h(ExclamationCircleOutlined),
                  async onOk() {
                    try {
                      await plasmaInWarehouseIn(record.batchNo);
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

    const res = await getWmsPlasmaInList(datas);

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
    expandedRightRowKeys.value = expandedRightRowKeys.value.filter((k: any) => k !== key);
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('已入库血浆')],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        showAdvancedButton: true,
        actionColOptions: {
          span: 12,
        },
        baseColProps: {
          span: 12,
        },
      },
    ] as Partial<FormProps>[],
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
    rowKeys: ['uniqueValue'],
    scrolls: [{ x: 800, y: 300 }],
    columns: [
      [
        {
          title: t('血浆批号'),
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
            return record?.warehouse?.name;
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: warehouseDict,
            },
          },
        },
        {
          title: `${t('检疫期合格')}/${t('未通过数量')}`,
          dataIndex: 'resultNum',
          width: 200,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('大托盘号'),
          dataIndex: 'bigContainerNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('箱/托数'),
          dataIndex: 'containerNum',
          width: 110,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('总数'),
          dataIndex: 'amount',
          width: 110,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('仓储状态'),
          dataIndex: ['storageStatus', 'label'],
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
              label: t('合并出库'),
              ifShow: hasPermission('170090001000001') && record?.storageStatus == 2,
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
