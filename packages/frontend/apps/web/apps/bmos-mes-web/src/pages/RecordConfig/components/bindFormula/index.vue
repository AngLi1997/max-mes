<template>
  <div class="tree-container">
    <BMSearchTree
      v-model:checked-keys="tree.CHECKED_KEYS"
      v-model:expanded-keys="tree.EXPANDED_KEYS"
      :showSearch="true"
      :showAllAddIcon="false"
      :showAction="false"
      :tree-data="tree.treeData"
      :checkable="true"
      :fieldNames="{ title: 'name', key: 'id' }"></BMSearchTree>
  </div>
</template>

<script setup lang="ts">
  import { BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { onMounted, reactive } from 'vue';
  import { getExpressionBindTree, getBoundExpressionIdList } from '@/services';

  const props = withDefaults(
    defineProps<{
      record: string;
    }>(),
    {
      record: '',
    },
  );

  const tree = reactive<{
    treeData: any[];
    CHECKED_KEYS: KEY[];
    EXPANDED_KEYS: KEY[];
  }>({
    treeData: [],
    CHECKED_KEYS: [],
    EXPANDED_KEYS: [],
  });
  // 选中的产品节点
  const checkedProductNodes = ref<any[]>([]);
  const filterTreeData = (data: any[]) => {
    return data.filter(item => {
      if (item.children && item.children.length) {
        item.children = filterTreeData(item.children);
      }
      return !item.categoryFlag || item.children.length;
    });
  };
  const getTreeData = async () => {
    try {
      const res = await getExpressionBindTree({ id: props.record });
      // 过滤没有产品的节点
      res.data = filterTreeData(res.data);
      tree.treeData = [
        {
          name: t('全部'),
          id: '0',
          children: res.data,
        },
      ] as any[];
    } catch (error: any) {
      tree.treeData = [];
      message.error(error.message);
    } finally {
      tree.EXPANDED_KEYS = ['0'];
    }
  };

  const getSelectKeys = () => {
    const productIds = checkedProductNodes.value.map((item: any) => item.id);
    if (productIds.length > 0) {
      return productIds;
    }
    return tree.CHECKED_KEYS.filter(item => item !== '0');
  };

  const getChecked = async () => {
    const { data } = await getBoundExpressionIdList({ id: props.record });
    tree.CHECKED_KEYS = data;
  };

  onMounted(() => {
    getTreeData();
    getChecked();
  });

  defineExpose({
    getSelectKeys,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
  }
</style>
