<template>
  <wd-popup
    v-model="open"
    custom-style="width:375.15rpx;height:287.11rpx;border-radius:7.03rpx;"
    :z-index="999"
    @close="open = false"
  >
    <view class="material-popup-container">
      <view class="title">
        {{ title }}
      </view>
      <view class="select_box">
        <wd-datetime-picker-view v-model="value" v-bind="dataTimeProps" :columns-height="255" :type="type" />
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
  import { ref, watch, computed } from 'vue';
  const props = defineProps({
    modelValue: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'datetime'
    },
    dataTimeProps: {
      type: Object,
      default: () => ({})
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
  const emit = defineEmits(['confirm', 'update:modelValue']);

  const value = ref(Date.now());

  // 弹框关闭
  const close = () => {
    console.log('关闭');
    open.value = false;
  };
  // 弹框确认
  const confirm = () => {
    emit('confirm', value.value);
  };
  watch(
    () => props.defaultValue,
    () => {
      if (props.defaultValue) {
        value.value = props.defaultValue;
      }
    },
    {
      immediate: true
    }
  );
</script>

<style lang="scss" scoped>
.material-popup-container {
  .title {
    height: 41.03rpx;
    line-height: 41.03rpx;
    font-size: 15.24rpx;
    text-align: center;
  }
  .select_box {
    height: 189.84rpx;
    overflow: auto;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    // :deep(.wd-picker-view-column__item){
    //     height: 37.5rpx;
    //     line-height: 37.5rpx!important;
    // }
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
