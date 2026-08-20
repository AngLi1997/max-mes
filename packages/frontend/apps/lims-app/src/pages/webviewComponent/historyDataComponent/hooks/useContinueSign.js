import { ref, computed } from 'vue';
import {
  getCurrentCopyRecordItem,
  urlQueryRef,
  pageBasicDataRef,
  newSignOptionsRef,
  initFillData2
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { postUpdateExecuteDataApi } from '@/api/webViewApi.js';
import { t } from '@/utils/useBmosI18n.js';
import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
import { format } from 'date-fns';
import { getCurrentTime } from '@/utils/time.js';

export const useContinueSign = ({ componentData, getFieldDataList, showNotify, signFormat }) => {
  // 继续签名
  const showContinueSignPopup = ref(false);
  const handleContinueSignValue = ref({});
  const SignatureActionEnum = {
    SUBMIT_SIGN: 1,
    REVIEW_SIGN: 2
  };
  const handleContinueSignLabelList = computed(() => {
    return [
      {
        label: t('账号'),
        signatureAction: SignatureActionEnum[componentData.value.componentType],
        options: newSignOptionsRef.value,
        currentUser: true
      }
    ];
  });
  const handleContinueSignCurrentTimeValue = ref(getCurrentTime());
  const continueSignParams = computed(() => {
    const curValue = componentData.value.value
      ? `${componentData.value.value} \n ${handleContinueSignValue.value.userName1} ${format(
        handleContinueSignCurrentTimeValue.value,
        signFormat.value
      )}`
      : `${handleContinueSignValue.value.userName1} ${format(
        handleContinueSignCurrentTimeValue.value,
        signFormat.value
      )}`;
    let valueExtension = '';
    if (Array.isArray(componentData.value.valueExtension)) {
      valueExtension = JSON.stringify(componentData.value.valueExtension);
    } else {
      valueExtension = componentData.value.valueExtension;
    }
    const copyRecordItem = getCurrentCopyRecordItem();
    return {
      batchNo: urlQueryRef.value.batchNo,
      componentType: componentData.value.componentType,
      copyVersion: copyRecordItem.version,
      fieldId: componentData.value.fieldId,
      procedureStepId: pageBasicDataRef.value.procedureStepId,
      processId: urlQueryRef.value.processId,
      processVersion: urlQueryRef.value.processVersion,
      productPlanId: urlQueryRef.value.productPlanId,
      recordItemId: pageBasicDataRef.value.recordItemId,
      recordVersionId: pageBasicDataRef.value.recordVersionId,
      remark: '',
      reuse: pageBasicDataRef.value.reusable,
      value: curValue,
      valueExtension: valueExtension,
      operationTime: handleContinueSignCurrentTimeValue.value,
      operationUser: handleContinueSignValue.value.userId1,
      processChangeNumber: urlQueryRef.value.processChangeNumber,
      procedureChangeNumber: urlQueryRef.value.procedureChangeNumber
    };
  });
  const continueSign = () => {
    showContinueSignPopup.value = true;
  };

  const handleContinueSignConfirm = async() => {
    try {
      await postUpdateExecuteDataApi(continueSignParams.value);
      initFillData2();
      uni.showToast({
        title: t('签名保存成功'),
        icon: 'none'
      });
      H5AppNavigateBack();
      getFieldDataList(componentData.value.fieldId);
    } catch (e) {
      e.message && showNotify({
        type: 'danger',
        message: e.message
      });
    }
  };
  return {
    showContinueSignPopup,
    handleContinueSignValue,
    handleContinueSignLabelList,
    handleContinueSignCurrentTimeValue,
    continueSignParams,
    continueSign,
    handleContinueSignConfirm
  };
};
