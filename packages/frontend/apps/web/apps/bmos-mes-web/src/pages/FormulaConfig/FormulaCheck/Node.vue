<template>
  <div class="formula-node">
    <div class="node-container">
      <div class="node-content-container">
        <span class="node-content node-content-icon">
          <SvgIcon :icon="ALL_NODE_INFO[target.componentType!]?.icon" />
        </span>

        <span class="node-content node-content-type">
          {{ target.componentName }}
        </span>
        <span class="node-content node-content-title">No.{{ target.componentNumber }}</span>
      </div>
      <div class="node-operation">
        <CloseOutlined v-if="showIcon" class="operation-icon icon-delete" @click.stop="$emit('icon-click', target)" />
      </div>
    </div>
    <div v-if="target.relevance" class="node-relevance">
      <span>{{ t('引用') }}:{{ target.relevance }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ComponentNode } from '../../../components/Record/NodeList/type';
  import { ALL_NODE_INFO } from '@/components/Record';
  import { CloseOutlined } from '@ant-design/icons-vue';
  import { t } from '@bmos/i18n';
  import SvgIcon from '../../../components/svg-icon/index.vue';
  defineEmits(['icon-click']);
  withDefaults(
    defineProps<{
      target: ComponentNode & { relevance: string };
      showIcon: boolean;
    }>(),
    {
      target: () => ({
        componentType: '',
        componentName: '',
        componentNumber: '',
      }),
      showIcon: true,
    },
  );
</script>

<style scoped lang="less">
  .formula-node {
    width: 100%;
    .node-relevance {
      color: #909398;
      margin-top: 5px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      width: 100%;
    }
  }
  .node-container {
    border: 1px solid rgba(212, 215, 217, 1);
    margin-bottom: 0;
  }
</style>
