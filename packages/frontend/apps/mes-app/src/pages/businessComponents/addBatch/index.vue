<template>
  <BMLayout>
    <BMBasicPage
      :title="t('添加物料批次')"
      :confirm-text="t('完成')"
      :cancel-text="t('取消')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <div class="container">
        <BMDataInfoDisplay
          :title="t('物料信息')"
          icon="xinxi"
          background="#F7F8FA"
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
            {
              label: t('配料总量'),
              field: 'toBeAddedQuantity',
            },
          ]"
          :info-data="{
            materialCode: {
              value: `${currentList?.materialMergeCode || '-'}-${currentList?.materialName || '-'}`,
            },
            theoreticalDosage: {
              value: `${currentList?.theoreticalQuantity
                || '-'}${currentList?.unitName
                || '-'}`,
              waring: Number(currentList?.theoreticalQuantity) > Number(theoryAmount),
              success: Number(currentList?.theoreticalQuantity) <= Number(theoryAmount),
            },
            bookedQuantity: {
              value: `${theoryAmount || '-'}${currentList?.unitName || '-'}`,
            },
            toBeAddedQuantity: {
              value: `${orderQuantity || '-'}${currentList?.unitName || '-'}`,
            },
          }"
        />
        <view class="table-content">
          <BMTable ref="tableRef" v-bind="tableProps" @selection-change="selectionChange" />
        </view>
      </div>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMDataInfoDisplay, BMLayout, BMTable } from '@/BMComponents'
import {
  t,
} from '@/utils/useBmosI18n.js'
import {
  onLoad,
} from '@dcloudio/uni-app'
import {
  useSubTab,
  useTable,
} from './hooks'

const toBack = () => {
  uni.navigateBack()
}
const UseSubTab = useSubTab()
const {
  currentList,
  theoryAmount,
  orderQuantity,
} = UseSubTab
const UseTable = useTable({
  UseSubTab,
})
const {
  tableRef,
  tableProps,
  selectionChange,
  materialList,
  submit,
} = UseTable
onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]))
  currentList.value = query
  await materialList()
  // #endif
  // #ifdef H5
  currentList.value = e
  await materialList()
  // #endif
})
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
