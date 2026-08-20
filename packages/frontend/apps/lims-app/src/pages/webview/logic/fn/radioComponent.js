import { setCacheComponentData, wvRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { encryptedString } from '@/pages/webview/utils/encryptedString.js';
// 选择组件
import { showRadioComponentRef } from '@/pages/webview/utils/index.js';

// 打开选择组件
export function radioComponentOpen(component) {
  // #ifdef APP-PLUS
  uni.navigateTo({
    url: '/pages/webviewComponent/radioComponent/index',
  });
  // #endif
  // #ifdef H5
  showRadioComponentRef.value = true;
  // #endif
  setTimeout(() => {
    uni.$emit('page-radioComponent', component);
  }, 0);
}

// 选择组件确定
export function radioPopupConfirm(data) {
  setCacheComponentData(data);
  wvRef.value.evalJS(`echoSingleData('${encryptedString(data)}')`);
  // #ifdef APP-PLUS
  uni.navigateBack();
  // #endif
  // #ifdef H5
  showRadioComponentRef.value = false;
  // #endif
}
