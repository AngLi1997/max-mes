<template>
  <wd-radio-group v-bind="getGroupProps" v-model="radioValue">
    <wd-radio v-for="item in options" v-bind="getRadioProps(item)" :key="item[fieldNames.value]" :value="item[fieldNames.value]" class="bm-form-radio-label">
      {{ item[fieldNames.label] }}
    </wd-radio>
  </wd-radio-group>
</template>

<script setup>
import { computed, useAttrs } from 'vue';

const props = defineProps({
  modelValue: {
    type: [String, Number, Boolean],
    default: '',
  },
  radioGroupProps: {
    type: Object,
    default: () => ({
      cell: true,
      shape: 'button',
    }),
  },
  radioProps: {
    type: Object,
    default: () => ({}),
  },
  options: {
    type: Array,
    default: () => [],
  },
  fieldNames: {
    type: Object,
    default: () => ({
      label: 'label',
      value: 'value',
    }),
  },
});
const emit = defineEmits(['update:modelValue']);
const attrs = useAttrs();

const getGroupProps = computed(() => {
  return {
    ...props.radioGroupProps,
    ...attrs,
  };
});

const getRadioProps = (item) => {
  return {
    ...props.radioProps,
    ...item,
  };
};

const radioValue = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val),
});
</script>

<style lang="scss" scoped>
.bm-form-radio-label {
  :deep(.wd-radio__label) {
    text-align: center;
  }
}
.wd-radio-group .wd-radio.is-button-radio {
  width: 50%;
}
</style>
