<template>
  <BMLayout>
    <BMBasicPage
      :title="t('领料接收')"
      @left-click="toBack"
    >
      <view class="container">
        <view class="ingredients-msg">
          <view class="left">
            <view class="icon-view">
              <wd-icon name="list" size="11.72rpx" color="#2871FF" />
            </view>
            <text class="label"> {{ t("领料单") }}： </text>
            <view class="ingredients">
              {{ seg.ordeName }}
            </view>
          </view>
          <view class="right">
            <wd-button
              type="text"
              size="small"
              @click="materialOpen(true)"
            >
              {{ t('切换领料单') }}
            </wd-button>
          </view>
        </view>
        <view class="table-container">
          <view class="demo-uni-back">
            <uni-card v-for="(item, index) in formData" :key="index" :title="item.label" :class="[item.select&&!item.receiveCompleted?'select':'',item.receiveCompleted?'selectDisable':'']">
              <template #title>
                <uv-checkbox-group
                  icon-placement="right"
                  placement="column"
                  :label-size="24"
                  :size="30"
                  @change="groupChange(item)"
                >
                  <uv-checkbox
                    :checked="item.receiveCompleted"
                    :disabled="item.receiveCompleted"
                    :name="item.id"
                    :label="`${item.materialMergeCode}-${item.materialName}`"
                  />
                </uv-checkbox-group>
              </template>
              <view class="card-content">
                <view class="box-msg">
                  {{ t('批号') }}：<span :class="['ico',item.receiveCompleted?'grey':'']">{{ item.materialBatchNo || '-' }}</span>
                </view>
                <view class="box-msg">
                  <view>{{ t('出库量') }}：<span :class="['ico',item.receiveCompleted?'grey':'']">{{ item.outboundQuantity || '-' }}{{ item.unitName || '-' }}</span></view>
                </view>
                <view class="box-msg" style="justify-content:space-between">
                  <view>{{ t('货位') }}：<span :class="['ico',item.receiveCompleted?'grey':'']">{{ item.cargoPositionName || '-' }}</span></view>
                  <view class="detail" @click="detailsMap(item)">
                    {{ t("查看详情") }}
                    <uv-icon style="margin-left: 4.69rpx;" name="arrow-right" color="#2871FF" size="10.72rpx" />
                  </view>
                </view>
              </view>
            </uni-card>
          </view>
        </view>
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button type="success" block @click="complete">
              {{ t("完成") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button block @click="submit">
              {{ t("接收物料") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <!--选择领料单-->
    <BMRadioModal
      v-model="materialValueId"
      v-model:open="materialModal"
      :title="t('领料单选择')"
      :options="formColumns"
      :required="true"
      :field-names="{
        label: 'name',
        value: 'id',
      }"
      @confirm="confirm"
      @cancel="cancel"
    />
    <!-- 签名 -->
    <BMModal
      v-model="showSign"
      :title="t('接收物料')"
      size="large" 
      @cancel="showSign = false"
      @confirm="signSubmit"
    >
      <view style="height: 280.08rpx; overflow-y: hidden;">
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
      <view class="tip">{{ t("物料批次未接收是否返回") }}</view>
    </BMModal>
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="skipPrinter" />
  </BMLayout>
</template>

<script setup>
  import {
    t
  } from '@/utils/useBmosI18n';
  import BmosPrinter from '@/components/BmosPrinter/index.vue';
  import {
    useParams,
    useList,
    useModal
  } from './hooks';
  import {
    onLoad,
    onShow
  } from '@dcloudio/uni-app';
  import {
    initFillData2
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { BMBasicPage, BMForm, BMModal, BMSign, BMRadioModal, BMLayout } from '@/BMComponents/index.js';
  import { ref } from 'vue';
 
  const UseParams = useParams();
  const {
    seg,
    paramsData,
    signatureData
  } = UseParams;
  const UseList = useList({
    UseParams
  });
  const {
    needRefresh,
    checkboxValue,
    formData,
    groupChange,
    detailsMap,
    complete,
    estOute
  } = UseList;
  const UseModal = useModal({
    UseParams,
    UseList
  });
  const {
    formColumns,
    bmosPrinterInstance,
    materialValueId,
    materialModal,
    labelList,
    formRef,
    formProps,
    signRef,
    showSign,
    signValue,
    materialOpen,
    confirm,
    cancel,
    signSubmit,
    submit,
    skipPrinter
  } = UseModal;
  // 返回
  const toBack = () => {
    if (checkboxValue.value.length > 0) {
      showSurePopup.value = true;
    } else {
      uni.navigateBack();
      initFillData2();
    }
  };
  const showSurePopup = ref(false);
  const confirmSurePopup = () => {
    uni.navigateBack();
    initFillData2();
    showSurePopup.value = false;
  };
  const cancelSurePopup = () => {
    showSurePopup.value = false;
  };
  onLoad((e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    paramsData.value = query;
    materialOpen(false);
    // await reqDetailApi();
    // #endif
    // #ifdef H5
    paramsData.value = e;
    materialOpen(false);
    // await reqDetailApi();
    // #endif
  });
  onShow(() => {
    if (needRefresh.value) {
      estOute(needRefresh.value);
      needRefresh.value = false; // 刷新完成后，重置标识
    }
  });
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 9.38rpx;
  .table-container {
    flex: 1;
    // overflow: hidden;
    overflow: scroll;
  }
}
.ingredients-msg {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #F2F7FF;
  line-height: 32.81rpx;
  padding: 0 10rpx;
  border-radius: var(--bmos-border-radius-medium);
  .left {
    display: flex;
    align-items: center;
    .icon-view {
    height: 18.75rpx;
    width: 18.75rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #D9E5FF;
    border-radius: 2.34rpx;
    }
    .label {
      margin-left: 10rpx;
      color: var(--bmos-color-text-sub);
      font-size: 11.72rpx;
    }
    .ingredients {
      flex: 1;
      display: flex;
      align-items: center;
      font-size: 11.72rpx;
      .arrow-right {
        margin-left: 5rpx;
      }
    }
  }
}
.top-scan {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .wd-input  {
    width: 50%;
  }
}
// 中间选择可选内容的样式
.demo-uni-back {
			display: grid;
			grid-template-columns: repeat(2, 1fr);
			column-gap: 9.38rpx;
			row-gap: 14.07rpx;
      :deep(.uv-icon__icon){
          font-size: 16.41rpx!important;//✔的大小
        }
        :deep(.uv-checkbox__icon-wrap--square){
          border-radius: 8px;
        }
			:deep(.uni-card){
				margin: 0 !important;
				padding: 0 !important;
        border-radius: 4.69rpx;
        box-shadow: none!important;
				.uv-checkbox-group {
					padding: 9.96rpx 9.38rpx;
          background: #F2F7FF;
				}
				.uni-card__content {
					overflow: hidden;
					padding: 9.38rpx !important;
					display: flex;

					.card-content {
						display: flex;
						flex-direction: column;
						width: 100%;

						.box-msg {
							color: var(---, #6C6E73);
							font-size: 11.72rpx;
							font-style: normal;
							font-weight: 513;
							padding: 2.34rpx 0;
							display: flex;
							align-items: center;
// 信息的字体样式
							.ico {
                color: #242526;
								font-size: 11.72rpx;
							}
              // 已接收的样式
              .grey{
                color: #6C6E73;
              }
							.tab {
								margin-left: 8.79rpx;
							}
              .detail{
                display: flex;
                color: #2871FF;

              }
						}
					}
				}
			}
		}
    :deep(.tip-popup .modal-container .modal-content) {
      min-height: 44.53rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14.07rpx;
  }
  // 未接收的且选中的样式
  .select{
     border: 1px solid #2871FF;
  }
  // 已接收的背景样式
  .selectDisable.uni-card .uv-checkbox-group{
    background: #F5F6F7;
  }
</style>
