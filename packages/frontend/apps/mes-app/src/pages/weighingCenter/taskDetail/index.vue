<template>
  <BMBasicPage
    :title="t('任务详情')"
    :show-buttons="false"
    @left-click="leftClick"
  >
    <view class="task-detail-content">
      <BMInfoDisplay
        :title="t('称量信息')"
        :basic-items="infoItems"
        :info-data="detailData"
        icon="xinxi"
        background="var(--bmos-bg-form)"
      />
      <view class="data-info">
        <BMDataInfoDisplay
          :basic-items="dataInfoItems"
          :info-data="dataInfoData"
          background="var(--bmos-bg-form)"
        />
      </view>
      <view class="table-box">
        <BMTable ref="tableRef" v-bind="tableProps" />
      </view>
    </view>
  </BMBasicPage>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { onShow } from '@dcloudio/uni-app';
  import {
    BMBasicPage,
    BMInfoDisplay,
    BMDataInfoDisplay,
    BMTable
  } from '@/BMComponents';
  import { useDetail } from './hooks/useDetail.jsx';
  const props = defineProps({
    id: {
      type: String,
      default: ''
    }
  });
  const {
    infoItems,
    detailData,
    dataInfoItems,
    dataInfoData,
    tableRef,
    tableProps,
    leftClick,
    getWeighCenterExecuteTaskById
  } = useDetail({ props });
  onShow(() => {
    getWeighCenterExecuteTaskById();
  });
</script>

<style lang="scss" scoped>
.task-detail-content {
  height: 100%;
  .data-info {
    margin: 9.38rpx 0;
  }
  .table-box {
    height: calc(100% - 60.94rpx - 46.88rpx - 18.75rpx - 9.38rpx);
  }
}
</style>
