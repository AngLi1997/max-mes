<template>
  <uni-td v-bind="colBindValues" :class="[colBindValues.fixed === 'right' ? 'fixed-right' : '', colBindValues.fixed === 'left' ? 'fixed-left' : '']">
    <view v-if="showInput(row)" class="bm-table-input-area">
      <wd-textarea
        v-bind="getComponentProps(row)"
        :model-value="row[colBindValues.prop]"
        auto-height
        custom-class="bm-table-input"
        :disabled="getDisabled(row)"
        @input="({ value }) => {
          updateRowChange(value, colBindValues.prop);
        }"
      />
      <wd-icon
        class-prefix="bmos-app-icon"
        name="kebianji"
        size="14.07rpx"
        color="#2871FF"
      />
    </view>
    <view v-else-if="showInputNumber(row)" class="bm-table-input-number-area">
      <wd-input-number
        :precision="9"
        :min="0"
        v-bind="getComponentProps(row)"
        :model-value="row[colBindValues.prop]"
        custom-class="bm-table-input-number"
        :disabled="getDisabled(row)"
        :placeholder="t('请输入')"
        @change="({ value }) => {
          updateRowChange(value, colBindValues.prop);
        }"
      />
      <wd-icon
        class-prefix="bmos-app-icon"
        name="kebianji"
        size="14.07rpx"
        color="#2871FF"
      />
    </view>
    <BMFormSelect
      v-else-if="showSelect(row)"
      v-bind="getComponentProps(row)"
      :model-value="row[colBindValues.prop]"
      custom-class="bm-table-select"
      @update:model-value="value => updateRowChange(value, colBindValues.prop)"
    />
    <component :is="getColComponent(row, index)" v-else />
  </uni-td>
</template>

<script setup lang="jsx">
import {
  BMFormSelect,
} from '@/BMComponents';
import { isBoolean, isFunction, isString } from '@/utils/is.js';
import {
  t,
} from '@/utils/useBmosI18n.js';
import { computed, isVNode, unref, useAttrs } from 'vue';
import TableAction from './components/TableAction.vue';
import { useMultipleSelect, useTableContext } from './hooks';

const props = defineProps({
  row: {
    type: Object,
    default: () => ({}),
  },
  index: {
    type: Number,
    default: 0,
  },
  showPagination: {
    type: [Boolean, Object],
    default: false,
  },
  paginationRef: {
    type: [Boolean, Object],
    default: () => ({}),
  },
});
const emit = defineEmits([
  'selection-change',
  'updateRow',
]);
const attrs = useAttrs();
const colBindValues = computed(() => {
  return {
    ...attrs,
  };
});

const tableInstance = useTableContext();

const updateRowChange = (value, key) => {
  emit('updateRow', value, key, props.index);
};

const vNodeFactory = (
  component,
  values = unref(colBindValues),
) => {
  if (isString(component)) {
    return <>{component}</>;
  }
  else if (isVNode(component)) {
    return component;
  }
  else if (isFunction(component)) {
    return vNodeFactory(component(values));
  }
  return <>{component}</>;
};
const getColComponent = (row, index) => {
  const component = colBindValues.value.customRender;
  const options = { row, colBindValues: unref(colBindValues), tableInstance, index };
  if (colBindValues.value.prop === 'ACTION') {
    return (
      <TableAction
        actions={colBindValues.value.actions(options)}
        columnParams={options}
      />
    );
  }
  if (colBindValues.value.prop === 'INDEX') {
    if (!props.showPagination) {
      return <>{index + 1}</>;
    }
    return <>{(props.paginationRef.current - 1) * props.paginationRef.pageSize + index + 1}</>;
  }
  return component ? vNodeFactory(component, options) : vNodeFactory(row[colBindValues.value.prop]);
};

const getComponentProps = (row) => {
  const componentProps = colBindValues.value.componentProps;
  if (isFunction(componentProps)) {
    return componentProps(row);
  }
  return componentProps;
};

// 新增通用工具函数
const getBooleanOrFunctionValue = (configValue, row) => {
  if (isFunction(configValue)) {
    return configValue(row);
  }
  if (isBoolean(configValue)) {
    console.log(colBindValues.value, configValue, row);
    return configValue;
  }
  return false;
};

const showInput = row => getBooleanOrFunctionValue(colBindValues.value.showInput, row);
const showInputNumber = row => getBooleanOrFunctionValue(colBindValues.value.showInputNumber, row);
const showSelect = row => getBooleanOrFunctionValue(colBindValues.value.showSelect, row);

const {
  getDisabled,
} = useMultipleSelect({
  props,
  colBindValues,
  emit,
});
</script>

<style lang="scss" scoped>
  :deep(.bm-table-input) {
  border: none;
}
:deep(.bm-table-select) {
  border: none;
  .wd-input__inner {
    height: 18.75rpx;
  }
}
.bm-table-input-area {
  width: 100%;
  display: flex;
  align-items: center;
  .bm-table-input {
    flex: 1;
  }
  .bm-table-input.wd-textarea.is-auto-height:not(.is-cell) {
    padding: 0;
  }
  .bm-table-input.wd-textarea.is-auto-height::after {
    display: none;
  }
}
:deep(.bm-table-input-number-area) {
  width: 100%;
  display: flex;
  align-items: center;
  .bm-table-input-number {
    .wd-input-number__action {
      display: none;
    }
    .wd-input-number__input-border {
      border: none;
    }
    .wd-input-number__inner {
      .wd-input-number__input {
        height: 18.75rpx;
      }
    }
  }
}
.fixed-right {
  position: sticky;
  right: 0;
  background-color: #fff;
}
.fixed-right::after {
  position: absolute;
  top: 0;
  bottom: -1px;
  left: 0;
  width: 2.14rem;
  transform: translateX(-100%);
  transition: box-shadow 0.3s;
  content: '';
  pointer-events: none;
  box-shadow: inset -10px 0 8px -8px rgba(5, 22, 38, 0.12);
}
.fixed-left {
  position: sticky;
  left: 0;
  background-color: #fff;
}
.fixed-left::after {
  position: absolute;
  top: 0;
  bottom: -1px;
  right: 0;
  width: 2.14rem;
  transform: translateX(100%);
  transition: box-shadow 0.3s;
  content: '';
  pointer-events: none;
  box-shadow: inset 10px 0 8px -8px rgba(5, 22, 38, 0.12);
}
</style>
