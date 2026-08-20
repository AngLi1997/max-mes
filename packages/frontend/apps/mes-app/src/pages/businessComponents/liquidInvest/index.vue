<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配液投入')"
      :confirm-text="(isAllInput && !hasUnmeasured) ? t('完成') : t('投料')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirmPage"
    >
      <view class="content">
        <view class="title_box">
          <Info
            :basic-items="infoItems"
            :info-data="liquidPreparation"
          />
        </view>
        <view class="scanning_box">
          <BMScan
            v-model="liquidScan"
            type="input"
            :placeholder="t('物料件号/容器编号')"
            @success="onScanSuccess"
            @fail="onScanFail"
            @confirm="onScanSuccess"
          />
        </view>
        <view class="table_box">
          <BMTable
            ref="tableRef"
            v-bind="tableProps"
            @selection-change="selectionChange"
          />
        </view>
      </view>
      <BMFormSelect
        v-model="liquidMonad"
        v-model:open="showliquidMonad"
        :title="t('配液单选择')"
        :options="checkboxModalOptions"
        required
        :field-names="{
          label: 'name',
          value: 'id',
        }"
        custom-class="liquid_select"
        @cancel="liquidMonadCancel"
        @confirm="liquidMonadConfirm"
      />
      <BMModal
        v-model="showEquipmentModel"
        :title="t('确认投入设备')"
        size="medium"
        @cancel="showEquipmentModel = false"
        @confirm="EquipmentSuccess"
      >
        <BMScan
          v-model="equipmentScan"
          type="input"
          :placeholder="t('设备编号')"
          @success="onMaterialSuccess"
          @fail="onScanFail"
          @confirm="onMaterialSuccess"
        />
        <BMInfoDisplay
          v-if="equipmentData.deviceName"
          :title="t('设备信息')"
          icon="shebei"
          :basic-items="[
            {
              label: t('设备名称'),
              field: 'deviceName',
            },
            {
              label: t('设备编号'),
              field: 'deviceCode',
            },
            {
              label: t('规格型号'),
              field: 'deviceSpecification',
            },
            {
              label: t('设备厂商'),
              field: 'deviceManufacturer',
            },
          ]"
          :info-data="equipmentData"
          is-show-one
        />
        <view
          v-else
          class="no-data-box"
        >
          <BMNoData
            :position="false"
            type="emptyData"
            :text="t('请扫描设备标签')"
          />
        </view>
      </BMModal>
      <BMMessageBox
        v-model="isSuccessModal"
        :title="t('是否完成配料投入')"
        :cancel-text="t('否')"
        :confirm-text="t('是')"
        @cancel="isSuccessModal = false"
        @confirm="isSuccessConfirm"
      />
      <BMSignModal
        v-model:show="showSign"
        v-model="signValue"
        show-remark
        :signature-data="submitData"
        :label-list="labelList"
        @confirm="signConfirm"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  getInputInstance,
  getQueryInputList,
  preparationInputBind,
  preparationInputComplete,
  preparationInputContainerCode,
  preparationInputOperate,
  queryPendingInputPlanList,
  scanPreparationInputMaterial,
} from '@/api';
import {
  BMBasicPage,
  BMFormSelect,
  BMInfoDisplay,
  BMLayout,
  BMMessageBox,
  BMModal,
  BMNoData,
  BMScan,
  BMSignModal,
  BMTable,
} from '@/BMComponents';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import Info from '@/pages/weighingComponents/info';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useTable } from './hooks/table.jsx';
import { useInvest } from './hooks/useInvest.js';

const { showNotify } = useNotify();

const { tableRef, tableProps, selectionChange } = useTable();
const { showliquidMonad, infoItems } = useInvest({});
const liquidScan = ref(); // 容器扫描
const { version } = getCurrentCopyRecordItem();
const {
  procedureStepModelId,
  recordItemId,
  recordVersionId,
  reusable,
  procedureStepId,
} = pageBasicDataRef.value;
const { batchNo, processId, processVersion, productPlanId }
    = urlQueryRef.value;
const liquidPreparation = ref({}); // 配液单信息
const submitData = ref({ storateMaterialNoList: [] }); // 提交信息
const isAllInput = ref(false);
const complete = ref(false);// 配液投入是否已完成
const hasUnmeasured = ref(false);// 配液单存在未完成量取的批次
// ------------------切换配料单配置--------------------
const liquidMonad = ref(); // 配液单
const checkboxModalOptions = ref([]);
// 点击取消
const liquidMonadCancel = () => {
  if (!liquidPreparation.value) {
    showliquidMonad.value = true;
  }
};
  // -------------------物料件扫描----------------------
const getMaterialByCode = async (deviceCode) => {
  try {
    if (!deviceCode) {
      return;
    }

    const data = {
      code: deviceCode,
      componentInstanceId: liquidPreparation.value.componentInstanceId,
      processId,
      processVersion,
    };
    // 校验物料件
    const { data: responseData } = await scanPreparationInputMaterial(data);
    const material = tableProps.data.find(
      item => item.storageMaterialNo === responseData?.no,
    );
    if (material?.inputStatus.value !== 1 || !material) {
      showNotify({
        type: 'danger',
        message: t('请扫描待投入的物料件'),
      });
      return;
    }
    // 向提交数据中添加已投入物料件编号
    submitData.value.storateMaterialNoList.push(responseData?.no);
    tableProps.data.forEach((item) => {
      if (item.storageMaterialNo === deviceCode || item.storageMaterialNo === responseData?.no) {
        item.inputStatus.value = 2;
      }
    });
  }
  catch (error) {
    error.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
const onScanSuccess = (code) => {
  if (!code) {
    toast.error(t('扫码失败'));
    return;
  }
  if (complete.value) {
    showNotify({
      message: t('配液投入已完成'),
      type: 'danger',
    });
    return;
  }
  getMaterialByCode(code);
};
  // -------------------设备确认------------------------
const equipmentScan = ref(); // 扫描设备
const showEquipmentModel = ref(false);
const equipmentData = ref({}); // 设备信息
const showSign = ref(false);
// 获取设备信息
const getEquipmentByCode = async (deviceCode) => {
  try {
    if (!deviceCode) {
      equipmentData.value = {};
      return;
    }
    // 校验设备
    const res = await preparationInputContainerCode({
      code: deviceCode,
      componentInstanceId: liquidPreparation.value.componentInstanceId,
      processId,
      processVersion,
    });
    if (!res.data.deviceId) {
      showNotify({
        type: 'danger',
        message: t('未查询到该设备信息'),
      });
      return;
    }
    equipmentData.value = res.data;
    // 向投料数据中添加投料设备id
    submitData.value.deviceId = res.data.deviceId;
  }
  catch (error) {
    equipmentData.value = {};
    error.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
// 获取扫描信息
const onMaterialSuccess = (code) => {
  if (!code) {
    toast.error(t('扫码失败'));
    return;
  }
  getEquipmentByCode(code);
};
const queryInfo = ref({});
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
// 确认投入设备,打开签名
const EquipmentSuccess = () => {
  if (!submitData.value.deviceId) {
    showNotify({
      type: 'danger',
      message: t('请确认投入设备'),
    });
    return;
  }
  // 确认投入设备,打开签名弹窗
  showEquipmentModel.value = false;
  showSign.value = true;
  // 签名日志
  submitData.value = {
    ...submitData.value,
    preparationPlanId: liquidMonad.value,
    inputUserId: signValue.value.userId1,
    remark: signValue.value.remark,
    componentId: queryInfo.value.id,
    productPlanId,
    reuse: reusable,
    procedureStepModelId, // 工序步骤模型id
    copyVersion: version,
    batchNo,
    processId,
    processVersion,
    recordItemId,
    recordVersionId,
    procedureStepId,
  };
};
  // ------------------签名------------------------------
const labelList = ref([
  {
    label: t('投料人'),
    // 签名动作
    signatureAction: 105,
    options: null,
    disabled: true,
  },
]);
  // ----------------------------------------
const isSuccessModal = ref(false);
const onScanFail = () => {
  toast.error(t('扫码失败'));
};
  // 点击确定
const confirmPage = () => {
  if (!isAllInput.value) {
    // 判断列表中是否有待投料物料
    const flag = tableProps.data.find(item => item.inputStatus.value === 2);
    if (!flag) {
      showNotify({
        message: t('请添加物料件'),
        type: 'danger',
      });
      return;
    }
    // 未完成投料需扫描设备
    showEquipmentModel.value = true;
    return;
  }
  if (complete.value) {
    showNotify({
      message: t('配液投入已完成'),
      type: 'danger',
    });
    return;
  }
  isSuccessModal.value = true;
};
  // 返回
const toBack = () => {
  uni.navigateBack();
  initFillData2();
};
  // 完成投入
const isSuccessConfirm = async () => {
  try {
    await preparationInputComplete({
      componentInstanceId: liquidPreparation.value.componentInstanceId,
    });
    toBack();
  }
  catch (error) {
    error.message && showNotify({
      message: error.message,
      type: 'danger',
    });
  }
};

const queryInputList = async () => {
  const { data } = await getQueryInputList({
    componentInstanceId: liquidPreparation.value.componentInstanceId,
  });
  tableProps.data = [...data.inputList];

  isAllInput.value = tableProps.data.length > 0;
  // 校验是否列表中是否存在其他状态的物料件（待投料、未签名、投料中），若存在，不展示【完成】按钮
  tableProps.data.forEach((item) => {
    // 切换已投料状态
    // if(submitData.value.storateMaterialNoList.indexOf(item.storageMaterialNo) >= 0){
    //   item.weighInputStatus = 'FINISHED'
    // }
    if (item.inputStatus.value !== 3 && item.inputStatus.value !== 4) {
      isAllInput.value = false;
    }
  });
};

const signConfirm = async () => {
  showSign.value = false;
  try {
    await preparationInputOperate({
      ...submitData.value,
      inputUserId: signValue.value.userId1,
      remark: signValue.value.remark.value,
    });
    await queryInputList();
    submitData.value.storateMaterialNoList = [];
  }
  catch (error) {
    error.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
  // 查询已绑定的配液单
const getBindList = async () => {
  // 查询当前选择的投入组件选择的配液单信息
  const res = await getInputInstance({
    componentId: queryInfo.value.id, // 组件id
    copyVersion: version, // 复制版本
    procedureStepModelId, // 工序步骤模型id
    productPlanId, // 生产指令单id
    reuse: reusable, // 是否复用
  });
  liquidPreparation.value = res.data || {};
  // 必须选择配液单后才能进入配液投入功能
  if (!res.data) {
    showliquidMonad.value = true;
  }
  else {
    liquidMonad.value = liquidPreparation.value.planId;
    complete.value = res.data.complete;
    hasUnmeasured.value = res.data.hasUnmeasured;
    queryInputList();
  }
};
// 选择配液单,绑定
const liquidMonadConfirm = async () => {
  try {
    await preparationInputBind({
      componentId: queryInfo.value.id,
      copyVersion: version,
      preparationPlanId: liquidMonad.value, // 配液单id
      procedureStepModelId,
      productPlanId,
      reuse: reusable,
    });
    await getBindList();
  }
  catch (error) {
    liquidMonad.value = liquidPreparation.value.planId;
    if (!liquidPreparation.value) {
      showliquidMonad.value = true;
    }
    error.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
const initData = async () => {
  await getBindList();
  const { data } = await queryPendingInputPlanList({
    productPlanId,
  });
  checkboxModalOptions.value = [...data];
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
  // getTableData();
  initData();
});
</script>

<style>
  :deep(.liquid_select) {
  display: none;
}
</style>

<style lang="scss" scoped>
  .content {
  height: 100%;
  .title_box {
    height: 35.81rpx;
    padding: 0 7.86rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #f2f7ff;
    border-radius: 4.69rpx;
    .title_left {
      display: flex;
      align-items: center;
      font-size: 12.89rpx;
      font-weight: 513;
      .title_icon {
        padding: 2.93rpx;
        background-color: #d9e5ff;
        border-radius: 4.69rpx;
        text-align: center;
        margin-right: 5.86rpx;
      }
      .title {
        margin-right: 9.38rpx;
      }
    }
    .title_right {
      display: flex;
      align-items: center;
      color: #2871ff;
      font-weight: 513;
      .title_btn {
        margin-right: 4.69rpx;
      }
    }
  }
  .scanning_box {
    height: 35.81rpx;
    width: 50%;
    margin: 9.38rpx 0 9.38rpx 50%;
  }

  .table_box {
    height: calc(100% - 90.38rpx);
  }
}
.no-data-box {
  height: 140.63rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
