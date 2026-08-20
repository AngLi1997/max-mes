<!-- 称量数据-称量结果 -->
<template>
  <BMLayout>
    <BMBasicPage
      :title="t('称量结果')"
      :show-buttons="false"
      @left-click="leftClick"
    >
      <template #titleRight>
        <wd-button type="text" @click="exitWeighing">
          {{ t("退出称量") }}
        </wd-button>
      </template>
      <view class="weighing-result-content">
        <view class="table-box">
          <BMTable ref="tableRef" v-bind="tableProps" />
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import {
    BMBasicPage,
    BMLayout,
    BMTable,
  } from '@/BMComponents';
  import { t } from '@/utils/useBmosI18n.js';
  import { useResult } from './hooks/useResult.jsx';

  const props = defineProps({
    id: {
      type: String,
      default: '',
  }
  });
  const {
    tableRef,
    tableProps,
    leftClick,
    exitWeighing
  } = useResult({ props });
</script>

  <style lang="scss" scoped>
  .weighing-result-content{
    height: 100%;

    .table-box {
      height: calc(100% - 11.72rpx);
    }
  }
  </style>
