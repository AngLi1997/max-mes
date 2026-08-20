<template>
  <view class="mask">
    <view class="upload-popup-container">
      <view class="title">
        <text>{{ t('APP升级') }}</text>
      </view>
      <view class="sub-title">
        <text>{{ t('检测到APP有新的版本') }}？</text>
      </view>

      <view v-if="downing" class="process">
        <uv-line-progress :percentage="percentage" active-color="" height="20rpx" />
      </view>
      <view v-else class="button-container">
        <uv-row justify="center" gutter="10">
          <uv-col span="6">
            <BmosButton type="primary" :text="t('下载')" @click="download" />
          </uv-col>
        </uv-row>
      </view>
    </view>
  </view>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { dtask } from '@/utils/checkUpdateApp.js';
  import { ref } from 'vue';
  import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
  import { IP_CONFIG } from '@/utils/uniStorage/const.js';
  const downing = ref(0);
  const percentage = ref(0);
  const callback = function(task, status) {
    console.log('callback', task, status);

    percentage.value = parseInt(task.downloadedSize / task.totalSize * 100);

    switch (task.state) {
    case 1: // 开始
      downing.value = true;
      break;
    case 2: // 已连接到服务器
      break;
    case 3: // 已接收到数据
      break;
    case 4: // 下载完成
      downing.value = false;
      break;
    }
    // 下载完成  
    if (status === 200) {
      plus.runtime.install(plus.io.convertLocalFileSystemURL(task.filename), {}, () => {
        plus.runtime.restart();
      }, function(error) {
        uni.showToast({
          title: t('安装失败'),
          mask: false,
          duration: 1500
        });
      });
    } else {
      uni.showToast({
        title: t('更新失败'),
        mask: false,
        duration: 1500
      });
    }
  };
  const download = () => {
    // #ifdef APP-PLUS
    dtask(t, callback);
    // #endif
    // #ifdef H5
    const url = 'http://' + getStorageSync(IP_CONFIG) + '/front-end/download/packages/Bmos-Mes.apk';
    window?.openUrl(url);
    // #endif
  };
</script>

<style>
	page {
		background: transparent;
	}
</style>

<style lang="scss" scoped>
	.mask {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		justify-content: center;
		align-items: center;
		background-color: rgba(0, 0, 0, 0.4);
		z-index: 99;
	}

	.upload-popup-container {
		width: 246.19rpx;
		height: 138.34rpx;
		border-radius: 11.72rpx;
		background-color: #ffffff;
		box-sizing: border-box;
		padding: 20.52rpx 9.38rpx 9.38rpx;
		position: relative;
	}

	.title {
		width: 100%;
		height: 32.83rpx;
		padding-bottom: 10.55rpx;
		box-sizing: border-box;
		font-size: 15.24rpx;
		color: #242526;
		font-weight: 400;
		text-align: center;
	}

	.sub-title {
		width: 100%;
		font-size: 12.9rpx;
		color: #545659;
		font-weight: 400;
		text-align: center;
	}

	.process {
		width: 100%;
		padding: 20.55rpx 10rpx 0;
		box-sizing: border-box;
	}

	.button-container {
		position: absolute;
		bottom: 12.31rpx;
		left: 0;
		width: 100%;
		padding: 0 9.38rpx;
		box-sizing: border-box;
	}
</style>
