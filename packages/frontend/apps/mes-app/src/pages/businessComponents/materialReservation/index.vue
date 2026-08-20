<template>
  <BMLayout>
    <BMBasicPage
      :title="confirmBefore ? t('生产前确认') : t('物料预定')"
      :confirm-text="t('完成')" :default-padding="false"
      :all-padding="confirmBefore ? false : true"
      :show-buttons="confirmBefore ? false : true"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="toBack"
    >
      <view class="container">
        <scroll-view class="subTitle" scroll-y="true">
          <wd-sidebar v-model="current.active" @change="change">
            <wd-sidebar-item
              v-for="item in splitSigning"
              :key="item.id"
              :value="item.id"
              :label="item.materialName"
            />
          </wd-sidebar>
        </scroll-view>
        <view class="content">
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
            ]"
            :info-data="{
              materialCode: {
                value: `${current.currentList?.materialMergeCode || '-'}-${current.currentList?.materialName || '-'}`,
              },
              theoreticalDosage: {
                value: `${current.currentList?.theoreticalQuantity || '-'}${current.currentList?.unitName || '-'}`,
                waring: Number(current.currentList?.theoreticalQuantity) > Number(isCur),
                success: Number(current.currentList?.theoreticalQuantity) <= Number(isCur),
              },
              bookedQuantity: {
                value: `${Number(orderQuantity) || '-'}${current.currentList?.unitName || '-'}`,
              },
            }"
          />
          <view
            class="actions"
            style="justify-content:space-between"
          >
            <view>{{ t('预定物料信息') }}</view>

            <view>
              <wd-button @click="toMaterial">
                {{ t('预定物料') }}
              </wd-button>
            </view>
          </view>
          <view class="table-content">
            <BMTable ref="tableRef" v-bind="tableProps" :data="tableData" />
          </view>
        </view>
      </view>
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :title="t('签名')"
      :label-list="labelList"
      :signature-data="signatureData"
      @confirm="signSubmit"
    />
    <BMModal
      v-model="showDeletePopup"
      :show-title="false"
      size="small"
      custom-class="tip-popup"
      :close-on-click-modal="false"
      :confirm-text="t('移除')"
      @confirm="confirmDeletePopup"
      @cancel="cancelDeletePopup"
    >
      <view class="tip">
        {{ t("是否取消预定当前物料件？") }}
      </view>
    </BMModal>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMDataInfoDisplay, BMLayout, BMModal, BMSignModal, BMTable } from '@/BMComponents';
import {
  initFillData2,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  t,
} from '@/utils/useBmosI18n.js';
import {
  onLoad,
  onShow,
} from '@dcloudio/uni-app';
import {
  useColumns,
  useModel,
  useSubTab,
  useTable,
} from './hooks';

const props = defineProps({
  confirmBefore: { // 生产前确认里的
    type: Boolean,
    default: false,
  },
});
const UseColumns = useColumns();
const {
  current,
  isCur,
  refreshPage,
  signatureData,
  orderQuantity,
  paramsData,
} = UseColumns;
const UseTable = useTable({
  UseColumns,
  props,
});
const {
  tableRef,
  tableProps,
  tableData,
  getPage,
  showDeletePopup,
  confirmDeletePopup,
  cancelDeletePopup,
} = UseTable;
const UseSubTab = useSubTab({
  UseTable,
  UseColumns,
  props,
});
const {
  splitSigning,
  change,
  toMaterial,
  reqDetailApi,
} = UseSubTab;
const UseModel = useModel({
  UseColumns,
});
const {
  labelList,
  showSign,
  signValue,
  signSubmit,
} = UseModel;
  // 返回
const toBack = () => {
  if (props?.confirmBefore) {
    uni.navigateBack();
    return;
  }
  initFillData2();
  uni.navigateBack();
};
onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  paramsData.value = query;
  await reqDetailApi();
  // #endif
  // #ifdef H5
  paramsData.value = e;
  await reqDetailApi();
  // #endif
});
onShow(() => {
  if (refreshPage.value) {
    getPage();
    refreshPage.value = false;
  }
});
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  position: relative;
  display: flex;
  .subTitle {
    width: 133.06rpx;
    height: 100%;
  .wd-sidebar {
    width: 100%;
  }
  }
  .content {
    width: calc(100% - 133.06rpx);
    height: 100%;
    padding: 0 8.79rpx;
    overflow: hidden;
    display: flex;
    flex-direction: column;

  .type-tab {
    border-bottom: 1px solid var(--bmos-color-border);
  }
    .actions {
      display: flex;
      width: 100%;
      padding: 8.79rpx 0;
      align-items: center;
      gap: 17.58rpx;
>view{
  font-size: 14.06rpx;
  font-size: #18191A;
}
      .material-total {
      flex: 1;
      display: flex;
      height: 21.09rpx;
      padding: 7.03rpx 9.38rpx 7.03rpx 0;
      align-items: center;
      gap: 14.06rpx;
      flex-shrink: 0;
      border-radius: 4.69rpx;
      background: #F4F8FF;
    }
    .total-title {
      color: var(--bmos-color-primary);
      border-right: 1px solid var(--bmos-color-border);
    }
    .total-title, .item {
      display: flex;
      padding: 0 14.06rpx;
      align-items: center;
      font-size: var(--bmos-font-size-sub);
      .title {
        color: var(--bmos-color-text-sub);
      }
      .value {
        color: var(--bmos-color-text-main);
      }
    }
    .item {
      border-right: 1px solid var(--bmos-color-border);
    }
    .no-border-right {
      border-right: none;
    }
    }

    .table-content {
      flex: 1;
      overflow: hidden;
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
</style>
