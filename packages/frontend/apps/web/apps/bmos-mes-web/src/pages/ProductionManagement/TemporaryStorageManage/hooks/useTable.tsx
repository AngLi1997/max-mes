import { LabelList } from '@/components/SignModal/type';
import {
  postTagInstancePrintBatch,
  reqMaterialInfoListByMaterialId,
  reqStorageConfigQueryAllTreeWithCargoPosition,
  reqStorageMaterialCancelReserve,
  reqStorageMaterialInfoByNo,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { debounce, isEmpty } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { StorageLevel } from '../types';
import { usePositionUserList } from './usePositionUserList';

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
      title: t('物料名称'),
      dataIndex: 'materialName',
      fixed: 'left',
      width: 180,
      resizable: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode',
      width: 180,
      resizable: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      width: 180,
      resizable: true,
      sorter: true,
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
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
      title: t('有效期至'),
      dataIndex: 'expiredDate',
      width: 160,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120030008000003'),
          onClick: () => {
            firstRowData.value = record;
            materialViewOpen.value = true;
          },
        },
      ],
    },
  ];

  const formFirstProps = reactive<Partial<FormProps>>({
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

  // 物料接收
  const materialReceivingOpen = ref<boolean>(false);
  const openMaterialReceivingModal = () => {
    materialReceivingOpen.value = true;
  };

  // 物料入库
  const inboundOpen = ref<boolean>(false);
  const openInboundModal = () => {
    inboundOpen.value = true;
  };

  // 物料退库
  const sendBackOpen = ref<boolean>(false);
  const openSendBackModal = debounce(async (node: any) => {
    const res = await outboundMoveModal(node);
    sendBackOpen.value = res;
  }, 400);
  // 物料销毁
  const destroyOpen = ref<boolean>(false);
  const openDestroyModal = debounce(async (node: any) => {
    const res = await outboundMoveModal(node);
    destroyOpen.value = res;
  }, 400);
  // 物料使用
  const useOpen = ref<boolean>(false);
  const openUseModal = debounce(async (node: any) => {
    const res = await outboundMoveModal(node);
    useOpen.value = res;
  }, 400);

  // 物料出库
  const outboundOpen = ref<boolean>(false);
  const outboundModalIsView = ref<boolean>(false);
  const outboundModal = debounce(async (node: any) => {
    const res = await outboundMoveModal(node);
    outboundOpen.value = res;
    outboundModalIsView.value = false;
  }, 400);

  // 物料移库
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

  // 预定
  const reserveOpen = ref<boolean>(false);
  const openReserveModal = () => {
    reserveOpen.value = true;
  };
  // 取消预定
  const cancelReserveSignOpen = ref<boolean>(false);
  const cancelReserveSignatureData = ref<any>({});
  const cancelReserveAlertDesc = ref<string>('');
  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();
  const cancelReserveLabelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('操作人'),
        action: 68,
        disabled: true,
      },
      {
        label: t('复核人'),
        action: 69,
        options: positionUserList.value,
      },
    ];
  });
  const cancelReserveSignSuccess = async (data: Recordable) => {
    try {
      const { userId0, userId1, remark } = data;
      cancelReserveSignatureData.value = {
        ...cancelReserveSignatureData.value,
        operatorId: userId0,
        reCheckerId: userId1,
        remark,
      };
      await reqStorageMaterialCancelReserve(cancelReserveSignatureData.value);
      updateSecondTable();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 打印标签
  const printOpen = ref<boolean>(false);
  const printConfirm = async (printerParams: any) => {
    try {
      const { printerIp, printerPort, printerDpi, sceneId } = printerParams;
      await postTagInstancePrintBatch([
        {
          printerIp,
          printerPort,
          dpi: printerDpi,
          sceneId,
          body: {
            no: secondRowData.value?.materialNo,
          },
        },
      ]);
      message.success(t('打印成功'));
      printOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 拆包出库
  const splitPackageOpen = ref<boolean>(false);

  // 查看
  const materialPartsViewOpen = ref<boolean>(false);
  // 当前操作行数据
  const secondRowData = ref<Recordable>({});

  const columnsSecond: TableColumn[] = [
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
      fixed: 'left',
      width: 300,
      resizable: true,
      headerSearchComponent: 'Input',
      sorter: true,
    },
    {
      title: t('可用量'),
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
      width: 280,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120030008000006'),
          onClick: () => {
            secondRowData.value = record;
            materialPartsViewOpen.value = true;
          },
        },
        {
          label: t('盘点'),
          ifShow: isEmpty(record.productPlanId) && hasPermission('120030008000007'),
          onClick: () => {
            secondRowData.value = record;
            openCheckModal();
          },
        },
        {
          label: t('标签'),
          ifShow: hasPermission('120030008000011'),
          onClick: () => {
            secondRowData.value = record;
            printOpen.value = true;
          },
        },
        {
          label: t('预定'),
          ifShow: isEmpty(record.productPlanId) && hasPermission('120030008000008'),
          onClick: () => {
            secondRowData.value = record;
            openReserveModal();
          },
        },
        {
          label: t('取消预定'),
          ifShow: !isEmpty(record.productPlanId) && hasPermission('120030008000009'),
          onClick: async () => {
            try {
              const { data } = await reqStorageMaterialInfoByNo(record.materialNo);
              cancelReserveSignatureData.value = {
                batchId: data.batchId,
                processId: data.processId,
                productId: data.productId,
                storageMaterialIdList: [data.id],
              };
              cancelReserveAlertDesc.value = `${t('物料已预定生产批次')}${data.batchNo}, ${t('是否取消')}?`;
              curPositionId.value = record.materialPositionId;
              await getPositionUserList('120030008000009');
              cancelReserveSignOpen.value = true;
            } catch (error) {}
          },
        },
        {
          label: t('拆包出库'),
          ifShow: hasPermission('120030008000010'),
          onClick: () => {
            secondRowData.value = record;
            splitPackageOpen.value = true;
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
          level: { value: 0 },
          children: data,
        },
      ];
    } catch (error) {}
  };

  const modalTableData = ref<any[]>([]);
  //查询出库移库
  const outboundMoveModal = async (node: any) => {
    if (curSelectData?.value?.level?.value !== StorageLevel.POSITION) {
      message.error(t('请先选择货位'));
      return false;
    }

    if (Object.keys(node[0]).length === 0) {
      message.error(t('请先选择物料批次'));
      return false;
    }
    const id = {
      storageMaterialBatchId: node[0]?.id,
      materialPositionId: curSelectData?.value?.id,
    };
    try {
      const { data } = await reqMaterialInfoListByMaterialId(id);
      modalTableData.value = data; //获取列表详情
      return true;
    } catch (error: any) {
      error.message && message.error(error.message);
      return false;
    }
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

    // 物料接收
    materialReceivingOpen,
    openMaterialReceivingModal,
    // 物料入库
    inboundOpen,
    openInboundModal,

    // 查看
    materialViewOpen,
    materialPartsViewOpen,

    // 物料出库
    outboundOpen,
    outboundModalIsView,
    outboundModal,

    // 物料移库
    moveOpen,
    moveModalIsView,
    openMoveModal,
    // 盘点
    checkOpen,
    // 预定
    reserveOpen,
    // 取消预定
    cancelReserveSignOpen,
    cancelReserveSignatureData,
    cancelReserveLabelList,
    cancelReserveSignSuccess,
    openReserveModal,
    cancelReserveAlertDesc,
    // 打印标签
    printOpen,
    printConfirm,
    // 退库
    sendBackOpen,
    openSendBackModal,
    // 销毁
    destroyOpen,
    openDestroyModal,
    // 使用
    useOpen,
    openUseModal,
    // 拆包出库
    splitPackageOpen,

    modalTableData,
  };
};
