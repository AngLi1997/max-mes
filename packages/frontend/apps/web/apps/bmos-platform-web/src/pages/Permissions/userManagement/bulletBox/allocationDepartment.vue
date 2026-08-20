<template>
  <!-- 分配部门弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('分配部门')"
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
        checkStrictly
        v-bind="treeProps"
        @check="check"
        @action="action"></BMSearchTree>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance, BMSearchTree, SearchTreeInstance, ActionListItem } from '@bmos/components';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { reactive, ref, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { relateDeptSave, departmentTreeAll } from '../../../../api/Permissions/departmentManagement';
  import { t } from '@bmos/i18n';
  import Empty from '../../../../components/Empty/index.vue';
  const emit = defineEmits(['updateData']);
  const checkedDatas = ref(); //选中的分配部门信息
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
  // 分配部门弹框确定
  const ok = async () => {
    try {
      if (treeProps.treeData.length == 0) {
        open.value = false;
        return;
      }
      if (!checkedDatas.value || checkedDatas.value.length == 0) {
        let temp = checkedKeys.value.map((item: any) => {
          return {
            deptId: item,
            userId: rowId.value,
          };
        });
        // 如果啥也没选,改为也不传空数组
        if (temp.length === 0) {
          temp = [{ userId: rowId.value, deptId: null }];
        }
        await relateDeptSave(temp);
        message.success(t('操作成功'));
        emit('updateData');
        open.value = false;
      } else {
        await relateDeptSave(checkedDatas.value);
        message.success(t('操作成功'));
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
  // 勾选复选框
  const check = (selectedKeys: any) => {
    let checkedData = selectedKeys.checked?.map((item: any) => {
      return {
        deptId: item,
        userId: rowId.value,
      };
    });
    // 如果啥也没选,改为也不传空数组
    if (checkedData.length === 0) {
      checkedData = [
        {
          deptId: null,
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
  // 初始化展示部门树
  const getDepartmentTreeData = async () => {
    const data = { name: '' };
    const res = await departmentTreeAll(data);
    const treeData = dealTreeData(res.data);
    treeProps.treeData = treeData;
    expandedKeys.value = [treeProps.treeData[0]?.id]; //默认展开第一级树
  };
  onMounted(() => {
    getDepartmentTreeData();
  });
  defineExpose({ openModal, treeProps, rowId, checkedKeys, expandedKeys });
</script>
<style lang="less" scoped>
  .assignPersonnel .bmos-search-tree {
    width: 100%;
  }
</style>
