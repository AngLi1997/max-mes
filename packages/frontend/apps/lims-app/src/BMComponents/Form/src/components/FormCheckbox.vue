<template>
  <wd-checkbox-group v-bind="getGroupProps" v-model="checkValue">
    <wd-checkbox v-for="item in options" v-bind="getCheckboxProps(item)" :key="item[fieldNames.value]" :model-value="item[fieldNames.value]">
      {{ item[fieldNames.label] }}
    </wd-checkbox>
  </wd-checkbox-group>
</template>

<script setup>
import { merge } from 'lodash-es';
import { computed, useAttrs } from 'vue';

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => ([]),
  },
  checkboxGroupProps: {
    type: Object,
    default: () => ({
      cell: true,
      inline: true,
    }),
  },
  checkboxProps: {
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
    ...attrs,
    ...props.checkboxGroupProps,
  };
});

const getCheckboxProps = (item) => {
  return merge({}, props.checkboxProps, item);
};

const checkValue = computed({
  get: () => props.modelValue || [],
  set: val => emit('update:modelValue', val),
});
</script>

<style lang="scss" scoped>

</style>
