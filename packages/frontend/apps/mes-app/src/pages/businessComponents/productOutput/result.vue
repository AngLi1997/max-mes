<template>
  <BMBasicPage
    :title="t('产出结果')"
    :show-buttons="false"
    @left-click="toWebview"
  >
    <template #titleRight>
      <wd-button type="text" @click="toBack">
        {{ t("退出") }}
      </wd-button>
    </template>
    <view class="container">
      <BMTable ref="tableRef" v-bind="tableProps" />
    </view>
  </BMBasicPage>
</template>

<script setup>
import { BMBasicPage, BMTable } from '@/BMComponents/index.js';
import { goBackToTargetPath, initFillData2 } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import {
  onLoad,
} from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useTable } from './hooks';

// 返回
const toBack = () => {
  goBackToTargetPath();
};

const toWebview = (url) => {
  initFillData2();
  uni.navigateBack({
    delta: 2,
  });
};

const finishedId = ref('');
const field = ref('');

// 表格
const UseTable = useTable(finishedId);
const {
  tableRef,
  tableProps,
} = UseTable;

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  finishedId.value = query.id;
  field.value = query.field;
  // #endif
  // #ifdef H5
  finishedId.value = e.id;
  field.value = e.field;
  // #endif
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  background: #ffffff;
}
</style>
