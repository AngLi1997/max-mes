<template>
  <BMLayout>
    <BMBasicPage
      :title="t('首页手写签名设置')"
      :disabled-confirm="!hasChanged"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <BMHandleWriteSign ref="handleSignRef" @change="writeSignChange" />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import { BMBasicPage, BMHandleWriteSign, BMLayout } from '@/BMComponents/index.js';
  import { t } from '@/utils/useBmosI18n.js';
  import { 
    getCurrentCopyRecordItem, urlQueryRef, pageBasicDataRef,
    initFillData2
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { ref } from 'vue';
  import { useToast } from 'wot-design-uni';
  import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { reqSignatureComponentSaveApi, reqSignatureSaveApi } from '@/api';

  const toast = useToast();

  const toBack = async() => {
    initFillData2();
    // #ifdef APP-PLUS
    uni.navigateBack({
      delta: 2
    });
    // #endif
    // #ifdef H5
    uni.navigateBack();
    H5AppNavigateBack();
    // #endif
  };

  const queryInfo = ref({});

  const handleSignRef = ref(null);
  const signData = ref('');
  const curParams = ref({});
  const submit = async() => {
    try {
      const base64Data = await handleSignRef.value.save();
      signData.value = base64Data;
      const { userId } = queryInfo.value;
      const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
      const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
      const { version } = getCurrentCopyRecordItem();
      curParams.value = {
        batchNo,
        componentId: queryInfo.value.id,
        copyVersion: version,
        procedureStepId,
        procedureStepModelId,
        processId,
        processVersion,
        productPlanId,
        recordItemId,
        recordVersionId,
        reuse: reusable,
        userId
      };
      await reqSignatureSaveApi({
        fileBase64Content: signData.value,
        suffix: '.png',
        userId
      });
      await reqSignatureComponentSaveApi({
        ...curParams.value
      });
      hasChanged.value = false;
      toBack();
    } catch (error) {
      signData.value = '';
      error.message && toast.error(error.message);
    }
  };

  const hasChanged = ref(false);
  const writeSignChange = () => {
    hasChanged.value = true;
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
</style>
