<template>
  <div class="mind_item_box">
    <div class="title">{{ data.data.mergeCode }}-{{ data.data.materialName }}</div>
    <div class="batch_box">
      <div class="batch">{{ data.data.storageMaterialBatchNo }}</div>
    </div>
    <div class="msg msg_box">
      <div class="msg_item">{{ t('产出生产批号') }}:{{ data.data.sourceBatchNo }}</div>
      <div v-if="data.data.materialCategoryType?.value != 0" class="msg_item open_btn" @click="openNext">
        {{ t('查看产出') }}
      </div>
    </div>
    <div class="msg msg_box">
      <div class="msg_item">{{ t('消耗量') }}:{{ data.data.consumeQuantity }}{{ data.data.unit }}</div>
      <div v-if="data.data.materialCategoryType?.value != 2" class="msg_item open_btn" @click="openDetail('input')">
        {{ t('消耗详情') }}
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
  const emit = defineEmits(['openNext']);
  const openDetail = (type: string) => {
    openModal.value = true;
    msgType.value = type;
  };
  const openNext = () => {
    emit('openNext', props.data.data);
  };
</script>
<style lang="less" scoped>
  .mind_item_box {
    width: 260px;
    min-height: 100px;
    border-radius: 4px;
    padding: 12px 6px;
    background: #f6f8fa;
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
      .open_btn {
        color: #2871ff;
        cursor: pointer;
      }
    }
  }
</style>
