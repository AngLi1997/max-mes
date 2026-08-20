<template>
  <BMModal
    v-model="open"
    :title="title"
    :size="size"
    overflow="visible"
    @cancel="cancel"
    @confirm="confirm"
  >
    <view class="replace-operator-content">
      <view class="label" style="margin-top: 0;">
        {{ labelTitle[0] }}
      </view>
      <BMSign ref="BMSignRef1" v-model="signValue1" :label-list="labelList1" :="attrs" />
      <view class="label">
        {{ labelTitle[1] }}
      </view>
      <BMSign ref="BMSignRef2" v-model="signValue2" :label-list="labelList2" :="attrs" />
    </view>
  </BMModal>
</template>

<script setup>
import { BMModal, BMSign } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, useAttrs, watch } from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: () => t('更换操作人'),
  },
  size: {
    type: String,
    default: 'xLarge',
  },
  modelValue: {
    type: Object,
    default: () => {
      return {};
    },
  },
  labelList1: {
    type: Array,
    default: () => [],
  },
  labelList2: {
    type: Array,
    default: () => [],
  },
  labelTitle: {
    type: Array,
    default: () => [t('当前称量人签名'), t('更换操作人签名')],
  },
});
const emit = defineEmits([
  'update:show',
  'update:modelValue',
  'confirm',
  'cancel',
]);
// eslint-disable-next-line no-unused-vars, unused-imports/no-unused-vars
const attrs = useAttrs();
const open = computed({
  get: () => props.show,
  set: (val) => {
    emit('update:show', val);
  },
});
const signValue1 = ref({
  userName1: '',
  loginName1: '',
  password1: '',
  userId1: '',
  userName2: '',
  loginName2: '',
  password2: '',
  userId2: '',
});
const signValue2 = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});
const BMSignRef1 = ref();
const BMSignRef2 = ref();

const cancel = () => {
  emit('cancel');
  emit('update:show', false);
};
const confirm = async () => {
  try {
    await BMSignRef1.value.checkSign();
    await BMSignRef2.value.checkSign();
    emit('confirm');
  }
  catch (error) {
    console.log(1111, error);
  }
};

watch(() => props.show, (val) => {
  if (val) {
    signValue1.value.password1 = '';
    signValue1.value.password2 = '';
  }
});
</script>

  <style lang="scss" scoped>
    .replace-operator-content {
  height: 234.38rpx;
  font-size: 12.89rpx;
  .label {
    margin: 9.38rpx 0;
  }
}
</style>
