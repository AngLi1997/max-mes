<template>
  <BMLayout>
    <BMBasicPage :title="t('配料计划')" :confirm-text="t('完成')" :default-padding="false" @left-click="toBack" @cancel="toBack" @confirm="submit">
      <template #titleRight>
        <span class="right-title">{{ `${t('配料单')}: ${ingredientName}` }}</span>
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
                label: t('配料总量'),
                field: 'bookedQuantity',
              },
            ]"
            :info-data="{
              materialCode: {
                value: `${current.currentList?.materialMergeCode || '-'}-${current.currentList?.materialName || '-'}`,
              },
              theoreticalDosage: {
                value: `${current.currentList?.theoreticalQuantity || '-'}${current.currentList?.unitName || '-'}`,
                waring: Number(current.currentList?.theoreticalQuantity) > Number(ingredientQuantity),
                success: Number(current.currentList?.theoreticalQuantity) <= Number(ingredientQuantity),
              },
              bookedQuantity: {
                value: `${ingredientQuantity ||
                  '-'}${current.currentList?.unitName
                  || '-'}`,
              },
            }"
          />
          <view
            class="actions"
            style="justify-content:end"
          >
            <view><wd-button @click="toMaterial">{{ t('添加物料') }}</wd-button></view>
          </view>
          <view class="table-content">
            <BMTable ref="tableRef" v-bind="tableProps" :data="tableData" />
          </view>
        </view>
      </view>
    </BMBasicPage>
    <!-- 签名-->
    <BMSignModal 
      v-model:show="showSign" 
      v-model="signValue" 
      :title="t('签名')"
      :label-list="labelList"
      :show-remark="true"
      :signature-data="signatureData"
      @confirm="signSubmit"
    />
  </BMLayout>
</template>

<script setup>
  import {
    t
  } from '@/utils/useBmosI18n.js';
  import {
    onLoad,
    onShow,
    onUnload
  } from '@dcloudio/uni-app';
  import {
    useSubTab,
    useTable,
    useCommon,
    useModel
  } from './hooks';
  import { BMLayout, BMSignModal, BMBasicPage, BMDataInfoDisplay, BMTable } from '@/BMComponents';

  import { useToast } from 'wot-design-uni';
  const toast = useToast();
  const UseCommon = useCommon();
  const {
    current,
    paramsData,
    ingredientQuantity,
    signValue,
    signatureData
  } = UseCommon;
  const UseTable = useTable({
    UseCommon
  });
  const {
    tableRef,
    tableProps,
    tableData,
    estIngredientPlan
  } = UseTable;
  const UseSubTab = useSubTab({
    UseCommon,
    UseTable,
    toast
  });
  const {
    splitSigning,
    change,
    toMaterial,
    subDetails,
    ingredientName
  } = UseSubTab;
  const UseModel = useModel({
    UseCommon,
    UseTable,
    toast
  });
  const {
    labelList,
    showSign,
    submit,
    signSubmit,
    getUserList
  } = UseModel;
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    paramsData.value = query;
    await subDetails();
    // #endif
    // #ifdef H5
    paramsData.value = e;
    await subDetails();
    // #endif

    getUserList();
  });
  onShow(() => {
    uni.$on('IngredientPlan', estIngredientPlan);
  });
  onUnload(() => {
    uni.$off('IngredientPlan', estIngredientPlan);
  });
</script>

<style lang="scss" scoped>
// 新ui样式
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
</style>
