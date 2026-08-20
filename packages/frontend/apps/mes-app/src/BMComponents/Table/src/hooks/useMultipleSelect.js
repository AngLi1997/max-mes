
import { ref } from 'vue';
import { isFunction, isBoolean } from '@/utils/is.js';

export const useMultipleSelect = ({ props, colBindValues, emit }) => {
  // 选中的行
  const selectedRows = ref([]);
  // 是否选中
  const isSelected = (row) => {
    return selectedRows.value.includes(row);
  };
  // 是否禁用
  const getDisabled = (row) => {
    if (colBindValues.disabled && isFunction(colBindValues.disabled)) {
      return colBindValues.disabled(row);
    }
    // 如果是 boolean
    if (isBoolean(colBindValues.disabled)) {
      return colBindValues.disabled;
    }
    return false;
  };

  const multipleSelect = (row) => {
    const index = selectedRows.value.findIndex((item) => item[props.rowKey] === row[props.rowKey]);
    if (index === -1) {
      selectedRows.value.push(row);
    } else {
      selectedRows.value.splice(index, 1);
    }
    emit('selection-change', selectedRows.value);
  };

  return {
    selectedRows,
    isSelected,
    getDisabled,
    multipleSelect
  };
};
