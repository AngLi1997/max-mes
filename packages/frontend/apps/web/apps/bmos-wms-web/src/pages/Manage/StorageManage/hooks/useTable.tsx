import { reqInventoryListByBatchIdAndPositionId, reqStorageConfigQueryAllTreeWithCargoPosition } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { StorageLevel } from '../types';

export type UseTableParams = {
  curSelect: any;
};

export const useTable = ({ curSelect }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const curSelectData = curSelect;
  const pageRef = ref<any>();

  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const materialViewOpen = ref<boolean>(false);
  const columnsFirst: TableColumn[] = [
    {
      title: t('货品名称'),
      dataIndex: 'cargoName',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('货品编码'),
      dataIndex: 'mergeCode',
      width: 200,
      resizable: true,
    },
    {
      title: t('货品规格'),
      dataIndex: 'specification',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('货品批号'),
      dataIndex: 'inventoryBatchNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('货品量'),
      dataIndex: 'quantity',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('件数'),
      dataIndex: 'size',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('150020001000002'),
          onClick: () => {
            firstRowData.value = record;
            materialViewOpen.value = true;
          },
        },
      ],
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
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

  // 货品入库
  const inboundOpen = ref<boolean>(false);
  const openInboundModal = () => {
    inboundOpen.value = true;
  };

  // 货品出库
  const outboundOpen = ref<boolean>(false);
  const outboundModalIsView = ref<boolean>(false);
  const outboundModal = debounce(async (node: any) => {
    const res = await outboundMoveModal(node);
    outboundOpen.value = res;
    outboundModalIsView.value = false;
  }, 400);

  // 货品移库
  const moveOpen = ref<boolean>(false);
  const moveModalIsView = ref<boolean>(false);
  const openMoveModal = debounce(async (node: any) => {
    const res = await outboundMoveModal(node);
    moveOpen.value = res;
    moveModalIsView.value = false;
  }, 400);
  // 盘点
  const checkOpen = ref<boolean>(false);
  const openCheckModal = () => {
    checkOpen.value = true;
  };

  // 查看
  const materialPartsViewOpen = ref<boolean>(false);
  // 当前操作行数据
  const secondRowData = ref<Recordable>({});

  const columnsSecond: TableColumn[] = [
    {
      title: t('货品件号'),
      dataIndex: 'inventoryNo',
      fixed: 'left',
      width: 300,
      resizable: true,
      headerSearchComponent: 'Input',
      sorter: true,
    },
    {
      title: t('库存量'),
      dataIndex: 'availableQuantity',
      width: 300,
      resizable: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 300,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 150,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('150020001000005'),
          onClick: () => {
            secondRowData.value = record;
            materialPartsViewOpen.value = true;
          },
        },
        {
          label: t('盘点'),
          ifShow: hasPermission('150020001000006'),
          onClick: () => {
            const { reserveQuantity } = record;
            if (Number(reserveQuantity) !== 0) {
              message.error(t('货品件已预定，无法盘点'));
            } else {
              secondRowData.value = record;
              openCheckModal();
            }
          },
        },
      ],
    },
  ];

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqStorageConfigQueryAllTreeWithCargoPosition();
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
  //查询出库移库
  const inventoryList = ref<any[]>([]);
  const outboundMoveModal = async (node: any) => {
    if (curSelectData?.value?.level?.value !== StorageLevel.POSITION) {
      message.error(t('请先选择货位'));
      return false;
    }

    if (Object.keys(node[0]).length === 0) {
      message.error(t('请先选择货品批次'));
      return false;
    }
    const ids = {
      inventoryBatchId: node[0]?.id,
      positionId: curSelectData?.value?.id,
    };
    try {
      const res = await reqInventoryListByBatchIdAndPositionId(ids);
      inventoryList.value = res.data; //获取列表详情
    } catch (error: any) {
      error.message && message.error(error.message);
    }
    return true;
  };
  onActivated(() => {
    getTreeData();
  });

  return {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    secondRowData,
    inventoryList,
    firstRowData,
    pageRef,
    treeData,
    updateTable,
    updateFirstTable,
    updateSecondTable,

    // 货品入库
    inboundOpen,
    openInboundModal,

    // 查看
    materialViewOpen,
    materialPartsViewOpen,

    // 货品出库
    outboundOpen,
    outboundModalIsView,
    outboundModal,

    // 货品移库
    moveOpen,
    moveModalIsView,
    openMoveModal,
    // 盘点
    checkOpen,
  };
};
