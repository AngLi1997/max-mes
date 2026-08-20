<template>
  <div class="detail-list">
    <div class="detail-list-container" @click="handleItemClick">
      <DetailItem
        v-for="(item, index) in nodeList"
        :icon="icon"
        :key="index"
        :isView="isView"
        :item="item"></DetailItem>
    </div>
  </div>
</template>

<script setup lang="ts">
  import DetailItem from './DetailItem/index.vue';
  import { DetailListEmits, DetailListProps } from '../props/detailList';
  import { useDetailList } from '../hooks/useDetailList';
  import { DetailListPropsType } from '../types';
  const emit = defineEmits(DetailListEmits);
  const props = withDefaults(
    defineProps<DetailListPropsType>(),
    DetailListProps,
  );
  const { handleItemClick, nodeList } = useDetailList(emit, props);
</script>

<style scoped lang="less">
  .detail-list {
    padding-block: 16px;
    height: 100%;
    overflow: auto;
  }
  .detail-list-container {
    display: flex;
    flex-direction: column;
    row-gap: 10px;
    transition: all 1s linear;
  }
</style>
