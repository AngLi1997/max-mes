import { ref } from 'vue';

export const showSignModalComponentRef = ref(false);
export const showHandleWriteSignPopupRef = ref(false);
export const showFinishComponentRef = ref(false);
export const showMenuComponentRef = ref(false);
export const showTakePhotoPopupRef = ref(false);
export const showTimeDateComponentRef = ref(false);
export const showTakePhotoHistoryRef = ref(false);
export const showWarningDataComponentRef = ref(false);
export const showSaveTipsComponentRef = ref(false);

export const showSelectComponentRef = ref(false);
export const showCheckboxComponentRef = ref(false);
export const showRadioComponentRef = ref(false);
export const showHistoryDataComponentRef = ref(false);

export function initComponentShowRefs() {
  showSelectComponentRef.value = false;
  showCheckboxComponentRef.value = false;
  showRadioComponentRef.value = false;
  showHistoryDataComponentRef.value = false;
  showSaveTipsComponentRef.value = false;
  showSignModalComponentRef.value = false;
  showHandleWriteSignPopupRef.value = false;
  showFinishComponentRef.value = false;
  showMenuComponentRef.value = false;
  showTakePhotoPopupRef.value = false;
  showTimeDateComponentRef.value = false;
  showTakePhotoHistoryRef.value = false;
  showWarningDataComponentRef.value = false;
}

export function H5AppNavigateBack() {
  //  #ifdef H5
  initComponentShowRefs();
  //  #endif
  // #ifdef APP-PLUS
  uni.navigateBack();
  // #endif
}
