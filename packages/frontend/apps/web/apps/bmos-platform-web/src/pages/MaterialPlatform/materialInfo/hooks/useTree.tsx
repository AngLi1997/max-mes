import {
  deleteMaterialCategoryApi,
  getMaterialCategoryTreeApi,
  postMaterialCategorySaveApi,
  putMaterialCategoryUpdateApi,
} from '@/api/materialPlatform/materialInfo';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type {
  ActionListItemCustomRenderParams,
  FormProps,
  ModalFormInstance,
  ModalFormType,
  NodeKey,
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { createVNode, reactive, ref } from 'vue';

export const useTree = () => {
  const { hasPermission } = usePermissionStore();
  const treeSelectedKeys = ref<string[]>(['all']);
  const treeField = reactive({
    field: {
      materialCategoryId: 'id',
    },
  });

  const actionList = [
    {
      title: t('新增子分类'),
      action: 'addChildren',
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 7 && hasPermission('100040002000009');
      },
    },
    {
      title: t('编辑分类'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('100040002000010');
      },
    },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('100040002000011');
      },
    },
  ];
  const treeData = ref<any>([
    {
      id: 'all',
      showName: t('全部'),
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
            label: 'showName',
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
      {
        field: 'code',
        component: 'Input',
        label: t('分类编码'),
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            disabled: !!formModel.id,
          };
        },
      },
    ],
  });
  const modalFormRef = ref<ModalFormInstance>();

  const fetchTreeData = async () => {
    try {
      const res = await getMaterialCategoryTreeApi();
      const data = res.data;
      treeData.value[0].children = data;
      // emit('treeDataChange', treeProps.treeData);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const okModal = (instance: ModalFormType) => {
    instance.validate().then(async params => {
      let res;
      const data: any = { ...params };
      data.parentId = data.parentId === 'all' ? '0' : data.parentId;
      try {
        if (data.id) {
          const { parentId, ...params } = data;
          res = await putMaterialCategoryUpdateApi(params as any);
        } else {
          res = await postMaterialCategorySaveApi(data);
        }
        message.success(data.id ? t('编辑成功') : t('新增成功'));
      } catch (error: any) {
        message.error(error.message);
      }
      try {
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
      name: node.name,
      code: node.code,
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
          await deleteMaterialCategoryApi(node.id);
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

  const actionMap = new Map([
    ['ADD', addItem],
    ['addChildren', addChildrenFn],
    ['editNode', editNodeFn],
    ['deleteNode', deleteNodeFn],
  ]);
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
