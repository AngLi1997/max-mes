<template>
  <div class="node-list-container">
    <div id="container-content" ref="treeParent" class="node-list-container-content">
      <Tree
        ref="nodeListTree"
        v-model:expanded-keys="expandedKeys"
        :treeData="nodeList"
        :field-names="{ key: 'fieldId' }"
        :height="treeHeight">
        <template #title="data">
          <AddButton
            v-if="ALL_BUTTON_INFO.includes(data.componentType)"
            :type="data.componentType"
            :title="data.componentName"
            :nodeKey="data.fieldId"
            :componentNumber="data.componentNumber"
            :lookup="lookup"
            :showIcon="icon"
            :item="data"
            :actived="data.actived === void 0 ? ActiveKey.includes(data.fieldId as KEY) : data.actived"
            :icon="data.icon || data.componentType"
            @node-click="nodeClick"></AddButton>
          <Node
            v-else
            :id="data.fieldId"
            :key="data.fieldId"
            :type="data.componentType"
            :title="data.componentName"
            :used="componentUsedValue(data)"
            :nodeKey="data.fieldId"
            :componentNumber="data.componentNumber"
            :lookup="lookup"
            :showIcon="icon"
            :item="data"
            :actived="data.actived === void 0 ? ActiveKey.includes(data.fieldId as KEY) : data.actived"
            :icon="getIcon(data)"
            @node-click="nodeClick"
            @icon-click="iconClick"
            @edit-click="data1 => $emit('edit-click', data1)"
            @copy-click="copyClick"></Node>
        </template>
      </Tree>
    </div>
  </div>
</template>
<script setup lang="ts" generic="T extends object & ComponentNode">
  import { watchEffect, ref, watch } from 'vue';
  import { KEY } from '@/components/Layout/type';
  import Node from './node.vue';
  import AddButton from './components/addButton.vue';
  import { debounce } from '@bmos/utils';
  import { NodeDataType, OptionsType, ComponentNode } from './type';
  import { Tree } from 'ant-design-vue';
  import {
    ALL_BUTTON_INFO,
    ALL_NODE_INFO,
    DATE_COMPONENT,
    NUMBER_COMPONENT,
    SELECT_COMPONENT,
    SUBMIT_SIGN_COMPONENT,
    TEXT_COMPONENT,
    COPY_BUSINESS_NODE_INFO,
    REVIEW_SIGN_COMPONENT,
    CUSTOM_FIELD_COMPONENT,
    ALL_DYNAMIC_TABLE_NODE,
    EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE,
  } from './enum';
  const emit = defineEmits(['node-click', 'update:activeKeys', 'icon-click', 'edit-click', 'copy-click']);

  const props = withDefaults(
    defineProps<{
      nodeList: Array<T & OptionsType & Record<any, any>>;
      field?: OptionsType;
      activeKeys?: Array<KEY>;
      single?: boolean;
      icon?: boolean;
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
  const componentUsedValue = (data: ComponentNode & { used: boolean }) => {
    if (
      // 根节点使用used标识
      ALL_NODE_INFO[data.componentType!] ||
      ALL_DYNAMIC_TABLE_NODE.includes(data.componentType!)
    ) {
      return data.used;
    }
    return true;
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

  // 树展开的节点
  const expandedKeys = ref<KEY[]>([]);
  // 树收起来的节点
  const collapsedKeys = ref<KEY[]>([]);
  // 所有的节点id
  const allKeys = ref<KEY[]>([]);
  // 所有节点及父级
  const allFatherKeys = ref({});
  const setExpendKeys = () => {
    if (props.nodeList.length == 0) {
      return;
    }
    expandedKeys.value = [...expandedKeys.value];
    allKeys.value = [];
    allFatherKeys.value = {};
    props.nodeList.forEach(item => {
      allKeys.value.push(item.fieldId);
      // 排除基础组件
      if (item.children && item.children.length > 0) {
        // 三层组件展开第一层
        if (item.children[0].children?.length && expandedKeys.value.indexOf(item.fieldId) < 0) {
          expandedKeys.value.push(item.fieldId);
        }
        // 第二层遍历
        item.children.forEach(child => {
          allKeys.value.push(child.fieldId);
          allFatherKeys.value[child.fieldId] = [item.fieldId];
          // 有第三层,但是第二层不展开
          if (child.children && child.children.length > 0) {
            child.children.forEach(nodeItem => {
              allFatherKeys.value[nodeItem.fieldId] = [item.fieldId, child.fieldId];
            });
          }
        });
      }
    });
    expandedKeys.value = expandedKeys.value.filter(item => collapsedKeys.value.indexOf(item) === -1);
  };

  const getIcon = (data: ComponentNode) => {
    if (data.icon) {
      return data.icon;
    }
    if (!data.componentType) {
      return '';
    }
    if (TEXT_COMPONENT.includes(data.componentType)) {
      return 'TEXT';
    }
    if (DATE_COMPONENT.includes(data.componentType)) {
      return 'DATE';
    }
    if (NUMBER_COMPONENT.includes(data.componentType)) {
      return 'NUMBER';
    }
    if (SELECT_COMPONENT.includes(data.componentType)) {
      return 'SELECT';
    }
    if (SUBMIT_SIGN_COMPONENT.includes(data.componentType)) {
      return 'SUBMIT_SIGN';
    }
    if (REVIEW_SIGN_COMPONENT.includes(data.componentType)) {
      return 'REVIEW_SIGN';
    }
    if (CUSTOM_FIELD_COMPONENT.includes(data.componentType)) {
      return 'CUSTOM';
    }
    if (ALL_DYNAMIC_TABLE_NODE.includes(data.componentType)) {
      return EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE.EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE.icon;
    }
    if (data.componentType == 'EQUIPMENT_DATA_DRAW') {
      return 'Frame';
    }
    return data.componentType;
  };

  const copyClick = (data: any) => {
    const copyTypeButton = COPY_BUSINESS_NODE_INFO[data.type];
    emit('copy-click', data, copyTypeButton);
  };

  // 全部收起
  const retractAll = () => {
    expandedKeys.value = [];
  };

  // 全部展开
  const openAll = () => {
    expandedKeys.value = [...allKeys.value];
  };

  const treeParent = ref<HTMLElement | null>(null);
  const treeHeight = computed(() => {
    return treeParent.value?.clientHeight;
  });

  const nodeListTree = ref();
  const scrollTo = (key: KEY) => {
    nodeListTree.value?.scrollTo({ key, align: 'auto' });
  };

  defineExpose({
    retractAll,
    openAll,
    scrollTo,
  });

  watch(
    () => expandedKeys.value.length,
    () => {
      collapsedKeys.value = allKeys.value.filter(item => expandedKeys.value.indexOf(item) === -1);
    },
  );

  watch(
    () => props.nodeList,
    () => {
      setExpendKeys();
    },
    { deep: true },
  );

  watch(
    () => props.activeKeys,
    async newVal => {
      if (newVal?.length == 1) {
        // 如果点击的组件被收起,展开
        allFatherKeys.value[props.activeKeys?.[0]]?.map((item: string) => {
          if (expandedKeys.value.indexOf(item) < 0) {
            expandedKeys.value.push(item);
          }
        });
        // 如果已经展示出,还会重新定位
        await nextTick();
        scrollTo(newVal[0]);
      }
    },
  );
</script>

<style scoped lang="less">
  .node-list-container {
    user-select: none;
    width: 100%;
    height: 100%;
  }
  .container-title {
    width: 100%;
  }
  .node-list-container-content {
    height: 100%;
    :deep(.mes-tree) {
      width: 100%;
      .mes-tree-treenode {
        display: flex;
        .mes-tree-node-content-wrapper {
          flex: 1;
        }
      }
      .mes-tree-treenode-switcher-close:hover {
        background-color: transparent !important;
      }
      .mes-tree-list-scrollbar {
        width: 4px !important;
        right: -12px !important;
      }
    }
  }
  .node-container:last-child {
    margin-bottom: 0;
  }
</style>
