import {
  reqBatchRecordsTemplateCategoryDelete,
  reqBatchRecordsTemplateCategorySave,
  reqBatchRecordsTemplateCategoryTree,
  reqBatchRecordsTemplateCategoryUpdate,
  reqBatchRecordsTemplatePage,
  reqBatchRecordsTemplateVersionConfirm,
  reqBatchRecordsTemplateVersionDownload,
  reqBatchRecordsTemplateVersionNormal,
  reqBatchRecordsTemplateVersionPage,
  reqBatchRecordsTemplateVersionScrap,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import {
  BMStateTag,
  BMStateTagType,
  type ActionListItem,
  type ActionListItemCustomRenderParams,
  type DataRequestFn,
  type FormProps,
  type Recordable,
  type TableColumn,
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { fileStreamDownload } from '@bmos/utils';
import { Modal, Tag, message } from 'ant-design-vue';
import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
import { createVNode } from 'vue';
import { OperationType, StatusType } from '../type';

export const useTables = () => {
  const { hasPermission } = usePermissionStore();

  const pageRef = ref<any>();

  // 升级版本model
  const upgradeVersionModalOpen = ref<boolean>(false);
  const rowData = ref<Recordable>({});
  const firstRowData = ref<Recordable>({});

  const permissionModalOpen = ref<boolean>(false);
  const updateFirstTable = () => {
    console.log('updateFirstTable');
    pageRef.value?.fetchData(0);
  };

  // 绑定工艺
  const bindProcessModalOpen = ref<boolean>(false);
  const checkedProcessIds = ref<string[]>([]);

  const templateColumn: TableColumn[] = [
    {
      title: t('模板名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('分类'),
      dataIndex: 'categoryName',
      width: 200,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('数据权限'),
          ifShow: hasPermission('120080001000005'),
          onClick: () => {
            firstRowData.value = record;
            permissionModalOpen.value = true;
          },
        },
        {
          label: t('绑定工艺'),
          ifShow: hasPermission('120080001000006'),
          onClick: () => {
            firstRowData.value = record;
            checkedProcessIds.value = record.processIdList || [];
            bindProcessModalOpen.value = true;
          },
        },
      ],
    },
  ];

  const VersionStatusClassMap: Map<
    StatusType,
    {
      type: BMStateTagType;
      stateName: string;
    }
  > = new Map([
    [
      StatusType.EDIT,
      {
        type: 'primary',
        stateName: t('编辑'),
      },
    ],
    [
      StatusType.MAKE_SURE,
      {
        type: 'success',
        stateName: t('确认'),
      },
    ],
    [
      StatusType.SCRAP,
      {
        type: 'default',
        stateName: t('作废'),
      },
    ],
  ]);

  const updateSecondTable = () => {
    pageRef.value.fetchData(1);
  };

  const confirmVersion = async (record: Recordable) => {
    Modal.confirm({
      title: t('是否确认此版本'),
      icon: h(ExclamationCircleOutlined),
      content: t('确认后此版本的批记录将无法修改'),
      async onOk() {
        try {
          await reqBatchRecordsTemplateVersionConfirm(record.id);
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

  const voidVersion = async (record: Recordable) => {
    Modal.confirm({
      title: t('是否作废次此版本'),
      icon: h(ExclamationCircleOutlined),
      content: t('作废后此版本的批记录将无法使用'),
      async onOk() {
        try {
          await reqBatchRecordsTemplateVersionScrap(record.id);
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

  const setDefaultVersion = async (record: Recordable) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否将此版本设为默认版本'),
      async onOk() {
        try {
          await reqBatchRecordsTemplateVersionNormal(record.id);
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

  const downloadFile = async (record: any) => {
    try {
      const res = await reqBatchRecordsTemplateVersionDownload(record.id);
      fileStreamDownload(
        res,
        `${record.templateName ? record.templateName : ''}${record.version}.docx`,
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      );
    } catch (error) {}
  };

  const secondRowData = ref<Recordable>({});
  const uploadTemplateModalOpen = ref<boolean>(false);

  const stepOpen = ref<boolean>(false);

  const historyOpen = ref<boolean>(false);

  const listColumn: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'version',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 200,
      customRender: ({ record }) => (
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
          }}>
          <BMStateTag type={VersionStatusClassMap.get(record.status?.value)?.type}>{record.status?.label}</BMStateTag>
          {record.normal && (
            <Tag
              bordered={false}
              style={{
                marginLeft: '10px',
              }}
              color='warning'>
              {t('默认版本')}
            </Tag>
          )}
        </div>
      ),
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 200,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('上传'),
          ifShow: hasPermission('120080001000008') && record.status?.value === StatusType.EDIT,
          onClick: () => {
            uploadTemplateModalOpen.value = true;
            secondRowData.value = record;
          },
        },
        {
          label: t('测试'),
          ifShow: hasPermission('120080001000010') && record.status?.value !== StatusType.SCRAP,
          onClick: () => {
            secondRowData.value = record;
            stepOpen.value = true;
          },
        },
        {
          label: t('确认'),
          ifShow: hasPermission('120080001000011') && record.status?.value === StatusType.EDIT,
          onClick: () => {
            confirmVersion(record);
          },
        },
        {
          label: t('下载'),
          ifShow: hasPermission('120080001000009'),
          onClick: () => {
            downloadFile(record);
          },
        },
        {
          label: t('设为默认'),
          ifShow: hasPermission('120080001000014') && record.status?.value === StatusType.MAKE_SURE,
          onClick: () => {
            setDefaultVersion(record);
          },
        },
        {
          label: t('历史'),
          ifShow: hasPermission('120080001000012'),
          onClick: () => {
            historyOpen.value = true;
            secondRowData.value = record;
          },
        },
        {
          label: t('作废'),
          ifShow: hasPermission('120080001000013') && record.status?.value !== StatusType.SCRAP,
          danger: true,
          onClick: () => {
            voidVersion(record);
          },
        },
      ],
    },
  ];

  // 树
  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqBatchRecordsTemplateCategoryTree();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          categoryFlag: true,
          key: 'all',
          children: data,
        },
      ];
    } catch (error) {}
  };

  const currentNodeSelect = ref<DataNode | null>({
    id: 'all',
    name: t('全部'),
    categoryFlag: true,
    key: 'all',
  });
  const selectTreeNode = (
    _selectedKeys: KEY[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    currentNodeSelect.value = info.node;
  };

  const treeSelectedKeys = ref<string[]>(['all']);

  const deleteNodeFn = (node: DataNode) => {
    Modal.confirm({
      title: t('是否删除分类信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('分类信息删除后无法恢复, 是否删除?'),
      okText: t('确定'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await reqBatchRecordsTemplateCategoryDelete(node.id);
          message.success(t('删除成功'));
          getTreeData();
          if (node.selected) {
            treeSelectedKeys.value = ['all'];
          }
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };

  const actionList: ActionListItem[] = [
    {
      title: t('新增子分类'),
      action: 'addChildren',
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 7 && hasPermission('120080001000001');
      },
    },
    {
      title: t('编辑分类'),
      action: 'editNode',
      ifShow: () => hasPermission('120080001000002'),
    },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => hasPermission('120080001000003'),
    },
  ];
  const treeModalOpen = ref<boolean>(false);
  const treeModalTitle = ref<string>(t('新增子分类'));
  const treeModalSubmit = async (values: Recordable) => {
    try {
      if (treeOperation.value === OperationType.Add) {
        await reqBatchRecordsTemplateCategorySave({
          name: values.name,
          parentId: values.parentId === 'all' ? null : values.parentId,
        });
        message.success(t('新增成功'));
      } else {
        await reqBatchRecordsTemplateCategoryUpdate({
          id: values.id,
          name: values.name,
        });
        message.success(t('编辑成功'));
      }
      getTreeData();
      treeModalOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const treeModalFormProps = reactive<FormProps>({
    initialValues: {
      parentId: 'all',
    },
    schemas: [
      {
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return treeData.value;
          },
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('分类名称'),
        required: true,
      },
    ],
  });
  const treeOperation = ref<OperationType>(OperationType.Add);
  const handleTreeAction = (actionItem: ActionListItem, node: DataNode) => {
    switch (actionItem.action) {
      case 'ADD':
        treeModalTitle.value = t('新增分类');
        treeModalFormProps.initialValues = {
          parentId: 'all',
        };
        treeModalOpen.value = true;
        treeOperation.value = OperationType.Add;
        break;
      case 'addChildren':
        treeModalTitle.value = t('新增分类');
        treeModalFormProps.initialValues = {
          parentId: node.id,
        };
        treeModalOpen.value = true;
        treeOperation.value = OperationType.Add;
        break;
      case 'editNode':
        treeModalTitle.value = t('编辑分类');
        treeModalFormProps.initialValues = {
          id: node.id,
          parentId: node.data?.parentId ? node.data.parentId : 'all',
          name: node.name,
        };
        treeModalOpen.value = true;
        treeOperation.value = OperationType.Edit;
        break;
      case 'deleteNode':
        deleteNodeFn(node);
        break;
      default:
        break;
    }
  };

  onMounted(() => {
    getTreeData();
  });

  const getDatasetPageList = async (params: any) => {
    if (!params.categoryId || params.categoryId === 'all') {
      return await reqBatchRecordsTemplatePage({
        ...params,
        categoryId: undefined,
      });
    }
    return await reqBatchRecordsTemplatePage(params);
  };

  const getDatasetVersionPageList = async (params: any) => {
    if (!params.templateInfoId) return Promise.resolve({ data: [] });
    return await reqBatchRecordsTemplateVersionPage(params);
  };

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  };

  return {
    columns: [templateColumn, listColumn],
    requests: [getDatasetPageList, getDatasetVersionPageList] as DataRequestFn[],
    treeData,
    upgradeVersionModalOpen,
    rowData,
    pageRef,
    formFirstProps,
    actionList,
    handleTreeAction,
    treeSelectedKeys,
    treeModalOpen,
    treeModalTitle,
    treeModalFormProps,
    treeModalSubmit,
    selectTreeNode,
    currentNodeSelect,
    permissionModalOpen,
    updateFirstTable,
    firstRowData,
    bindProcessModalOpen,
    checkedProcessIds,
    uploadTemplateModalOpen,
    secondRowData,
    stepOpen,
    historyOpen,
    updateSecondTable,
  };
};
