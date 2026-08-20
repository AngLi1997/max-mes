<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('分配角色')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @okModal="ok">
    <div class="tree-container">
      <BMSearchTree
        v-if="tree.treeData[0]?.children.length > 0"
        v-model:checked-keys="tree.CHECKED_KEYS"
        v-model:expanded-keys="tree.EXPANDED_KEYS"
        :showSearch="true"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="tree.treeData"
        :checkable="true"
        :fieldNames="{ title: 'name', key: 'id' }"
        @check="check"></BMSearchTree>
      <Empty v-else :emptyName="inside === '1' ? t('请先分配角色') : t('暂无数据')"></Empty>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { message } from 'ant-design-vue';
  import { reactive, watch } from 'vue';
  import { reqDeptBindRole, reqDeptDeptRole } from '@/api';
  import {
    getRoleAggregate,
    //  reqRoleDeptRoleTree
  } from '@/api/Permissions/userManagement';
  const open = ref<boolean>(false);
  const props = defineProps({
    deptId: {
      type: [Number, String],
      default: '',
    },
    //'1'为部门内部管理,'2'为部门管理
    inside: {
      type: String,
      default: '2',
    },
  });
  const tree = reactive<any>({
    treeData: [],
    CHECKED_KEYS: [],
    EXPANDED_KEYS: [],
  });
  const checkedIds = ref<any>([]); //id集合
  // 获取分配角色的角色树
  const getTreeData = async () => {
    try {
      // const res =
      //   props.inside === '1'
      //     ? await reqRoleDeptRoleTree({ deptId: props.deptId })
      //     : await getRoleAggregate({ name: '' });
      const res = await getRoleAggregate({ name: '' }); //均调全量
      tree.treeData = [
        {
          name: t('全部'),
          id: '0',
          children: res.data || [],
        },
      ] as any[];
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
    const temp = info.checkedNodes.filter((item: any) => item.roleTypeFlag == false);
    temp.forEach((item: any) => {
      checkedIds.value.push(item.id);
    });
  };
  // 确定弹框
  const ok = async () => {
    if (tree.treeData[0]?.children.length === 0) {
      open.value = false;
      return;
    }
    try {
      const data = {
        id: props.deptId,
        roleIdList: checkedIds.value,
      };
      await reqDeptBindRole(data);
      message.success(t('分配成功'));
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
        await getTreeData();
        const { data } = await reqDeptDeptRole({ id: props.deptId });
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
