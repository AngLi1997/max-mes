<template>
  <BMMessageBox
    v-model="show"
    :title="t('记录数据未保存，是否保存?')"
    content=""
    :close-on-click-modal="false"
  >
    <template #buttons>
      <wd-row :gutter="16">
        <wd-col :span="12">
          <wd-button type="info" block @click="cancel">
            {{ t("取消") }}
          </wd-button>
        </wd-col>
        <wd-col :span="12">
          <wd-button block @click="confirm">
            {{ t("保存") }}
          </wd-button>
        </wd-col>
      </wd-row>
    </template>
  </BMMessageBox>
</template>
<script setup>
  import { BMMessageBox } from '@/BMComponents';
  import { t } from '@/utils/useBmosI18n.js';
  import { ref } from 'vue';
  import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
  import {
    pageSave,
    urlQueryRef,
    goBackToTargetPath
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  const show = ref(true);
  const cancel = () => {
    if (urlQueryRef.value.taskId) {
      goBackToTargetPath('pages/home/index');
    } else {
      H5AppNavigateBack();
      uni.navigateBack();
    }
  };
  const confirm = () => {
    H5AppNavigateBack();
    pageSave();
  };
</script>

<style>
page {
  background: transparent;
}
</style>

<style lang="scss" scoped></style>
