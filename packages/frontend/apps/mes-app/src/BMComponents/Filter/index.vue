<template>
  <view>
    <BMIcon
      :name="icon"
      :size="iconSize"
      :color="iconColor"
      @click="openFilter"
    />
    <BMModal
      v-model="show"
      :default-padding="false"
      :title="title"
      size="small"
      position="right"
      closable
      :cancel-text="t('重置')"
      @confirm="confirm"
      @cancel="reset"
    >
      <view class="filter_form_box">
        <BMForm ref="formRef" v-bind="formProps" />
      </view>
    </BMModal>
  </view>
</template>

<script setup>
import { BMForm, BMIcon, BMModal } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { isEmpty, values } from 'lodash-es';
import { computed, nextTick, ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({}),
  },
  title: {
    type: String,
    default: () => t('筛选'),
  },
  icon: {
    type: String,
    default: 'shaixuan',
  },
  formProps: {
    type: Object,
    default: () => ({}),
  },
  iconSize: {
    type: String,
    default: '18.75rpx',
  },
});
const emit = defineEmits(['update:modelValue', 'reset', 'confirm']);

const filterValue = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val),
});

const show = ref(false);
const iconColor = ref('#434C59');
const formRef = ref();
const openFilter = async () => {
  show.value = true;
  nextTick(() => {
    formRef.value?.setFieldsValue(filterValue.value);
  });
};

const confirm = async () => {
  const values = await formRef.value?.validate();
  show.value = false;
  filterValue.value = values;
  emit('confirm', filterValue.value);
};
const reset = async () => {
  await formRef.value?.resetForm();
  filterValue.value = {};
  show.value = false;
  emit('reset');
};

watch(
  () => filterValue.value,
  (value) => {
    if (isEmpty(value) || values(value).every(val => isEmpty(val))) {
      iconColor.value = '#434C59';
    }
    else {
      iconColor.value = '#2871FF';
    }
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
.filter_form_box {
  width: 269.53rpx;
  box-sizing: border-box;
  padding: 11.72rpx 0 11.72rpx 4.69rpx;
}
</style>
