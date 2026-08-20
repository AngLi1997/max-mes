<template>
  <BMBasicPage
    :title="t('配液单详情')"
    :show-buttons="false"
    @left-click="leftClick"
  >
    <view class="liquid-sheet-content">
      <Info
        icon="xinxi"
        :basic-items="infoItems"
        :info-data="liquidSheetDetail"
      />
      <view class="title-box">
        {{ t("配料信息") }}
      </view>
      <view class="table-box">
        <BMTable ref="tableRef" v-bind="tableProps" />
      </view>
    </view>
    <!--配液单选择-->
    <BMRadioModal
      v-model="liquidSheetValue"
      v-model:open="showLiquidSheet"
      :title="t('配液单选择')"
      :field-names="{
        label: 'name',
        value: 'id',
      }"
      required
      :options="liquidSheetOptions"
      @confirm="onLiquidSheetConfirm"
    />
  </BMBasicPage>
  <wd-notify :safe-height="90" />
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import Info from '@/pages/weighingComponents/info';
  import { BMBasicPage, BMTable, BMRadioModal } from '@/BMComponents';
  import { useDetail } from './hooks/useDetail.jsx';

  const props = defineProps({
    id: {
      type: String,
      default: ''
    },
    liquidPreparationId: {
      type: String,
      default: ''
    },
    switch: {
      type: String,
      default: 'true'
    }
  });
  const {
    liquidSheetDetail,
    infoItems,
    tableRef,
    tableProps,
    showLiquidSheet,
    liquidSheetValue,
    liquidSheetOptions,
    leftClick,
    onLiquidSheetConfirm
  } = useDetail({ props });
</script>

<style lang="scss" scoped>
.liquid-sheet-content {
  height: 100%;

  .title-box {
    color: var(--bmos-color-text-title);
    font-size: 14.06rpx;
    margin: 9.38rpx auto;
  }
  .table-box {
    height: calc(100% - 32.81rpx - 35.16rpx - 5.86rpx);
  }
}
</style>
