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
      :fieldNames="{ title: 'showName', key: 'id' }"
      @check="handleTreeNodeCheck"></BMSearchTree>
  </div>
</template>

<script setup lang="ts">
  import { BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { onMounted, reactive } from 'vue';
  import { getRecordProductTree, recordQueryProductId } from '../../../../services';

  const props = withDefaults(
    defineProps<{
      checks: Array<string>;
      record: string;
    }>(),
    {
      checks: () => [],
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
  /**
   * @description 遍历树数据,如果树所有节点下（包括子孙节点）都没有categoryFlag=false的节点,则删除该节点,返回新的树数据
   *
   */
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
      const res = await getRecordProductTree();
      // 过滤没有产品的节点
      res.data = filterTreeData(res.data);
      tree.treeData = [
        {
          showName: t('全部'),
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

  const getCheckes = async () => {
    try {
      const { data } = await recordQueryProductId({ recordId: props.record });
      tree.CHECKED_KEYS = data;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const exponseSelectKeys = () => {
    const productIds = checkedProductNodes.value.map((item: any) => item.id);
    if (productIds.length > 0) {
      return productIds;
    }
    return tree.CHECKED_KEYS.filter(item => item !== '0');
  };

  const handleTreeNodeCheck = (keys: any, { checkedNodes }: any) => {
    checkedProductNodes.value = checkedNodes.filter((item: any) => !item.categoryFlag && item.id !== '0');
  };

  onMounted(() => {
    getCheckes();
    getTreeData();
    // tree.CHECKED_KEYS = props.checks;
  });

  defineExpose({
    getSelectKeys: exponseSelectKeys,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
  }
</style>
