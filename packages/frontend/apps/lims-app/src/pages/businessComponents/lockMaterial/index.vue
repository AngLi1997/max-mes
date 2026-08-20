<template>
  <BMLayout>
    <BMBasicPage
      :title="t('预定暂存物料')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <view class="container">
        <BMDataInfoDisplay
          :basic-items="[
            {
              label: t('物料信息'),
              field: 'materialCode',
            },
            {
              label: t('理论用量'),
              field: 'theoreticalDosage',
            },
            {
              label: t('已预定暂存量'),
              field: 'bookedQuantity',
            },
            {
              label: t('待添加量'),
              field: 'toBeAddedQuantity',
            },
          ]"
          :info-data="{
            materialCode: {
              value: `${currentList?.materialMergeCode || '-'}-${currentList?.materialName || '-'}`,
            },
            theoreticalDosage: {
              value: `${currentList?.theoreticalQuantity || '-'}${currentList?.unitName || '-'}`,
              waring: Number(currentList?.theoreticalQuantity) > Number(params.selectTotal),
              success:Number(currentList?.theoreticalQuantity) <= Number(params.selectTotal),
            },
            bookedQuantity: {
              value: `${params.selectTotal || '-'}${currentList?.unitName || '-'}`,
            },
            toBeAddedQuantity: {
              value: `${params.wholeTotal || '0'}${currentList?.unitName || '-'}`,
            },
          }"
        />
        <view class="table-content">
          <BMTable ref="tableRef" v-bind="tableProps" :data="tableData" @selection-change="selectionChange" />
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import {
    t
  } from '@/utils/useBmosI18n.js';
  import {
    onLoad
  } from '@dcloudio/uni-app';
  import {
    useSubTab,
    useTable
  } from './hooks';
  import { BMBasicPage, BMDataInfoDisplay, BMLayout, BMTable } from '@/BMComponents/index.js';
  import { useNotify } from 'wot-design-uni';

  const { showNotify } = useNotify();
  const toBack = () => {
    uni.navigateBack();
  };
  const UseSubTab = useSubTab();
  const {
    params,
    currentList
  } = UseSubTab;
  const UseTable = useTable({
    UseSubTab,
    showNotify
  });
  const {
    tableRef,
    tableProps,
    tableData,
    selectionChange,
    materialApi,
    submit
  } = UseTable;
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    currentList.value = query;
    await materialApi();
    // #endif
    // #ifdef H5
    currentList.value = e;
    await materialApi();
    // #endif
  });
</script>

<style lang="scss" scoped>
	.container {
		height: 100%;
		display: flex;
		flex-direction: column;
		gap: 8.2rpx;
		.table-content {
      flex: 1;
      overflow: hidden;
    }
	}
</style>
