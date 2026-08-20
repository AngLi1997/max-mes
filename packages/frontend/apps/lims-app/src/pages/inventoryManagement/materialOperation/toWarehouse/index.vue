<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料入库')"
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
      :title="t('入库签名')"
      size="large"
      overflow="hidden"
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
        <!-- 扫码 -->
        <BMScanNew @success="scanCheck" />
      </view>
    </BMModal>
  </BMLayout>
</template>

<script setup>
import {
  getQueryPositionBoundUserListByPermissionCodeApi,
  postStorageMaterialSendBackMobile,
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

const labelList = ref([
  {
    label: t('接收人'),
    signatureAction: 81,
    disabled: true,
  },
  {
    label: t('递交人'),
    signatureAction: 82,
    options: [],
  },
]);
const curParams = ref({});
const tableData = ref([]);

// 扫码校验 弹窗开启时，才能调用扫码
const scanCheck = (value) => {
  if (showSign.value) {
    onScanSuccess(value);
  }
};

// 打开签名弹窗
const openSign = async () => {
  if (tableData.value.length === 0) {
    return showNotify({
      type: 'danger',
      message: t('请扫描物料件'),
    });
  }
  curParams.value = {
    sendBackList: tableData.value,
  };
  const boundUser = await getQueryPositionBoundUserListByPermissionCodeApi({
    positionId: urlQuery.value.materialPositionId,
    permissionCode: '121020002000009',
  });
  labelList.value[1].options = boundUser.data.map((item) => {
    return {
      value: item.loginName,
      label: item.userName,
      id: item.userId,
    };
  });
  showSign.value = true;
};
const confirmSignPopup = async () => {
  try {
    const values = await formRef.value.validateFields();
    await signRef.value.checkSign();
    await postStorageMaterialSendBackMobile({
      ...curParams.value,
      ...values,
      receiverId: signValue.value.userId1,
      senderId: signValue.value.userId2,
    });
    showSign.value = false;
    uni.reLaunch({
      url: `/pages/inventoryManagement/inventoryInfo/index?materialPositionId=${
        urlQuery.value.materialPositionId
      }`,
    });
  }
  catch (error) {
    error?.message
    && showNotify({
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
