<template>
  <BMLayout>
    <BMBasicPage
      :title="title"
      :show-buttons="false"
      :default-padding="false"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <template #titleRight>
        <view class="navBar-right">
          <text class="label">{{ t("当前班次") }}：</text>
          <text class="value">{{ currentFlights }}</text>
        </view>
      </template>
      <view class="quick-entry-container">
        <view>
          <wd-sidebar v-model="active" @change="changeProgressStep">
            <template v-for="item in procedureViewList" :key="item.procedureId">
              <wd-sidebar-item
                custom-class="categoryItems"
                disabled
                :label="item.procedureName"
              >
                <template #icon>
                  <wd-icon
                    class-prefix="bmos-app-icon"
                    name="gongxu2"
                    size="14.07rpx"
                    color="#B6B9BF"
                  />
                </template>
              </wd-sidebar-item>
              <wd-sidebar-item
                v-for="step in item.procedureStepViewList"
                :key="step.procedureStepModelId"
                :value="step.procedureStepModelId"
                :label="step.procedureStepName"
              />
            </template>
          </wd-sidebar>
        </view>
        <view class="right">
          <view class="top">
            <view class="craft-box">
              <wd-tabs v-model="currentProcessNumber" @change="renderInitData">
                <wd-tab
                  v-for="item in stepVersionList.length"
                  :key="item"
                  :title="`${t('工艺班次')}${stepVersionList[item - 1].processChangeNumber + 1}`"
                />
              </wd-tabs>
            </view>

            <view class="process-box">
              <view class="title">{{ t("工序班次") }}</view>
              <view class="group">
                <wd-radio-group
                  v-model="currentProcedureNumber"
                  shape="button"
                  @change="renderInitData"
                >
                  <wd-radio
                    v-for="item in procedureVersionList.length"
                    :key="item"
                    :value="item - 1"
                  >
                    {{ `${t("班次")}${procedureVersionList[item-1].procedureChangeNumber+1}` }}
                  </wd-radio>
                </wd-radio-group>
              </view>
              <view v-if="totalPageList.length > 1" class="pagination">
                <wd-icon
                  name="arrow-left"
                  size="18.75rpx"
                  color="#B6B9BF"
                  @click="prePage"
                />
                <text style="margin: 0 17.58rpx;display: block; min-width: 30px;">
                  {{ currentPage
                  }}<text style="color:#9DA0A6">
                    /{{ totalPageList.length }}
                  </text>
                </text>
                <wd-icon
                  name="arrow-right"
                  size="18.75rpx"
                  color="#B6B9BF"
                  @click="nextPage"
                />
              </view>
            </view>
          </view>
          <view class="web-view-box">
            <web-view
              v-if="src"
              id="quick-entry"
              :src="src"
              :webview-styles="webview_styles"
              style="position: absolute; top: 0; width: 100%; height: 100%;"
              @message="onMessage"
              @on-post-message="onMessage"
            />
          </view>
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import { executionQuickEntryEvent } from '@/pages/webview/logic/quickEntryWebviewCallbacks.js';
  import {
    reqProcedureViewApi,
    reqStepVersionListApi
  } from '@/api/webViewApi.js';
  import { onMounted, onBeforeUnmount, reactive, ref, computed } from 'vue';
  import { BMBasicPage, BMLayout } from '@/BMComponents/index.js';
  import { useNotify } from 'wot-design-uni';
  import { onLoad } from '@dcloudio/uni-app';
  import { urlQueryRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { t } from '@/utils/useBmosI18n.js';
  import { addQuickListeningQueue, clearQuickListeningQueue } from './utils/listeningQueue.js';

  const { showNotify } = useNotify();

  var wv; // 计划创建的webview
  const wvRef = ref(null);
  const src = ref(null);
  const active = ref();
  const nodeId = ref('');
  const currentProcessNumber = ref(0);
  const currentProcedureNumber = ref(0);
  const procedureViewList = ref([]);
  const stepVersionList = ref([]);
  const urlQuery = ref({});

  const currentPage = ref(1);
  const webview_styles = reactive({
    progress: {
      color: '#f7011a'
    }
  });

  const webViewParams = computed(() => {
    return {
      nodeId: nodeId.value,
      copyVersion: totalPageList.value[currentPage.value - 1],
      componentId: urlQuery.value?.componentId,
      componentType: urlQuery.value?.componentType,
      quickEntry: urlQuery.value?.quickEntry
    };
  });

  const title = computed(() => {
    return urlQuery.value.quickEntry ? t('快捷录入') : t('工序查看');
  });
  // 当前班次展示
  const currentFlights = computed(() => {
    const number1 = Number(urlQueryRef.value?.processChangeNumber) + 1;
    const number2 = Number(urlQueryRef.value?.procedureChangeNumber) + 1;
    return `${t('工艺班次')}${number1} ${t('工序班次')}${number2}`;
  });

  // 当前工序班次列表
  const procedureVersionList = computed(() => {
    return (
      stepVersionList.value[currentProcessNumber.value]?.procedureChangeList || []
    );
  });

  // 记录总页数列表
  const totalPageList = computed(() => {
    return (
      procedureVersionList.value[currentProcedureNumber.value]?.copyVersionList ||
      []
    );
  });

  onLoad((query) => {
    urlQuery.value = { ...query };
  });

  // 切换时渲染webview页面
  const renderWebView = () => {
    executionQuickEntryEvent({
      data: { type: 'QUICK_ENTRY_RENDER' },
      wv,
      params: webViewParams.value
    });
  };

  // 切换工艺班次/工序班次时，重新渲染数据
  const renderInitData = () => {
    executionQuickEntryEvent({
      data: { type: 'QUICK_ENTRY_INIT_DATA' },
      wv,
      params: webViewParams.value
    });
  };

  // 获取工序/工艺列表
  const getProcedureViewList = async() => {
    try {
      const res = await reqProcedureViewApi({
        processId: urlQueryRef.value?.processId,
        processVersion: urlQueryRef.value?.processVersion
      });
      procedureViewList.value = res.data || [];

      // 默认选中第一个工序
      if (
        procedureViewList.value.length > 0 &&
        procedureViewList.value[0].procedureStepViewList.length > 0
      ) {
        active.value =
          procedureViewList.value[0].procedureStepViewList[0].procedureStepModelId;
        nodeId.value = procedureViewList.value[0].procedureStepViewList[0].nodeId;
      }
    } catch (error) {
      error.message && showNotify({ type: 'error', message: error.message });
    }
  };
  // 获取工艺、工序换班次数
  const getStepVersionList = async() => {
    try {
      currentProcessNumber.value = 0;
      currentProcedureNumber.value = 0;
      currentPage.value = 1;
      const res = await reqStepVersionListApi({
        procedureStepModelId: active.value,
        productPlanId: urlQueryRef.value?.productPlanId
      });
      stepVersionList.value = res.data || [];
    } catch (error) {
      error.message && showNotify({ type: 'error', message: error.message });
    }
  };
  const getMessageData = (data) => {
    // #ifdef APP-PLUS
    return data.detail.data[0];
    // #endif
    // #ifdef H5
    if (data.data.data) {
      return data.data.data.arg;
    } else {
      return {};
    }
  // #endif
  };
  const onMessage = (msg) => {
    const data = getMessageData(msg);
    if (data.type === 'QUICK_ENTRY_RENDER') {
      // 渲染时初始化webview
      initWebView();
    }
    executionQuickEntryEvent({
      data,
      wv,
      params: webViewParams.value
    });
  };

  const toBack = () => {
    uni.navigateBack();
  };

  const changeProgressStep = async() => {
    await getStepVersionList();
    procedureViewList.value.forEach((item) => {
      if (item.procedureStepViewList && item.procedureStepViewList.length > 0) {
        item.procedureStepViewList.forEach((step) => {
          if (step.procedureStepModelId === active.value) {
            nodeId.value = step.nodeId;
          }
        });
      }
    });
    renderWebView();
  };

  const prePage = () => {
    if (currentPage.value > 1) {
      currentPage.value--;
      renderInitData();
    }
  };

  const nextPage = () => {
    if (currentPage.value < totalPageList.value.length) {
      currentPage.value++;
      renderInitData();
    }
  };

  const webViewObj = ref(null);
  onMounted(async() => {
    await getProcedureViewList();
    await getStepVersionList();
    // #ifdef APP-PLUS
    src.value = '/hybrid/html/quickEntry.html';
    // #endif
    // #ifdef H5
    clearQuickListeningQueue();
    window.addEventListener('message', onMessage, false);
    addQuickListeningQueue('message', onMessage);
    if (process.env.NODE_ENV === 'development') {
      src.value = '/src/hybrid/html/quickEntry.html';
    } else {
      src.value = '/hybrid/html/quickEntry.html';
    }
  // #endif
  });
  onBeforeUnmount(() => {
    // #ifdef H5
    window.removeEventListener('message', onMessage, false);
    // #endif
    wvRef.value = null;
  });
  const initWebView = () => {
    // #ifdef APP-PLUS
    let pages = getCurrentPages();
    let currentWebview = pages[pages.length - 1].$getAppWebview();
    currentWebview.append(webViewObj.value); // 一定要append到当前的页面里！！！才能跟随当前页面一起做动画，一起关闭
    wv = currentWebview.children()[0];
    wv.setStyle({ scalable: true });
    let height = 0; // 定义动态的高度变量
    let width = 0;
    let statusbar = 0; // 动态状态栏高度		
    uni.getSystemInfo({
      // 获取当前设备的信息
      success: (systemInfo) => {
        // statusbar = systemInfo.statusBarHeight;
        height = systemInfo.windowHeight;
        width = systemInfo.windowWidth;
        let top = 224; // 设置webview的top
        let left = 316; // 设置webview的left
        const wSize = width / 1280;
        wv.setStyle({
          scalable: true,
          top: top * wSize,
          left: left * wSize,
          width: width - left * wSize,
          height: height - top * wSize // 设置webview的高度
        });
      }
    });
    // #endif
    // #ifdef H5
    let dom = window.document.getElementsByTagName('body')[0];
    let iframe = dom.getElementsByTagName('iframe');
    iframe = iframe[iframe.length - 1];
    iframe.style.backgroundColor = '#fff';
    wv = {
      evalJS: (str) => {
        iframe.contentWindow.postMessage(str, '*');
      }
    };
  // #endif
  };
</script>

<style lang="scss" scoped>
.navBar-right {
  font-size: 11.72rpx;
  .label {
    color: var(--bmos-color-text-sub);
  }
  .value {
    color: var(--bmos-color-text-main);
  }
}
.quick-entry-container {
  display: flex;
  height: 100%;
  width: 100%;
  background-color: #fff;
  border-top: 1px solid var(--bmos-color-border);
  .right {
    width: calc(100% - 185.16rpx);
    height: 100%;
    .top {
      padding: 0 9.38rpx;
      box-sizing: border-box;

      .craft-box {
        border-bottom: 1px solid var(--bmos-color-border);
      }

      .process-box {
        display: flex;
        align-items: center;
        border-bottom: 1px solid var(--bmos-color-border);
        padding: 7.03rpx 9.38rpx;
        gap: 11.72rpx;
        box-sizing: border-box;
        height: 44.53rpx;
        margin-top: -1px;
        .title {
          font-size: 12.89rpx;
          color: var(--bmos-color-text-main);
          flex-shrink: 0;
        }

        .group {
          white-space: nowrap;
          overflow-x: auto;
        }
        .pagination {
          flex-shrink: 0;
          display: flex;
          align-items: center;
          font-size: 12.89rpx;
        }
      }
    }
    .web-view-box {
      position: relative;
      width: 100%;
      height: calc(100% - 84.38rpx);
      overflow: auto;
    }
  }
}
</style>
