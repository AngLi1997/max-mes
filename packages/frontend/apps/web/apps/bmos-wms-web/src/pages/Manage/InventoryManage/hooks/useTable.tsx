import { reqCargoCategoryQueryTreeWithCargo } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { DataNode } from 'ant-design-vue/es/tree';

export type UseTableParams = {};

export const useTable = ({}: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const pageRef = ref<any>();

  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('货品名称'),
      dataIndex: 'cargoName',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('货品编码'),
      dataIndex: 'mergeCode',
      width: 200,
    },
    {
      title: t('货品规格'),
      dataIndex: 'specification',
      width: 200,
      hideInSearch: true,
    },
    {
      title: t('库存量'),
      dataIndex: 'quantity',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('预定量'),
      dataIndex: 'reserveQuantity',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('件数'),
      dataIndex: 'size',
      width: 100,
      hideInSearch: true,
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  });

  const updateTable = () => {
    pageRef.value?.fetchData(0);
    pageRef.value?.fetchData(1);
  };

  const updateFirstTable = () => {
    pageRef.value?.fetchData(0);
  };

  const updateSecondTable = () => {
    pageRef.value?.fetchData(1);
  };

  // 新增批次
  const addInventoryBatchModalOpen = ref<boolean>(false);
  const addInventoryBatch = () => {
    addInventoryBatchModalOpen.value = true;
  };

  // 编辑货品批号
  const editInventoryBatchModalOpen = ref<boolean>(false);
  // 查看货品批次
  const viewInventoryBatchModalOpen = ref<boolean>(false);
  // 当前操作行数据
  const secondRowData = ref<Recordable>({});

  const columnsSecond: TableColumn[] = [
    {
      title: t('货品批号'),
      dataIndex: 'inventoryBatchNo',
      fixed: 'left',
      width: 300,
      headerSearchComponent: 'Input',
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      width: 300,
    },
    {
      title: t('库存量'),
      dataIndex: 'quantity',
      width: 300,
    },
    {
      title: t('预定量'),
      dataIndex: 'reserveQuantity',
      width: 300,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 200,
    },
    {
      title: t('有效期'),
      dataIndex: 'expiredDate',
      width: 300,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }, action) => [
        {
          label: t('货品件'),
          ifShow: hasPermission('150020002000001'),
          onClick: () => {
            router.push({
              name: 'inventory-pieces',
              query: {
                id: record.id,
              },
            });
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('150020002000002'),
          onClick: () => {
            secondRowData.value = record;
            editInventoryBatchModalOpen.value = true;
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('150020002000003'),
          onClick: () => {
            secondRowData.value = record;
            viewInventoryBatchModalOpen.value = true;
          },
        },
      ],
    },
  ];

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqCargoCategoryQueryTreeWithCargo();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          key: 'all',
          level: 0,
          children: data,
        },
      ];
    } catch (error) {}
  };
  onActivated(() => {
    getTreeData();
  });

  return {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    secondRowData,
    firstRowData,
    pageRef,
    treeData,
    updateTable,
    updateFirstTable,
    updateSecondTable,

    // 新增批次
    addInventoryBatchModalOpen,
    addInventoryBatch,
    // 编辑货品批次
    editInventoryBatchModalOpen,
    // 查看货品批次
    viewInventoryBatchModalOpen,
  };
};
