<template>
  <div class="expression-config">
    <div class="tree-container">
      <SearchTree
        @selectTree="selectTree"
        @updateTree="updateTree"
        :treeData="treeData" />
    </div>
    <div class="table-container">
      <Table :treeData="treeData" :selectCategory="selectCategory"></Table>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { DataNode } from 'ant-design-vue/es/tree';
  import SearchTree from './components/SearchTree/index.vue';
  import Table from './components/Table/index.vue';
  import { reqCategoryTreeUsingGET } from '@/api';
  import { t } from '@bmos/i18n';
import { ALL_TYPE } from './types';

  const selectCategory = ref<string>('');
  // 选择树
  const selectTree = (id: string) => {
    selectCategory.value = id;
  };

  // 获取树数据
  const treeData = ref<DataNode[]>([
    {
      id: ALL_TYPE.ALL,
      name: t('全部'),
      children: [],
    },
  ] as unknown as DataNode[]);
  const getTreeData = async () => {
    try {
      const { data } = await reqCategoryTreeUsingGET();
      treeData.value[0].children = data;
    } catch (error) {}
  };

  const updateTree = () => {
    getTreeData();
  };

  onMounted(() => {
    getTreeData();
  });
</script>
<style lang="less" scoped>
  .expression-config {
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    width: 100%;
    display: flex;
    .tree-container {
      width: 265px;
      height: 100%;
      overflow-y: auto;
      overflow-x: hidden;
      flex-shrink: 0;
      border-right: 1px solid var(--bmos-second-level-border-color);
    }
    .table-container {
      flex: 1;
      height: 100%;
      width: calc(100% - 265px);
      .table-second-title {
        line-height: 40px;
        font-weight: 400;
        border-top: 8px solid var(--bmos-second-level-border-color);
        border-bottom: 1px solid var(--bmos-second-level-border-color);
        padding-left: var(--bmos-padding-small);
      }
    }
  }
</style>
