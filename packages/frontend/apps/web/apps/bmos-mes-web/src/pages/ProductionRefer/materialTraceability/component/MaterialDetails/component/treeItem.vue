<template>
  <div class="tree_item">
    <div class="tree_title">
      <div class="title_box">
        <BMIcon type="Box" />
        <div v-if="data.isFather" class="title">{{ data.processName }}</div>
        <div v-else class="title" :style="data.isUnMatched === false ? 'color: red;' : ''">
          {{ data.fullName }}
        </div>
      </div>
      <div v-if="data.storageMaterialBatchNo" class="batch_box">
        {{ data.storageMaterialBatchNo }}
      </div>
    </div>
    <div v-if="!data.isFather" class="tree_content">
      <div class="tree_content_item">{{ t('消耗量') }}: {{ data.consumeQuantity }}{{ data.unit }}</div>
      <div class="tree_content_item">{{ t('生产批号') }}: {{ data.sourceBatchNo }}</div>
      <div class="tree_content_item">{{ t('产出量') }}: {{ data.outputQuantity }}{{ data.unit }}</div>
      <div v-if="data.showPercentYield" class="tree_content_item">{{ t('物料平衡') }}: {{ data.fixRate }}%</div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMIcon } from '@bmos/components';
  withDefaults(defineProps<{ data: any; selectedKeys: any }>(), { data: {} });
</script>
<style lang="less" scoped>
  .tree_item {
    padding: 8px 0;
    .tree_title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 26px;
      font-size: 14px;
      line-height: 14px;
      .title_box {
        width: calc(100% - 140px);
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-around;
        .title {
          width: calc(100% - 20px);
          color: #606266;
        }
      }
      .batch_box {
        max-width: 140px;
        height: 26px;
        line-height: 26px;
        padding: 0 8px;
        box-sizing: border-box;
        background: #d9e5ff;
        color: #2871ff;
        border-radius: 4px;
      }
    }
    .tree_content {
      color: #606266;
      width: 100%;
      height: 16px;
      display: flex;
      align-items: center;
      margin: 16px 0;
      .tree_content_item {
        width: 25%;
        line-height: 16px;
        box-sizing: border-box;
        border-left: 1px solid #e1e3e5;
        padding-left: 10px;
      }
      .tree_content_item:first-child {
        border: 0;
      }
    }
  }
</style>
