<template>
  <div class="room-detail">
    <ModalTitle :title="t('房间信息')" icon="room" @close="close"></ModalTitle>
    <div class="room-detail-content">
      <ModalItem
        :item="data"
        :fields="[
          {
            label: '房间名称',
            key: 'name',
          },
          {
            label: '房间编码',
            key: 'code',
          },
          {
            label: '洁净等级',
            key: 'clearLevel',
          },
        ]"></ModalItem>
    </div>
  </div>
</template>

<script setup>
  import ModalTitle from '@/pages/BaiE/components/ModalComponents/ModalTitle.vue';
  import ModalItem from './ModalItem.vue';
  import { t } from '@bmos/i18n';
  import { roomMap } from '@/pages/BaiE/consts/roomConst';
  const props = defineProps({
    modelId: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits(['close']);
  const data = computed(() => {
    return roomMap.value.get(props.modelId) || {};
  });
  const close = () => {
    emit('close');
  };
</script>

<style lang="less" scoped>
  .room-detail {
    position: fixed;
    top: calc(50% - 135px);
    left: calc(50% - 170px);
    width: 340px;
    .room-detail-title {
      width: 100%;
      height: 40px;
    }
    .room-detail-content {
      width: 100%;
      height: 230px;
      padding: 18px 20px;
      box-sizing: border-box;
      background: rgba(31, 37, 51, 0.9);
      border-width: 0px 1px 1px 1px;
      border-style: solid;
      border-color: #364159;
      border-radius: 0px 0px 8px 8px;
      color: #fff;
      :deep(.process-item-line) {
        margin: 15px 0;
      }
    }
  }
</style>
