import { reqVersionChangeState } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { BMStateTagType, FormProps, Recordable, TableColumn } from '@bmos/components';
import { BMStateTag, BMStateTagEnum } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { PROCESS_STATE, VersionStatus, VersionStatusType } from '../enum';

export const useTable = () => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const pageRef = ref<any>();

  const updateSecondTable = async () => {
    pageRef.value.fetchData(1);
  };

  // 数据权限modal
  const permissionModalOpen = ref<boolean>(false);
  const savePermission = async () => {
    pageRef.value.fetchData(0);
  };

  // 关联工艺
  const relatedProcessesOpen = ref<boolean>(false);
  const updateRelatedProcesses = async () => {
    pageRef.value?.fetchData(0);
  };

  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'mergeCode',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('规格'),
      dataIndex: 'specification',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生效版本'),
      dataIndex: 'activeVersion',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 240,
      actions: ({ record }) => [
        {
          label: t('关联工艺'),
          ifShow: hasPermission('120020006000002'),
          onClick: () => {
            relatedProcessesOpen.value = true;
            firstRowData.value = record;
          },
        },
        {
          label: t('数据权限'),
          ifShow: hasPermission('120020006000003'),
          onClick: () => {
            permissionModalOpen.value = true;
            firstRowData.value = record;
          },
        },
      ],
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  });

  const VersionStatusClassMap: Map<
    VersionStatus,
    {
      type: BMStateTagType;
      stateName: string;
    }
  > = new Map([
    [
      VersionStatus.EDIT,
      {
        type: BMStateTagEnum.PRIMARY,
        stateName: t('编辑'),
      },
    ],
    [
      VersionStatus.APPROVAL,
      {
        type: BMStateTagEnum.WARNING,
        stateName: t('审核'),
      },
    ],
    [
      VersionStatus.CONFIRM,
      {
        type: BMStateTagEnum.CONFIRM,
        stateName: t('确认'),
      },
    ],
    [
      VersionStatus.INVALID,
      {
        type: BMStateTagEnum.DEFAULT,
        stateName: t('失效'),
      },
    ],
    [
      VersionStatus.VALID,
      {
        type: BMStateTagEnum.SUCCESS,
        stateName: t('生效'),
      },
    ],
    [
      VersionStatus.WAIT_VALID,
      {
        type: BMStateTagEnum.WARNING,
        stateName: t('待生效'),
      },
    ],
  ]);

  // 归档顺序
  const filingOrderOpen = ref<boolean>(false);
  // 历史
  const historyOpen = ref<boolean>(false);
  // 当前操作行数据
  const secondRowData = ref<Recordable>({});

  const changeState = async (record: Recordable, content: string, actionState: VersionStatusType) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content,
      async onOk() {
        try {
          await reqVersionChangeState({
            id: record.id,
            actionState,
          });
          message.success(t('操作成功'));
          updateSecondTable();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  // 启用
  const enableModal = ref<boolean>(false);

  const columnsSecond: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'version',
      fixed: 'left',
      width: 120,
      resizable: true,
    },
    {
      title: t('BOM名称'),
      dataIndex: 'productFormulaName',
      width: 200,
      resizable: true,
    },
    {
      title: t('生产BOM版本'),
      dataIndex: 'productFormulaVersionNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'description',
      width: 200,
      resizable: true,
    },
    {
      title: t('生效日期'),
      dataIndex: 'effectDate',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('状态'),
      dataIndex: 'actionState',
      width: 110,
      resizable: true,
      fixed: 'right',
      customRender: ({ record }) => (
        <BMStateTag type={VersionStatusClassMap.get(record.actionState?.value)?.type}>
          {record.actionState?.label}
        </BMStateTag>
      ),
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 250,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: record.actionState?.value === VersionStatus.EDIT && hasPermission('120020006000007'),
          onClick: () => {
            router.push({
              name: 'process-flow',
              query: {
                status: PROCESS_STATE.EDIT_VERSION,
                version: record.version,
                versionId: record.id,
                processId: record.processId,
              },
            });
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120020006000008'),
          onClick: () => {
            router.push({
              name: 'process-flow',
              query: {
                status: PROCESS_STATE.VIEW_VERSION,
                version: record.version,
                processId: record.processId,
                versionId: record.id,
              },
            });
          },
        },
        {
          label: t('启用'),
          ifShow:
            (record.actionState?.value === VersionStatus.INVALID ||
              record.actionState?.value === VersionStatus.CONFIRM) &&
            hasPermission('120020006000009'),
          onClick: () => {
            secondRowData.value = record;
            enableModal.value = true;
          },
        },
        {
          label: t('重新编辑'),
          ifShow: record.actionState?.value === VersionStatus.CONFIRM && hasPermission('120020006000016'),
          onClick: () => {
            changeState(record, t('确认的工艺版本是否需要重新编辑？'), VersionStatus.FRESH_EDIT);
          },
        },
        {
          label: t('确认'),
          ifShow: record.actionState?.value === VersionStatus.EDIT && hasPermission('120020006000013'),
          onClick: () => {
            changeState(record, t('确认后工艺无法编辑，是否确认？'), VersionStatus.CONFIRM);
          },
        },
        {
          label: t('停用'),
          ifShow:
            (record.actionState?.value === VersionStatus.WAIT_VALID ||
              record.actionState?.value === VersionStatus.VALID) &&
            hasPermission('120020006000011'),
          onClick: () => {
            changeState(record, t('是否停用当前工艺版本？'), VersionStatus.INVALID);
          },
        },
        {
          label: t('立即生效'),
          ifShow: record.actionState?.value === VersionStatus.WAIT_VALID && hasPermission('120020006000014'),
          onClick: () => {
            changeState(record, t('是否立即生效当前工艺版本？'), VersionStatus.VALID);
          },
        },
        {
          label: t('审核进度'),
          ifShow: record.actionState?.value === VersionStatus.APPROVAL && hasPermission('120020006000012'),
          onClick: () => {
            router.push({
              name: 'process-config-schedule',
              query: {
                processInstanceId: record.processInstanceId,
                fromList: 'fromList',
                title: t('工艺配置'),
              },
            });
          },
        },
        {
          label: t('历史'),
          ifShow: hasPermission('120020006000010'),
          onClick: () => {
            secondRowData.value = record;
            historyOpen.value = true;
          },
        },
      ],
    },
  ];

  const addProcess = (currentNode: any) => {
    router.push({
      name: 'process-flow',
      query: {
        status: PROCESS_STATE.ADD_PROCESS,
        ...(currentNode.id &&
          !currentNode.categoryFlag && {
            productId: currentNode.id,
          }),
      },
    });
  };

  const selectCurrentNode = ref<any>({});
  const openFilingOrderModal = (currentNode: any) => {
    if (currentNode?.[1]?.version) {
      selectCurrentNode.value = currentNode;
      filingOrderOpen.value = true;
    } else {
      message.warning(t('请先选择工艺版本'));
    }
  };

  // 复刻工艺
  const copyProcess = (currentNode: any) => {
    if (currentNode?.[1]?.version) {
      selectCurrentNode.value = currentNode;
      router.push({
        name: 'process-flow',
        query: {
          status: PROCESS_STATE.COPY_VERSION,
          version: currentNode?.[1]?.version,
          versionId: currentNode?.[1]?.id,
          processId: currentNode?.[0]?.id,
        },
      });
    } else {
      message.warning(t('请先选择工艺版本'));
    }
  };

  // 新增版本
  const addVersion = (currentNode: any) => {
    if (currentNode?.[1]?.version) {
      selectCurrentNode.value = currentNode;
      router.push({
        name: 'process-flow',
        query: {
          status: PROCESS_STATE.ADD_VERSION,
          version: currentNode?.[1]?.version,
          versionId: currentNode?.[1]?.id,
          processId: currentNode?.[0]?.id,
        },
      });
    } else {
      message.warning(t('请先选择工艺版本'));
    }
  };

  return {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    addProcess,
    historyOpen,
    secondRowData,
    firstRowData,
    filingOrderOpen,
    permissionModalOpen,
    openFilingOrderModal,
    selectCurrentNode,
    savePermission,
    copyProcess,
    addVersion,
    pageRef,
    relatedProcessesOpen,
    updateRelatedProcesses,
    enableModal,
    updateSecondTable,
  };
};
