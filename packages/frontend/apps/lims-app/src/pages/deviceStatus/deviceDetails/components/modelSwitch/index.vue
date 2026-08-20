<template>
  <view
    class="switch-container"
    :style="[{ background: bj_color }]"
  >
    <view class="switch_view">
      <view
        class="switch-item"
        :class="{ checked_switch: isSwitch }"
        :style="isSwitch ? `color:${checked_color}` : ''"
        :animation="animationData2"
        @click.prevent.stop="changeSwitch(true)"
      >
        {{ switchList[0] }}
      </view>
      <view
        class="switch-item"
        :class="{ checked_switch: !isSwitch }"
        :style="!isSwitch ? `color:${checked_color}` : ''"
        :animation="animationData3"
        @click.prevent.stop="changeSwitch(false)"
      >
        {{ switchList[1] }}
      </view>
    </view>
    <view
      v-if="disabled"
      class="disabled"
    />
    <view
      class="position_view"
      :animation="animationData1"
      :style="[{ background: checked_bj_color }]"
    />
  </view>
  <wd-message-box />
  <BMMessageBox
    v-model="showMessageBox"
    :title="t('提示')"
    :content="msgContent"
    @confirm="msgBoxConfirm"
  />
</template>

<script setup>
import { BMMessageBox } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n';
import { onMounted, ref, watch } from 'vue';

const props = defineProps({
  switchList: {
    type: Array,
    default: () => {
      return ['开', '关'];
    },
  },
  defaultSwitch: {
    // 默认值
    type: Boolean,
    default: true,
  },
  isShowModal: {
    // 改变开关时，是否弹框提醒
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  bj_color: {
    type: String,
    default: '#fff',
  },
  checked_bj_color: {
    type: String,
    default: '#1989fa',
  },
  checked_color: {
    type: String,
    default: '#fff',
  },
  id: {
    type: null,
    default: null,
  },
});
const emits = defineEmits(['change']);
const isSwitch = ref(true);
const initAnimation = ref({});
const animationData1 = ref({});
const animationData2 = ref({});
const animationData3 = ref({});
const msgContent = ref('');
const showMessageBox = ref(false);
const saveBot = ref();
const changeSwitch = (bot) => {
  if (bot == isSwitch.value || props.disabled) {
    return;
  }
  if (props.isShowModal) {
    const index = bot ? 0 : 1;
    const text = props.switchList[index];
    showMessageBox.value = true;
    msgContent.value = t('您确定要将其调整为') + text + t('吗？');
    saveBot.value = bot;
  }
  else {
    isSwitch.value = bot;
    changeAnimation();
    callParentEvent(bot);
  }
};
const msgBoxConfirm = () => {
  isSwitch.value = saveBot.value;
  changeAnimation();
  callParentEvent(saveBot.value);
};
  // change回调
const callParentEvent = () => {
  emits('change', isSwitch.value, props.id);
};
  // 动画效果
const changeAnimation = () => {
  if (isSwitch.value) {
    animationData1.value = initAnimation.value
      .left(0)
      .width('50%')
      .step()
      .export();
    animationData2.value = initAnimation.value.width('50%').step().export();
    animationData3.value = initAnimation.value.width('50%').step().export();
  }
  else {
    animationData1.value = initAnimation.value
      .left('50%')
      .width('50%')
      .step()
      .export();
    animationData2.value = initAnimation.value.width('50%').step().export();
    animationData3.value = initAnimation.value.width('50%').step().export();
  }
};
onMounted(() => {
  initAnimation.value = uni.createAnimation({
    duration: 500,
    timingFunction: 'ease',
  });
  isSwitch.value = props.defaultSwitch;
  changeAnimation();
});
watch(
  () => props.defaultSwitch,
  (newVal) => {
    console.log(newVal);
    if (isSwitch.value != props.defaultSwitch) {
      isSwitch.value = props.defaultSwitch;
      changeAnimation();
    }
  },
  {
    // immediate: true,
    deep: true,
  },
);
</script>

<style lang="scss" scoped>
  .switch-container {
    display: flex;
    flex-direction: row;
    width: 133.65rpx;
    height: 23.45rpx;
    border-radius: 100upx;
    border: 0 solid #ccc;
    position: relative;

    .switch_view {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 1;
      display: flex;
      border-radius: 100upx;

      .switch-item {
        color: #666;
        font-size: 11.72rpx;
        font-style: normal;
        font-weight: 513;
        height: 100%;
        width: 50%;
        border-radius: 100upx;
        display: flex;
        justify-content: center;
        align-items: center;
        text-align: center;
      }
    }

    .position_view {
      position: absolute;
      top: 0;
      left: 0;
      width: 60%;
      height: 100%;
      border-radius: 100upx;
      background: $uni-color-primary;
    }

    .disabled {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 99;
      background: #fff;
      opacity: 0.6;
      border-radius: 100upx;
    }
  }
</style>
