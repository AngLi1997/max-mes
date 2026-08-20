<template>
  <BMLayout>
    <BMBasicPage
      :title="t('生产投料')"
      :confirm-text="confirmText"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirm"
    >
      <view class="content">
        <view class="top-scan">
          <BMScan
            v-model="scanMaterialOrDeviceCode"
            type="input"
            :placeholder="t('物料件号/容器编号')"
            :allow-types="['01', '02', '04']"
            :error-type-placeholder="t('请扫描物料件或容器')"
            @success="onScanSuccess"
            @fail="onScanFail"
            @confirm="onScanSuccess"
          />
        </view>
        <view class="table-content">
          <BMTable ref="tableRef" v-bind="tableProps" />
        </view>
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="6">
            <wd-button type="info" block @click="materialRecycling">
              <div style="display: flex;align-items: center;">
                <wd-icon name="huishou" class-prefix="bmos-app-icon" size="14.06rpx" color="#434C59" />
                <div style="margin-left: 5.86rpx;">
                  {{ t("物料回收") }}
                </div>
              </div>
            </wd-button>
          </wd-col>
          <wd-col :span="6">
            <wd-button type="success" block @click="toBack">
              {{ t("完成") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button block @click="feed">
              {{ t("投料") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :title="t('物料投入')"
      :label-list="labelList"
      :show-remark="true"
      :signature-data="signatureData"
      @confirm="confirmSignPopup"
    />
    <!-- 设备 -->
    <EquipmentPopup
      ref="EquipmentPopupRef"
      v-model="showEquipmentPopup"
      :title="t('确认投入设备')"
      :charge-recycle-id="chargeRecycleId"
      @confirm="equipmentPopupConfirm"
    />
    <BMModal
      v-model="showDeletePopup"
      :show-title="false"
      size="small"
      custom-class="tip-popup"
      :close-on-click-modal="false"
      :confirm-text="t('移除')"
      @confirm="confirmDeletePopup"
      @cancel="cancelDeletePopup"
    >
      <view class="tip">
        {{ t("是否移除当前物料") }}
      </view>
    </BMModal>
    <BmosPrinter ref="bmosPrinterInstance" />
  </BMLayout>
</template>

<script setup lang="jsx">
import { getChargeRecycleMaterialList, getStorageMaterialPrintTag, postChargeRecycleCharge, postScanScanMaterialOrDevice } from '@/api';

import { BMBasicPage, BMIcon, BMLayout, BMModal, BMScan, BMSignModal, BMTable } from '@/BMComponents/index.js';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';

import { onLoad } from '@dcloudio/uni-app';
import { computed, ref } from 'vue';
import EquipmentPopup from './components/equipmentPopup.vue';

const EquipmentPopupRef = ref();
const showEquipmentPopup = ref(false);
const chargeRecycleId = ref('');
const bmosPrinterInstance = ref(null);
const recoveryQuery = ref({});
const signatureData = ref({});
const labelList = ref([
  {
    label: t('投料人'),
    signatureAction: 67,
    disabled: true,
  },
]);
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
  remark: '',
});
const scanMaterialOrDeviceCode = ref('');
const tableRef = ref();
const showDeletePopup = ref(false);
// 置灰row
const isRowDisabled = (item) => {
  return !item.newFlag;
};
const tableData = ref([]);
const deleteRow = ref({});
const queryInfo = ref({});

const viewDelete = (data) => {
  deleteRow.value = data;
  showDeletePopup.value = true;
};
  // 打印
const printing = async (row) => {
  const device = bmosPrinterInstance.value.print();
  if (device) {
    // 调打印接口
    try {
      const data = {
        body: {
          no: row.storageMaterialNo,
        },
        deviceId: device?.id,
        sceneId: row.categoryInfoType?.value === 1 ? 121002005 : 121001005,
      };
      await getStorageMaterialPrintTag(data);
      uni.showLoading({
        title: t('打印中...'),
        mask: true,
      });
      setTimeout(() => {
        uni.hideLoading();
      }, 2000);
    }
    catch (error) {
      // TODO handle the exception
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  }
};
const tableProps = computed(() => {
  return {
    trProps: (row) => {
      return {
        class: isRowDisabled(row) ? 'disabledRow' : '',
      };
    },
    pagination: false,
    data: tableData.value,
    tableColProps: [
      {
        label: '',
        fixed: 'left',
        prop: 'BMOSDelete',
        width: 50,
        customRender: ({ row }) => {
          if (row.newFlag) { // 新加的就可以删
            return <BMIcon onClick={() => viewDelete(row)} name="shanchu" size="18.75rpx" color="var(--bmos-color-error)" />;
          }
          return '';
        },
      },
      {
        label: t('物料名称'),
        prop: 'materialName',
      },
      {
        label: t('物料编码'),
        prop: 'materialMergeCode',
      },
      {
        label: t('物料批号'),
        prop: 'materialBatchNo',
      },
      {
        label: t('物料件号'),
        prop: 'storageMaterialNo',
      },
      {
        label: t('物料量'),
        width: 100,
        prop: 'quantity',
        showInput: (row) => {
          return !!row.newFlag;
        },

      },
      {
        label: t('单位'),
        prop: 'unitName',
      },
      {
        label: t('操作类型'),
        prop: 'operationType',
        customRender: ({ row }) => {
          return row.operationType?.label;
        },
      },
      {
        label: t('操作人'),
        prop: 'operator',
      },
      {
        label: t('操作时间'),
        prop: 'createTime',
      },
      {
        label: t('设备名称'),
        prop: 'equipmentName',
      },
      {
        label: t('设备编号'),
        prop: 'equipmentCode',
      },
      {
        prop: 'ACTION',
        label: t('操作'),
        width: 100,
        actions: ({ row }) => {
          return [
            {
              label: t('打印'),
              ifShow: () => row.operationType?.value === 'CHARGE' && row.useUp === false,
              onClick: () => {
                printing(row);
              },
            },
          ];
        },
      },
    ],
  };
});
// 物料/容器 扫
const getMaterialInfo = async () => {
  try {
    const data = {
      chargeRecycleId: chargeRecycleId.value,
      code: scanMaterialOrDeviceCode.value,
    };
    const res = await postScanScanMaterialOrDevice(data);
    const index = tableData.value.findIndex((item) => {
      return item.newFlag && item.storageMaterialNo === res.data.materialInfo?.storageMaterialNo;
    });
    if (index >= 0) {
      return uni.showToast({
        title: t('物料件已添加'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    if (res.data.materialInfo) {
      res.data.materialInfo.newFlag = true;
      res.data.materialInfo.date = Date.now();
      tableData.value.push(res.data.materialInfo || res.data?.deviceInfo);
      tableData.value.sort((a, b) => (a.newFlag && b.newFlag) && a.storageMaterialNo - b.storageMaterialNo);
    }
  }
  catch (error) {
    error.message && uni.showToast({
      title: error.message,
      icon: 'none',
      duration: 2000,
      mask: true,
    });
  }
};
const onScanSuccess = (code) => {
  if (!code) {
    uni.showToast({
      title: t('扫码失败'),
      icon: 'error',
      duration: 2000,
      mask: true,
    });
    return;
  }
  scanMaterialOrDeviceCode.value = code;
  getMaterialInfo();
};
const onScanFail = () => {
  uni.showToast({
    title: t('扫码失败'),
    icon: 'error',
    duration: 2000,
    mask: true,
  });
};

//   物料回收按钮
const materialRecycling = () => {
  const query = Object.keys(recoveryQuery.value)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(recoveryQuery.value[key])}`)
    .join('&');
  uni.redirectTo({
    url: `/pages/businessComponents/materialRecycling/index?${query}`,
  });
};

// 删除弹窗确定
const confirmDeletePopup = () => {
  tableData.value = tableData.value.filter(item => item.date !== deleteRow.value.date);
  showDeletePopup.value = false;
};

// 删除弹窗取消
const cancelDeletePopup = () => {
  showDeletePopup.value = false;
};
//   查询表格
const getTableData = async () => {
  try {
    const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
    const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
    const { version } = getCurrentCopyRecordItem();
    const data = {
      batchNo,
      componentId: queryInfo.value.id,
      copyVersion: version,
      procedureStepId,
      procedureStepModelId,
      processId,
      processVersion,
      productPlanId,
      recordItemId,
      recordVersionId,
      reuse: reusable,
    };
    const res = await getChargeRecycleMaterialList(data);
    tableData.value = res.data.list;
    chargeRecycleId.value = res.data.chargeRecycleComponentId;
    recoveryQuery.value = { ...data, chargeRecycleComponentId: res.data.chargeRecycleComponentId, id: queryInfo.value.id };
  }
  catch (error) {
    uni.showToast({
      title: error.message,
      icon: 'none',
      duration: 2000,
      mask: true,
    });
  }
};
// 投料按钮
const feed = () => {
  tableData.value = tableRef.value.getTableData();// 输入框修改不是响应式,所以需要再用实例方法赋值一次
  const tempTable = tableData.value.filter((item) => {
    return item.newFlag === true;
  });
  // 校验修改的投料量
  const validator1 = tempTable.every((item) => {
    return Number(item.quantity) > 0;
  });
  if (!validator1) {
    return uni.showToast({
      title: t('请输入正数'),
      icon: 'none',
      duration: 2000,
      mask: true,
    });
  }
  const validator2 = tempTable.every((item) => {
    return /^\d{1,10}(?:\.\d{1,9})?$/.test(item.quantity);
  });
  if (!validator2) {
    return uni.showToast({
      title: t('整数部分最多10位,小数位数最多为9位'),
      icon: 'none',
      duration: 2000,
      mask: true,
    });
  }
  showEquipmentPopup.value = true;
};
  // 设备弹框确认按钮
const equipmentPopupConfirm = () => {
  signatureData.value = { ...recoveryQuery.value };
  showSign.value = true;
  signValue.value = {
    loginName1: '',
    password1: '',
    userId1: '',
    remark: '',
  };
};
  // 返回
const toBack = () => {
  uni.navigateBack();
  initFillData2();
};
  // 签名确定按钮
const confirmSignPopup = async () => {
  const operatorId = signValue.value.userId1; // 操作人id
  const deviceId = EquipmentPopupRef.value.equipmentModel?.deviceId || ''; // 设备id
  const chargeMaterialList = [];
  tableData.value.forEach((item) => {
    if (item.newFlag) {
      chargeMaterialList.push({
        storageMaterialId: item?.storageMaterialId,
        chargeQuantity: item?.quantity,
        unitId: item?.unitId,
      });
    }
  });
  const data = { ...recoveryQuery.value, operatorId, deviceId, chargeMaterialList };
  if (data.chargeMaterialList.length === 0) {
    return uni.showToast({
      title: t('待投料物料件不能为空'),
      icon: 'none',
      duration: 2000,
      mask: true,
    });
  }
  try {
    await postChargeRecycleCharge(data);
    uni.showToast({
      title: t('投料成功'),
      icon: 'none',
    });
    showSign.value = false;
    // 投料完成后刷新表格
    getTableData();
    initFillData2();
  }
  catch (error) {
    uni.showToast({
      title: error.message,
      icon: 'none',
      duration: 2000,
      mask: true,
    });
  }
};

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  getTableData();
});
</script>

<style lang="scss" scoped>
// 新ui样式
.top-scan {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 9.38rpx;
  .wd-input {
    width: 50%;
  }
}
:deep(.tip-popup .modal-container .modal-content) {
  min-height: 44.53rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14.07rpx;
}
:deep(.disabledRow) {
  // pointer-events: none; //看得见 摸不着
  opacity: 0.5; /* 置灰效果，不过仍可见 */
}
.content {
  display: flex;
  flex-direction: column;
  height: 100%;
  .table-content {
    flex: 1;
    overflow: hidden;
  }
}
</style>
