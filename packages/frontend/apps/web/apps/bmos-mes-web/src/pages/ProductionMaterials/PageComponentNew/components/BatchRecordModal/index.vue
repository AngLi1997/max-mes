<template>
  <Modal v-model:open="open" wrapClassName="modalSizeLarge" :title="t('绑定记录')" @ok="handleOk">
    <div class="tree-container">
      <BMSearchTree
        v-model:expandedKeys="expandedKeys"
        v-model:checkedKeys="checkedKeys"
        :showSearch="true"
        checkable
        :field-names="{ key: 'id', title: 'name', children: 'children' }"
        :tree-data="treeData"
        @check="handleCheck"></BMSearchTree>
    </div>
  </Modal>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { getRecordTreeApi, postSaveBatchRecordApi, getBatchRecordIdsByIdApi } from '@/services';
  import { BMSearchTree } from '@bmos/components';
  import type { TreeProps } from 'ant-design-vue';
  import { message, Modal } from 'ant-design-vue';
  const props = defineProps<{
    open: boolean;
    categoryType: number;
    recordId: string;
  }>();
  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();
  const open = computed({
    get() {
      return props.open;
    },
    set(value: boolean) {
      emit('update:open', value);
    },
  });

  const expandedKeys = ref<string[]>(['0']);
  const checkedKeys = ref<string[]>([]);
  const checkedNodes = ref<any[]>([]);
  const treeData = ref<TreeProps['treeData']>([]);
  const isCheck = ref<boolean>(false);
  /**
   * @description 遍历树数据,如果树所有节点下（包括子孙节点）都没有categoryFlag=false的节点,则删除该节点,返回新的树数据
   *
   */
  const filterTreeData = (data: any[]) => {
    return data.filter(item => {
      if (item.children && item.children.length) {
        item.children = filterTreeData(item.children);
      }
      return item.categoryId || item.children.length;
    });
  };
  const getTreeData = async () => {
    try {
      const res = await getRecordTreeApi();
      res.data = filterTreeData(res.data);
      treeData.value = [{ id: '0', name: t('全部'), children: [...res.data] }];
    } catch (error) {
      message.error(error.message);
    }
  };
  const getCheckedKeys = async () => {
    try {
      const res = await getBatchRecordIdsByIdApi({ productId: props.recordId });
      checkedKeys.value = res.data;
    } catch (error) {
      message.error(error.message);
    }
  };

  getTreeData();

  const handleCheck = (checkedKeys, e: any) => {
    isCheck.value = true;
    checkedNodes.value = e.checkedNodes;
  };
  const handleOk = async () => {
    try {
      const ids = checkedNodes.value.filter(item => item.categoryId).map(item => item.id);
      const data = {
        productId: props.recordId,
        recordIds: isCheck.value ? ids : checkedKeys.value,
      };
      const res = await postSaveBatchRecordApi(data);
      message.success(t('批记录绑定成功'));
      open.value = false;
    } catch (error) {
      message.error(error.message);
    }
  };

  watch(
    () => open.value,
    v => {
      if (v) {
        isCheck.value = false;
        expandedKeys.value = ['0'];
        checkedKeys.value = [];
        checkedNodes.value = [];
        getCheckedKeys();
      }
    },
  );
</script>

<style scoped lang="less">
  .tree-container {
    height: 392px;
    overflow-y: auto;
  }
</style>
