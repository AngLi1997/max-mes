<template>
  <wd-input
    v-model="value"
    type="text"
    :placeholder="placeholder"
    use-suffix-slot
    :readonly="type === 'select'"
    custom-class="scan-input"
    @confirm="onConfirm"
    @click="onSelect"
  >
    <template #suffix>
      <view class="scan-button">
        <wd-icon
          v-if="type === 'select' && !value"
          name="jiantou-you"
          size="14.06rpx"
          color="#434C59"
          class-prefix="bmos-app-icon"
          style="margin-right: 11.72rpx"
          @click.stop="onSelect"
        />
        <wd-icon
          v-if="type === 'input'"
          name="sousuo"
          size="14.06rpx"
          color="#797C80"
          class-prefix="bmos-app-icon"
          style="margin-right: 11.72rpx"
          @click.stop="onConfirm"
        />
        <wd-icon
          v-if="type === 'select' && value"
          name="qingchu"
          size="14.06rpx"
          color="#797C80"
          class-prefix="bmos-app-icon"
          style="margin-right: 11.72rpx"
          @click.stop="onClear"
        />
      </view>
    </template>
  </wd-input>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';
import { computed, watch } from 'vue';

const props = defineProps({
  placeholder: {
    type: String,
    default: () => t('请输入'),
  },
  type: {
    type: String,
    default: 'input',
  },
  modelValue: {
    type: String,
    default: '',
  },
});

const emit = defineEmits([
  'update:modelValue',
  'select',
  'confirm',
  'clear',
]);

const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});

const onConfirm = () => {
  if (value.value === '') {
    return;
  }
  emit('confirm', value.value);
};

const onSelect = () => {
  if (props.type === 'select') {
    emit('select');
  }
};
const onClear = () => {
  value.value = '';
  if (props.type === 'select') {
    emit('clear');
  }
};
watch(() => value.value, (newValue) => {
  if (props.type === 'input' && (!newValue || newValue === '')) {
    emit('clear');
  }
});
</script>

<style lang="scss" scoped>
.scan-input {
  background-color: #f2f7ff;
  border: 0.59rpx solid #b5d4ff;
  :deep(.wd-input__inner) {
    height: 32.81rpx;
    font-size: 11.72rpx;
    .uni-input-wrapper {
      padding: 9.38rpx;
    }
  }
}
.scan-button {
  display: flex;
  align-items: center;
  z-index: 9;
  position: relative;
  .select-item {
    width: 17.58rpx;
    text-align: right;
  }
  .scan-item {
    padding: 0 0 0 9.38rpx;
    display: flex;
    align-items: center;
  }
}
</style>
