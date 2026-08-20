import {
  requisitionReceiveRepositoryComplete,
  reqUserListByAuthCodeAndPlanIdApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  t,
} from '@/utils/useBmosI18n.js';
import {
  ref,
} from 'vue';

export const useModel = ({
  UseColumns,
}) => {
  const showSign = ref(false);
  const signValue = ref({});
  const labelList = ref([{
    label: t('计划人'),
    signatureAction: 52,
    options: [],
    currentUser: true,
  }]);
  const getUserList = async () => {
    try {
      const { data } = await reqUserListByAuthCodeAndPlanIdApi({
        permissionCode: '121010001002001',
        productPlanId: urlQueryRef.value?.productPlanId,
      });
      labelList.value[0].options = data?.map((item) => {
        return {
          label: item.userName,
          value: item.loginName,
          id: item.userId,
          userName: item.userName,
        };
      });
    }
    catch (error) {
      labelList.value[0].options = [];
    }
  };
  const {
    signatureData,
    paramsData,
    current,
  } = UseColumns;
  const signSubmit = async () => {
    uni.showLoading({
      title: t('保存中...'),
      mask: true,
    });
    try {
      await requisitionReceiveRepositoryComplete({
        ...signatureData.value,
        operatorId: signValue.value.userId1,
      });
      uni.hideLoading();
      uni.navigateBack();
      initFillData2();
    }
    catch (error) {
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  // 提交表格
  const submit = () => {
    signatureData.value = {
      batchNo: urlQueryRef.value?.batchNo,
      componentId: paramsData.value?.id,
      copyVersion: getCurrentCopyRecordItem()?.version,
      procedureStepId: pageBasicDataRef.value?.procedureStepId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
      processId: urlQueryRef.value?.processId,
      processVersion: urlQueryRef.value?.processVersion,
      productPlanId: urlQueryRef.value?.productPlanId,
      recordItemId: pageBasicDataRef.value?.recordItemId,
      recordVersionId: pageBasicDataRef.value?.recordVersionId,
      requisitionPlanId: current.currentList?.requisitionPlanId,
    };
    showSign.value = true;
  };

  return {
    labelList,
    showSign,
    signValue,
    submit,
    signSubmit,
    getUserList,
  };
};
