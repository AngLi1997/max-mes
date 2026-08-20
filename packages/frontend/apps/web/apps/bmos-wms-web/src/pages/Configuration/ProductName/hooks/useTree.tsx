import {
  deleteCargoCategoryApi,
  postCargoCategorySaveApi,
  postCargoCategoryTreeApi,
  putCargoCategoryUpdateApi,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type {
  ActionListItemCustomRenderParams,
  FormProps,
  ModalFormType,
  NodeKey,
  RenderCallbackParams,
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { createVNode, reactive, ref } from 'vue';

export const useTree = (props: any) => {
  const { hasPermission } = usePermissionStore();

  const treeSelectedKeys = ref<string[]>([]);
  const treeField = reactive({
    field: {
      cargoCategoryId: 'id',
    },
  });

  const actionList = [
    {
      title: t('新增子分类'),
      action: 'addChildren',
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 7 && hasPermission('150010001000001');
      },
    },
    // {
    //   title: t('编辑分类'),
    //   action: 'editNode',
    //   ifShow: () => {
    //     return hasPermission('150010001000002');
    //   },
    // },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('150010001000003');
      },
    },
  ];
  const treeData = ref<any>([
    {
      id: 'all',
      cargoCategoryName: t('全部'),
      fullName: t('全部'),
      children: [],
    },
  ]);

  const treeOpen = ref<boolean | undefined>(false);
  const treeTitle = ref<string>(t('新增分类'));
  const formProps = reactive<FormProps>({
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
          treeData: treeData.value,
          fieldNames: {
            label: 'fullName',
            value: 'id',
          },
        },
      },
      {
        field: 'cargoCategoryName',
        component: 'Input',
        label: t('分类名称'),
        required: true,
      },
      {
        field: 'cargoCategoryCode',
        component: 'Input',
        label: t('分类编码'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            disabled: !!formModel.id,
          };
        },
      },
    ],
  });
  const modalFormRef = ref<any>();
  // 获取左侧树
  const fetchTreeData = async () => {
    try {
      const res = await postCargoCategoryTreeApi({});
      let data = res.data;
      treeData.value[0].children = data;
      // emit('treeDataChange', treeProps.treeData);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const okModal = (instance: ModalFormType) => {
    instance.validate().then(async params => {
      const data: any = { ...params };
      data.parentId = data.parentId === 'all' ? '0' : data.parentId;
      try {
        if (data.id) {
          const { parentId, ...params } = data;
          await putCargoCategoryUpdateApi(params as any);
        } else {
          await postCargoCategorySaveApi(data);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        fetchTreeData();
        treeOpen.value = false;
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };

  const addChildrenFn = (node: DataNode) => {
    addItem(node.id);
  };

  const editNodeFn = (node: DataNode) => {
    treeTitle.value = t('编辑分类');
    formProps.initialValues = {
      id: node.id,
      cargoCategoryName: node.cargoCategoryName,
      cargoCategoryCode: node.cargoCategoryCode,
      parentId: node.parentId === '0' ? 'all' : node.parentId,
    };
    treeOpen.value = true;
  };

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
          await deleteCargoCategoryApi(node.id);
          message.success(t('删除成功'));
          fetchTreeData();
          if (node.selected) {
            treeSelectedKeys.value = ['all'];
          }
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };

  const addItem = (key: NodeKey) => {
    treeTitle.value = t('新增分类');
    formProps.initialValues = {
      parentId: key || 'all',
    };
    treeOpen.value = true;
  };

  const actionMap: Map<string, any> = new Map([
    ['ADD', addItem],
    ['addChildren', addChildrenFn],
    ['editNode', editNodeFn],
    ['deleteNode', deleteNodeFn],
  ] as unknown as Map<string, any>);
  const handleTreeAction = (action: any, node: any) => {
    const fn = actionMap.get(action.action);
    fn && fn(node);
  };

  return {
    treeField,
    treeData,
    fetchTreeData,
    treeActionList: actionList,
    treeOpen,
    treeTitle,
    formProps,
    modalFormRef,
    okModal,
    handleTreeAction,
    treeSelectedKeys,
  };
};
