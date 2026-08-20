import { getInstanceByProps } from '@/api';
import {
  getCurrentCopyRecordItem,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { buildUrlQuery } from '@/utils/url';

// 称量数据组件
export const weighingData = async (data) => {
  // 获取业务组件componentInstanceId
  const { procedureStepModelId, reusable } = pageBasicDataRef.value;
  const { productPlanId } = urlQueryRef.value;
  const { version } = getCurrentCopyRecordItem();
  let componentInstanceId = '';
  try {
    const res = await getInstanceByProps({
      componentId: data.parent.id,
      copyVersion: version,
      procedureStepModelId,
      productPlanId,
      reuse: reusable,
    });
    componentInstanceId = res.data.id;
  }
  catch (error) {
    error.message && uni.showToast({
      title: error.message,
      icon: 'none',
    });
  }
  const query = buildUrlQuery({
    id: componentInstanceId,
  });
  uni.navigateTo({
    url: `/pages/businessComponents/weighingData/modeDevice/index?${query}`,
  });
};
