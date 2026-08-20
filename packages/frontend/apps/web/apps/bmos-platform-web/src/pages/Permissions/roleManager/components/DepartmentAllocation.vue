<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('部门分配')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @okModal="ok">
    <div class="tree-container">
      <BMSearchTree
        v-if="tree.treeData?.length > 0"
        v-model:checked-keys="tree.CHECKED_KEYS"
        v-model:expanded-keys="tree.EXPANDED_KEYS"
        :showSearch="true"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="tree.treeData"
        :checkable="true"
        checkStrictly
        :fieldNames="{ title: 'name', key: 'id' }"
        @check="check"></BMSearchTree>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { message } from 'ant-design-vue';
  import { reactive, watch } from 'vue';
  import { departmentTreeAll } from '@/api/Permissions/departmentManagement';
  import { reqDeptRoleBindDept, reqDeptRoleDeptList } from '@/api/Permissions/roleManagement';

  const open = ref<boolean>(false);
  // const emit = defineEmits(['updateTable']);
  const props = defineProps({
    roleId: {
      type: [Number, String],
      default: '',
    },
  });
  const tree = reactive<any>({
    treeData: [],
    CHECKED_KEYS: [],
    EXPANDED_KEYS: [],
  });
  const checkedIds = ref<any>([]); //id集合
  // 获取全部部门树
  const getTreeData = async () => {
    try {
      const res = await departmentTreeAll({ name: '' });
      // tree.treeData = [
      //   {
      //     name: t('全部'),
      //     id: '0',
      //     children: res.data || [],
      //   },
      // ] as any[];
      tree.treeData = res.data || [];
    } catch (error: any) {
      tree.treeData = [];
      message.error(error.message);
    } finally {
      tree.EXPANDED_KEYS = ['0'];
    }
  };
  // 选中复选框触发
  const check = (
    selectedKeys: Key[],
    info: {
      checkedNodes: any;
    },
  ) => {
    checkedIds.value = [];
    const temp = info.checkedNodes;
    temp.forEach((item: any) => {
      checkedIds.value.push(item.id);
    });
  };
  // 确定弹框
  const ok = async () => {
    if (tree.treeData?.length === 0) {
      open.value = false;
      return;
    }
    try {
      const data = {
        id: props.roleId,
        deptIdList: checkedIds.value,
      };
      await reqDeptRoleBindDept(data);
      message.success(t('操作成功'));
      open.value = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        getTreeData();
        const { data } = await reqDeptRoleDeptList({ roleId: props.roleId });
        tree.CHECKED_KEYS = data;
        checkedIds.value = data;
      } else {
        tree.CHECKED_KEYS = [];
      }
    },
    {
      immediate: true,
    },
  );
  const openModal = () => {
    open.value = true;
  };
  defineExpose({
    openModal,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
  }
</style>
