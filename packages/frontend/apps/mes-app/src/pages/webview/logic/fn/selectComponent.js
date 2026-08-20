import { setCacheComponentData, wvRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { encryptedString } from '@/pages/webview/utils/encryptedString.js';
// 选择组件
import { showSelectComponentRef } from '@/pages/webview/utils/index.js';

// 打开选择组件
export function selectComponentOpen(component) {
  // #ifdef APP-PLUS
  uni.navigateTo({
    url: '/pages/webviewComponent/selectComponent/index',
  });
  // #endif
  // #ifdef H5
  showSelectComponentRef.value = true;
  // #endif
  setTimeout(() => {
    uni.$emit('page-selectComponent', component);
  }, 0);
}

// 选择组件确定
export function selectPopupConfirm(data) {
  setCacheComponentData(data);
  wvRef.value.evalJS(`echoSingleData('${encryptedString(data)}')`);
  // #ifdef APP-PLUS
  uni.navigateBack();
  // #endif
  // #ifdef H5
  showSelectComponentRef.value = false;
  // #endif
}
