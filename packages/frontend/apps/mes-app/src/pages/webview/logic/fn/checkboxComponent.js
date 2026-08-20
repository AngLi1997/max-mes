import { setCacheComponentData, wvRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { encryptedString } from '@/pages/webview/utils/encryptedString.js';
// 选择组件
import { showCheckboxComponentRef } from '@/pages/webview/utils/index.js';

// 打开选择组件
export function checkboxComponentOpen(component) {
  // #ifdef APP-PLUS
  uni.navigateTo({
    url: '/pages/webviewComponent/checkboxComponent/index',
  });
  // #endif
  // #ifdef H5
  showCheckboxComponentRef.value = true;
  // #endif
  setTimeout(() => {
    uni.$emit('page-checkboxComponent', component);
  }, 0);
}

// 选择组件确定
export function checkboxPopupConfirm(data) {
  setCacheComponentData(data);
  wvRef.value.evalJS(`echoSingleData('${encryptedString(data)}')`);
  // #ifdef APP-PLUS
  uni.navigateBack();
  // #endif
  // #ifdef H5
  showCheckboxComponentRef.value = false;
  // #endif
}
