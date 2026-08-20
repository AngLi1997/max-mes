<template>
  <view :style="{ background: backgroundColor }">
    <wd-steps :active="active" align-center v-bind="attrs">
      <wd-step v-for="(item, index) in stepList" v-bind="item" :key="index" />
    </wd-steps>
  </view>
</template>

<script setup>
  import { useAttrs } from 'vue';

  const attrs = useAttrs();
  defineProps({
    active: {
      type: Number,
      default: 0,
    },
    stepList: {
      type: Array,
      default: () => [],
    },
    backgroundColor: {
      type: String,
      default: 'var(--bmos-bg-color)',
    },

  });
</script>
