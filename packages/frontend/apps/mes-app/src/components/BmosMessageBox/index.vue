<template>
  <wd-popup
    v-model="open"
    custom-style="width:246.09rpx;height:138.28rpx;border-radius:7.03rpx;"
    :z-index="999"
    @close="open = false"
  >
    <view class="popup-container">
      <view class="title">
        {{ title }}
      </view>
      <view v-if="subTitle" class="sub-title">
        {{ subTitle }}
      </view>
      <view class="button-container">
        <wd-row gutter="16">
          <wd-col :span="12">
            <BmosButton type="default" :text="t('取消')" @click="close" />
          </wd-col>
          <wd-col :span="12">
            <BmosButton type="primary" :text="t('确定')" @click="confirm" />
          </wd-col>
        </wd-row>
      </view>
    </view>
  </wd-popup>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { computed } from 'vue';
  const props = defineProps({
    modelValue: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    },
    subTitle: {
      type: String,
      default: ''
    }
  });
  const open = computed({
    get() {
      return props.modelValue;
    },
    set(value) {
      emit('update:modelValue', value);
    }
  });
  const emit = defineEmits(['confirm', 'update:modelValue', 'cancel']);
  // 弹框关闭
  const close = () => {
    open.value = false;
    emit('cancel');
  };
  // 弹框确认
  const confirm = () => {
    emit('confirm');
  };
</script>

<style lang="scss" scoped>
.popup-container {
    font-weight: 500;
  .title {
    height: 17.58rpx;
    line-height: 17.58rpx;
    font-size: 15.24rpx;
    text-align: center;
    margin: 22.85rpx auto 8.79rpx;
  }
  .sub-title {
    height: 15.23rpx;
    line-height: 15.23rpx;
    font-size: 12.89rpx;
    text-align: center;
    color: #6C6E73;
  }
  .button-container {
    position: absolute;
    bottom: 12.31rpx;
    left: 0;
    width: 100%;
    padding: 0 9.38rpx;
    box-sizing: border-box;
  }
}
</style>
