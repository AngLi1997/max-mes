<template>
  <BMModal v-model="show" :title="t('确认投入设备')" size="medium" @cancel="show = false" @confirm="confirm">
    <view>
      <BMScan
        v-model="scanValue"
        type="input"
        :placeholder="t('设备编号')"
        :allow-types="['04']"
        :error-type-placeholder="t('请扫描设备标签')"
        @success="onScanSuccess"
        @fail="onScanFail"
        @confirm="onScanSuccess"
      />
      <BMInfoDisplay
        v-if="infoData"
        :title="t('设备信息')"
        narrow
        :basic-items="basicItems"
        :info-data="infoData"
      />
      <view v-else class="no-data-box">
        <BMNoData :position="false" type="emptyData" :text="t('请扫描设备标签')" />
      </view>
    </view>
  </BMModal>
  <BMSignModal v-model:show="showSign" v-model="signValue" :signature-data="submitData" show-remark :label-list="labelList" @confirm="signConfirm" />
</template>

<script setup>
import {
  postScaScanDeviceCodeAndValidateStationIds,
} from '@/api';
import { BMInfoDisplay, BMModal, BMNoData, BMScan, BMSignModal } from '@/BMComponents';
import {
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, watch } from 'vue';
import {
  signValue,
  submitData,
} from '../../hooks/datas';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  componentId: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['update:modelValue', 'confirm']);
const show = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});

const basicItems = ref([
  {
    label: t('设备名称'),
    field: 'deviceName',
  },
  {
    label: t('设备编号'),
    field: 'deviceCode',
  },
]);

const infoData = ref(null);
const scanValue = ref('');
const showSign = ref(false);
const labelList = ref([
  {
    label: t('投料人'),
    // 签名动作
    signatureAction: 98,
    options: null,
    disabled: true,
  },
]);
const getMaterialByCode = async (deviceCode) => {
  try {
    if (!deviceCode) {
      infoData.value = {};
      return;
    }
    const { procedureStepModelId } = pageBasicDataRef.value;
    const { productPlanId } = urlQueryRef.value;
    const data = {
      deviceCode,
      productPlanId,
      procedureStepModelId,
      componentId: props.componentId,
    };
    const res = await postScaScanDeviceCodeAndValidateStationIds(data);
    if (!res.data.deviceId) {
      uni.showToast({
        title: t('未查询到该设备信息'),
        icon: 'error',
        duration: 2000,
        mask: true,
      });
      return;
    }
    infoData.value = res.data;
    // 向投料数据中添加投料设备id
    submitData.value.deviceId = res.data.deviceId;
  }
  catch (error) {
    infoData.value = {};
    error.message && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
};
  // 投入设备扫码
const onScanSuccess = (code) => {
  if (!code) {
    toast.error(t('扫码失败'));
    return;
  }
  getMaterialByCode(code);
};

const onScanFail = () => {
  toast.error(t('扫码失败'));
};
const confirm = () => {
  if (!submitData.value.deviceId) {
    return uni.showToast({
      title: t('请确认投入设备'),
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
  // 确认投入设备,打开签名弹窗
  showSign.value = true;
};
  // 签名成功
const signConfirm = () => {
  submitData.value.inputUserId = signValue.value.userId1;
  submitData.value.remark = signValue.value.remark;
  emit('update:modelValue', false);
  emit('confirm');
  showSign.value = false;
};

watch(
  () => props.modelValue,
  () => {
    scanValue.value = '';
    infoData.value = null;
  },
);
</script>

<style lang="scss" scoped>
.no-data-box {
  height: 140.63rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
