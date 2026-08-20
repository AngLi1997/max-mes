<template>
  <wd-input
    v-model="inputValue"
    use-suffix-slot
    :placeholder="t('选择日期')"
    readonly
    :="attrs"
    :disabled="disabled"
    @click="onClick"
  >
    <template #suffix>
      <view class="right-icon">
        <wd-icon class-prefix="bmos-app-icon" name="shijianxuanzeqi" size="14.06rpx" color="#434C59" />
      </view>
    </template>
  </wd-input>
  <BMDatePickerModal
    v-model="value"
    v-model:open="open"
    :format-date="formatDate"
    :max-date="maxDate"
    :min-date="minDate"
    :title="title"
    @confirm="confirm"
    @cancel="cancel"
  />
</template>

<script setup>
import { BMDatePickerModal } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { isEmpty } from 'lodash-es';
import { computed, ref, useAttrs, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: [String, Date, Number],
    default: '',
  },
  formatDate: {
    type: String,
    default: 'yyyy-MM-dd HH:mm',
  },
  valueFormat: {
    type: String,
    default: '',
  },
  maxDate: {
    type: [String, Date, Number],
    default: Date.now() + 365 * 24 * 60 * 60 * 10000,
  },
  minDate: {
    type: [String, Date, Number],
    default: Date.now() - 365 * 24 * 60 * 60 * 10000,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: () => t('请选择时间'),
  },
});
const emit = defineEmits(['change', 'update:modelValue']);
// eslint-disable-next-line unused-imports/no-unused-vars, no-unused-vars
const attrs = useAttrs();
const inputValue = ref('');

const open = ref(false);
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('change', val);
    emit('update:modelValue', isEmpty(props.valueFormat) ? val : format(val, props.valueFormat));
  },
});

const onClick = () => {
  if (props.disabled)
    return;
  open.value = true;
};

const confirm = (val) => {
  value.value = val;
  open.value = false;
};
const cancel = () => {
  open.value = false;
};
watch(() => value.value, () => {
  try {
    inputValue.value = format(value.value, props.formatDate);
  }
  catch (_error) {
    inputValue.value = '';
  }
});
</script>

<style lang="scss" scoped>
.right-icon {
  margin-right: 9.38rpx;
}
</style>
