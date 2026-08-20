<template>
  <div :class="`my-card`">
    <!-- <div class="lims-card-header">
      {{ props.title }}
    </div> -->
    <slot v-if="slots.title" name="title"></slot>
    <BMTableTitle style="margin-bottom: 20px" v-else-if="props.title" :title="props.title" />
    <div class="my-card-content">
      <slot></slot>
      <Divider v-if="props.type === 'item'"></Divider>
      <slot name="footer"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMTableTitle } from '@bmos/components';
  import { Divider } from 'ant-design-vue';
  import { useSlots } from 'vue';

  const slots = useSlots();

  const props = defineProps({
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: 'card', // 默认为card，可选值为card，item
    },
  });

  // const cardStyleMap = {
  //   card: 'my-mb-small',
  //   item: '',
  // };
</script>

<style lang="less" scoped>
  // .my-mb-small {
  //   margin-bottom: var(--bmos-margin-small);
  // }

  .my-card {
    background-color: #fff;
    width: 100% !important;
    // height: calc(100vh - 105px);
    padding: 16px;
    // margin-bottom: var(--bmos-margin-small);
    &-header {
      width: 100%;
      color: #18191a;
      font-weight: 600;
      font-size: 16px;
      line-height: 1.5;
      margin-bottom: 16px;
    }
  }

  .my-card-content {
    height: 100%;
    // overflow: auto;
  }

  :deep(.bsms-divider-horizontal) {
    margin: 12px 0;
  }
</style>
