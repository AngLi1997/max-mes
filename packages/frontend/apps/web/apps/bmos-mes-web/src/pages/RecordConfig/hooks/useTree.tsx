import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { ActionListItem, FormProps, ActionListItemCustomRenderParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';
import { createVNode, onMounted, reactive, ref } from 'vue';
import { recordDeleteCategory, reqCategoryList } from '../../../services';
import { ADD_CATETORY, CATETORY_DELETE, EDIT_CATETORY } from '../enum';
import { usePermissionStore } from '@/stores/permission';

const NEED_NAME = ['EDIT_CATETORY'];

const title_enum: Record<string, string> = {
  ADD: t('新增分类'),
  EDIT_CATETORY: t('编辑分类'),
};

export const useTree = () => {
  const { hasPermission } = usePermissionStore();
  const TREEFIELD = reactive<{ selectedKeys: KEY[] }>({
    selectedKeys: ['all'],
  });
  const treeField = reactive({
    field: {
      categoryId: 'id',
    },
  });
  const fieldNames = {
    children: 'itemList',
    title: 'name',
    key: 'id',
  };
  const TREE_DATA = ref([
    {
      id: 'all',
      itemList: [],
      name: t('全部'),
    },
  ]);
  const FORM_ITEMS = reactive<FormProps>({
    initialValues: {
      name: '',
      id: '',
    },
    schemas: [
      {
        field: 'id',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          treeData: TREE_DATA.value,
          fieldNames: {
            label: 'name',
            value: 'id',
            children: 'itemList',
          },
          // disabled:true
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

  const MODALTITLE = ref(t('新增分类'));

  const DELETE_CATETORY = async (node: DataNode) => {
    Modal.confirm({
      title: t('删除确认'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否删除数据'),
      centered: true,
      async onOk(e) {
        const data = { id: node.id };
        try {
          await recordDeleteCategory(data);
          message.success(t('删除成功'));

          INIT_TREE_DATA();
          if (TREEFIELD.selectedKeys[0] === node.id) {
            TREEFIELD.selectedKeys = ['all'];
          }
          return true;
        } catch (error: any) {
          message.error(error.message);
          return false;
        }
      },
    });
  };

  const ACTION_LIST = [
    {
      title: t('新增子分类'),
      action: ADD_CATETORY,
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 7 && hasPermission('120020001000001');
      },
    },
    {
      title: t('编辑分类'),
      action: EDIT_CATETORY,
      ifShow: () => {
        return hasPermission('120020001000002');
      },
    },
    {
      title: t('删除分类'),
      action: CATETORY_DELETE,
      render: (node: DataNode) => {
        return (
          <a href='javascript:;' onClick={() => DELETE_CATETORY(node)}>
            {t('删除分类')}
          </a>
        );
      },
      ifShow: () => {
        return hasPermission('120020001000003');
      },
    },
  ];

  const CURRENT = ref<{ action: ActionListItem; node: any }>();
  const INIT_TREE_DATA = () => {
    reqCategoryList().then(res => {
      if (res.code === 0) {
        TREE_DATA.value[0].itemList = res.data;
      }
    });
  };

  const STATUS = ref<boolean>(false);

  const TREE_ACTION = (action: ActionListItem, node: any) => {
    CURRENT.value = {
      action,
      node,
    };
    FORM_ITEMS.initialValues!.name = '';
    FORM_ITEMS.initialValues!.id = node ? node?.data?.id : 'all';

    if (NEED_NAME.includes(action.action)) {
      FORM_ITEMS.initialValues!.name = node.data.name || '';
    }
    if (EDIT_CATETORY === action.action) {
      FORM_ITEMS.initialValues!.id =
        node.data.parentId === '0' ? 'all' : node.data.parentId;
    }

    MODALTITLE.value = title_enum[action.action] || title_enum['ADD'];
    STATUS.value = true;
  };

  onMounted(() => {
    INIT_TREE_DATA();
  });

  return {
    TREE_DATA,
    INIT_TREE_DATA,
    TREE_ACTION,
    CURRENT,
    FORM_ITEMS,
    ACTION_LIST,
    treeField,
    fieldNames,
    MODALTITLE,
    TREEFIELD,
  };
};
