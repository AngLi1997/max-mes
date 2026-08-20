<template>
  <div :class="{ mind_item_box: true, father_box: data.isFather }" @click="openDetail">
    <div v-if="data.isFather" class="flex_box">
      <div class="title">{{ data.processName }}</div>
      <div class="msg msg_box">{{ t('工艺版本') }}: {{ data.processVersion }}</div>
      <div class="msg msg_box">{{ t('生产批号') }}: {{ data.batchNo }}</div>
    </div>
    <div v-else class="flex_box">
      <div class="title">{{ data.mergeCode }}-{{ data.materialName }}</div>
      <div class="batch_box">
        <div class="batch">{{ data.storageMaterialBatchNo }}</div>
        <div
          v-if="(data.fixRate ?? false) && data.showPercentYield"
          class="yield"
          :style="isOverrun ? 'color: #59bf78;' : 'color: #FF5633;'">
          {{ t('物料平衡') }} {{ data.fixRate }}%
        </div>
      </div>
      <div class="msg">{{ t('生产批号') }}:{{ data.sourceBatchNo }}</div>
      <div class="msg msg_box">
        <div class="msg_item">{{ t('消耗量') }}:{{ data.consumeQuantity }}{{ data.unit }}</div>
        <div class="msg_item">{{ t('产出量') }}:{{ data.outputQuantity }}{{ data.unit }}</div>
      </div>
    </div>
    <div v-if="props.data.children.length > 0" class="expand_box" @click.stop="open">
      {{ allCollapseId.indexOf(data.id) < 0 ? '-' : '+' }}
    </div>
  </div>
  <NormalModalForm
    v-model:open="openModal"
    width="860px"
    :title="t('物料详情')"
    wrap-class-name="modalSizeExtraLarge filing-order-modal"
    :showCancelButton="false"
    :showOkButton="false">
    <msgTableBox type="" :treeSelectData="data" />
  </NormalModalForm>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { NormalModalForm } from '@bmos/components';
  import msgTableBox from '../../msgTableBox/index.vue';
  const props = withDefaults(defineProps<{ data: any; allCollapseId: any }>(), { data: {} });
  const emit = defineEmits(['updateSate']);
  const openModal = ref(false);
  const open = () => {
    emit('updateSate', props.data.id);
  };
  const isOverrun = computed(() => {
    const percentYieldRange = props.data.percentYieldRange;
    const fixRate = props.data.fixRate * 0.01 || 0;
    if (percentYieldRange.lowerSymbol == 'LESS_AND_EQUAL' && fixRate < percentYieldRange.lowerValue) {
      // 范围大于等于下限,收率小于下限
      return false;
    }
    if (percentYieldRange.lowerSymbol == 'LESS_THAN' && fixRate <= percentYieldRange.lowerValue) {
      // 范围大于下限,收率小于等于下限
      return false;
    }
    if (percentYieldRange.upperSymbol == 'LESS_AND_EQUAL' && fixRate > percentYieldRange.upperValue) {
      // 范围小于等于下限,收率大于上限
      return false;
    }
    if (percentYieldRange.upperSymbol == 'LESS_THAN' && fixRate >= percentYieldRange.upperValue) {
      // 范围小于等于下限,收率大于上限
      return false;
    }
    return true;
  });
  const openDetail = () => {
    if (props.data.isFather) {
      return;
    }
    openModal.value = true;
  };
</script>
<style lang="less" scoped>
  .mind_item_box {
    width: 260px;
    height: 140px;
    border: 1px solid #d4d7d9;
    border-radius: 4px;
    padding: 7px;
    background-color: #fff;
    position: relative;
    cursor: pointer;
    .flex_box {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .title {
      font-size: 14px;
      line-height: 16px;
      max-height: 48px;
      display: flex;
      align-items: center;
      color: #242526;
    }
    .batch_box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: 7px;
      .batch {
        padding: 2px 8px;
        background: #ebf1ff;
        color: #2871ff;
        border-radius: 4px;
      }
      .yield {
        color: #59bf78;
      }
    }
    .msg {
      margin-top: 7px;
      font-size: 11.15px;
      line-height: 14.86px;
      color: #606266;
    }
    .msg_box {
      display: flex;
      justify-content: space-between;
    }
    .expand_box {
      width: 20px;
      height: 20px;
      position: absolute;
      right: -14px;
      top: 0;
      bottom: 0;
      margin: auto;
      border: 1px solid #d4d7d9;
      background-color: #fff;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #d4d7d9;
    }
  }
  .father_box {
    height: 100px;
    position: relative;
    top: 20px;
  }
</style>
