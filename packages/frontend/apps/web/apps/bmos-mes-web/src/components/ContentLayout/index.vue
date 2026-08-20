<template>
  <div class="formula-node-container">
    <OpeartionTitle class="container-title" v-bind="$props">
      <template #opeartionIcon>
        <slot name="opeartionIcon"></slot>
      </template>
      <template #ortherIcon>
        <slot name="ortherIcon"></slot>
      </template>
    </OpeartionTitle>
    <div class="container-content">
      <slot></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  import OpeartionTitle from '../OpeartionTitle/index.vue';

  defineProps({
    title: {
      type: String,
      default: '',
    },
    iconClick: {
      type: Function,
      default: () => {},
    },
    isIcon: {
      type: Boolean,
      default: true,
    },
  });
</script>

<style scoped lang="less">
  .formula-node-container {
    background-color: #ffffff;
    // width: 320px;
    height: 100%;
  }
  .container-title {
    // width: 320px;
  }
  .container-content {
    padding-block: 18px;
    padding-inline: 16px;
  }
  .node-container:last-child {
    margin-bottom: 0;
  }
</style>
