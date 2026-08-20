import {
  deleteEquipmentCategoryApi,
  getEquipmentCategoryList,
  postEquipmentCategorySave,
  putEquipmentCategoryUpdate,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Recordable } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { createVNode } from 'vue';
import { ActionType, modalStatus } from '../../../enum';
import { loopTree } from '../utils';
export const useTree = ({ emits }: any) => {
  const { hasPermission } = usePermissionStore();
  const status = ref<boolean>(false);
  //类型
  const SubmitKey = ref<string>('');
  //树名称Name
  const modalTitle = ref(t('新增分类'));
  //tree 取值节点
  const fieldNames = {
    children: 'children',
    title: 'showName',
    key: 'id',
  };
  //设备管理树节点
  const treeData = ref<DataNode[]>([]);
  //获取所有节点
  const getTreeData = async () => {
    try {
      const { data } = await getEquipmentCategoryList();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          children: loopTree(data),
        },
      ];
      emits('treeData', treeData.value);
    } catch (error) {}
  };
  //暂存间树节点操作
  const actionList = [
    {
      title: t('新增子分类'),
      action: modalStatus.Add,
      ifShow: () => {
        return hasPermission('160010002000001');
      },
    },
    {
      title: t('编辑分类'),
      action: modalStatus.Edit,
      ifShow: () => {
        return hasPermission('160010002000002');
      },
    },
    {
      title: t('删除分类'),
      action: modalStatus.Delete,
      ifShow: () => {
        return hasPermission('160010002000003');
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
  //弹出框节点
  const treeFormProps = computed(() => {
    const initialValues = {
      parentId: 'all',
      name: '',
      id: 'all',
      code: '',
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
      {
        field: 'code',
        component: 'Input',
        label: t('分类编码'),
        required: true,
        componentProps: {
          disabled: modalStatus.Edit.includes(SubmitKey.value),
          maxlength: 100,
        },
      },
    ];
    return { initialValues, schemas };
  });
  //控制节点树
  const treeAllField = reactive<{ selectedKeys: any[]; expandedKeys: any[] }>({
    selectedKeys: ['all'],
    expandedKeys: ['all'],
  });
  //TREE节点点击事件
  const treeAction = (action: any, node: any) => {
    SubmitKey.value = action?.action == 'ADD' ? modalStatus.Add : action?.action;
    treeFormProps.value.initialValues!.code = '';
    treeFormProps.value.initialValues!.name = '';
    treeFormProps.value.initialValues!.id = node ? node.data.id : 'all';
    treeFormProps.value.initialValues!.parentId = node ? node.data.id : 'all';
    modalTitle.value = action?.action == modalStatus.Edit ? t('编辑分类') : t('新增分类');
    if (modalStatus.Edit.includes(action.action)) {
      treeFormProps.value.initialValues!.code = node.data.code || '';
      treeFormProps.value.initialValues!.name = node.data.name || '';
      treeFormProps.value.initialValues!.parentId = node.data?.parentId !== '0' ? node.data.parentId : 'all';
    }
    status.value = true;
  };
  //删除节点
  const deletePacking = async (data: any) => {
    Modal.confirm({
      title: t('删除确认'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('设备信息删除后无法恢复，是否删除'),
      centered: true,
      async onOk(e) {
        try {
          await deleteEquipmentCategoryApi(data?.data.id);
          message.success(t('删除成功'));
          if (treeAllField.selectedKeys[0] === data?.data.id) {
            treeAllField.selectedKeys = ['all'];
          }
          await getTreeData();
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };
  //新增节点
  const addPacking = async (data: Recordable) => {
    try {
      await postEquipmentCategorySave(data);
      message.success(t('新增成功'));
      return Promise.resolve(true);
    } catch (error: any) {
      message.error(error.message);
      return Promise.reject(false);
    }
  };
  //编辑节点
  const editPacking = async (data: Recordable) => {
    try {
      await putEquipmentCategoryUpdate(data);
      message.success(t('编辑成功'));
      return Promise.resolve(true);
    } catch (error: any) {
      message.error(error.message);
      return Promise.reject(false);
    }
  };
  const actionType: ActionType = {
    [modalStatus.Add]: addPacking,
    [modalStatus.Edit]: editPacking,
  };
  const categorySubmit = async (formValues: Recordable) => {
    try {
      const { id, parentId, name, code } = formValues;
      const data = {
        id: id == 'all' ? '' : id,
        parentId: parentId == 'all' ? '' : parentId,
        name: name,
        code: code,
      };
      const res = await actionType[SubmitKey.value](data);
      if (res) await getTreeData();
      status.value = !res;
    } catch (error: any) {}
  };

  return {
    status,
    treeData,
    modalTitle,
    fieldNames,
    actionList,
    treeAllField,
    treeFormProps,
    treeAction,
    categorySubmit,
    getTreeData,
  };
};
