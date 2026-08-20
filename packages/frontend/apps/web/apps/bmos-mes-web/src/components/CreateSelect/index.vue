<template>
  <Select
    v-model:value="innerValue"
    v-bind="attrs"
    :show-search="true"
    :not-found-content="null"
    :showArrow="false"
    :filterOption="false"
    :autoClearSearchValue="false"
    :options="options"
    @search="handleSearch"
    @blur="handleBlur"></Select>
</template>

<script lang="tsx" setup>
  import { omit } from '@bmos/utils';
  import { Select } from 'ant-design-vue';
  import { SelectValue } from 'ant-design-vue/lib/select';
  import { computed, onMounted, ref, useAttrs } from 'vue';

  const emit = defineEmits(['update:select']);
  const attrs = useAttrs();
  const props = defineProps({
    select: {
      type: String,
      default: '',
    },
    request: {
      type: Function,
      default: () => {},
    },
  });

  defineOptions({
    name: 'CreateSelect',
    inheritAttrs: false,
  });

  const innerValue = computed({
    get: () => props.select,
    set: val => emit('update:select', val),
  });

  const options = ref<any[]>([]);
  const inputValue = ref('');

  const setOptions = async (val?: string) => {
    const data = await props.request(val);
    options.value = data;
  };

  // 实现select选择框可下拉单选，也可输入赋值
  const handleSearch = value => {
    setOptions(value);
    if (value.length > 0) {
      inputValue.value = value;
    }
  };
  const handleBlur = e => {
    //判断数组里有没输入的值 如果没有再赋值
    if (
      inputValue.value &&
      options.value.findIndex(item => item.value == inputValue.value) == -1
    ) {
      const val = inputValue.value;
      options.value.push({
        value: val,
        label: val,
      });
      innerValue.value = val;
    }
  };
  onMounted(async () => {
    setOptions();
  });
</script>
