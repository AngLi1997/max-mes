<template>
  <BMLayout>
    <view class="mask">
      <BMModal
        v-model="showTipPopup"
        :show-title="false"
        size="small"
        custom-class="tip-popup"
        :close-on-click-modal="false"
        :confirm-text="t('去录入')"
        @confirm="confirm"
        @cancel="cancel"
      >
        <view class="tip">{{ t('您还没有录入手写签名') }}</view>
      </BMModal>
    </view>
    <BMSignModal
      v-model:show="showSignPopup"
      v-model="signValue"
      :label-list="labelList"
      :title="title"
      :signature-data="curParams"
      @confirm="signConfirm"
      @cancel="cancel"
    />
  </BMLayout>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { computed, ref } from 'vue';
  import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
  import { 
    getCurrentCopyRecordItem, urlQueryRef, pageBasicDataRef,
    initFillData2, signOptionsRef
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
  import { reqSignatureInfoApi } from '@/api/index.js';
  import { BMSignModal, BMModal, BMLayout } from '@/BMComponents/index.js';
  import { reqSignatureComponentSaveApi } from '@/api';

  const component = ref(null);
  const showSignPopup = ref(false);
  const showTipPopup = ref(false);

  const signValue = ref({});
  const curParams = ref({});
  const checkHasSignUrl = async(userId) => {
    try {
      const { data } = await reqSignatureInfoApi(userId);
      if (data) {
        return Promise.resolve();
      } else {
        return Promise.reject();
      }
    } catch (error) {
      return Promise.reject();
    }
  };

  useSubNvueLinster('page-handleWriteSignPopup', (data) => {
    component.value = data;
    componentType.value = data.componentType;
    const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
    const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
    const { version } = getCurrentCopyRecordItem();
    curParams.value = {
      batchNo,
      componentId: component.value.id,
      copyVersion: version,
      procedureStepId,
      procedureStepModelId,
      processId,
      processVersion,
      productPlanId,
      recordItemId,
      recordVersionId,
      reuse: reusable
    };
    showSignPopup.value = true;
  });
  const SignatureActionEnum = {
    'HANDLE_SUBMIT_SIGN': 85,
    'HANDLE_REVIEW_SIGN': 84
  };
  const componentType = ref('HANDLE_SUBMIT_SIGN');
  const labelList = computed(() => {
    return [{
      label: t('签名人'),
      signatureAction: SignatureActionEnum[componentType.value],
      options: signOptionsRef.value?.map(item => ({
        label: item.userName,
        value: item.value,
        id: item.id
      })) || [],
      currentUser: true
    }];
  });
  const titleMap = new Map([
    ['HANDLE_SUBMIT_SIGN', t('提交签名')],
    ['HANDLE_REVIEW_SIGN', t('复核签名')]
  ]);
  const title = computed(() => {
    return titleMap.get(componentType.value);
  });

  const cancel = () => {
    H5AppNavigateBack();
  };

  const signConfirm = async() => {
    try {
      await checkHasSignUrl(signValue.value.userId1);
      await reqSignatureComponentSaveApi({
        ...curParams.value,
        userId: signValue.value.userId1
      });
      showSignPopup.value = false;
      initFillData2();
      cancel();
    } catch (error) {
      showSignPopup.value = false;
      showTipPopup.value = true;
    }
  };

  const confirm = () => {
    const params = {
      ...component.value,
      userId: signValue.value.userId1
    };
    const query = Object.keys(params)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/webviewPopups/HandleWriteSignPopup/HandleSign?${query}`
    });
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

	.sign-popup-container {
		width: 375.15rpx;
		height: 234.47rpx;
		border-radius: 11.72rpx;
		background-color: #ffffff;
		box-sizing: border-box;
		padding: 9.38rpx 9.38rpx 12.31rpx;
		position: relative;

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
