<template>
  <Popover trigger="click">
    <template #content>
      <ColorSelect :color="colorValue" @update:color="updateColor" />
    </template>
    <div class="color">
      <div class="color-picker" :style="{ border: `1px solid ${colorValue}` }">
        <div class="color-bg" :style="{ backgroundColor: colorValue }"></div>
      </div>
      <div style="margin-left: 8px">{{ colorValue }}</div>
    </div>
  </Popover>
</template>

<script setup lang="ts">
  import ColorSelect from './ColorSelect.vue';
  import { Popover } from 'ant-design-vue';

  type colorType = 'rgb' | 'rgba' | 'hex6' | 'hex8' | 'hsv' | 'hsl';

  const props = defineProps({
    modelValue: {
      type: String,
      default: '#000000',
    },
    type: {
      type: String as PropType<colorType>,
      default: 'hex8',
    },
  });

  const emits = defineEmits(['update:modelValue']);

  const colorValue = computed({
    get: () => props.modelValue,
    set: val => {
      emits('update:modelValue', val);
    },
  });

  const updateColor = (colorObj: any) => {
    colorValue.value = colorObj[props.type];
  };
</script>

<style lang="less" scoped>
  @size: 24px;
  .color-picker {
    width: @size;
    height: @size;
    border-radius: 3px;
    padding: 4px;
    .color-bg {
      width: 100%;
      height: 100%;
      border-radius: 3px;
    }
  }

  .color {
    width: 100%;
    height: @size;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    cursor: pointer;
  }
</style>
