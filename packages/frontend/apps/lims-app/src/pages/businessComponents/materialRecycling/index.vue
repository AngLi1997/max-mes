<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料回收')"
      :default-padding="false"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <BMForm
        ref="formRef"
        v-bind="formProps"
      >
        <template #selectA>
          <div class="selectA">
            <div>{{ t('容器') }}</div>
            <BMScan
              v-model="scanDeviceCode"
              type="input"
              :placeholder="t('请输入')"
              :allow-types="['04']"
              :error-type-placeholder="t('请扫描容器')"
              @success="onScanSuccess"
              @fail="onScanFail"
              @confirm="onScanSuccess"
            />
          </div>
        </template>
      </BMForm>
      <!-- 打印机 -->
      <BmosPrinter ref="bmosPrinterInstance" @jump-over="skipPrinter" />
      <!-- 签名 -->
      <BMSignModal
        v-model:show="showSign"
        v-model="signValue"
        :title="t('物料回收')"
        :label-list="labelList"
        :show-remark="true"
        :signature-data="signatureData"
        @confirm="confirmSignPopup"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { getChargeRecycleMaterialChargeList, postChargeRecycleRecycle, postScanScanScanChargeRecycleContainer } from '@/api';
import { BMBasicPage, BMForm, BMLayout, BMScan, BMSignModal } from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';

const formRef = ref();
const scanDeviceCode = ref('');
const recoveryQuery = ref({}); // 路由参数
const showSign = ref(false);
const deviceId = ref('');
const printerId = ref('');
const signatureData = ref({});
const bmosPrinterInstance = ref(null);
const labelList = ref([
  {
    label: t('回收人'),
    signatureAction: 49,
  },
]);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
  remark: '',
});
  // 校验 正数,整数部分最多10位 小数部分最多9位
const validatorExpression = async (value) => {
  if (!value) {
    return Promise.reject(t('请输入回收量'));
  }
  if (value == 0) {
    return Promise.reject(t('正数,整数部分最多10位,小数位数最多为9位'));
  }
  else if (!/^\d{1,10}$|^\d{1,10}\.\d{1,9}$/.test(value)) {
    return Promise.reject(t('正数,整数部分最多10位,小数位数最多为9位'));
  }
  else {
    return Promise.resolve();
  }
};
  // 表单配置
const formProps = reactive({
  schemas: [
    {
      field: 'formTitle1',
      component: 'FormTitle',
      label: t('物料信息'),
      colProps: { // 不设置则默认为12
        span: 24,
      },
    },
    {
      field: 'materialId',
      component: 'BMFormSelect',
      label: t('物料名称'),
      required: true,
      componentProps: ({ formModel }) => {
        return {
          placeholder: t('请选择'),
          options: [],
          title: t('选择回收物料'),
          fieldNames: {
            label: 'materialName',
            value: 'materialId',
          },
          onConfirm: async (data) => {
            // 回显值
            formRef.value.setFormModels({
              materialName: data?.materialName,
              materialId: data?.materialId,
              mergeCode: data?.materialMergeCode,
              specification: data?.specification,
              feedQuantity: '',
              unitName: '',
              unitId: '',
            });
            formRef.value?.updateSchema({
              field: 'materialBatchId',
              componentProps: {
                options: data?.chargeBatchInfoList || [],
              },
            });
          },
          onClear: () => {
            formModel.materialName = '';
            formModel.materialId = '';
            formModel.mergeCode = '';
            formModel.specification = '';
            formModel.materialBatchId = '';
            formModel.feedQuantity = '';
            formModel.unitName = '';
            formModel.unitId = '';
            formRef.value?.updateSchema({
              field: 'materialBatchId',
              componentProps: {
                options: [],
              },
            });
          },
        };
      },
    },
    {
      field: 'mergeCode',
      component: 'Input',
      label: t('物料编码'),
      componentProps: {
        disabled: true,
        placeholder: t('自动填入'),
      },
    },
    {
      field: 'specification',
      component: 'Input',
      label: t('物料规格'),
      componentProps: {
        disabled: true,
        placeholder: t('自动填入'),
      },
    },
    {
      field: 'materialBatchId',
      component: 'BMFormSelect',
      label: t('物料批号'),
      required: true,
      componentProps: ({ formModel }) => {
        return {
          placeholder: t('请选择'),
          options: [],
          title: t('选择物料批号'),
          fieldNames: {
            label: 'materialBatchNo',
            value: 'materialBatchId',
          },
          onConfirm: async (data) => {
            formRef.value.setFormModels({
              materialBatchNo: data?.materialBatchNo,
              materialBatchId: data?.materialBatchId,
              feedQuantity: data?.quantity,
              unitName: data?.unitName,
              unitId: data?.unitId,
            });
          },
          onClear: () => {
            formModel.materialBatchNo = '';
            formModel.materialBatchId = '';
            formModel.feedQuantity = '';
            formModel.unitName = '';
            formModel.unitId = '';
          },
        };
      },
    },
    {
      field: 'feedQuantity',
      component: 'Input',
      label: t('投料量'),
      componentProps: {
        disabled: true,
        placeholder: t('自动填入'),
      },
    },
    {
      field: 'unitName',
      component: 'Input',
      label: t('单位'),
      componentProps: {
        disabled: true,
        placeholder: t('自动填入'),
      },
    },
    {
      field: 'formTitle2',
      component: 'FormTitle',
      label: '回收信息',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'recoveryAmount',
      component: 'Input',
      label: t('回收量'),
      rules: [{ required: true, validator: validatorExpression, message: t('请输入回收量') }],
      componentProps: {
        placeholder: t('请输入'),
      },
    },
    {
      field: 'container',
      component: 'Input',
      label: t('容器'),
      componentProps: {
        placeholder: t('请输入'),
      },
      slot: 'selectA',
    },
    {
      field: 'remark',
      component: 'Input',
      label: t('备注'),
      colProps: { // 不设置则默认为12
        span: 24,
      },
      componentProps: {
        placeholder: t('请输入'),
      },
    },
  ],
});
  // 返回
const toBack = () => {
  const query = ['id']
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(recoveryQuery.value[key])}`)
    .join('&');
  uni.redirectTo({
    url: `/pages/businessComponents/feedRecycling/index?${query}`,
  });
};
  // 容器 扫
const getEquipmentInfo = async () => {
  try {
    const data = { chargeRecycleId: recoveryQuery.value?.chargeRecycleComponentId, deviceCode: scanDeviceCode.value };
    const res = await postScanScanScanChargeRecycleContainer(data);
    deviceId.value = res.data.deviceId;
  }
  catch (error) {
    error.message && uni.showToast({
      title: error.message,
      icon: 'none',
      duration: 2000,
      mask: true,
    });
    scanDeviceCode.value = '';
    deviceId.value = '';
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
  scanDeviceCode.value = code;
  getEquipmentInfo();
};
const onScanFail = (err) => {
  uni.showToast({
    title: t('扫码失败'),
    icon: 'error',
    duration: 2000,
    mask: true,
  });
};
  // 表单校验
const submit = async () => {
  const res = await formRef.value?.validate();
  try {
    signatureData.value = {
      ...recoveryQuery.value,
      deviceId: scanDeviceCode.value ? deviceId.value : '',
      materialBatchId: res.materialBatchId,
      quantity: res.recoveryAmount,
      remark: res?.remark,
      unitId: res?.unitId,
      printerId: printerId.value?.id,
    };
    // 调打印机
    printerId.value = bmosPrinterInstance.value.print();
    if (printerId.value?.id) {
      showSign.value = true;
      signValue.value = {
        loginName1: '',
        password1: '',
        userId1: '',
        remark: '',
      };
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
const skipPrinter = async () => {
  showSign.value = true;
  signValue.value = {
    loginName1: '',
    password1: '',
    userId1: '',
    remark: '',
  };
};
  // 签名确定按钮
const confirmSignPopup = async () => {
  const operatorId = signValue.value.userId1; // 操作人id
  const res = await formRef.value?.validate();
  const data = { ...recoveryQuery.value, deviceId: scanDeviceCode.value ? deviceId.value : '', materialBatchId: res.materialBatchId, operatorId, quantity: res.recoveryAmount, remark: res.remark, unitId: res.unitId, printerId: printerId.value?.id };
  try {
    const res = await postChargeRecycleRecycle(data);
    if (res.code === 0) {
      uni.showToast({
        title: t('回收成功'),
        icon: 'none',
      });
      showSign.value = false;
      uni.navigateBack();
    }
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
  // 获取物料列表下拉
const getMaterialList = async () => {
  const data = { chargeRecycleComponentId: recoveryQuery.value?.chargeRecycleComponentId };
  const res = await getChargeRecycleMaterialChargeList(data);
  formRef.value?.updateSchema({
    field: 'materialId',
    componentProps: {
      options: [...res.data],
    },
  });
};

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  recoveryQuery.value = query;
  // #endif
  // #ifdef H5
  recoveryQuery.value = e;
  // #endif
  getMaterialList();
});
</script>

  <style lang="scss" scoped>
  .selectA {
  > div {
    font-size: 11.72rpx;
    margin-bottom: 5.86rpx;
  }
  :deep(.wd-input__value) {
    height: 36.33rpx;
  }
}
</style>
