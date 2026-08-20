import {
  getPlanInfoFormulaMaterialList,
  getRequisitionReserveInstance,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  ref,
} from 'vue';

export const useSubTab = ({
  UseTable,
  UseColumns,
  props,
}) => {
  const {
    getPage,
  } = UseTable;
  const {
    current,
    paramsData,
    pendingQuantity,
    tabSub,
    refreshPage,
    completedPlan,
    orderQuantity,
  } = UseColumns;
  const splitSigning = ref([]);
  const change = ({ value }) => {
    const data = splitSigning.value?.find(item => item.id === value);
    if (data) {
      current.active = data.id;
      current.currentList = {
        ...data,
        name: current.currentList?.name,
        // componentId:current.currentList?.componentId,
        processId: paramsData.value?.processId,
        productPlanId: current.currentList?.productPlanId,
        componentInstanceId: current.currentList?.componentInstanceId,
      };
      getPage();
    }
  };

  // 跳转处理界面
  const toMaterial = () => {
    refreshPage.value = true;
    const params = { ...current.currentList, orderQuantity: orderQuantity.value, pendingQuantityNum: pendingQuantity.value, confirmBefore: props?.confirmBefore };
    const query = Object.keys(params)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/businessComponents/reservationLock/index?${query}`,
    });
  };
  // 领料api
  const reqDetailApi = async () => {
    try {
      const {
        version,
      } = getCurrentCopyRecordItem();
      const res = props?.confirmBefore
        ? await getPlanInfoFormulaMaterialList({ productPlanId: paramsData.value?.id })
        : await getRequisitionReserveInstance({
          ...paramsData.value,
          componentId: paramsData.value?.id,
          copyVersion: version,
        });
      splitSigning.value = props?.confirmBefore ? res.data : res.data?.materialList;
      current.currentList = {
        ...(props?.confirmBefore ? res.data[0] : res.data?.materialList[0]),
        name: res.data?.name,
        processId: paramsData.value?.processId,
        productPlanId: props?.confirmBefore ? paramsData.value?.id : urlQueryRef.value?.productPlanId,
        componentInstanceId: res.data?.componentInstanceId,
      };
      completedPlan.value = res.data?.completedPlan || false;
      current.active = props?.confirmBefore ? res.data[0]?.id : res.data?.materialList[0]?.id;
      await getPage();
    }
    catch (error) {
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
      });
    }
  };
  return {
    current,
    tabSub,
    splitSigning,
    change,
    toMaterial,
    reqDetailApi,
  };
};
