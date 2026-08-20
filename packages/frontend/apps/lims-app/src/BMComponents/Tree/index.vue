<template>
  <BMInputSearch v-model="searchValue" :placeholder="placeholder" />
  <view
    v-if="props.treeData.length && !(searchValue && showKeys.length === 0)"
    class="tree-content"
  >
    <template v-for="item in props.treeData" :key="item[customFieldNames.key]">
      <TreeNode
        :select-keys="selectKeys"
        :node="item"
        :field-names="customFieldNames"
        :search-value="searchValue"
        :show-keys="showKeys"
        :default-expand="checkedParentKeys.includes(item[customFieldNames.key])"
        @node-click="nodeClick"
      />
    </template>
  </view>
  <view v-else class="tree-content">
    <BMNoData
      v-if="props.treeData.length"
      type="emptySearch"
      :text="t('暂无搜索结果')"
    />
    <BMNoData v-else type="emptyData" :text="t('暂无数据')" />
  </view>
</template>

<script setup>
import BMInputSearch from '@/BMComponents/InputSearch/index.vue';
import BMNoData from '@/BMComponents/NoData/index.vue';
import { getNestedValue } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, watch } from 'vue';
import TreeNode from './components/treeNode.vue';
import { useTree } from './hooks/useTree.js';

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  checkedNodes: {
    type: Array,
    default: () => [],
  },
  placeholder: {
    type: String,
    default: () => t('请输入关键字'),
  },
  fieldNames: {
    type: Object,
    default: () => ({}),
  },
  treeData: {
    type: Array,
    default: () => [],
  },
  mode: {
    type: String,
    default: 'single',
  },
});
const emit = defineEmits(['update:modelValue', 'update:checkedNodes']);
const customFieldNames = computed(() => {
  return Object.assign(
    {
      name: 'name',
      key: 'key',
      checkKey: 'key',
      parentId: 'parentId',
      children: 'children',
    },
    props.fieldNames,
  );
});
const {
  searchValue,
  treeDataMap,
  showKeys,
  generateMap,
  getParentKeys,
} = useTree({ props, customFieldNames });
// 被选中节点的父级
const checkedParentKeys = ref([]);
// 选中的树节点key
const selectKeys = ref([]);
// 选中的树节点
const selectNodes = computed(() => {
  return selectKeys.value.map((key) => {
    return treeDataMap.value.get(key);
  });
});

// 树节点点击事件
const nodeClick = (node) => {
  let flag = false;
  if (customFieldNames.value.checkKeyValue !== undefined) {
    flag = getNestedValue(node, customFieldNames.value.checkKey)
    === customFieldNames.value.checkKeyValue;
  }
  else {
    flag = getNestedValue(node, customFieldNames.value.checkKey);
  }
  if (flag) {
    const id = node[customFieldNames.value.key];
    // 单选模式
    if (props.mode === 'single') {
      if (selectKeys.value.includes(id)) {
        selectKeys.value = [];
      }
      else {
        selectKeys.value = [id];
      }
    }
    // 多选模式
    if (props.mode === 'multiple') {
      if (selectKeys.value.includes(id)) {
        selectKeys.value = selectKeys.value.filter(key => key !== id);
      }
      else {
        selectKeys.value.push(id);
      }
    }

    emit('update:modelValue', selectKeys.value);
    emit('update:checkedNodes', selectNodes.value);
  }
};
watch(
  () => props.treeData,
  () => {
    searchValue.value = '';
    if (Array.isArray(props.modelValue)) {
      selectKeys.value = props.modelValue;
    }
    else {
      selectKeys.value = [props.modelValue];
    }
    generateMap(props.treeData);
    treeDataMap.value.forEach((value, key) => {
      if (selectKeys.value.includes(key)) {
        checkedParentKeys.value = [
          ...getParentKeys(treeDataMap.value, key),
          ...checkedParentKeys.value,
        ];
      }
    });
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
.tree-content {
  height: 237.89rpx;
  overflow-y: auto;
  margin-top: 4.69rpx;
}
</style>
