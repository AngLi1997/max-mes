import { ref } from 'vue';
import { reqSignatureComponentSaveApi } from '@/api/index.js';
import {
  getCurrentCopyRecordItem,
  urlQueryRef,
  pageBasicDataRef,
  newSignOptionsRef,
  initFillData2
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';

export const useHandleSign = ({ componentData, getFieldDataList }) => {
  const showHandleSignPopup = ref(false);
  const handleSignValue = ref({});
  const handleSignLabelList = ref([
    {
      label: t('修订人'),
      signatureAction: 3,
      options: newSignOptionsRef.value,
      currentUser: true
    },
    {
      label: t('复核人'),
      signatureAction: 4,
      options: newSignOptionsRef.value
    }
  ]);
  const curParams = ref({});
  const openHandleSignPopup = () => {
    const copyRecordItem = getCurrentCopyRecordItem();
    showHandleSignPopup.value = true;
    curParams.value = {
      batchNo: urlQueryRef.value.batchNo,
      componentId: componentData.value.id,
      copyVersion: copyRecordItem.version,
      procedureStepId: pageBasicDataRef.value.procedureStepId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      processId: urlQueryRef.value.processId,
      processVersion: urlQueryRef.value.processVersion,
      productPlanId: urlQueryRef.value.productPlanId,
      recordItemId: pageBasicDataRef.value.recordItemId,
      recordVersionId: pageBasicDataRef.value.recordVersionId,
      reuse: pageBasicDataRef.value.reusable
    };
  };
  const showTipPopup = ref(false);
  const confirmTipPopup = () => {
    showTipPopup.value = false;
    showHandleSignPopup.value = false;
    const params = {
      ...componentData.value
    };
    const query = Object.keys(params)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/webviewPopups/HandleWriteSignPopup/HandleSign?${query}`
    });
  };
  const cancelTipPopup = () => {
    showTipPopup.value = false;
    showHandleSignPopup.value = false;
    H5AppNavigateBack();
  };
  const handleSignConfirm = async() => {
    try {
      await reqSignatureComponentSaveApi(
        { ...curParams.value,
          userId: handleSignValue.value.userId1 }
      );
      initFillData2();
      getFieldDataList(componentData.value.fieldId);
      showHandleSignPopup.value = false;
      H5AppNavigateBack();
    } catch (error) {
      showTipPopup.value = true;
    }
  };
  return {
    showHandleSignPopup,
    handleSignValue,
    handleSignLabelList,
    openHandleSignPopup,
    handleSignConfirm,
    showTipPopup,
    confirmTipPopup,
    cancelTipPopup,
    curParams
  };
};
