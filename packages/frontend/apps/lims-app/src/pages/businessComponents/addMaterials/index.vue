<template>
  <BMLayout>
    <BMBasicPage
      :title="t('添加物料')"
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
              label: t('已选理论量'),
              field: 'bookedQuantity',
            },
          ]"
          :info-data="{
            materialCode: {
              value: `${currentList?.materialMergeCode || '-'}-${currentList?.materialName || '-'}`,
            },
            theoreticalDosage: {
              value: `${Number(currentList?.pendingQuantityNum) > 0 ?
                currentList?.pendingQuantityNum : '-'}${currentList?.unitName || '-'}`,
              waring: Number(currentList?.pendingQuantityNum) > Number(orderQuantity),
              success:Number(currentList?.pendingQuantityNum) <= Number(orderQuantity),
            },
            bookedQuantity: {
              value: `${Number(orderQuantity) || '-'}${currentList?.unitName ||
                '-'}`,
            },
          }"
        />
        <view class="table-content">
          <BMTable ref="tableRef" v-bind="tableProps" :data="tableData" @selection-change="selectionChange" @update-row="updateRow" />
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
    useSubTab,
    useTable
  } from './hooks';
  import {
    onLoad
  } from '@dcloudio/uni-app';
  import { BMBasicPage, BMDataInfoDisplay, BMLayout, BMTable } from '@/BMComponents/index.js';
  import { useNotify } from 'wot-design-uni';

  const { showNotify } = useNotify();

  const toBack = () => {
    uni.navigateBack();
  };
  const UseSubTab = useSubTab();
  const {
    currentList,
    orderQuantity
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
    materialList,
    submit,
    updateRow
  } = UseTable;
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    currentList.value = query;
    await materialList();
    // #endif
    // #ifdef H5
    currentList.value = e;
    await materialList();
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
