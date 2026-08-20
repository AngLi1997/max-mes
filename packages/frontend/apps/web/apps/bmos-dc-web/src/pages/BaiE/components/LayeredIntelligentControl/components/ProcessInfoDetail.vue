<template>
  <div class="process-info-detail">
    <ModalTitle :title="t('工序信息')" icon="process" @close="close"></ModalTitle>
    <div class="process-info-detail-content">
      <div
        v-for="(item, index) in list"
        :key="index"
        class="process-item"
        :class="[index !== list.length - 1 ? 'margin-b-10' : '']">
        <ModalItem
          :item="item"
          :fields="[
            {
              label: t('当前生产批次'),
              key: 'currentBatchNo',
            },
            {
              label: t('工序名称'),
              key: 'showName',
            },
            {
              label: t('所属工艺'),
              key: 'processName',
            },
            {
              label: t('所属产线'),
              key: 'productLineName',
            },
          ]"></ModalItem>
      </div>
    </div>
  </div>
</template>

<script setup>
  import ModalTitle from '@/pages/BaiE/components/ModalComponents/ModalTitle.vue';
  import ModalItem from './ModalItem.vue';
  import { t } from '@bmos/i18n';
  const props = defineProps({
    processList: {
      type: Array,
      default: () => [],
    },
  });
  const emit = defineEmits(['close']);
  const list = computed(() => {
    return props.processList.map(item => {
      return {
        ...item,
        currentBatchNo: item.pendingProductProcedureVOS
          ? item.pendingProductProcedureVOS
              .map(i => {
                return `${i.productName}-${i.batchNo}`;
              })
              .join(',')
          : '-',
      };
    });
  });
  const close = () => {
    emit('close');
  };
</script>

<style lang="less" scoped>
  .margin-b-10 {
    margin-bottom: 10px;
  }
  .process-info-detail {
    position: fixed;
    top: calc(50% - 290px);
    left: calc(50% - 220px);
    width: 470px;
    .process-info-detail-title {
      width: 100%;
      height: 40px;
    }
    .process-info-detail-content {
      width: 100%;
      min-height: 160px;
      max-height: 590px;
      overflow-y: auto;
      padding: 10px;
      box-sizing: border-box;
      background: rgba(31, 37, 51, 0.9);
      border-width: 0px 1px 1px 1px;
      border-style: solid;
      border-color: #364159;
      border-radius: 0px 0px 8px 8px;
      color: #fff;
      .process-item {
        width: 100%;
        min-height: 260px;
        padding: 12px 10px;
        box-sizing: border-box;
        background-image: url('@/assets/baiePng/process-item-bg.png');
        background-size: 100% 100%;
      }
    }
  }
</style>
