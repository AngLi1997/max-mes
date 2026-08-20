<template>
  <div class="formula-node-container">
    <div class="container-content">
      <Tree :treeData="nodeList">
        <template #title="data">
          <Node
            :key="data.fieldId"
            :type="data.componentType"
            :title="data.componentName"
            :used="data.dataRef.used || componentUsedValue(data.componentType)"
            :nodeKey="data.fieldId"
            :componentNumber="data.componentNumber"
            :lookup="lookup"
            :showIcon="icon"
            :item="data"
            :actived="data.actived === void 0 ? ActiveKey.includes(data.fieldId as KEY) : data.actived"
            :icon="data.icon || data.componentType"
            @node-click="nodeClick"
            @icon-click="iconClick"
            @edit-click="data1 => $emit('edit-click', data1)"></Node>
        </template>
      </Tree>
    </div>
  </div>
</template>
<script setup lang="ts" generic="T extends object & ComponentNode">
  import { watchEffect, ref } from 'vue';
  import { KEY } from '@/components/Layout/type';
  import Node from './node.vue';
  import { debounce } from '@bmos/utils';
  import { NodeDataType, OptionsType, ComponentNode } from './type';
  import { Tree } from 'ant-design-vue';
  const emit = defineEmits(['node-click', 'update:activeKeys', 'icon-click', 'edit-click']);

  const props = withDefaults(
    defineProps<{
      nodeList: Array<T & OptionsType & Record<any, any>>;
      field?: OptionsType;
      activeKeys?: Array<KEY>;
      single?: boolean;
      icon: boolean;
      lookup?: boolean;
    }>(),
    {
      nodeList: () => [],
      field: () => ({
        name: 'componentName',
        key: 'fieldId',
        type: 'componentType',
      }),
      activeKeys: void 0,
      single: true,
      icon: true,
      lookup: false,
    },
  );
  const componentUsedValue = type => {
    return ['BUSINESS_PRODUCT_INFO', 'BUSINESS_FORMULA_INFO', 'BUSINESS_FORMULA_INFO_MATERIAL'].includes(type);
  };

  const ActiveKey = ref<KEY[]>([]);

  const nodeClick = debounce((data: NodeDataType) => {
    if (props.single === true) {
      ActiveKey.value = [data.key];
    } else {
      ActiveKey.value.push(data.key);
    }
    emit('node-click', data.key, ActiveKey.value, data);
    if (props.activeKeys !== void 0) {
      emit('update:activeKeys', ActiveKey.value);
    }
  });

  const iconClick = (data: NodeDataType) => {
    emit('icon-click', data);
    emit('update:activeKeys');
  };

  watchEffect(() => {
    if (props.activeKeys === void 0) return;
    ActiveKey.value = props.activeKeys;
  });
</script>

<style scoped lang="less">
  .formula-node-container {
    user-select: none;
    width: 300px;
    height: inherit;
  }
  .container-title {
    width: 300px;
  }
  .container-content {
    // padding-block: 18px;
    // padding-inline: 16px;
    :deep(.mes-tree) {
      width: 100%;
      .mes-tree-treenode {
        display: flex;
        .mes-tree-node-content-wrapper {
          flex: 1;
        }
      }
    }
  }
  .node-container:last-child {
    margin-bottom: 0;
  }
</style>
