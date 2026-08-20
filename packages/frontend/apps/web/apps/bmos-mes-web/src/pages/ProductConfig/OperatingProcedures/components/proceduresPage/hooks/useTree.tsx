import {
  deleteOperateDeleteCategory,
  getOperateListCategory,
  postOperateSaveCategory,
  postOperateUpdateCategory,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Recordable } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { createVNode } from 'vue';
import { ActionType, modalStatus } from '../../../enum';
export const useTree = ({ UseParams, emits }: any) => {
  const { hasPermission } = usePermissionStore();
  const { status, SubmitKey, modalTitle, treeData, treeAllField } = UseParams;
  //弹出框节点
  const treeFormProps = computed(() => {
    return {
      initialValues: {
        parentId: 'all',
        name: '',
        id: 'all',
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
      ],
    };
  });
  //暂存间树节点操作
  const actionList = [
    {
      title: t('新增子分类'),
      action: modalStatus.Add,
      ifShow: () => {
        return hasPermission('120020011000001');
      },
    },
    {
      title: t('编辑分类'),
      action: modalStatus.Edit,
      ifShow: () => {
        return hasPermission('120020011000002');
      },
    },
    {
      title: t('删除分类'),
      action: modalStatus.Delete,
      ifShow: () => {
        return hasPermission('120020011000003');
      },
      render: (node: DataNode) => {
        return (
          <a href='javascript:;' onClick={() => deleteTreeList(node)}>
            {t('删除分类')}
          </a>
        );
      },
    },
  ];
  //新增节点
  const addPacking = async (data: Recordable) => {
    try {
      await postOperateSaveCategory(data);
      message.success(t('新增成功'));
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  //获取所有节点
  const getTreeData = async () => {
    try {
      const { data } = await getOperateListCategory();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          key: 'all',
          children: data,
        },
      ];
      emits('update:treeList', treeData.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //TREE节点点击事件
  const treeAction = (action: any, node: any) => {
    SubmitKey.value = action?.action == 'ADD' ? modalStatus.Add : action?.action;
    treeFormProps.value.initialValues!.name = '';
    treeFormProps.value.initialValues!.id = node ? node.data.id : 'all';
    treeFormProps.value.initialValues!.parentId = node ? node.data.id : 'all';
    modalTitle.value = action?.action == modalStatus.Edit ? t('编辑分类') : t('新增分类');
    if (modalStatus.Edit.includes(action.action)) {
      treeFormProps.value.initialValues!.name = node.data.name || '';
      treeFormProps.value.initialValues!.parentId = node.data?.parentId !== '0' ? node.data.parentId : 'all';
    }
    status.value = true;
  };
  //删除节点
  const deleteTreeList = async (data: any) => {
    Modal.confirm({
      title: t('删除确认'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('分类信息删除后无法恢复，是否删除?'),
      async onOk(e) {
        try {
          await deleteOperateDeleteCategory({ id: data?.data.id });
          message.success(t('删除成功'));
          if (treeAllField.selectedKeys[0] === data?.data.id) {
            treeAllField.selectedKeys = ['all'];
          }
          await getTreeData();
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
    });
  };
  //编辑节点
  const editPacking = async (data: Recordable) => {
    try {
      await postOperateUpdateCategory(data);
      message.success(t('编辑成功'));
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };
  const actionType: ActionType = {
    [modalStatus.Add]: addPacking,
    [modalStatus.Edit]: editPacking,
  };
  const categorySubmit = async (formValues: Recordable): Promise<any> => {
    try {
      const { id, parentId, name } = formValues;
      const data = {
        id: id == 'all' ? '' : id,
        parentId: parentId == 'all' ? '0' : parentId,
        name: name,
      };
      const res = await actionType[SubmitKey.value](data);
      if (res) await getTreeData();
      status.value = !res;
    } catch (error) {}
  };
  return {
    actionList,
    treeFormProps,
    getTreeData,
    treeAction,
    categorySubmit,
  };
};
