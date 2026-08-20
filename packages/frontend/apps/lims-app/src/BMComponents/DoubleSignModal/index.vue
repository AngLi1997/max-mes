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
        {{ labelTitle1 }}
      </view>
      <BMSign
        ref="BMSignRef1"
        v-model="signValue1"
        :label-list="labelList1"
        :="attrs"
      />
      <view class="label">
        {{ labelTitle2 }}
      </view>
      <BMSign
        ref="BMSignRef2"
        v-model="signValue2"
        :label-list="labelList2"
        :="attrs"
      />
    </view>
  </BMModal>
</template>

<script setup>
import { BMModal, BMSign } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, useAttrs } from 'vue';

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
    default: 'large',
  },
  value1: {
    type: Object,
    default: () => {
      return {};
    },
  },
  value2: {
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
  labelTitle1: {
    type: String,
    default: '',
  },
  labelTitle2: {
    type: String,
    default: '',
  },
});
const emit = defineEmits([
  'update:show',
  'update:value1',
  'update:value2',
  'confirm',
  'cancel',
]);
// eslint-disable-next-line unused-imports/no-unused-vars, no-unused-vars
const attrs = useAttrs();
const open = computed({
  get: () => props.show,
  set: (val) => {
    emit('update:show', val);
  },
});
const signValue1 = computed({
  get: () => props.value1,
  set: (val) => {
    emit('update:value1', val);
  },
});
const signValue2 = computed({
  get: () => props.value2,
  set: (val) => {
    emit('update:value2', val);
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

//   watch(
//     () => props.show,
//     (val) => {
//       if (val) {
//         signValue1.value.password1 = '';
//         signValue1.value.password2 = '';
//         signValue2.value.password1 = '';
//         signValue2.value.password2 = '';
//       }
//     }
//   );
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
