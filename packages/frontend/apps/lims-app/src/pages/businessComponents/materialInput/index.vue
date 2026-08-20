<!-- 物料投入 -->
<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料投入')"
      :confirm-text="confirmText"
      :loading="loading"
      @left-click="cancelPage"
      @cancel="cancelPage"
      @confirm="confirmPage"
    >
      <view class="content">
        <view class="scan-box">
          <view class="scan-item">
            <BMScan
              v-model="scanValue"
              type="input"
              :placeholder="t('物料件号/容器编号')"
              :allow-types="['01', '02', '04']"
              :error-type-placeholder="t('请扫描物料件或容器标签')"
              @success="onScanSuccess"
              @fail="onScanFail"
              @confirm="onScanSuccess"
            />
          </view>
        </view>
        <view class="table-box">
          <BMTable
            ref="tableRef"
            v-bind="tableProps"
          />
        </view>
      </view>
      <!-- 设备确认弹窗 -->
      <EquipmentConfirm v-model="showEquipmentConfirm" :component-id="queryInfo.id" @confirm="changeTable" />
      <!-- 完成提示 -->
      <BMMessageBox
        v-model="showMessageBox"
        :title="t('是否完成配料投入')"
        @confirm="messageConfirm"
        @cancel="showMessageBox = false"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  getInputListList,
  getInstanceByProps,
  inputWeighCentre,
  reqScanStorageMaterialWithCommonValidateApi,
  weighFinishInput,
} from '@/api';
import { BMBasicPage, BMLayout, BMMessageBox, BMScan, BMTable } from '@/BMComponents';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { computed, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import EquipmentConfirm from './components/equipmentConfirm/index.vue';
import {
  isEquipented,
  submitData,
} from './hooks/datas';
import { useTable } from './hooks/useTable.jsx';

const { tableRef, tableProps } = useTable();
const { showNotify } = useNotify();

const confirmText = computed(() => {
  return !isEquipented.value ? t('投料') : t('完成');
});
const queryInfo = ref();
const loading = ref(false);
const { version } = getCurrentCopyRecordItem();
const { procedureStepModelId, reusable } = pageBasicDataRef.value;
const { productPlanId } = urlQueryRef.value;
const componentInstanceId = ref('');
const scanValue = ref('');
const showEquipmentConfirm = ref(false);
const showMessageBox = ref(false);
const getTableList = async () => {
  const { data } = await getInputListList({
    componentInstanceId: componentInstanceId.value,
  });
  tableProps.data = data.list;
  isEquipented.value = true;
  // 校验是否列表中是否存在其他状态的物料件（待投料、未签名、投料中），若存在，不展示【完成】按钮
  tableProps.data.map((item) => {
    if (item.weighInputStatus?.value !== 3 && item.weighInputStatus?.value !== 4) {
      isEquipented.value = false;
    }
    return item;
  });
  if (isEquipented.value?.length === 0) {
    isEquipented.value = false;
  }
  isEquipented.value = isEquipented.value && data.canFinished;
};
const getMaterialByCode = async (deviceCode) => {
  try {
    if (!deviceCode) {
      infoData.value = {};
      return;
    }
    const { data } = await reqScanStorageMaterialWithCommonValidateApi({
      no: deviceCode,
      productPlanId,
    });
    // if (!data) {
    //   showNotify({
    //     type: 'danger',
    //     message: t('物料件号不存在'),
    //   });
    //   return;
    // }
    // 不存在物料件号,但是存在容器
    // if (!data.materialNo && data.containerNo) {
    //   showNotify({
    //     type: 'danger',
    //     message: t('容器无物料'),
    //   });
    //   return;
    // }
    const materialNo = data.materialNo;
    const material = tableProps.data.find(item => item.storageMaterialNo === materialNo);
    if (!material || material.weighInputStatus?.value !== 1) {
      showNotify({
        type: 'danger',
        message: t('请扫描待投入的物料件'),
      });
      return;
    }
    // if (!data.isAvailable) {
    //   showNotify({
    //     type: 'danger',
    //     message: t('物料件未生效'),
    //   });
    //   return;
    // }
    // const params = {
    //   componentInstanceId: componentInstanceId.value,
    //   ingredientPlanId: '',
    //   no: materialNo,
    // };
    // 向提交数据中添加已投入物料件编号
    submitData.value.storateMaterialNoList.push(materialNo);
    tableProps.data.map((item) => {
      if (item.storageMaterialNo === materialNo) {
        item.weighInputStatus.value = 2;
      }
      return item;
    });
  }
  catch (error) {
    error.message && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
};
const onScanSuccess = (code) => {
  if (!code) {
    toast.error(t('扫码失败'));
    return;
  }
  getMaterialByCode(code);
};

// 修改物料件状态
const changeTable = async () => {
  try {
    loading.value = true;
    await inputWeighCentre(submitData.value);
    submitData.value.storateMaterialNoList = [];
    submitData.value.deviceId = '';
    getTableList();
  }
  catch (error) {
    error.message && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
  finally {
    loading.value = false;
  }
};

const onScanFail = () => {
  toast.error(t('扫码失败'));
};

const cancelPage = () => {
  uni.navigateBack();
  initFillData2();
};
const confirmPage = async () => {
  if (!isEquipented.value) {
    // 未投料
    if (submitData.value.storateMaterialNoList.length === 0) {
      showNotify({
        type: 'danger',
        message: t('请投入物料件'),
      });
      return;
    };
    showEquipmentConfirm.value = true;
    return;
  }
  showMessageBox.value = true;
};
const messageConfirm = async () => {
  try {
    loading.value = true;
    await weighFinishInput({
      componentInstanceId: componentInstanceId.value,
    });
    cancelPage();
  }
  catch (error) {
    isEquipented.value = false;
    error.message && showNotify({
      message: error.message,
      type: 'danger',
    });
  }
  finally {
    loading.value = false;
  }
};
const initData = async () => {
  const { data } = await getInstanceByProps({
    componentId: queryInfo.value.id,
    copyVersion: version,
    procedureStepModelId,
    productPlanId,
    reuse: reusable,
  });
  componentInstanceId.value = data.id;
  submitData.value = {
    componentInstanceId: data.id,
    deviceId: '',
    inputUserId: '',
    remark: '',
    storateMaterialNoList: [],
  };
  getTableList();
};
onLoad(async (e) => {
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
  initData();
});
</script>

<style lang="scss" scoped>
  .content {
  height: 100%;
  overflow: hidden;
  .scan-box {
    margin-bottom: 9.38rpx;
    display: flex;
    justify-content: flex-end;
    .scan-item {
      width: 50%;
    }
  }
  .table-box {
    height: calc(100% - 9.38rpx - 36.33rpx);
    width: 100%;
    overflow-x: auto;
    :deep(.bm-table) {
      width: 1370px;
    }
  }
}
</style>
