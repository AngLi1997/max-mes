<template>
  <BMBasicPage :title="t('操作规程')" :show-buttons="false" background-color="#F2F3F5" @left-click="toBack">
    <view style="height: 100%;">
      <view class="content">
        <BMInfoDisplay
          v-if="fileList.specifics" :title="fileList.specifics?.name || '-'" icon="xinxi" :basic-items="[
            {
              label: t('文件编号'),
              field: 'code',
            },
            {
              label: t('版本号'),
              field: 'version',
            },
          ]" :info-data="fileList.specifics"
        />
        <view class="code-rule-form-pdf">
          <PDFDew v-if="fileList.file" :file="fileList.file" />
        </view>
      </view>
    </view>
  </BMBasicPage>
</template>

<script setup>
import { getOperateRulePreviewApi } from '@/api';
import { BMBasicPage, BMInfoDisplay } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';
import PDFDew from './components/PDFDew/index.vue';

const fileList = reactive({
  type: '',
  file: null,
  specifics: {},
});
const queryInfo = ref({});
async function h5PdfView(id) {
  try {
    const pdfRes = await getOperateRulePreviewApi({ versionId: id });
    let pdfData = pdfRes.data;
    const blob = new Blob([pdfData], {
      type: 'application/pdf;charset=UTF-8',
    });
    pdfData = window.URL.createObjectURL(blob);
    fileList.file = encodeURIComponent(pdfData);
  }
  catch (error) {
    console.log('error:', error);
  }
}
// 获取pdf
const getPdfFile = (data) => {
  fileList.specifics = { ...data };
  // #ifdef APP-PLUS
  switch (uni.getSystemInfoSync().platform) {
    case 'android':
      fileList.file = encodeURIComponent(data.url);
      break;
    case 'ios':
      break;
  }
  // #endif
  // #ifdef H5
  h5PdfView(data.id);
  // #endif
};

function toBack() {
  uni.navigateBack();
}
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  getPdfFile(queryInfo.value);
});
</script>

<style lang="scss" scoped>
.left-content {
  display: flex;

  .title {
    font-size: 15.24rpx;
    font-weight: 500;
    line-height: 22.27rpx;
    letter-spacing: 0em;
    color: #18191a;
    margin-left: 14.65rpx;
  }
}

.content {
  padding-top: 9.38rpx;
  height: 100%;
}

.function-card {
  margin: 8.79rpx 0;
  padding: 9.38rpx 9.38rpx;

  :deep .function-title {
    padding: 0;
  }
}

:deep .function-footer {
  padding: 0;
}

.wd-row-content {
  margin-top: 8.79rpx;

  .function-content-text {
    color: var(---, #9da0a6);
    font-size: 11.72rpx;
    font-style: normal;
    font-weight: 513;
    display: flex;
    align-items: center;
    gap: 5.86rpx;

    .content-text {
      color: var(---, #18191a);
      font-size: 11.72rpx;
      font-style: normal;
      font-weight: 513;
    }

    .footer-dey {
      display: flex;
      flex-direction: row;
      width: 42.2rpx;
      height: 16.41rpx;
      padding: 2.34rpx 7.03rpx;
      justify-content: center;
      align-items: center;
      border-radius: 2.34rpx;

      .state {
        flex-direction: column;
      }
    }
  }
}

.code-rule-form-pdf {
  position: relative;
  height: calc(100% - 90rpx);
}

.rules_list_box {
  display: flex;
  gap: 9.38rpx;
  flex-wrap: wrap;
  width: 100%;
  padding: 9.38rpx 0;
}
</style>
