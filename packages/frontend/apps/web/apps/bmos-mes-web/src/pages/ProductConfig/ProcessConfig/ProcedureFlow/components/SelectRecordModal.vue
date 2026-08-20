<template>
  <NormalModalForm
    v-model:open="selectRecordOpen"
    :title="t('记录项')"
    destroyOnClose
    wrap-class-name="modalSizeExtraLarge procedure-select-record-modal"
    @okModal="handleSelectRecordOk">
    <div class="container">
      <div class="left">
        <Tree
          v-model:expandedKeys="expandedKeys"
          v-model:selectedKeys="selectedKeys"
          selectable
          :tree-data="treeData"
          @select="handleSelectRecord"></Tree>
      </div>
      <div class="right">
        <Record ref="recordRef" style="flex: 1"></Record>
      </div>
    </div>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { Tree, message } from 'ant-design-vue';
  import { Record } from '@/components/Record/Record';
  import type { BatchRecordItems } from '../types';
  import { reqRecordListComponentReq, reqRecordListItem } from '@/services';
  import type { EventDataNode, DataNode } from 'ant-design-vue/es/tree';
  import { Key } from 'ant-design-vue/es/vc-tree/interface';
  import { getNodeByKeyInTree } from '../utils';
  import { t } from '@bmos/i18n';
  import { NormalModalForm } from '@bmos/components';

  const emits = defineEmits(['update:selectRecordOpen', 'selectRecordItemId']);
  const props = defineProps({
    selectRecordOpen: {
      type: Boolean,
      default: false,
    },
    batchRecordItems: {
      type: Array as PropType<BatchRecordItems[]>,
      default: () => [],
    },
    curSelectRecordItemId: {
      type: String,
      default: '',
    },
    treeData: {
      type: Array as PropType<DataNode[]>,
      default: () => [],
    },
    expandedKeys: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    selectedKeys: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    fileContent: {
      type: String,
      default: '',
    },
  });

  const selectRecordOpen = computed<boolean>({
    get() {
      return props.selectRecordOpen;
    },
    set(val) {
      emits('update:selectRecordOpen', val);
    },
  });

  // TREE
  // eslint-disable-next-line vue/no-dupe-keys
  const expandedKeys = ref<string[]>([]);
  // eslint-disable-next-line vue/no-dupe-keys
  const selectedKeys = ref<string[]>([]);
  // eslint-disable-next-line vue/no-dupe-keys
  const treeData = ref<DataNode[]>([]);

  const setRecordContent = async (id: string, versionId: string) => {
    if (!recordRef.value || !id || !versionId) return;
    await nextTick();
    try {
      const { data } = await reqRecordListComponentReq({
        itemId: id,
        recordVersionId: versionId,
      } as any);
      recordRef.value?.setContentByConfig(data);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const recordRef = ref<any>();
  const handleSelectRecord = (
    _selectedKeys: Key[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
    },
  ) => {
    selectedKeys.value = [info.node.key as string];
    setRecordContent(info.node.key as string, info.node.recordVersionId as string);
  };

  const handleSelectRecordOk = () => {
    if (selectedKeys.value.length > 0) {
      emits('selectRecordItemId', selectedKeys.value[0], getNodeByKeyInTree(treeData.value, selectedKeys.value[0]));
      selectRecordOpen.value = false;
    } else {
      message.error(t('请选择记录项'));
    }
  };

  watch(
    () => selectRecordOpen.value,
    async val => {
      if (val) {
        try {
          // 如果有传入 treeData 则不请求数据
          if (props.treeData.length > 0 && props.expandedKeys.length > 0 && props.selectedKeys.length > 0) {
            treeData.value = props.treeData;
            expandedKeys.value = props.expandedKeys;
            selectedKeys.value = props.selectedKeys;
            await nextTick();
            const node = getNodeByKeyInTree(treeData.value, props.curSelectRecordItemId);
            if (node) {
              setRecordContent(node.key as string, node.recordVersionId as string);
            }
            return;
          }
          treeData.value = [];
          const { data } = await reqRecordListItem(
            props.batchRecordItems.map(item => item.batchRecordVersionId) as unknown as API.ListRecordItemReq,
          );
          data.forEach((item: any) => {
            treeData.value.push({
              title: item.recordName,
              key: item.versionId,
              selectable: false,
              children: item.recordItemList.map((record: any) => {
                if (props.curSelectRecordItemId === record.itemId) {
                  recordRef.value?.setContentByConfig(record);
                  selectedKeys.value = [record.itemId];
                }
                return {
                  ...record,
                  title: record.name,
                  key: record.itemId,
                };
              }),
            });
          });
          if (!props.curSelectRecordItemId) {
            let first = treeData.value[0]?.children?.[0];
            if (first) {
              selectedKeys.value = [first?.key as string];
              setRecordContent(first.key as string, first.recordVersionId as string);
            }
          }
          expandedKeys.value = data.map((item: any) => item.versionId);
        } catch (error) {}
      }
    },
  );
</script>

<style lang="less">
  .procedure-select-record-modal {
    .mes-modal-body {
      height: calc(100vh - 200px - 52px - 52px);
    }
    .container {
      display: flex;
      border-bottom: 1px solid var(--bmos-second-level-border-color);
      height: calc(100vh - 200px - 52px - 52px);
      .left {
        padding: var(--bmos-padding-small) 0;
        max-height: calc(100vh - 200px - 52px - 32px);
        width: 310px;
        overflow-y: scroll;
        overflow-x: hidden;
        border-right: 1px solid var(--bmos-second-level-border-color);
        .mes-tree {
          width: 309px;
          .mes-tree-treenode {
            width: 95%;
          }
          .mes-tree-treenode-selected {
            background-color: var(--bmos-primary-color-background);
            .mes-tree-node-selected {
              color: var(--bmos-primary-color);
            }
          }
          .mes-tree-node-content-wrapper {
            width: 98%;
          }
        }
      }
      .right {
        height: 100%;
        overflow: auto;
        flex: 1;
        .formula {
          overflow-y: auto;
          height: 100%;
        }
      }
    }
    .record-item {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      padding: 8px;
      .record-drag-icon {
        cursor: move;
        font-size: 16px;
        visibility: hidden;
      }
      .show-drag-icon {
        visibility: visible;
      }
      .record-item-content {
        display: flex;
        flex-direction: column;
        margin-left: var(--bmos-margin-small);
        .title {
          font-weight: 400;
          line-height: 20px;
          color: var(--bmos-second-level-text-color);
        }
        .content {
          font-size: 12px;
          color: var(--bmos-fourth-level-text-color);
        }
      }
    }
    .record-item-select {
      background-color: var(--bmos-primary-color-background);
    }
  }
</style>
