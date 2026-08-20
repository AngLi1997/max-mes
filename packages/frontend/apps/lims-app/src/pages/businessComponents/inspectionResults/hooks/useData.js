import { getNotRejectPage, inspectComponentConfirm, postInspectionInstanceByProps } from '@/api';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { onLoad } from '@dcloudio/uni-app';
import { onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useData = () => {
  const { showNotify } = useNotify();
  const { version } = getCurrentCopyRecordItem();
  const { procedureStepModelId, reusable } = pageBasicDataRef.value;
  const { productPlanId } = urlQueryRef.value;
  const queryInfo = ref({});
  const showType = ref('list');
  const triggered = ref(false);
  const loadMoreStatus = ref('loadmore');
  const params = reactive({
    pageNum: 1,
    pageSize: 20,
  });
  const total = ref(0);
  const dataList = ref([]);
  const clickData = ref(null);
  const instanceId = ref('');
  // 结果页
  const toResult = (data) => {
    showType.value = 'result';
    clickData.value = data;
  };
  // 详情页
  const toDetail = (data) => {
    clickData.value = { ...data };
    showType.value = 'detail';
  };
  // 展示列表页
  const cancel = () => {
    showType.value = 'list';
    clickData.value = null;
  };

  onLoad(async (e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(
      Object.keys(e).map(key => [
        decodeURIComponent(key),
        decodeURIComponent(e[key]),
      ]),
    );
    queryInfo.value = query;
    // #endif
    // #ifdef H5
    queryInfo.value = e;
    // #endif
  });
  const resultSubmit = async () => {
    try {
      await inspectComponentConfirm({
        componentId: queryInfo.value.componentId,
        groupComponentId: queryInfo.value.parentId,
        inspectId: clickData.value.id,
        instanceId: instanceId.value,
      });
      // showType.value = 'list';
      // clickData.value = null;
      uni.navigateBack();
      initFillData2();
    }
    catch (error) {
      error.message && showNotify({ type: 'warning', message: error.message });
    }
  };
  const getList = async () => {
    const { data } = await getNotRejectPage({ ...params, productPlanId });
    if (params.pageNum === 1) {
      dataList.value = data.list;
    }
    else {
      dataList.value = dataList.value.concat(data.list);
    }
    total.value = data.total;
    loadMoreStatus.value = total.value > dataList.value.length ? 'loadmore' : 'nomore';
  };
  // 下拉刷新触发
  const onRefresh = async () => {
    params.pageNum = 1;
    triggered.value = true;
    await getList();
  };
  // 上拉触底
  function onScrollToLower() {
    console.log('上拉触底');
    if (
      params.pageNum * params.pageSize < total.value
      && triggered.value === false
    ) {
      params.pageNum++;
      loadMoreStatus.value = 'loading';
      getList();
    }
  }
  onMounted(async () => {
    const { data } = await postInspectionInstanceByProps({
      componentId: queryInfo.value.componentId,
      copyVersion: version,
      procedureStepModelId,
      productPlanId,
      reuse: reusable,
    });
    instanceId.value = data.id;
    getList();
  });
  return {
    showType,
    dataList,
    triggered,
    loadMoreStatus,
    clickData,
    toResult,
    toDetail,
    cancel,
    onRefresh,
    onScrollToLower,
    resultSubmit,
  };
};
