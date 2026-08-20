<template>
  <BMLayout>
    <BMBasicPage :title="t('领料计划')" :default-padding="false" @left-click="toBack" @cancel="toBack" @confirm="finish">
      <template #titleRight>
        <span class="right-title">{{ `${t('领料单')}：${current.currentList?.name}` }}</span>
      </template>
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
          <wd-tabs v-model="tabSub.index" class="type-tab" @change="({index})=> tabChange(index)">
            <wd-tab v-for="item in tabSub.list" :key="item.name" :title="item.name" />
          </wd-tabs>
          <view
            class="actions"
            :style="{ 'justify-content': `${tabSub.index === 1 ? 'space-between' : 'flex-end'}` }"
          >
            <view v-if="tabSub.index === 1" class="material-total">
              <view class="total-title">{{ t('合计') }}</view>
              <view class="item">
                <view class="title">{{ t('待领量') }}：</view>
                <view class="value">
                  {{
                    `${Number(pendingQuantity) > 0 ? pendingQuantity : '-'}${current.currentList?.unitName || '-'}`
                  }}
                </view>
              </view>
              <view class="item no-border-right">
                <view class="title">{{ t('领料量') }}：</view>
                <view class="value">
                  {{
                    `${Number(totalPlannedQuantity) || '-'}${current.currentList?.unitName || '-'}`
                  }}
                </view>
              </view>
            </view>
            <view><wd-button @click="toMaterial">{{ tabSub.index === 1 ? t('添加物料') : t('预定物料') }}</wd-button></view>
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
      <view class="tip">{{ t("是否移除当前物料") }}</view>
    </BMModal>
  </BMLayout>
</template>

<script setup>
  import {
    t
  } from '@/utils/useBmosI18n.js';
  import { BMLayout, BMSignModal, BMBasicPage, BMDataInfoDisplay, BMTable, BMModal } from '@/BMComponents';
  import {
    onLoad,
    onShow
  } from '@dcloudio/uni-app';
  import {
    useSubTab,
    useTable,
    useColumns,
    useModel
  } from './hooks';
  import { useNotify } from 'wot-design-uni';

  const { showNotify } = useNotify();
  const UseColumns = useColumns();
  const {
    tabSub,
    current,
    isCur,
    refreshPage,
    signatureData,
    orderQuantity,
    paramsData,
    pendingQuantity,
    totalPlannedQuantity,
    completedPlan
  } = UseColumns;
  const UseTable = useTable({
    UseColumns,
    showNotify
  });
  const { 
    tableRef,
    tableProps,
    tableData,
    getPage,
    showDeletePopup,
    confirmDeletePopup,
    cancelDeletePopup
  } = UseTable;
  const UseSubTab = useSubTab({
    UseTable,
    UseColumns,
    showNotify
  });
  const { splitSigning, change, tabChange, toMaterial, reqDetailApi } = UseSubTab;
  const UseModel = useModel({
    UseColumns
  });
  const { labelList, showSign, signValue, getUserList, signSubmit, submit } = UseModel;
  // 完成
  const finish = () => {
    if (completedPlan.value) {
      showNotify({
        type: 'warning',
        message: t('领料计划已完成')
      });
      return;
    }
    submit();
  };
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e).map((key) => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    paramsData.value = query;
    await reqDetailApi();
    // #endif
    // #ifdef H5
    paramsData.value = e;
    await reqDetailApi();
    // #endif

    getUserList();
  });
  onShow(() => {
    if (refreshPage.value) {
      getPage();
      refreshPage.value = false;
    }
  });
</script>

<style lang="scss" scoped>
.right-title {
  font-size: var(--bmos-font-size-sub);
  color: var(--bmos-color-text-sub);
}
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
