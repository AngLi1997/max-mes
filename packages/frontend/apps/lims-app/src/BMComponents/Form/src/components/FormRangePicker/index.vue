<template>
  <wd-input
    v-model="inputValue"
    use-suffix-slot
    :placeholder="t('请选择区间')"
    readonly
    :="attrs"
    :disabled="disabled"
    @click="onClick"
  >
    <template #suffix>
      <view class="right-icon">
        <wd-icon
          class-prefix="bmos-app-icon"
          name="shijianxuanzeqi"
          size="14.06rpx"
          color="#434C59"
        />
      </view>
    </template>
  </wd-input>
  <BMRangePickerModal
    :key="domKey"
    v-model="value"
    v-model:open="open"
    :format-date="formatDate"
    :max-date="maxDate"
    :min-date="minDate"
    @confirm="confirm"
    @cancel="cancel"
  />
</template>

<script setup>
import { BMRangePickerModal } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { computed, ref, useAttrs, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  formatDate: {
    type: String,
    default: 'yyyy-MM-dd HH:mm',
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
});
const emit = defineEmits(['change', 'update:modelValue']);
const attrs = useAttrs();
const domKey = ref(0);

const inputValue = computed(() => {
  try {
    return `${format(value.value[0], props.formatDate)} —— ${format(value.value[1], props.formatDate)}`;
  }
  catch (error) {
    return '';
  }
});

const open = ref(false);
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('change', val);
    emit('update:modelValue', val);
  },
});

watch(
  ()=>open.value,
  ()=>{
    domKey.value++;
  }
)

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
</script>

  <style lang="scss" scoped>
  .right-icon {
  margin-right: 9.38rpx;
}
</style>
