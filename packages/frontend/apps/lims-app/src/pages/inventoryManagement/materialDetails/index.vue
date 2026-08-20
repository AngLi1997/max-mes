<template>
  <BMLayout>
    <BMBasicPage
      :title="t('查询物料')"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="container">
        <view class="content">
          <InfoTable style="height:100%;overflow:hidden;" :details="details" :data="detailsApiList" :title="t('物料信息')" />
        </view>

        <view class="flex-bottom">
          <template v-for="(item, index) in tagKeys" :key="index">
            <wd-button
              v-show="item.isShow"
              :type="item.type"
              block
              custom-class="tab-th"
              @click="item.onClick(item)"
            >
              {{ item.title }}
            </wd-button>
          </template>
        </view>
        <BmosPrinter ref="bmosPrinterInstance" />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMLayout } from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import InfoTable from '@/pages/inventoryManagement/components/infoTable/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useDetails } from './hooks';

const data = useDetails();
const { details, tagKeys, detailsApi, bmosPrinterInstance, detailsApiList } = data;

const initId = ref('');
// 返回
const toBack = () => {
  const query = initId.value || '';
  console.log(query);
  uni.reLaunch({
    url: `/pages/inventoryManagement/inventoryInfo/index?materialPositionId=${query}`,
  });
};
onLoad(async (e) => {
  await detailsApi(e?.materialNo);

  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e || '').map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e?.[key]),
    ]),
  );
  initId.value = query?.goodsLocation;
  // #endif
  // #ifdef H5
  initId.value = e?.goodsLocation;
  // #endif
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  .content {
    overflow: hidden;
    height: 100%;
  }
  .flex-bottom {
    flex-shrink: 0;
    width: 100%;
    padding: 9.38rpx 0;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    column-gap: 14.07rpx;
    row-gap: 9.38rpx;
    .tab-th {
      width: 100%;
    }
  }
}
</style>
