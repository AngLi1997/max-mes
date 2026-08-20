<!-- eslint-disable import/first -->
<template>
  <view class="container">
    <view v-show="showNode" class="node-item">
      <span class="name-box">
        <wd-icon
          v-show="showArrow" name="arrow-down" size="14.07rpx" :class="{ 'icon-rotate': expend }"
          @click="nodeExpendClick"
        />
        <span class="name" :class="{ checked }" @click="nodeClick(node)">
          <span
            v-if="
              node[fieldNames.name]
                && node[fieldNames.name].indexOf(searchValue) > -1
            "
          >
            {{
              node[fieldNames.name].substr(
                0,
                node[fieldNames.name].indexOf(searchValue),
              )
            }}<span style="color: var(--bmos-color-error)">{{
              searchValue
            }}</span>{{
              node[fieldNames.name].substr(
                node[fieldNames.name].indexOf(searchValue) + searchValue.length,
              )
            }}
          </span>
          <span v-else>
            {{ node[fieldNames.name] }}
          </span>
        </span>
      </span>
      <BMIcon v-if="checked" name="xuanze" size="14.06rpx" />
    </view>
    <view class="children-box" :style="{ 'max-height': expend ? '' : 0 }">
      <view v-for="child in node[fieldNames.children]" :key="child.id">
        <TreeNode
          :select-keys="selectKeys" :node="child" :field-names="fieldNames" :search-value="searchValue"
          :show-keys="showKeys" :default-expand="defaultExpand" @node-click="childNodeClick"
        />
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'TreeNode',
};
</script>

<script setup>
// eslint-disable-next-line import/first
import { BMIcon } from '@/BMComponents';
// eslint-disable-next-line import/first
import { getNestedValue } from '@/utils/func.js';
// eslint-disable-next-line import/first
import { computed, ref, toRefs, watch } from 'vue';

const props = defineProps({
  node: {
    type: Object,
    default: () => ({}),
  },
  selectKeys: {
    type: Array,
    default: () => {
      return [];
    },
  },
  showKeys: {
    type: Array,
    default: () => {
      return [];
    },
  },
  searchValue: {
    type: String,
    default: '',
  },
  fieldNames: {
    type: Object,
    default: () => {
      return {};
    },
  },
  defaultExpand: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['nodeClick']);

const { node } = toRefs(props);
node.value.expend = props.defaultExpand;

// 搜索时默认需要展开的节点
const defaultShowKeys = ref([]);

// 是否展示节点
const showNode = computed(() => {
  if (props.searchValue) {
    return props.showKeys.includes(node.value.id);
  }
  return true;
});

// 是否展示箭头
const showArrow = computed(() => {
  let flag = false;
  if (props.fieldNames.checkKeyValue !== undefined) {
    flag
      = !(getNestedValue(node.value, props.fieldNames.checkKey) === props.fieldNames.checkKeyValue);
  }
  else {
    flag = getNestedValue(node.value, props.fieldNames.checkKey);
  }
  return flag && node.value.children && node.value.children.length;
});

// 节点选中状态
const checked = computed(() => {
  return props.selectKeys.includes(node.value[props.fieldNames.key]);
});

// 节点展开状态
const expend = computed(() => {
  return (
    node.value.expend
    || defaultShowKeys.value.includes(node.value[props.fieldNames.key])
  );
});

// 节点展开收起
const nodeExpendClick = () => {
  node.value.expend = !node.value.expend;
  if (defaultShowKeys.value.includes(node.value[props.fieldNames.key])) {
    node.value.expend = false;
    defaultShowKeys.value.splice(
      defaultShowKeys.value.indexOf(node.value[props.fieldNames.key]),
      1,
    );
  }
};
const nodeClick = (data) => {
  let flag = false;
  if (props.fieldNames.checkKeyValue !== undefined) {
    flag = getNestedValue(data, props.fieldNames.checkKey) === props.fieldNames.checkKeyValue;
  }
  else {
    flag = getNestedValue(data, props.fieldNames.checkKey);
  }
  if (flag) {
    emit('nodeClick', data);
  }
};

const childNodeClick = (data) => {
  nodeClick(data);
};

watch(
  () => props.showKeys,
  (val) => {
    defaultShowKeys.value = [...val];
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
.container {
  overflow: hidden;
  transition: 0.5s all;
}

.node-item {
  width: 100%;
  min-height: 33.98rpx;
  border-radius: 4.69rpx;
  padding: 9.38rpx 0;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .name-box {
    width: calc(100% - 23.44rpx);
    display: flex;
    justify-content: flex-start;
    align-items: center;
    color: var(--bmos-color-text-sub);

    .name {
      flex: 1;
      margin-left: 4.69rpx;
    }
  }

  span {
    font-size: var(--bmos-font-size-main);
    font-weight: 500;
    line-height: 15.23rpx;
  }

  .icon {
    transition: 0.5s all;
  }

  .icon-rotate {
    transform: rotate(180deg);
  }
}

.checked {
  color: var(--bmos-color-primary);
}

.children-box {
  padding-left: 18.76rpx;
  transition: 0.5s all;
  overflow: hidden;
}
</style>
