<template>
  <div class="formula-node-container">
    <div class="container-content">
      <Node
        @node-click="nodeClick"
        @icon-click="iconClick"
        @edit-click="data => $emit('edit-click', data)"
        v-for="item in nodeList"
        :key="item.fieldId"
        :type="item.componentType"
        :title="item.componentName"
        :used="item.used"
        :nodeKey="item.fieldId"
        :componentNumber="item.componentNumber"
        :lookup="lookup"
        :showIcon="icon"
        :item="item"
        :actived="
          item.actived === void 0
            ? ActiveKey.includes(item.fieldId)
            : item.actived
        "
        :icon="item.icon || item.componentType"></Node>
    </div>
  </div>
</template>
<script setup lang="ts" generic="T extends object & ComponentNode">
  import { watchEffect, ref } from 'vue';
  import { KEY } from '@/components/Layout/type';
  import Node from './node.vue';
  import { debounce } from '@bmos/utils';
  import { NodeDataType, OptionsType, ComponentNode } from './type';
  const emit = defineEmits([
    'node-click',
    'update:activeKeys',
    'icon-click',
    'edit-click',
  ]);

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
      single: true,
      icon: true,
      lookup: false,
    },
  );

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
  }
  .node-container:last-child {
    margin-bottom: 0;
  }
</style>
