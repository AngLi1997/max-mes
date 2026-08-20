<template>
  <Modal
    v-model:open="permissionOpen"
    :title="t('数据权限')"
    @ok="okModal"
    destroyOnClose
    wrap-class-name="modalSizeMedium"
    class="permission-modal">
    <Tree
      v-model:checkedKeys="checkedKeys"
      checkStrictly
      :field-names="{
        key: 'id',
        title: 'name',
      }"
      checkable
      @check="check"
      :tree-data="treeData"></Tree>
  </Modal>
</template>

<script setup lang="tsx">
  import { computed, watch } from 'vue';
  import { Modal, TreeProps } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { reqResourcePermissionListDeptReq } from '@/api';
  import { departmentTreeAll } from '@/api/Permissions/departmentManagement';

  const emits = defineEmits(['update:permissionOpen', 'ok']);
  const props = defineProps({
    permissionOpen: {
      type: Boolean,
      default: false,
    },
    resourceId: {
      type: String,
      default: '',
    },
  });

  const permissionOpen = computed<boolean>({
    get() {
      return props.permissionOpen;
    },
    set(val) {
      emits('update:permissionOpen', val);
    },
  });

  // 监听 open
  watch(
    () => permissionOpen.value,
    val => {
      if (val) {
        getTreeData();
      }
    },
  );

  // 监听 status
  watch(
    () => props.resourceId,
    (val: string) => {
      if (val && props.permissionOpen) {
        getCheckedKeys(val);
      }
    },
  );

  const treeData = ref<TreeProps['treeData']>([]);

  const checkedKeys = ref<{ checked: string[]; halfChecked: string[] }>({
    checked: [],
    halfChecked: [],
  });
  const check = (
    keys: any,
    info: { checked: boolean; checkedNodes: any; node: any; event: any },
  ) => {
    if (info.checked) {
      if (!checkedKeys.value.checked.includes(info.node.key)) {
        checkedKeys.value.checked.push(info.node.key);
      }
      while (info.node.parent) {
        info.node = info.node.parent;
        if (!checkedKeys.value.checked.includes(info.node.key)) {
          checkedKeys.value.checked.push(info.node.key);
        }
      }
    }
  };

  const getTreeData = async () => {
    try {
      const { data } = await departmentTreeAll();
      treeData.value = data;
    } catch (error) {}
  };

  const getCheckedKeys = async (id: string) => {
    try {
      const { data } = await reqResourcePermissionListDeptReq(id);
      checkedKeys.value.checked = data;
    } catch (error) {}
  };

  const okModal = () => {
    emits('ok', checkedKeys.value.checked);
    permissionOpen.value = false;
  };
</script>

<style lang="less"></style>
