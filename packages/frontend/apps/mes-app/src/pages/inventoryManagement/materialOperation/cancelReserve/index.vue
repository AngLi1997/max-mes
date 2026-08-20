<template>
  <BMLayout>
    <BMBasicPage
      :title="t('取消预定')"
      class="page-container"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="openSign"
    >
      <BMInfoDisplay
        :title="t('生产信息')"
        icon="xinxi"
        background="#F7F8FA"
        :basic-items="[
          {
            label: t('产品信息'),
            field: 'productInfo',
          },
          {
            label: t('生产工艺'),
            field: 'processName',
          },
          {
            label: t('生产批次'),
            field: 'batchNo',
          },
        ]"
        :info-data="urlQuery"
      />
      <view class="scan-table-box">
        <ScanTable
          v-model="tableData"
          :material-position-id="urlQuery.materialPositionId"
          table-height="calc(100% - 33.98rpx - 28.13rpx - 18.75rpx)"
        />
      </view>
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :label-list="labelList"
      :title="t('签名确认')"
      :remark-label="t('备注')"
      show-remark
      :signature-data="curParams"
      :remark-required="true"
      @confirm="confirmSignPopup"
    />
  </BMLayout>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import {
    BMLayout,
    BMBasicPage,
    BMSignModal,
    BMInfoDisplay
  } from '@/BMComponents';
  import ScanTable from '@/pages/inventoryManagement/components/scanTable/index.vue';
  import { ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import {
    getQueryPositionBoundUserListByPermissionCodeApi,
    putStorageMaterialCancelReserve
  } from '@/api';
  import { useNotify } from 'wot-design-uni';
  const { showNotify } = useNotify();

  const urlQuery = ref();
  const showSign = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
    loginName2: '',
    password2: '',
    userId2: '',
    remark: ''
  });
  const labelList = ref([
    {
      label: t('操作人'),
      signatureAction: 68,
      disabled: true
    },
    {
      label: t('复核人'),
      signatureAction: 69,
      options: []
    }
  ]);
  const curParams = ref({});

  const tableData = ref([]);

  // 打开签名弹窗
  const openSign = async() => {
    if (tableData.value.length === 0) {
      return showNotify({
        type: 'danger',
        message: t('请扫描物料件')
      });
    }
    curParams.value = {
      productId: urlQuery.value.productId,
      processId: urlQuery.value.processId,
      batchId: urlQuery.value.batchId,
      storageMaterialIdList: tableData.value.map((item) => item.id)
    };
    const boundUser = await getQueryPositionBoundUserListByPermissionCodeApi({
      positionId: urlQuery.value.materialPositionId,
      permissionCode: '121020002000011'
    });
    labelList.value[1].options = boundUser.data.map((item) => {
      return {
        value: item.loginName,
        label: item.userName,
        id: item.userId
      };
    });
    showSign.value = true;
  };
  const confirmSignPopup = async() => {
    try {
      await putStorageMaterialCancelReserve({
        ...curParams.value,
        remark: signValue.value.remark, // 来源/去向
        operatorId: signValue.value.userId1,
        reCheckerId: signValue.value.userId2
      });
      showSign.value = false;
      uni.reLaunch({
        url: `/pages/inventoryManagement/inventoryInfo/index?materialPositionId=${
          urlQuery.value.materialPositionId
        }`
      });
    } catch (error) {
      error?.message &&
        showNotify({
          type: 'danger',
          message: error.message
        });
    }
  };

  onLoad((e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(
      Object.keys(e).map((key) => [
        decodeURIComponent(key),
        decodeURIComponent(e[key])
      ])
    );
    urlQuery.value = {
      ...query
    };
    // #endif
    // #ifdef H5
    urlQuery.value = {
      ...e
    };
    // #endif
    urlQuery.value.productInfo = `${urlQuery.value.productMergeCode}-${
      urlQuery.value.productName
    }`;
    tableData.value.push(urlQuery.value);
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
.scan-table-box {
  height: calc(100% - 84.38rpx - 9.38rpx);
  margin-top: 9.38rpx;
}
</style>
