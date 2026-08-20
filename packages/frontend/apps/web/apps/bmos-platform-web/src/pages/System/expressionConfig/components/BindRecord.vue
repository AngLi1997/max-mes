<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('绑定记录')"
    wrapClassName="modalSizeMedium"
    @okModal="ok">
    <div class="assignPersonnel">
      <BMSearchTree
        ref="searchTreeRef"
        v-model:checked-keys="checkedKeys"
        v-model:expanded-keys="expandedKeys"
        :fieldNames="{ title: 'name', key: 'id' }"
        :showSearch="true"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="treeData"
        :checkable="true"></BMSearchTree>
    </div>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { ref } from 'vue';
  import { getRecordTree, getBoundRecordIdList, bindRecord } from '@/api';
  import { message } from 'ant-design-vue';
  const expandedKeys = ref<string[]>([]);
  const checkedKeys = ref<string[]>([]); //用于回显的数据
  const treeData = ref();
  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData: any;
    }>(),
    {
      open: false,
      rowData: () => ({}),
    },
  );

  const open = computed({
    get: () => {
      getRoleTreeData();
      getBindList();
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });
  // 绑定记录确定
  const ok = async () => {
    try {
      await bindRecord({
        id: props.rowData.id,
        recordIdList: checkedKeys.value,
      });
      message.success(t('绑定成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 初始化展示记录树
  const getRoleTreeData = async () => {
    const { data } = await getRecordTree({ id: props.rowData.id });
    // 外层加个全部
    const tempTreeData = [
      {
        name: t('全部'),
        id: '0',
        children: data,
      },
    ];
    treeData.value = tempTreeData;
    expandedKeys.value = ['0']; //默认展开第一级树
  };
  // 获取已绑定的记录
  const getBindList = async () => {
    const { data } = await getBoundRecordIdList({ id: props.rowData.id });
    checkedKeys.value = data;
  };
</script>
<style lang="less" scoped>
  .assignPersonnel .bmos-search-tree {
    width: 100%;
  }
</style>
