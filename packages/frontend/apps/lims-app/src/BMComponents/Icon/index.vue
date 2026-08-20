<template>
  <wd-icon
    v-if="color"
    class-prefix="bmos-app-icon"
    :name="name"
    :size="size"
    :color="color"
    style="display: inline-block;line-height: 1;vertical-align: top;"
  />
  <view v-else :class="className" :style="{ width: size, height: size }" />
</template>

<script setup>
  import { computed } from 'vue';
  const props = defineProps({
    size: {
      type: String,
      default: '14.06rpx'
    },
    color: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    }
  });

  const className = computed(() => {
    return `symbol-icon bmos-app-symbol-icon-${props.name}`;
  });
</script>

<style lang="scss" scoped></style>
