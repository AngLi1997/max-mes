<template>
  <view class="bmos-select">
    <wd-input
      v-model="inputValue"
      type="text"
      :placeholder="placeholder"
      use-suffix-slot
      :disabled="disabled"
      readonly
      :custom-class="customClass"
      @click="onClick"
      @clear="onClear"
    >
      <template #suffix>
        <view class="right-box">
          <wd-icon
            v-if="!inputValue || disabled"
            name="jiantou-you"
            size="14.06rpx"
            color="#434C59"
            style="margin-right: 9.38rpx"
            class-prefix="bmos-app-icon"
            @click.stop="onClick"
          />
          <wd-icon
            v-if="inputValue && !disabled && clearable"
            name="qingchu"
            size="14.06rpx"
            color="#797C80"
            class-prefix="bmos-app-icon"
            style="margin-right: 9.38rpx"
            @click.stop="clearValue"
          />
          <view v-if="slots.right" class="right-item" @click.stop>
            <slot name="right" class="right" />
          </view>
        </view>
      </template>
    </wd-input>
    <slot name="modal">
      <BMRadioModal
        v-if="modalType === 'radio'"
        v-model="value"
        v-model:open="modalOpen"
        :="attrs"
        :field-names="fieldNames"
        @confirm="confirm"
        @cancel="cancel"
      />
      <BMCheckboxModal
        v-if="modalType === 'checkbox'"
        v-model="value"
        v-model:open="modalOpen"
        :="attrs"
        :field-names="fieldNames"
        @confirm="confirm"
        @cancel="cancel"
      />
      <BMTreeModal
        v-if="modalType === 'tree'"
        v-model="value"
        v-model:open="modalOpen"
        :="attrs"
        :field-names="fieldNames"
        @confirm="confirm"
      />
    </slot>
  </view>
</template>

<script setup>
import { BMCheckboxModal, BMRadioModal, BMTreeModal } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, useAttrs, useSlots, watch } from 'vue';

const props = defineProps({
  placeholder: {
    type: String,
    default: () => t('请选择'),
  },
  modelValue: {
    type: String,
    default: '',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  customClass: {
    type: String,
    default: '',
  },
  type: {
    type: String,
    default: 'radio',
  },
  fieldNames: {
    type: Object,
    default: () => ({ label: 'label', value: 'value' }),
  },
  clearable: {
    type: Boolean,
    default: true,
  },
  showEmptyValue: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(['update:modelValue', 'clear', 'select', 'change', 'confirm', 'cancel']);
const slots = useSlots();
const attrs = useAttrs();
const modalType = computed(() => {
  return props.type;
});
const inputValue = ref('');
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('change', val);
    emit('update:modelValue', val);
  },
});

const confirm = (data) => {
  emit('confirm', data);
};
const cancel = () => {
  emit('cancel');
};

const onClear = () => {
  if (Array.isArray(value.value)) {
    value.value = [];
  }
  else {
    value.value = '';
  }
  emit('update:modelValue', value.value);
};

const modalOpen = ref(false);

// 判断是否选中
const isChecked = (val) => {
  if (Array.isArray(value.value)) {
    return value.value.includes(val);
  }
  return value.value === val;
};

const setInputValue = () => {
  inputValue.value = '';
  if (props.showEmptyValue) {
    inputValue.value = value.value;
    return;
  }
  let isFirst = true;
  if (attrs['tree-data'] && modalType.value === 'tree') {
    const findValue = (arr) => {
      arr.forEach((item) => {
        if (isChecked(item[props.fieldNames.key || 'key'])) {
          if (inputValue.value) {
            inputValue.value += ',';
          }
          inputValue.value += item[props.fieldNames.name || 'name'];
        }
        else if (item.children) {
          findValue(item.children);
        }
      });
    };
    findValue(attrs['tree-data'] || []);
    return;
  }
  if (attrs.options) {
    attrs.options?.forEach((item) => {
      if (
        modalType.value === 'radio'
        && item[props.fieldNames.value || 'value'] === value.value
      ) {
        inputValue.value = item[props.fieldNames.label || 'label'];
      }
      else if (
        modalType.value === 'checkbox'
        && value.value.includes(item[props.fieldNames.value || 'value'])
      ) {
        inputValue.value
            += (isFirst ? '' : ',') + item[props.fieldNames.label || 'label'];
        isFirst = false;
      }
    });
  }
  else {
    inputValue.value = value.value;
  }
};
const onClick = () => {
  if (props.disabled)
    return;
  modalOpen.value = true;
  emit('select');
};
const clearValue = () => {
  if (Array.isArray(value.value)) {
    value.value = [];
  }
  else {
    value.value = '';
  }
  emit('clear');
};

watch(
  () => [attrs.options, attrs['tree-data']],
  () => {
    setInputValue();
  },
  { deep: true },
);

watch(
  () => value.value,
  (val) => {
    if (val) {
      setInputValue();
    }
    else {
      inputValue.value = '';
    }
  },
  { immediate: true, deep: true },
);
</script>

<style lang="scss" scoped>
.bmos-select {
  :deep(.uni-input-input) {
    text-overflow: ellipsis;
  }
  .right-box {
    display: flex;
    align-items: center;
    z-index: 9;
    position: relative;
    .right-item {
      padding: 0 9.38rpx;
      position: relative;
    }
  }
  .right-item::before {
    position: absolute;
    width: 0.94rpx;
    background-color: var(--bmos-color-border);
    height: 24px;
    content: '';
    left: 0;
    top: 50%;
    transform: translateY(-50%);
  }
}
</style>
