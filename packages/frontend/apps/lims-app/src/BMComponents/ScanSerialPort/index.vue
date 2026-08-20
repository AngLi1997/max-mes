<template>
  <BMModal v-model="showScan" :title="t('扫码串口')" size="large" :closable="false" :default-padding="false" @close="portCancel" @cancel="portCancel" @confirm="portConfirm">
    <scroll-view scroll-y="auto" class=" radio-box">
      <wd-radio-group v-model="value" shape="dot">
        <wd-radio v-for="option in options" :key="option.path" :value="option.path">
          {{ option.friendlyName }}
        </wd-radio>
      </wd-radio-group>
    </scroll-view>
    <template #buttons>
      <wd-row :gutter="8">
        <wd-col :span="showNeverShowBtn ? 6 : 12">
          <wd-button type="info" block @click="portCancel">
            {{ t('取消') }}
          </wd-button>
        </wd-col>
        <wd-col v-if="showNeverShowBtn" :span="6">
          <wd-button type="info" block @click="handleNeverShow">
            {{ t('不再显示') }}
          </wd-button>
        </wd-col>
        <wd-col :span="12">
          <wd-button type="primary" block @click="portConfirm">
            {{ t('确定') }}
          </wd-button>
        </wd-col>
      </wd-row>
    </template>
  </BMModal>
</template>

<script setup>
import { BMModal } from '@/BMComponents';
import { NEVER_SHOW_SCAN_PORT, SCAN_SERIAL_PORT } from '@/utils/uniStorage/const.js';
import { getStorageSync, setStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, onMounted, ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  showNeverShowBtn: {
    type: Boolean,
    default: true,
  },
});
const emit = defineEmits(['update:modelValue', 'confirm']);
const value = ref('');
const showScan = computed({
  get: () => {
    return props.modelValue;
  },
  set(val) {
    emit('update:modelValue', val);
  },
});
const options = ref([]);

const handleNeverShow = () => {
  setStorageSync(NEVER_SHOW_SCAN_PORT, true);
  portCancel();
};
const portCancel = () => {
  showScan.value = false;
};
const portConfirm = () => {
  setStorageSync(SCAN_SERIAL_PORT, {
    path: value.value,
    friendlyName: options.value.find(item => item.path === value.value)?.friendlyName || '',
  });
  showScan.value = false;
  emit('confirm');
};
onMounted(async () => {

});
watch(
  () => props.modelValue,
  async (val) => {
    if (val) {
      const data = await window?.serialPortAPI?.getSerialPortList();
      options.value = data || [];
      value.value = getStorageSync(SCAN_SERIAL_PORT)?.path || '';
    }
  },
);
</script>

<style lang="scss" scoped>
.radio-box {
  padding: 0 9.38rpx;
  box-sizing: border-box;
}
</style>
