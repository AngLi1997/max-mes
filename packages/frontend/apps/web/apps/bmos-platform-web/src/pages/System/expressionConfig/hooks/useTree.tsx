import { reqCategoryTreeDelete, reqCategoryTreeSave, reqCategoryTreeUpdate, reqCategoryTreeUsingGET } from '@/api';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { ActionListItemCustomRenderParams, Recordable, TableInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { createVNode, onMounted, reactive, ref } from 'vue';
import { ActionType, modelType } from '../enum';

export const useTree = () => {
  const { hasPermission } = usePermissionStore();
  // TODO: 显示隐藏
  const status = ref<boolean>(false);
  //ref
  const pageExpression = ref<TableInstance>();
  //tree 取值节点
  const fieldNames = {
    children: 'children',
    title: 'name',
    key: 'id',
  };
  //暂存间树节点操作
  const actionList = [
    {
      title: t('新增子分类'),
      action: modelType.addTree,
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 7 && hasPermission('100020006000001');
      },
    },
    {
      title: t('编辑分类'),
      action: modelType.editTree,
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return hasPermission('100020006000002');
      },
    },
    {
      title: t('删除分类'),
      action: modelType.deleteTree,
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return hasPermission('100020006000003');
      },
      render: (node: DataNode) => {
        return (
          <a href='javascript:;' onClick={() => deletePacking(node)}>
            {t('删除分类')}
          </a>
        );
      },
    },
  ];
  //控制节点树
  const treeAllField = reactive<{ selectedKeys: KEY[]; expandedKeys: KEY[] }>({
    selectedKeys: ['all'],
    expandedKeys: ['all'],
  });
  //树名称Name
  const modalTitle = ref(t('新增分类'));
  //操作类型
  const SubmitKey = ref<string>('addTree');
  const textType = modelType.editTree;
  //弹出框节点
  const treeFormProps = computed(() => {
    const initialValues = {
      parentId: 'all',
      name: '',
      id: '0',
    };
    const schemas = [
      {
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          treeData: treeData.value,
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
        componentProps: {
          maxlength: 100,
        },
      },
    ];
    return { initialValues: initialValues, schemas: schemas };
  });
  //暂存间树节点
  const treeData = ref<DataNode[]>([]);
  //获取所有节点
  const getTreeData = async () => {
    try {
      const { data } = await reqCategoryTreeUsingGET();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          key: 'all',
          children: data,
        },
      ];
    } catch (error) {
      console.log(error);
    }
  };
  //TREE节点点击事件
  const treeAction = (action: any, node: any) => {
    treeFormProps.value.initialValues!.name = '';
    treeFormProps.value.initialValues!.id = node ? node.data.id : '0';
    treeFormProps.value.initialValues!.parentId = node ? node.data.id : 'all';
    SubmitKey.value = action?.action == 'ADD' ? modelType.addTree : action?.action;
    if (textType.includes(action.action)) {
      treeFormProps.value.initialValues!.name = node.data.name || '';
      treeFormProps.value.initialValues!.parentId = node.data?.parentId !== '0' ? node.data.parentId : 'all';
    }
    modalTitle.value = action?.action == modelType.editTree ? t('编辑分类') : t('新增分类');
    status.value = true;
  };
  //删除节点
  const deletePacking = async (data: any) => {
    // if (data.children?.length) {
    //   Modal.confirm({
    //     title: t('无法删除该分类信息'),
    //     icon: createVNode(ExclamationCircleOutlined),
    //     content: t(`该分类下还有子分类`),
    //     footer: null,
    //     maskClosable: true,
    //   });
    //   return;
    // }
    Modal.confirm({
      title: t('是否删除该分类信息'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('分类信息删除后无法恢复，是否删除？'),
      centered: true,
      async onOk(e) {
        try {
          const res = await reqCategoryTreeDelete(data?.data.id);
          if (res.code === 0) message.success(t('删除成功'));
          if (treeAllField.selectedKeys[0] === data?.data.id) {
            treeAllField.selectedKeys = ['all'];
          }
          getTreeData();
        } catch (error: any) {
          message.error(error.message);
          return false;
        }
      },
    });
  };
  //新增节点
  const addPacking = async (data: any) => {
    try {
      await reqCategoryTreeSave(data);
      message.success(t('新增成功'));
      return true;
    } catch (error: any) {
      message.error(error.message);
      return false;
    }
  };
  //编辑节点
  const editPacking = async (data: any) => {
    try {
      await reqCategoryTreeUpdate(data);
      message.success(t('编辑成功'));
      return true;
    } catch (error: any) {
      message.error(error.message);
      return false;
    }
  };
  const actionType: ActionType = {
    ['addTree']: addPacking,
    ['editTree']: editPacking,
  };
  const categorySubmit = async (formValues: Recordable) => {
    try {
      const { id, parentId, name } = formValues;
      const data = {
        id: id,
        parentId: parentId == 'all' ? '' : parentId,
        name: name,
      };
      const res = await actionType[SubmitKey.value](data);
      if (res) getTreeData();
      treeAllField.expandedKeys = [data.parentId ? data.parentId : 'all'];
      status.value = !res;
    } catch (error) {
      console.log(error);
    }
  };
  onMounted(() => {
    getTreeData();
  });
  return {
    status,
    treeData,
    fieldNames,
    actionList,
    modalTitle,
    treeAllField,
    treeFormProps,
    pageExpression,
    treeAction,
    categorySubmit,
  };
};
