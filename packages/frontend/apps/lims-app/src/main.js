import messages from '@/locale/index.js';
import * as Pinia from 'pinia';
import { createSSRApp } from 'vue';
import { createI18n } from 'vue-i18n';
import App from './App';
import btnPermission from './directives/btnPermission.js';

// #ifndef VUE3
import './uni-promisify-adaptor'
// #endif

const i18nConfig = {
  locale: uni.getLocale(),
  messages,
  legacy: false,
};
// #ifdef H5
uni.navigateBack = function (data) {
  if (data) {
    window.history.go(-data.delta);
  }
  else {
    window.history.back();
  }
};
// #endif

const i18n = createI18n(i18nConfig);
export function createApp() {
  const app = createSSRApp(App);
  btnPermission(app);
  app.use(i18n).use(Pinia.createPinia());
  return {
    app,
    Pinia, // 此处必须将 Pinia 返回
  };
}

// #ifndef MP
// 处理 wx.connectSocket promisify 兼容问题，强制返回 SocketTask
uni.connectSocket = (function (connectSocket) {
  return function (options) {
    console.log(options);
    options.success = options.success || function () {};
    return connectSocket.call(this, options);
  };
})(uni.connectSocket);
// #endif
