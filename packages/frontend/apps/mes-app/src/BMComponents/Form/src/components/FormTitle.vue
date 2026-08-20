<template>
  <view class="form-title" :class="[needPadding ? 'need-padding' : '']" v-bind="attrs">
    <view class="left" />
    <slot>标题</slot>
  </view>
</template>

<script setup>
import { useAttrs } from 'vue';
import { useFormContext } from '../hooks';

const props = defineProps({
  color: {
    type: String,
    default: '#F2F3F5',
  },
});

const attrs = useAttrs();

const { needPadding } = useFormContext();
</script>

<style lang="scss" scoped>
.form-title {
  width: 100%;
  background-color: v-bind('props.color');
  height: 35.16rpx;
  display: flex;
  align-items: center;
  gap: 9.38rpx;
  padding: 0 7.03rpx;
  margin-bottom: 11.72rpx;
  font-size: 12.89rpx;
  .left {
    width: 3.52rpx;
    height: 14.06rpx;
    border-radius: 1.76rpx;
    background-color: var(--bmos-color-primary);
  }
}
.need-padding {
  margin-left: -7.03rpx;
}
</style>
