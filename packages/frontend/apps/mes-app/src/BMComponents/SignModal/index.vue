<template>
  <BMModal
    v-model="open"
    :title="title"
    :size="size"
    :default-padding="false"
    overflow="visible"
    :cancel-text="cancelText"
    :confirm-text="confirmText"
    @cancel="cancel"
    @confirm="confirm"
  >
    <view style="padding: 0 9.38rpx;">
      <view v-if="subTitle" class="sub-title">
        {{ subTitle }}
      </view>
      <view v-if="showRemark" class="remark-box">
        <view style="margin-bottom: 5.86rpx;">
          <text v-if="remarkRequired" class="label-required">
            *
          </text>
          <text class="label">
            {{ remarkLabel }}
          </text>
        </view>
        <wd-input v-model="remark" :placeholder="t('请输入')" />
      </view>
      <BMSign ref="BMSignRef" v-model="signValue" :="attrs" />
    </view>
    <template v-if="slots.buttons" #buttons>
      <slot name="buttons" />
    </template>
  </BMModal>
</template>

<script setup>
import { BMModal, BMSign } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, useAttrs, useSlots, watch } from 'vue';
import { useNotify } from 'wot-design-uni';

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '签名',
  },
  subTitle: {
    type: String,
    default: '',
  },
  size: {
    type: String,
    default: 'medium',
  },
  showRemark: {
    type: Boolean,
    default: false,
  },
  modelValue: {
    type: Object,
    default: () => {
      return {};
    },
  },
  remarkRequired: {
    type: Boolean,
    default: false,
  },
  remarkLabel: {
    type: String,
    default: () => t('备注'),
  },
  cancelText: {
    type: String,
    default: () => t('取消'),
  },
  confirmText: {
    type: String,
    default: () => t('确定'),
  },
});

const emit = defineEmits([
  'update:show',
  'update:modelValue',
  'confirm',
  'cancel',
]);

const { showNotify } = useNotify();

const slots = useSlots();
// eslint-disable-next-line no-unused-vars, unused-imports/no-unused-vars
const attrs = useAttrs();
const open = computed({
  get: () => props.show,
  set: (val) => {
    emit('update:show', val);
  },
});

const remark = ref('');
const signValue = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', { ...val, remark: remark.value });
  },
});

watch(
  () => remark.value,
  (val) => {
    signValue.value = {
      ...signValue.value,
      remark: val
    }
  }
)

const BMSignRef = ref();

const cancel = () => {
  emit('cancel');
  emit('update:show', false);
};
const confirm = async () => {
  try {
    if (props.remarkRequired && !remark.value) {
      showNotify({
        type: 'warning',
        message: t('请输入') + props.remarkLabel,
      });
      return;
    }
    await BMSignRef.value.checkSign();
    emit('confirm');
  }
  catch (error) {
    console.log('error1', error);
  }
};
watch(
  () => props.show,
  (val) => {
    if (val) {
      // 签名组件打开时，重置签名
      remark.value = '';
      setTimeout(() => {
        BMSignRef.value.resetSignValue();
      }, 100);
    }
  },
);

defineExpose({
  checkSign: () => {
    return BMSignRef.value.checkSign();
  },
});
</script>

<style lang="scss" scoped>
.sub-title {
  font-size: 11.72rpx;
  font-weight: 400;
  line-height: 14.06rpx;
  text-align: center;
  color: var(--bmos-color-text-desc);
  margin-bottom: 14.06rpx;
}
.remark-box {
  display: flex;
  flex-direction: column;
  margin-bottom: 11.72rpx;

  .label-required {
    color: var(--bmos-color-error);
    vertical-align: top;
    font-size: 11.72rpx;
  }
  .label {
    font-size: 11.72rpx;
    font-weight: 300;
    color: var(--bmos-color-text-sub);
    margin-bottom: 5.86rpx;
  }
}
</style>
