<template>
  <div class="intermediate_box">
    <BMIcon type="Workmanship" />
    <div class="intermediate_content">
      <div class="content_title">{{ data.data.processName }}</div>
      <div class="content_version">{{ t('版本号') }}: {{ data.data.processVersion }}</div>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMIcon } from '@bmos/components';
  withDefaults(defineProps<{ data: any }>(), { data: {} });
</script>
<style lang="less" scoped>
  .intermediate_box {
    width: 320px;
    min-height: 60px;
    border-radius: 4px;
    box-sizing: border-box;
    padding: 12px;
    background: #ebf1ff;
    display: flex;
    align-items: center;
    justify-content: space-around;
    .intermediate_content {
      width: calc(100% - 40px);
      .content_title {
        color: #242526;
        font-size: 14px;
      }
      .content_version {
        font-size: 12px;
        color: #606266;
      }
    }
  }
</style>
