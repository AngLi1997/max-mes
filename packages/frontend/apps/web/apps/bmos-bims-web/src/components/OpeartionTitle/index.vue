<template>
  <div class="opeartion-title">
    <span class="title-content">{{ title }}</span>
    <span class="title-icon">
      <slot name="opeartionIcon" v-if="isIcon">
        <PlusOutlined @click="opeartionClick" class="cursor-common"/>
      </slot>
    </span>
  </div>
</template>

<script setup lang="ts">
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { VueElement } from 'vue';

  const opeartionClick = (e: MouseEvent) => {
    if (props.iconClick) {
      props.iconClick(e);
    }
  };
  const props = withDefaults(
    defineProps<{
      title: string;
      isIcon: boolean | VueElement;
      iconClick?: Function;
    }>(),
    {
      title: '实例',
      isIcon: true,
      iconClick: () => () => {},
    },
  );
</script>

<style scoped lang="less">
  .opeartion-title {
    display: flex;
    justify-content: space-between;
    padding: 10px;
    border-bottom: 1px solid rgba(225, 227, 229, 1);
  }
</style>
