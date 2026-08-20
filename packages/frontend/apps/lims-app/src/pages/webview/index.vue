<template>
  <view class="webView-container">
    <web-view
      v-if="src"
      id="web-view"
      :src="src"
      :webview-styles="webview_styles"
      style="height: calc( 100%  - 42.19rpx);"
      @message="onMessage"
      @on-post-message="onMessage"
    />
    <view class="webview-footer">
      <view
        class="footer-item"
        style="margin-left: 11.72rpx;"
        @click="pageBack"
      >
        <wd-icon name="arrow-left" size="18.75rpx" color="#fff" />
        <text>{{ t("返回") }}</text>
      </view>
      <view class="batchNo">
        <div class="scroll-content">
          <span>&nbsp; {{ productName }}-{{ productMergeCode }} {{ batchNo }}&nbsp;</span>
          <span>&nbsp;{{ productName }}-{{ productMergeCode }} {{ batchNo }}&nbsp;</span>
        </div>
      </view>
      <view v-if="totalPage !== 1" class="footer-item">
        <wd-icon
          name="arrow-left"
          size="18.75rpx"
          color="#fff"
          @click="prePage"
        />
        <text style="margin: 0 17.58rpx;">
          {{ currentPage }}<text style="color:#9DA0A6">
            /{{ totalPage }}
          </text>
        </text>
        <wd-icon
          name="arrow-right"
          size="18.75rpx"
          color="#fff"
          @click="nextPage"
        />
      </view>
      <view class="footer-item">
        <view
          v-if="showAnalysisButton && hasPermission('121010001001013')"
          class="button-item special-item"
          @click="toTrendAnalysis(quickAnalysisData)"
        >
          {{ t("趋势分析") }}
        </view>
        <view
          v-if="showQuickButton && hasPermission('121010001001018')"
          class="button-item special-item"
          @click="toQuickEntry(quickAnalysisData)"
        >
          {{ t("快捷录入") }}
        </view>
        <view
          v-if="(!viewOnly || productionRevision) && hasPermission('121010001001004')"
          class="button-item"
          @click="pageSave"
        >
          {{ t("保存") }}
        </view>
        <view
          v-if="!viewOnly && hasPermission('121010001001005')"
          class="button-item"
          @click="pageFinish"
        >
          {{ t("完成") }}
        </view>
        <view class="button-item" @click="pageMenu">
          {{ t("更多") }}
        </view>
      </view>
    </view>
    <SelectComponent v-if="showSelectComponentRef" />
    <RadioComponent v-if="showRadioComponentRef" />
    <CheckboxComponent v-if="showCheckboxComponentRef" />
    <HistoryDataComponent v-if="showHistoryDataComponentRef" />
    <SignModalComponent v-if="showSignModalComponentRef" />
    <HandleWriteSign v-if="showHandleWriteSignPopupRef" />
    <FinishComponent v-if="showFinishComponentRef" />
    <MenuComponent v-if="showMenuComponentRef" />
    <TakePhotoPopup v-if="showTakePhotoPopupRef" />
    <TimeDateComponent v-if="showTimeDateComponentRef" />
    <TakePhotoHistory v-if="showTakePhotoHistoryRef" />
    <WarningDataComponent v-if="showWarningDataComponentRef" />
    <SaveTipsComponent v-if="showSaveTipsComponentRef" />
  </view>
</template>

<script setup>
import {
  initComponentShowRefs,
  showCheckboxComponentRef,
  showFinishComponentRef,
  showHandleWriteSignPopupRef,
  showHistoryDataComponentRef,
  showMenuComponentRef,
  showRadioComponentRef,
  showSaveTipsComponentRef,
  showSelectComponentRef,
  showSignModalComponentRef,
  showTakePhotoHistoryRef,
  showTakePhotoPopupRef,
  showTimeDateComponentRef,
  showWarningDataComponentRef,
} from '@/pages/webview/utils/index.js';
import CheckboxComponent from '@/pages/webviewComponent/checkboxComponent/index.vue';
import FinishComponent from '@/pages/webviewComponent/finishComponent/index.vue';
import HistoryDataComponent from '@/pages/webviewComponent/historyDataComponent/index.vue';
import MenuComponent from '@/pages/webviewComponent/menuComponent/index.vue';
import RadioComponent from '@/pages/webviewComponent/radioComponent/index.vue';
import SaveTipsComponent from '@/pages/webviewComponent/saveTipsComponent/index.vue';
import SelectComponent from '@/pages/webviewComponent/selectComponent/index.vue';
import SignModalComponent from '@/pages/webviewComponent/signModalComponent/index.vue';
import TakePhotoHistory from '@/pages/webviewComponent/takePhotoComponent/index.vue';
import TimeDateComponent from '@/pages/webviewComponent/timeDateComponent/index.vue';
import WarningDataComponent from '@/pages/webviewComponent/warningDataComponent/index.vue';
import HandleWriteSign from '@/pages/webviewPopups/HandleWriteSignPopup/index.vue';
import TakePhotoPopup from '@/pages/webviewPopups/TakePhotosToCollectEvidencePage/index.vue';

import { usePermissionStore } from '@/stores/permission.js';
import { debounce } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';

import { onLoad, onShow } from '@dcloudio/uni-app';
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue';
// 趋势分析
import { toTrendAnalysis } from './logic/fn/trendAnalysis.js';
import {
  batchNo,
  componentId,
  componentsMap,
  currentPage,
  hideShortcutButton,
  nextPage,
  pageBack,
  pageFinish,
  pageMenu,
  pageSave,
  prePage,
  productionRevision,
  productMergeCode,
  productName,
  quickAnalysisData,
  showAnalysisButton,
  showQuickButton,
  totalPage,
  viewOnly,
  wvRef,
} from './logic/fn/webViewEventCallbacks.js';
// 快捷录入
import { toQuickEntry } from './logic/quickEntryWebviewCallbacks.js';
import { executionWebViewEvent } from './logic/uniAppEventCenter.js';
import { encryptedString } from './utils/encryptedString.js';
import { addListeningQueue, clearListeningQueue } from './utils/listeningQueue.js';

const { hasPermission } = usePermissionStore();

let wv; // 计划创建的webview
const src = ref(null);
const webview_styles = reactive({
  progress: {
    color: '#f7011a',
  },
});
const urlQuery = ref({});
const webViewObj = ref(null);
onLoad((query) => {
  urlQuery.value = { ...query };
  // #ifdef APP-PLUS
  plus.screen.lockOrientation('landscape-primary');
  // #endif
  hideShortcutButton();
});
// #ifdef H5
const setH5IframeHeight = () => {
  const dom = window.document.getElementsByTagName('body')[0];
  let iframe = dom.getElementsByTagName('iframe');
  iframe = iframe[iframe.length - 1];
  // iframe.style.height = 'calc( 100%  - 72px)';
  iframe.style.backgroundColor = '#fff';
  wv = {
    evalJS: (str) => {
      iframe.contentWindow.postMessage(str, '*');
    },
  };
};
// #endif
const windowResizeCallback = () => {
  debounce(setH5IframeHeight, 300)();
};
onShow(() => {
  // #ifdef APP-PLUS
  plus.screen.lockOrientation('landscape-primary');
  // #endif
  // #ifdef H5
  setTimeout(() => {
    const componentList = [];
    componentsMap.forEach((component) => {
      componentList.push(component);
    });
    wvRef.value
    && wvRef.value.evalJS(
      `setComponentsHeight('${encryptedString(componentList)}')`,
    );
  }, 100);
  uni.onWindowResize(windowResizeCallback);
  // #endif
  if (componentId.value !== '' && wvRef.value) {
    wvRef.value.evalJS(
      `showComponentById('${componentId.value}')`,
    );
  }
});

const initWebView = () => {
  // #ifdef APP-PLUS
  const pages = getCurrentPages();
  const currentWebview = pages[pages.length - 1].$getAppWebview();
  currentWebview.append(webViewObj.value); // 一定要append到当前的页面里！！！才能跟随当前页面一起做动画，一起关闭
  wv = currentWebview.children()[0];
  wv.setStyle({ scalable: true });
  let height = 0; // 定义动态的高度变量
  uni.getSystemInfo({
    // 获取当前设备的信息
    success: (systemInfo) => {
      // statusbar = systemInfo.statusBarHeight;
      height = systemInfo.windowHeight;
      wv.setStyle({
        scalable: true,
        height: height - 72, // 设置webview的高度
      });
    },
  });
  // #endif
  // #ifdef H5
  setH5IframeHeight();
  // #endif
};
  // webView
const getMessageData = (data) => {
  let res = {};
  // #ifdef APP-PLUS
  res = data.detail.data[0];
  // #endif
  // #ifdef H5
  if (data.data.data) {
    res = data.data.data.arg;
  }
  else {
    res = {};
  }
  // #endif
  return res;
};
  // 接收webView消息
const onMessage = (msg) => {
  const data = getMessageData(msg);
  if (data.type === 'RENDER') {
    // 渲染时初始化webview
    initWebView();
  }
  // 执行webview发送的事件
  executionWebViewEvent({ data, wv, urlQuery: urlQuery.value });
};
onMounted(() => {
  // #ifdef APP-PLUS
  src.value = '/hybrid/html/local.html';
  // #endif
  // #ifdef H5
  clearListeningQueue();
  window.addEventListener('message', onMessage, false);
  addListeningQueue('message', onMessage);
  console.log('webviewH5页面添加message监听');
  initComponentShowRefs();
  if (process.env.NODE_ENV === 'development') {
    src.value = '/src/hybrid/html/local.html';
  }
  else {
    src.value = '/hybrid/html/local.html';
  }
  // #endif
});
onBeforeUnmount(() => {
  // #ifdef H5
  window.removeEventListener('message', onMessage, false);
  console.log('webviewH5页面卸载message监听');
  // #endif
  wvRef.value = null;
});
</script>

<style lang="scss" scoped>
@keyframes scroll-left {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}
.webView-container {
  background-color: #fff;
  display: flex;
  padding: 0;
  .webview-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 42.19rpx;
    background-color: #425166;
    color: #fff;
    font-weight: 400;
    position: absolute;
    font-size: 12.89rpx;
    bottom: 0;
    left: 0;
    padding: 14.06rpx 0 12.89rpx;
    box-sizing: border-box;
    .footer-item {
      display: flex;
      align-items: center;
      .button-item {
        border-left: 1px solid #fff;
        padding: 0 23.44rpx;
      }
      .special-item {
        color: var(--bmos-color-info);
      }
    }
    .batchNo {
      max-width: 133.59rpx;
      height: 32.81rpx;
      line-height: 32.81rpx;
      box-sizing: border-box;
      white-space: nowrap;
      overflow: hidden;
      color: var(--bmos-color-info);
      .scroll-content {
        display: inline-block;
        white-space: nowrap;
        animation: scroll-left 8s linear infinite;
      }
    }
  }
}

page {
  background: none;
  height: 100%;
}
</style>
