<template>
  <view class="box">
    <view class="title_box" :style="{ background: resultsData.status.value === 1 ? '#ebf2ff' : '#DCF2EB' }">
      <view class="title">
        {{ resultsData.materialMergeCode }}-{{ resultsData.materialName }}
      </view>
      <wd-tag :type="statusTypes[resultsData.status.value]">
        {{ resultsData.status.label }}
      </wd-tag>
    </view>
    <BMInfoDisplay
      :is-show-title="false"
      is-show-one
      :basic-items="[
        {
          label: t('请验单号'),
          field: 'inspectNo',
        },
        {
          label: t('物料批号'),
          field: 'materialBatchNo',
        },
        {
          label: t('请验人'),
          field: 'inspector',
        },
        {
          label: t('请验时间'),
          field: 'inspectTime',
        },
      ]"
      :info-data="resultsData"
    />
    <view class="btn_box">
      <wd-button type="text" @click="toResults">
        {{ t('检验结果') }}
      </wd-button>
      <view class="line" />
      <wd-button type="text" @click="toDetail">
        {{ t('请验详情') }}
      </wd-button>
    </view>
  </view>
</template>

<script setup>
import { BMInfoDisplay } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';

const props = defineProps({
  resultsData: {
    type: Object,
    default: () => {},
  },
});
const emit = defineEmits(['toResult', 'toDetail']);
const statusTypes = {
  1: 'primary',
  2: 'success',
};
// 打开请验结果页
const toResults = () => {
  emit('toResult', props.resultsData);
};
// 打开请验详情页
const toDetail = () => {
  emit('toDetail', props.resultsData);
};
</script>

<style lang="scss">
.box {
  width: calc(50% - 4.69rpx);
  min-height: 117.19rpx;
  border: 0.59rpx solid #e1e3e5;
  border-radius: 4.69rpx;
  box-sizing: border-box;
  .title_box {
    padding: 9.38rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .title {
      width: 85%;
      font-size: 14.06rpx;
      line-height: 16.41rpx;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: #242526;
    }
  }
  :deep(.info-item) {
    margin-top: 4.69rpx !important;
  }
  .btn_box {
    margin: 0 9.38rpx;
    border-top: 1px solid #e1e3e5;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 4.69rpx 0;
  }
  .line {
    width: 0.59rpx;
    height: 14.06rpx;
    background-color: #e1e3e5;
  }
}
</style>
