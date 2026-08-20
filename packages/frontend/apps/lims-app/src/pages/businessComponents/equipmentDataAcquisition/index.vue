<template>
  <view class="container">
    <BmosNavBar @left-click="toBack">
      <template #left>
        <view class="left-content">
          <uv-icon
            color="#797C80"
            name="fanhui"
            size="14.07rpx"
            custom-prefix="bmos-icon"
          />
          <text class="title">{{ t("返回") }}</text>
        </view>
      </template>
    </BmosNavBar>
    <view class="content">
      <BmosNoData
        type="emptyHistoryBatches"
        :text="t('设备码无法识别')"
        :position="false"
      />
      <view class="button-box">
        <BmosButton
          type="primary"
          size="large"
          :text="t('重新扫码')"
          @click="reScanCode"
        />
        <BmosButton
          type="default"
          size="large"
          :text="t('选择设备')"
          @click="chooseEquipment"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import BmosNavBar from '@/components/BmosNavBar/index.vue';
  import BmosNoData from '@/components/BmosNoData/index.vue';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { onLoad } from '@dcloudio/uni-app';
  import { ref } from 'vue';
  import { reqPlatformEquipmentAppQueryEquipmentIdApi } from '@/api';
  import { useScan } from '@/utils/useScan.js';
  const { bmosScanCode } = useScan();
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  const queryInfo = ref({});
  // 选择设备
  const chooseEquipment = () => {
    const query = Object.keys(queryInfo.value)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(queryInfo.value[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/businessComponents/equipmentDataAcquisition/equipmentSelection/index?${query}`
    });
  };

  const reScanCode = () => {
    bmosScanCode({
      success: async res => {
        const { result } = res;
        if (!result) {
          return;
        }
        const type = result.slice(0, 2);
        const code = result.slice(2);
        if (type !== '04' || !code) {
          return;
        }
        const { data } = await reqPlatformEquipmentAppQueryEquipmentIdApi(code);
        if (data) {
          const params = {
            equipmentId: data,
            ...queryInfo.value,
            returnData: 2
          };
          const query = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
          if (queryInfo.value.componentType === 'EQUIPMENT_INFO') {
            uni.navigateTo({
              url: `/pages/businessComponents/equipmentInfo/index?${query}`
            });
            return;
          } else if (queryInfo.value.componentType === 'EQUIPMENT_DATA_ACQUISITION') {
            uni.navigateTo({
              url: `/pages/businessComponents/equipmentDataAcquisition/dataAcquisition/index?${query}`
            });
          }
          return;
        }
      },
      fail: err => {
        console.log('扫码失败', err);
      }
    });
  };

  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    queryInfo.value = query;
    // #endif
    // #ifdef H5
    queryInfo.value = e;
    // #endif
  });
</script>

<style lang="scss" scoped>
.container {
  padding-top: 46.89rpx;
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  background: #ffffff;

  .left-content {
    display: flex;

    .title {
      font-size: 15.24rpx;
      font-weight: 500;
      line-height: 22.27rpx;
      letter-spacing: 0em;
      color: #18191a;
      margin-left: 14.65rpx;
    }
  }

  .right-content {
    font-size: 15.24rpx;
    color: #2871ff;
  }
  .content {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .button-box {
      margin-top: 17.58rpx;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      width: 187.5rpx;
      row-gap: 9.38rpx;
    }
  }
}
</style>
