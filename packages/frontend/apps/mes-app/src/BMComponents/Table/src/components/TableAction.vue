<template>
  <template v-for="(actionItem, index) in actionFilters" :key="`${index}-${actionItem.label}`">
    <wd-button type="text" v-bind="actionItem" class="bm-table-action-btn">{{ actionItem.label }}</wd-button>
  </template>
</template>
<script setup lang="jsx">
  import { isBoolean, isFunction, isAsyncFunction } from '@/utils/is.js';
  import { computed } from 'vue';

  const props = defineProps({
    actions: {
      // 表格行动作
      type: Array,
      default: () => []
    },
    columnParams: {
      type: Object,
      default: () => ({})
    }
  });

  const isIfShow = (item) => {
    const ifShow = item.ifShow;

    let isIfShow = true;

    if (isBoolean(ifShow)) {
      isIfShow = ifShow;
    }
    if (isFunction(ifShow)) {
      isIfShow = ifShow(item);
    }
    return isIfShow;
  };

  const actionFilters = computed(() => {
    if (props.actions?.length === 0) {
      return [];
    }

    const curActionFilters = props.actions.filter(item => {
      return isIfShow(item);
    });

    const curActions = curActionFilters.map((item, index) => {
      const onClick = item.onClick;
      if (isAsyncFunction(onClick)) {
        item.onClick = async() => {
          await onClick(props.columnParams);
        };
      } else if (isFunction(onClick)) {
        item.onClick = () => {
          onClick(props.columnParams);
        };
      }
      return item;
    });
    return curActions;
  });
</script>
<style lang="scss" scoped>
  .bm-table-action-btn.wd-button.is-text.is-medium {
    padding-top: 0;
    padding-bottom: 0;
    height: 18.75rpx;
  }
</style>
