<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('绑定角色')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @cancelModal="cancel"
    @okModal="ok">
    <div class="assignPersonnel">
      <BMSearchTree
        v-if="treeProps.treeData.length > 0"
        ref="searchTreeRef"
        v-model:expandedKeys="expandedKeys"
        v-model:checkedKeys="checkedKeys"
        v-model:selectedKeys="selectedKeys"
        :showAllAddIcon="false"
        :showAddChildren="false"
        :showDeleteNode="false"
        :showAction="false"
        checkable
        v-bind="treeProps"
        @check="check"
        @action="action"></BMSearchTree>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance, BMSearchTree, SearchTreeInstance, ActionListItem } from '@bmos/components';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { t } from '@bmos/i18n';
  import Empty from '../../../../components/Empty/index.vue';
  import { reactive, ref, onMounted } from 'vue';
  import { bindRoleSave, getRoleAggregate } from '../../../../api/Permissions/userManagement';
  import { message } from 'ant-design-vue';
  const emit = defineEmits(['updateData']);
  const checkedDatas = ref();
  const rowId = ref(); //当行Id
  const expandedKeys = ref<string[]>([]);
  const selectedKeys = ref<string[]>([]);
  const checkedKeys = ref<string[]>([]); //用于回显的数据
  const openModal = () => {
    open.value = true;
  };
  const cancel = () => {
    checkedDatas.value = [];
  };
  // 绑定角色确定弹框
  const ok = async () => {
    if (treeProps.treeData.length == 0) {
      open.value = false;
      return;
    }
    try {
      if (!checkedDatas.value || checkedDatas.value.length == 0) {
        let temp = checkedKeys.value.map((item: any) => {
          return {
            roleId: item,
            userId: rowId.value,
          };
        });
        // 如果啥也没选,改为也不传空数组
        if (temp.length === 0) {
          temp = [{ userId: rowId.value, roleId: null }];
        }
        await bindRoleSave(temp);
        message.success(t('绑定成功'));
        emit('updateData');
        open.value = false;
      } else {
        await bindRoleSave(checkedDatas.value);
        message.success(t('绑定成功'));
        emit('updateData');
        open.value = false;
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean | undefined>(false);

  // 树方法
  const searchTreeRef = ref<SearchTreeInstance>();
  const treeProps = reactive<any>({
    addChildrenNeedCode: true,
    treeData: [],
    actionList: [],
  });
  // 选中复选框触发
  const check = (
    selectedKeys: Key[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
      checkedNodes: any;
    },
  ) => {
    let temp = info.checkedNodes.filter((item: any) => item.roleTypeFlag == false);
    let checkedData = temp.map((item: any) => {
      return {
        roleId: item.id,
        userId: rowId.value,
      };
    });
    // 如果啥也没选,改为也不传空数组
    if (checkedData.length === 0) {
      checkedData = [
        {
          roleId: null,
          userId: rowId.value,
        },
      ];
    }
    checkedDatas.value = checkedData;
  };

  const action = (action: ActionListItem, key: Key) => {
    if (action.action === 'editNode') {
      searchTreeRef.value?.changeNodeTitle(key, 'new title');
    }
  };

  // 处理树方法
  const dealTreeData = (treeData: any) => {
    const data = treeData.map((item: any) => ({
      ...item,
      title: item.name,
      key: item.id,
      children: item.children && item.children.length ? dealTreeData(item.children) : [],
    }));
    return data;
  };
  // 初始化展示角色树
  const getRoleTreeData = async () => {
    const data = { name: '' };
    const res = await getRoleAggregate(data);
    const treeData = dealTreeData(res.data);
    // 外层加个全部
    const tempTreeData = [
      {
        title: t('全部'),
        key: '0',
        children: treeData,
      },
    ];
    treeProps.treeData = tempTreeData;
    expandedKeys.value = ['0']; //默认展开第一级树
  };

  onMounted(() => {
    getRoleTreeData();
  });

  defineExpose({ openModal, treeProps, rowId, checkedKeys, expandedKeys });
</script>
<style lang="less" scoped>
  .assignPersonnel .bmos-search-tree {
    width: 100%;
  }
</style>
