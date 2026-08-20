<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料销毁')"
      :confirm-text="t('销毁')"
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
      :title="t('销毁签名')"
      :remark-label="t('来源/去向')"
      show-remark
      :signature-data="curParams"
      :remark-required="true"
      @confirm="confirmSignPopup"
    />
  </BMLayout>
</template>

<script setup>
import {
  destroyAndConsumeMobileApi,
  getQueryPositionBoundUserListByPermissionCodeApi,
} from '@/api';
import { BMBasicPage, BMLayout, BMSignModal } from '@/BMComponents';
import ScanTable from '@/pages/inventoryManagement/components/scanTable/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();

const urlQuery = ref();
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('销毁人'),
    signatureAction: 122,
    disabled: true,
  },
  {
    label: t('复核人'),
    signatureAction: 123,
    options: [],
  },
]);
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
  const boundUser = await getQueryPositionBoundUserListByPermissionCodeApi({
    positionId: urlQuery.value.materialPositionId,
    permissionCode: '121020002000017',
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
    await destroyAndConsumeMobileApi({
      ...curParams.value,
      linkExplain: signValue.value.remark, // 来源/去向
      operatorId: signValue.value.userId1,
      reCheckerId: signValue.value.userId2,
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
