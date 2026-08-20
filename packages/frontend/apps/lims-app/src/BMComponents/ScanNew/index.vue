<template>
  <BMScanSerialPort v-model="showSerialPortSelect" />
  <BMMessageBox
    v-model="isOpenMessage" :title="t('扫码串口')" :content="t('未绑定扫码串口，是否配置扫码串口？')" @cancel="isOpenMessage = false"
    @confirm="messageConfirm"
  />
</template>

<script setup>
import { BMMessageBox, BMScanSerialPort } from '@/BMComponents';
import { useBMScan } from '@/BMUtils/useBMScan.js';
import { SCAN_SERIAL_PORT } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';

const emit = defineEmits(['success']);
const isOpenMessage = ref(false);
const showSerialPortSelect = ref(false);
const { BMScanCode, openSerialPort } = useBMScan({
  callback: (res) => {
    emit('success', res);
  },
  showSerialPortSelect,
});

const messageConfirm = () => {
  isOpenMessage.value = false;
  showScan.value = true;
  if (getStorageSync(SCAN_SERIAL_PORT)?.path) {
    openSerialPort();
  }
};

defineExpose({
  BMScanCode,
});
</script>

<style lang="scss" scoped>

</style>
