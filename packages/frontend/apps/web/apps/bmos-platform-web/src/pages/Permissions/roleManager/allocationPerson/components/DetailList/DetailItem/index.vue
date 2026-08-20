<template>
  <div class="detail-item">
    <span class="detail-item-title">{{ item.title }}</span>
    <span
      class="detail-item-icon"
      v-if="!isView"
      :data-item_id="item.id"
      @click="e => ((e.target as HTMLElement).dataset.itemId = item.id)">
      <component :is="CloseCircleOutlined"></component>
   </span>
  </div>
</template>

<script setup lang="ts" generic="T = Record<any, any>">
  import { DetailItemEmits, DetailItemProps } from '../../props/detailItem';
  import { DetailItemPropsType } from '../../types';
  import { CloseCircleOutlined } from '@ant-design/icons-vue';
  const emit = defineEmits(DetailItemEmits);
  withDefaults(defineProps<DetailItemPropsType>(), DetailItemProps);
</script>

<style scoped lang="less">
  .detail-item {
    line-height: 20px;
    padding-block: 5px;
    padding-inline: 16px;
    transition: all 0.2s linear;
    &:hover {
      background-color: #ebf1ff;
      cursor: pointer;
    }
  }
  .detail-item-icon {
    color: #6c7380;
    transition: all 0.2s linear;
    &:hover {
      color: #2871ff;
    }
  }
  .detail-item {
    display: flex;
    justify-content: space-between;
    width: 100%;
  }
</style>
