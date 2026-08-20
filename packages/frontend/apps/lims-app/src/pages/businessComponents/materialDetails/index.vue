<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料详情')"
      :confirm-text="t('接收物料')"
      :cancel-text="t('返回')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="receiveMaterial"
    >
      <div class="content">
        <BMInfoDisplay
          :title="t('物料信息')"
          icon="xinxi"
          :background="'#F7F8FA'"
          :basic-items="[
            {
              label: t('物料名称'),
              field: 'name',
            },
            {
              label:t('物料信息'),
              field: 'code',
            },
            {
              label: t('物料批号'),
              field: 'batch',
            },
            {
              label: t('总出库量'),
              field: 'total',
            },
          ]"
          :info-data="{
            name: paramsData.materialName,
            code:paramsData.materialMergeCode,
            batch:paramsData.materialBatchNo,
            total: `${outboundQuantity}${paramsData.unitName}`,
          }"
        />
        <view class="table-container">
          <BMTable ref="tableRef" v-bind="tableProps" @selection-change="selectionChange" />
        </view>
      </div>
    </BMBasicPage>

    <!-- 签名 -->
    <BMModal
      v-model="showSign"
      :title="t('接收物料')"
      size="large" 
      @cancel="showSign = false"
      @confirm="signSubmit"
    >
      <view style="height: 280.08rpx">
        <BMForm ref="formRef" v-bind="formProps" />
        <BMSign
          ref="signRef"
          v-model="signValue"
          :label-list="labelList"
          :signature-data="signatureData"
          size="medium"
        />
      </view>
    </BMModal>
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="skipPrinter" />
    <BMModal
      v-model="showSurePopup"
      :show-title="false"
      size="small"
      custom-class="tip-popup"
      :close-on-click-modal="false"
      :confirm-text="t('确定')"
      @confirm="confirmSurePopup"
      @cancel="cancelSurePopup"
    >
      <view class="tip">{{ t("物料件未接收是否返回") }}</view>
    </BMModal>
  </BMLayout>
</template>

<script setup>
  import {
    t
  } from '@/utils/useBmosI18n.js';
  import BmosPrinter from '@/components/BmosPrinter/index.vue';
  import {
    useSubTab,
    useTable,
    useModal
  } from './hooks';
  import {
    onLoad
  } from '@dcloudio/uni-app';
  import { ref } from 'vue';

  import { BMLayout, BMBasicPage, BMInfoDisplay, BMForm, BMSign, BMTable, BMModal } from '@/BMComponents';
  const UseSubTab = useSubTab();
  const {
    seg,
    paramsData,
    signatureData
  } = UseSubTab;
  const UseTable = useTable({
    UseSubTab
  });
  const {
    tableRef,
    tableProps,
    isModifySelected,
    outboundQuantity,
    selectionChange,
    apiDetailsList
  } = UseTable;
  const UseModal = useModal({ UseSubTab, UseTable });
  const {
    formRef,
    formProps,
    signRef,
    bmosPrinterInstance,
    labelList,
    showSign,
    signValue,
    receiveMaterial,
    signSubmit,
    skipPrinter
  } = UseModal;
  const showSurePopup = ref(false);

  // 返回
  const toBack = () => {
    if (isModifySelected.value.length > 0) {
      showSurePopup.value = true;
    } else {
      uni.navigateBack();
    }
  };
  const confirmSurePopup = () => {
    uni.navigateBack();
    showSurePopup.value = false;
  };
  const cancelSurePopup = () => {
    showSurePopup.value = false;
  };
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    paramsData.value = query;
    await apiDetailsList();
    // #endif
    // #ifdef H5
    paramsData.value = e;
    await apiDetailsList();
    // #endif
  });
</script>

<style lang="scss" scoped>
.content{
	display: flex;
	flex-direction: column;
	height: 100%;
	.table-container{
		flex: 1;
		overflow:hidden;
	margin-top: 9.38rpx;
}
}
:deep(.confirm) {
  // pointer-events: none; //看得见 摸不着
  opacity: 0.5; /* 置灰效果，不过仍可见 */
  .uni-table-checkbox .checkbox__inner.is-checked{
    background-color:#c3c5c8;
  }
}
:deep(.confirm .checkbox .uni-table-checkbox .checkbox__inner .checkbox__inner-icon){
    border-color: #464141;
  }

:deep(.tip-popup .modal-container .modal-content) {
      min-height: 44.53rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14.07rpx;
  }

</style>
