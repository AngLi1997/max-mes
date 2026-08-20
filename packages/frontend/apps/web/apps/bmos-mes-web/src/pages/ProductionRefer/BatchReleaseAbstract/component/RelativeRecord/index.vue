<template>
  <div class="relatice-record">
    <div class="record-container">
      <div class="record-list-container">
        <BMSearchTree
          v-model:selected-keys="SELECTED_KEYS"
          v-model:expanded-keys="EXPANDED_KEYS"
          :tree-data="treeData"
          :fieldNames="{
            title: 'name',
            key: 'id',
          }"
          :showSearch="false"
          :showAllAddIcon="false"
          :showAction="false"
          :load-data="onLoadData"
          @select="TREE_SELECT">
          <template #title="data">
            <span class="recordItemName">{{ data.name }}</span>
            <div class="procedureName">
              {{ data.procedureName }}
            </div>
          </template>
        </BMSearchTree>
      </div>
      <Record ref="recordRef" :activeKeys="templateActiveKeys" @node-click="templateNodeClick"></Record>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMSearchTree } from '@bmos/components';
  import { Record } from '@/components/Record/Record';
  import { useEDITOR } from './hook';
  import { queryDatasetListByProcessIdApi } from '@/services';

  const props = defineProps({
    processTreeData: {
      type: Array,
      default: () => [],
    },
    processData: {
      type: Object,
      default: () => {},
    },
  });
  defineEmits(['confirm']);
  const {
    onLoadData,
    TREE_SELECT,
    templateNodeClick,
    recordRef,
    templateActiveKeys,
    getClickNodeData,
    EXPANDED_KEYS,
    SELECTED_KEYS,
    treeData,
  } = useEDITOR(props);

  onMounted(async () => {
    const { data } = await queryDatasetListByProcessIdApi({
      processId: props.processData.processId,
      datasetType: 'POINT',
    });
    treeData.value = data.map((item: any) => {
      item.children = [];
      item.disabled = true;
      item.selectable = false;
      return item;
    });
    EXPANDED_KEYS.value = [treeData.value[0].id];
  });

  defineExpose({
    getClickNodeData,
  });
</script>

<style scoped lang="less">
  .relatice-record {
    height: 700px;
    display: flex;
    flex-direction: column;
    .record-container {
      display: flex;
      flex: 1;
      overflow: auto;
      gap: 100px;
    }
    .record-list-container {
      width: 300px;
      height: 100%;
      overflow-y: auto;
      border-block: 1px solid #e6e6e6;
    }
    .record-content {
      flex: 1;
    }
    :deep(.mes-tree-node-content-wrapper){
      line-height: 20px !important;
      padding: 8px 8px 8px 0;
    }

    .recordItemName {
      font-weight: 400;
      line-height: 20px;
      color: var(--bmos-second-level-text-color);
    }
    .procedureName {
      font-size: 12px;
      color: var(--bmos-fourth-level-text-color);
      overflow: hidden;
      text-overflow: ellipsis;
      text-wrap: nowrap;
    }
  }
</style>
