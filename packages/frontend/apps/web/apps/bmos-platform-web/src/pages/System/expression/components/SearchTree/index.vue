<template>
  <BMSearchTree
    ref="searchTreeRef"
    v-bind="treeProps"
    v-model:expanded-keys="expandedKey"
    v-model:selected-keys="selectedKeys"
    @select="select"
    @action="action"
    @add-item="handleClickAllAdd"></BMSearchTree>
  <AddModel
    v-model:open="addClassificationModel"
    :treeData="treeProps.treeData"
    :isEdit="isEdit"
    :editNode="editNode"
    :title="addClassificationModelTitle"
    @updateTree="updateTree"></AddModel>
</template>

<script lang="tsx" setup>
  import { reqCategoryTreeDelete } from '@/api';
  import { BMSearchTree, SearchTreeProps, SearchTreeInstance, ActionListItem, Fn } from '@bmos/components';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { ActionList, ActionListType } from '../../types/searchTree';
  import AddModel from './components/AddModel.vue';
  import { Modal, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { ALL_TYPE } from '../../types';

  const emit = defineEmits<{
    (e: 'selectTree', productId: string): void;
    (e: 'updateTree'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      treeData: DataNode[];
    }>(),
    {},
  );

  // 树实例
  const searchTreeRef = ref<SearchTreeInstance>();
  // 树属性
  const treeProps: SearchTreeProps = reactive({
    fieldNames: {
      title: 'name',
      key: 'id',
    },
    actionList: [
      {
        title: t('新建子分类'),
        action: ActionList.AddClassification,
      },
      {
        title: t('编辑分类'),
        action: ActionList.EditClassification,
      },
      {
        title: t('删除分类'),
        action: ActionList.DeleteClassification,
      },
    ],
    treeData: [
      {
        id: ALL_TYPE.ALL,
        name: t('全部'),
        children: [],
      },
    ],
  });

  // 选择树
  const selectedKeys = ref<string[]>([ALL_TYPE.ALL]);
  const select = (
    selectedKeys: Key[],
    _info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    emit('selectTree', selectedKeys[0] as string);
  };

  // 展开的节点
  const expandedKey = ref<string[]>([ALL_TYPE.ALL]);

  const updateTree = (parentId?: string) => {
    emit('updateTree');
    if (parentId) {
      // 如果selectedKeys 里面有parentId，就不用更新了
      if (selectedKeys.value.includes(parentId)) {
        return;
      }
      selectedKeys.value.push(parentId);
    }
  };

  // 获取树数据
  watch(
    () => props.treeData,
    () => {
      treeProps.treeData = props.treeData;
    },
    {
      immediate: true,
      deep: true,
    },
  );
  // const getTreeData = async () => {
  //   try {
  //     const { data } = await reqCategoryTreeUsingGET();
  //     treeProps.treeData && (treeProps.treeData[0].children = data);
  //   } catch (error) {}
  // };

  // 树操作
  const addClassificationModel = ref<boolean>(false);
  const isEdit = ref<boolean>(false);
  // 新增分类
  const addClassification = (node: DataNode) => {
    addClassificationModelTitle.value = t('新增子分类');
    isEdit.value = false;
    editNode.value = node;
    addClassificationModel.value = true;
  };
  // 点击全部添加
  const handleClickAllAdd = () => {
    isEdit.value = false;
    editNode.value = {
      data: {
        id: ALL_TYPE.ALL,
        name: t('全部'),
      },
    } as unknown as DataNode;
    addClassificationModel.value = true;
  };

  // 编辑分类
  const editNode = ref<DataNode>();
  const addClassificationModelTitle = ref<string>(t('新增子分类'));
  const editClassification = (node: DataNode) => {
    addClassificationModelTitle.value = t('编辑分类');
    isEdit.value = true;
    editNode.value = node;
    addClassificationModel.value = true;
  };

  // 删除分类
  const deleteClassification = async (node: DataNode) => {
    if (node.children?.length) {
      Modal.confirm({
        title: t('无法删除该分类信息'),
        icon: h(ExclamationCircleOutlined),
        content: t('该分类下还有所属信息或子分类'),
      });
      return;
    }
    Modal.confirm({
      title: t('是否删除该分类信息'),
      icon: h(ExclamationCircleOutlined),
      content: t('分类信息删除后无法恢复，是否删除？'),
      async onOk() {
        try {
          await reqCategoryTreeDelete(node.data.id as string);
          updateTree();
          message.success(t('成功删除一条分类信息'));
          return Promise.resolve();
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
  const actionMap = new Map<ActionListType, Fn>([
    [ActionList.AddClassification, addClassification],
    [ActionList.EditClassification, editClassification],
    [ActionList.DeleteClassification, deleteClassification],
  ]);
  const action = (actionList: ActionListItem, node: DataNode) => {
    const actionFn = actionMap.get(actionList.action as ActionListType);
    actionFn && actionFn(node);
  };
</script>
<style lang="less" scoped></style>
