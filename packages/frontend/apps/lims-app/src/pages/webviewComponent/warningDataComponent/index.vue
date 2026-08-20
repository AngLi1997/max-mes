<template>
  <view class="mask">
    <BMModal
      v-model="showTipPopup"
      :show-title="false"
      size="small"
      custom-class="tip-popup"
      :close-on-click-modal="false"
      :confirm-text="t('保存')"
      @confirm="confirm"
      @cancel="cancel"
    >
      <view class="tip">{{ t('数据超限，是否继续保存？') }}</view>
    </BMModal>
  </view>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { ref } from 'vue';
  import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
  import { openSaveSignPopup } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { BMModal } from '@/BMComponents/index.js';

  const showTipPopup = ref(true);

  const cancel = () => {
    H5AppNavigateBack();
  };

  const confirm = () => {
    showTipPopup.value = false;
    H5AppNavigateBack();
    openSaveSignPopup();
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
		z-index: 1;
    
	}
  :deep(.tip-popup .modal-container .modal-content) {
      min-height: 44.53rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14.07rpx;
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

		.button-container {
			position: absolute;
			bottom: 12.31rpx;
			left: 0;
			width: 100%;
			padding: 0 9.38rpx;
			box-sizing: border-box;
		}
  .tip-popup-container {
		width: 375.15rpx;
		height: 234.47rpx;
		border-radius: 11.72rpx;
		background-color: #ffffff;
		box-sizing: border-box;
		padding: 9.38rpx 9.38rpx 12.31rpx;
		position: relative;

		.button-container {
			position: absolute;
			bottom: 12.31rpx;
			left: 0;
			width: 100%;
			padding: 0 9.38rpx;
			box-sizing: border-box;
		}
	}

	.refresh-content {
		width: 246.19rpx;
		height: 138.34rpx;
		padding: 32.83rpx 9.38rpx 9.38rpx;
		box-sizing: border-box;

		.text-content-new {
			color: #242526;
			font-size: 15.24rpx;
			text-align: center;
			margin-bottom: 29.31rpx;
		}
	}
</style>
