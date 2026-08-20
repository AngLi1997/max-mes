<template>
  <view class="project-display">
    <web-view v-if="pdfUrl" :src="pdfUrl" :webview-styles="webviewStyles"></web-view>
  </view>
</template>
<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { watch, ref, reactive } from 'vue';
var wv;//计划创建的webview
const props = defineProps({
  file: {
    type: String,
    default: ''
  },
  isPdfView: {
    type: Boolean,
    default: false
  }
});
const urlHtml = ref('/hybrid/html/pdf/web/viewer.html');
const pdfUrl = ref(null); //pdf文件的链接
const webViewObj = ref(null);
const webviewStyles = reactive({
  progress: {
    color: '#e3e3e3'
  }
})
const initWebView = () => {
  let height = 0; //定义动态的高度变量
  let statusbar = 0; // 动态状态栏高度
  uni.getSystemInfo({
    // 获取当前设备的具体信息
    success: sysinfo => {
      statusbar = sysinfo.statusBarHeight;
      height = sysinfo.windowHeight;
    }
  });
  let pages = getCurrentPages();
  let currentWebview = pages[pages.length - 1].$getAppWebview();
  setTimeout(() => {
    var wv = currentWebview.children()[0];
    wv.setStyle({
      //设置web-view距离顶部的距离以及自己的高度，单位为px
      top: statusbar + uni.upx2px(100), //此处是距离顶部的高度，应该是你页面的头部
      height: height - statusbar - uni.upx2px(90), //webview的高度
      scalable: false, //webview的页面是否可以缩放，双指放大缩小,
    });
  }, 200); //如页面初始化调用需要写延迟
};
watch(
  () => props.file,
  () => {
    if (props.file) {
      const file = props.file;
      // #ifdef APP-PLUS
      pdfUrl.value = urlHtml.value + '?file=' + file;
      // #endif
      // #ifdef H5
      if (process.env.NODE_ENV === 'development') {
        pdfUrl.value = '/src' + urlHtml.value + '?file=' + file;
      } else {
        pdfUrl.value = urlHtml.value + '?file=' + file;
      }
      // #endif
    }
  },
  {
    deep: true,
  },
);
onLoad(() => {
  // #ifdef APP-PLUS
  initWebView()
  // #endif
  if (props.file) {
    const file = props.file;
    // #ifdef APP-PLUS
    pdfUrl.value = urlHtml.value + '?file=' + file;
    // #endif
    // #ifdef H5
    if (process.env.NODE_ENV === 'development') {
      pdfUrl.value = '/src' + urlHtml.value + '?file=' + file;
    } else {
      pdfUrl.value = urlHtml.value + '?file=' + file;
    }
    // #endif
  }
})
</script>
<style lang="scss" scoped>
.project-display {
  position: relative;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 8.79rpx;
  gap: 8.79rpx;

}
</style>
