<template>
  <BMLayout>
    <BMBasicPage
      :title="t('库存出库')"
      :confirm-text="t('出库')"
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
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :label-list="labelList"
      :title="t('出库签名')"
      :remark-label="t('来源/去向')"
      show-remark
      :remark-required="true"
      :signature-data="curParams"
      @confirm="confirmSignPopup"
    />
  </BMLayout>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { BMLayout, BMBasicPage, BMSignModal } from '@/BMComponents';
  import ScanTable from '@/pages/inventoryManagement/components/scanTable/index.vue';
  import { ref } from 'vue';
  import {
    getQueryPositionBoundUserListByPermissionCodeApi,
    putStorageMaterialOutboundMobile
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
    userId2: ''
  });
  const labelList = ref([
    {
      label: t('出库人'),
      signatureAction: 53,
      disabled: true
    },
    {
      label: t('领用人'),
      signatureAction: 54,
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
      materialPositionId: urlQuery.value.materialPositionId,
      outboundList: tableData.value
    };
    const boundUser = await getQueryPositionBoundUserListByPermissionCodeApi({
      positionId: urlQuery.value.materialPositionId,
      permissionCode: '121020002000008'
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
      await putStorageMaterialOutboundMobile({
        ...curParams.value,
        linkExplain: signValue.value.remark, // 来源/去向
        senderId: signValue.value.userId1,
        receiverId: signValue.value.userId2
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
  // 返回
  const toBack = () => {
    uni.navigateBack();
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
    urlQuery.value?.id && tableData.value.push(urlQuery.value);
  });
</script>

<style lang="scss" scoped>
.page-container {
  :deep(.button-box) {
    border: none;
  }
}
.content {
  position: relative;
  height: 100%;
  .scan_box {
    width: 50%;
    margin: 0 0 9.38rpx 50%;
  }
  .table_box {
    height: calc(100% - 33.98rpx - 28.13rpx - 18.75rpx);
    margin-bottom: 9.38rpx;
  }
}
</style>
