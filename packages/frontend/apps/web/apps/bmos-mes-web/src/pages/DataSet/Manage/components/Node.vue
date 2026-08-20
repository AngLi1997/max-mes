<template>
  <Popover
    placement="bottomLeft"
    :overlayInnerStyle="{
      width: '200px',
    }"
    :arrow="false"
    overlayClassName="record-node-popover">
    <template #content>
      <span>{{ `${t('记录项')}：${target.recordItem?.recordItemName}` }}</span>
      <span>{{ `${t('步骤')}：${target.recordItem?.procedureName}` }}</span>
    </template>
    <div class="show-container" @click.stop="$emit('node-click', target)">
      <div class="node-content">
        <div class="top">
          <span class="name">
            {{ target.componentName }}
          </span>
          <span class="number">No.{{ target.componentNumber }}</span>
        </div>
        <span class="record-name">{{ target.recordItem?.recordItemName }}</span>
      </div>
      <CloseOutlined v-if="showIcon" class="node-operation" @click.stop="$emit('icon-click', target)" />
    </div>
  </Popover>
</template>

<script setup lang="ts">
  import { CloseOutlined } from '@ant-design/icons-vue';
  import { ComponentNode } from '@/components/Record';
  import { t } from '@bmos/i18n';

  defineEmits(['icon-click', 'node-click']);
  withDefaults(
    defineProps<{
      target: ComponentNode & {
        recordItem: {
          recordItemName: string;
          procedureName: string;
        };
      };
      showIcon: boolean;
    }>(),
    {
      // @ts-ignore
      target: () => ({
        componentType: '',
        componentName: '',
        componentNumber: '',
      }),
      showIcon: true,
    },
  );
</script>
<style lang="less">
  .record-node-popover {
    .mes-popover-inner {
      padding: 8px;
      border-radius: 4px;
      .mes-popover-inner-content {
        display: flex;
        flex-direction: column;
        font-size: 12px;
        color: var(--bmos-third-level-text-color);
      }
    }
  }
</style>
<style scoped lang="less">
  .show-container {
    border: 1px solid var(--bmos-first-level-border-color);
    display: flex;
    justify-content: space-between;
    padding: 4px 10px;
    border-radius: 4px;
    align-items: center;
    width: 100%;
    gap: 4px;
    cursor: pointer;
    .node-content {
      flex: 1;
      overflow: hidden;
      .top {
        display: flex;
        align-items: center;
        gap: 8px;
      }
      .name {
        // margin-right: var(--bmos-margin-small);
      }
      .number {
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        width: 100%;
        display: inline-block;
        flex: 1;
      }
      .record-name {
        font-size: 12px;
        line-height: 16px;
        color: var(--bmos-fourth-level-text-color);
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        width: 100%;
        display: block;
      }
    }
    .node-operation {
      height: 22px;
      width: 22px;
      display: flex;
      align-items: center;
      margin-left: var(--bmos-margin-small);

      .icon-delete {
        display: block;
      }
    }
  }
</style>
