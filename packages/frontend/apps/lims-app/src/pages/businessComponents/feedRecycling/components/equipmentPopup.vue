<!-- 确认投入设备弹筐 -->
<template>
  <BMModal
    v-model="open"
    :title="title"
    size="medium"
    @cancel="close"
    @confirm="confirm"
  >
    <view class="container">
      <BMScan
        v-model="deviceCode"
        type="input"
        :allow-types="['04']"
        :error-type-placeholder="t('请扫描设备标签')"
        @success="scanCode"
        @fail="onScanFail"
        @confirm="scanCode"
      />
      <BmosNoData v-if="!equipmentModel?.deviceCode" :text="t('请扫描设备标签')" type="emptyWorkbench" />
      <BMInfoDisplay
        v-else
        class="info-display"
        is-show-one
        icon="shebei"
        :title="t('设备信息')"
        :basic-items="equipmentItems"
        :info-data="equipmentModel"
      />
    </view>
  </BMModal>
  <wd-toast />
</template>

<script setup>
import { postScanScanScanChargeRecycleDeviceCode } from '@/api';
import { BMInfoDisplay, BMModal, BMScan } from '@/BMComponents/index.js';
import BmosNoData from '@/components/BmosNoData/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, watch } from 'vue';
import { useToast } from 'wot-design-uni';

const props = defineProps({
  title: {
    type: String,
    default: '',
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  chargeRecycleId: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['confirm', 'update:modelValue']);
const toast = useToast();
const open = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value);
  },
});
const deviceCode = ref();
const equipmentModel = ref({}); // 设备表单
const equipmentItems = [
  {
    label: t('设备名称'),
    field: 'deviceName',
  },
  {
    label: t('设备编码'),
    field: 'deviceCode',
  },
];
  // 设备弹框关闭
const close = () => {
  open.value = false;
};
  // 设备弹框确认
const confirm = () => {
  open.value = false;
  emit('confirm', '123');
};
  // 设备 扫 调接口
const getEquipmentInfo = async () => {
  try {
    const data = { chargeRecycleId: props.chargeRecycleId, deviceCode: deviceCode.value };
    const res = await postScanScanScanChargeRecycleDeviceCode(data);
    equipmentModel.value = res.data;
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
const onScanFail = () => {
  toast.error(t('扫码失败'));
};
const scanCode = (code) => {
  deviceCode.value = code;
  getEquipmentInfo();
};

watch(
  () => open.value,
  (val) => {
    if (val) {
      deviceCode.value = '';
      equipmentModel.value = {};
    }
  },
);

defineExpose({ equipmentModel });
</script>

<style lang="scss" scoped>
  .container {
    height: 158.2rpx;
  }
</style>
