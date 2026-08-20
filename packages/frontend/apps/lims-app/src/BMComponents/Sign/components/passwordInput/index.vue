<template>
  <view class="password-input-container">
    <view class="label-item">
      {{ label }}
    </view>
    <view class="input-item">
      <input
        v-model="value"
        :disabled="disabled"
        :placeholder="t('请输入密码')"
        style="flex:1;"
        :password="!showPassWord"
      >
      <wd-icon
        v-if="value"
        name="qingchu"
        size="14.06rpx"
        class-prefix="bmos-app-icon"
        @click="handleClear"
      />
      <wd-icon
        v-if="showPassWord"
        name="yulan-kai"
        size="14.06rpx"
        class-prefix="bmos-app-icon"
        @click="showPassWord = !showPassWord"
      />
      <wd-icon
        v-else
        name="yulan-guan"
        size="14.06rpx"
        class-prefix="bmos-app-icon"
        @click="showPassWord = !showPassWord"
      />
    </view>
  </view>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref } from 'vue';

const props = defineProps({
  label: {
    type: String,
    default: () => t('密码'),
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  modelValue: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['update:modelValue']);
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});
const showPassWord = ref(false);
const handleClear = () => {
  emit('update:modelValue', '');
};
</script>

<style lang="scss" scoped>
.password-input-container {
  width: 100%;
  height: 42.19rpx;
  display: flex;
  align-items: center;
  .label-item {
    line-height: 14.06rpx;
    margin-right: 10.55rpx;
    font-size: 11.72rpx;
    color: var(--bmos-color-text-sub);
  }
  .input-item {
    line-height: 14.06rpx;
    flex: 1;
    display: flex;
    align-items: center;
    gap: 5.86rpx;
    :deep(.input-placeholder) {
      color: var(--bmos-color-text-placeholder);
    }
    :deep(.uni-input-input) {
      height: 14.06rpx;
      line-height: 14.06rpx;
    }
    :deep(uni-input) {
      font-size: 9.38rpx;
    }
  }
}
</style>
