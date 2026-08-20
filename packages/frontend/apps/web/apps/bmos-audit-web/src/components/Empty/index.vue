<template>
  <div class="empty-container">
    <Empty>
      <template #image>
        <SvgIcon icon="no_data" style="width: 100px; height: 100px"></SvgIcon>
      </template>
      <template #description>
        <slot name="description">{{t('暂无数据')}}</slot>
      </template>
    </Empty>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { Empty } from 'ant-design-vue';
  import SvgIcon from '../svg_icon/index.vue';
</script>

<style scoped lang="less">
  .empty-container {
    height: 100%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
