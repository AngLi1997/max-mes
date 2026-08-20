<template>
  <BMLayout>
    <BMBasicPage
      :title="t('设备数采绘图')"
      background-color="#F2F3F5"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="content">
        <img :src="url" alt="">
        <view class="msg_box" style="line-height: normal;display: flex;align-items: center;justify-content: space-between;flex-wrap: wrap;">
          <view>{{ t('设备信息') }}：{{ queryInfo.equipmentInfo }}</view>
          <view>{{ t('设备数据') }}：{{ queryInfo.equipmentData }}</view>
          <view>{{ t('采集人') }}：{{ queryInfo.acquisitionUser }}</view>
          <view>{{ t('采集时间') }}：{{ queryInfo.acquisitionTime }}</view>
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMLayout } from '@/BMComponents';
import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';

const queryInfo = ref({});
const url = ref('');

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  queryInfo.value = JSON.parse(queryInfo.value.value);
  const baseUrl = `http://${
    getStorageSync(IP_CONFIG) || '172.30.1.160:80'
  }/`;
  url.value = baseUrl + queryInfo.value.url;
});
// 返回
const toBack = () => {
  uni.navigateBack();
};
</script>

<style scoped lang="scss">
  .content{
    margin: 9.38rpx;
    background-color: #fff;
    text-align: center;
    padding: 9.38rpx 0;
    border-radius: 4.69rpx;
    img {
      width: calc(100% - 18.75rpx);
    }
    .msg_box{
      margin: 0 9.38rpx;
    }
  }
</style>
