import { getFieldDataListApi } from '@/api/webViewApi.js';
import {
  getCurrentCopyRecordItem,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { ref } from 'vue';

export const useHistoryData = ({ componentData }) => {
  const historyDataList = ref([]);
  // 查询组件历史值
  const getFieldDataList = async (component) => {
    componentData.value = component;
    const id = component.fieldId;
    if (!id)
      return;
    const copyRecordItem = getCurrentCopyRecordItem();
    const data = {
      fieldId: id,
      procedureStepId: pageBasicDataRef.value.procedureStepId,
      productPlanId: urlQueryRef.value.productPlanId,
      reuse: pageBasicDataRef.value.reusable,
      copyVersion: copyRecordItem.version,
    };
    const res = await getFieldDataListApi(data);
    historyDataList.value = (res.data || []).map((item) => {
      if (component.componentType === 'CHECKBOX' && item.value) {
        if (!item.emptyValue && item.value !== nullValueRef.value) {
          item.value = JSON.parse(item.value).join(',');
        }
      }
      return item;
    });
  };
  return {
    historyDataList,
    getFieldDataList,
  };
};
