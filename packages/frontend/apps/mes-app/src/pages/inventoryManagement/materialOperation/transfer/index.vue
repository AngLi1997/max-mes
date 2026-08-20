<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料移库')"
      class="page-container"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="openSign"
    >
      <ScanTable
        v-model="tableData"
        :material-position-id="urlQuery.materialPositionId"
        table-height="calc(100% - 33.98rpx - 28.13rpx - 18.75rpx)"
      />
    </BMBasicPage>
    <!-- 签名 -->
    <BMModal
      v-model="showSign"
      :title="t('移库签名')"
      size="large"
      @cancel="showSign = false"
      @confirm="confirmSignPopup"
    >
      <view style="height: 280.08rpx">
        <BMForm ref="formRef" v-bind="formProps" />
        <BMSign
          ref="signRef"
          v-model="signValue"
          :label-list="labelList"
          :signature-data="curParams"
          size="medium"
        />
      </view>
    </BMModal>
    <!-- 扫码 -->
    <BMScanNew @success="scanCheck" />
  </BMLayout>
</template>

<script setup>
import {
  putStorageMaterialMoveMobile,
} from '@/api';
import { BMBasicPage, BMForm, BMLayout, BMModal, BMScanNew, BMSign } from '@/BMComponents';
import ScanTable from '@/pages/inventoryManagement/components/scanTable/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useForm } from '../../commonHooks/useForm.jsx';

const { showNotify } = useNotify();
const { formRef, formProps, signRef, signValue, onScanSuccess } = useForm();

const urlQuery = ref();
const showSign = ref(false);
const labelList = ref([{
  label: t('操作人'),
  signatureAction: 16,
  disabled: true,
}]);
const curParams = ref({});

const tableData = ref([]);

// 打开签名弹窗
const openSign = async () => {
  if (tableData.value.length === 0) {
    return showNotify({
      type: 'danger',
      message: t('请扫描物料件'),
    });
  }
  curParams.value = {
    storageMaterialIdList: tableData.value.map(item => item.id),
  };
  showSign.value = true;
};

// 扫码校验 弹窗开启时，才能调用扫码
const scanCheck = (value) => {
  if (showSign.value) {
    onScanSuccess(value);
  }
};
const confirmSignPopup = async () => {
  try {
    const values = await formRef.value.validateFields();
    await signRef.value.checkSign();
    await putStorageMaterialMoveMobile({
      ...curParams.value,
      sourceMaterialPositionId: urlQuery.value.materialPositionId,
      targetMaterialPositionId: values.materialPositionId,
      linkExplain: values.linkExplain, // 来源/去向
      moverId: signValue.value.userId1,
    });
    showSign.value = false;
    uni.reLaunch({
      url: `/pages/inventoryManagement/inventoryInfo/index?materialPositionId=${urlQuery.value.materialPositionId}`,
    });
  }
  catch (error) {
    error?.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};

onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  urlQuery.value = {
    ...query,
  };
  // #endif
  // #ifdef H5
  urlQuery.value = {
    ...e,
  };
  // #endif
  urlQuery.value?.id && tableData.value.push(urlQuery.value);
});
// 返回
const toBack = () => {
  uni.navigateBack();
};
</script>

  <style lang="scss" scoped>
  .page-container {
  :deep(.button-box) {
    border: none;
  }
}
</style>
