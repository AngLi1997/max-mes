<template>
  <svg :class="svgClass" aria-hidden="true" @click="onClick">
    <use :xlink:href="iconName"></use>
  </svg>
</template>
<script setup lang="ts">
  import { computed } from 'vue';
  import { bmIconProps } from './types';

  const props = defineProps(bmIconProps);

  defineOptions({
    name: 'BMIcons',
    inheritAttrs: true,
  });

  const svgClass = computed(() => {
    if (props.icon) {
      return `svg-icon ${props.icon}`;
    }
    return 'svg-icon';
  });

  const iconName = computed(() => {
    return `#icon-${props.icon}`;
  });
</script>
<style lang="css" scoped>
  .svg-icon {
    width: 1rem;
    height: 1rem;
    vertical-align: -0.15rem;
    fill: currentColor;
    overflow: hidden;
    display: inline-block;
    cursor: pointer;
  }
</style>
