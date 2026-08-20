<template>
  <div class="mind_item_box">
    <div class="title">{{ data.data.mergeCode }}-{{ data.data.materialName }}</div>
    <div class="batch_box">
      <div class="batch">{{ data.data.storageMaterialBatchNo }}</div>
      <div
        v-if="(data.data.fixRate ?? false) && data.data.showPercentYield"
        class="yield"
        :style="isOverrun ? 'color: #59bf78;' : 'color: #FF5633;'">
        {{ t('物料平衡') }} {{ data.data.fixRate }}%
      </div>
    </div>
    <div class="msg msg_box">
      <div class="msg_item">{{ t('产出生产批号') }}:{{ data.data.sourceBatchNo }}</div>
      <div v-if="data.data.materialCategoryType?.value != 2" class="msg_item open_btn" @click="openManager">
        {{ t('查看消耗') }}
      </div>
    </div>
    <div class="msg msg_box">
      <div class="msg_item">{{ t('产出量') }}:{{ data.data.outputQuantity }}{{ data.data.unit }}</div>
      <div v-if="data.data.materialCategoryType?.value != 0" class="msg_item open_btn" @click="openDetail('out')">
        {{ t('产出详情') }}
      </div>
    </div>
  </div>
  <NormalModalForm
    v-model:open="openModal"
    width="860px"
    :title="msgType == 'input' ? t('消耗详情') : t('产出详情')"
    wrap-class-name="modalSizeExtraLarge filing-order-modal"
    :showCancelButton="false"
    :showOkButton="false">
    <msgTableBox :type="msgType" :treeSelectData="data.data" />
  </NormalModalForm>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { NormalModalForm } from '@bmos/components';
  import msgTableBox from '../../msgTableBox/index.vue';
  const props = withDefaults(defineProps<{ data: any }>(), { data: {} });
  const openModal = ref(false);
  const msgType = ref('input');
  const emit = defineEmits(['openManager']);
  const isOverrun = computed(() => {
    const percentYieldRange = props.data.data.percentYieldRange;
    const fixRate = props.data.data.fixRate * 0.01 || 0;
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
  const openDetail = (type: string) => {
    msgType.value = type;
    openModal.value = true;
  };
  const openManager = () => {
    emit('openManager', props.data.data);
  };
</script>
<style lang="less" scoped>
  .mind_item_box {
    width: 260px;
    min-height: 100px;
    border-radius: 4px;
    padding: 12px 6px;
    background: #fff;
    border: 1px solid #d4d7d9;
    position: relative;
    .title {
      font-size: 14px;
      line-height: 16px;
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
    }
    .msg {
      font-size: 11.15px;
      line-height: 14.86px;
      color: #606266;
      margin-top: 5px;
    }
    .msg_box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .open_btn {
        color: #2871ff;
        cursor: pointer;
      }
    }
  }
</style>
