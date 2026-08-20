<template>
  <wd-input
    v-model="value"
    type="text"
    :placeholder="placeholder"
    use-suffix-slot
    :readonly="type === 'select'"
    custom-class="scan-input"
    @confirm="onConfirm"
    @click="onSelect"
  >
    <template #suffix>
      <view class="scan-button">
        <wd-icon
          v-if="type === 'select' && !value"
          name="jiantou-you"
          size="14.06rpx"
          color="#434C59"
          class-prefix="bmos-app-icon"
          style="margin-right: 11.72rpx"
          @click.stop="onSelect"
        />
        <wd-icon
          v-if="type === 'select' && value"
          name="qingchu"
          size="14.06rpx"
          color="#797C80"
          class-prefix="bmos-app-icon"
          style="margin-right: 11.72rpx"
          @click.stop="onClear"
        />
        <view v-if="showIcon" class="input-text-icon scan-item" @click.stop="onScan">
          <wd-icon name="saomiao" class-prefix="bmos-app-icon" size="14.06rpx" color="#2871FF" />
          <wd-button v-if="showAppScanText" type="text">
            {{ t('扫描识别') }}
          </wd-button>
        </view>
      </view>
    </template>
  </wd-input>
  <BMScanSerialPort v-model="showScan" />
  <BMMessageBox
    v-model="isOpenMessage" :title="t('扫码串口')" :content="t('未绑定扫码串口，是否配置扫码串口？')" @cancel="isOpenMessage = false"
    @confirm="messageConfirm"
  />
</template>

<script setup>
import { BMMessageBox, BMScanSerialPort } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { useScan } from '@/utils/useScan.js';
import { computed, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';

const props = defineProps({
  placeholder: {
    type: String,
    default: () => t('请输入'),
  },
  type: {
    type: String,
    default: 'input',
  },
  // 01:原辅包 02:中间品 03:暂存货位 04:设备 05:房间 06:皮重
  allowTypes: {
    type: Array,
    default: () => [],
  },
  errorTypePlaceholder: {
    type: String,
    default: () => t('不支持的类型'),
  },
  modelValue: {
    type: String,
    default: '',
  },
  showIcon: {
    type: Boolean,
    default: true,
  },
  showAppScanText: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits([
  'success',
  'fail',
  'complete',
  'update:modelValue',
  'select',
  'confirm',
  'clear',
]);

const { showNotify } = useNotify();

const showScan = ref(false);

const success = (res) => {
  const { result } = res;
  if (!result) {
    return;
  }
  const type = result.slice(0, 2);
  const code = result.slice(2);
  if (props.allowTypes.length && !props.allowTypes.includes(type)) {
    showNotify({ type: 'warning', message: props.errorTypePlaceholder });
    return;
  }
  emit('success', code);
};

const { bmosScanCode, isOpenMessage } = useScan();
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});

const messageConfirm = () => {
  isOpenMessage.value = false;
  showScan.value = true;
};

const onConfirm = () => {
  if (value.value === '') {
    return;
  }
  emit('confirm', value.value);
};
const onScan = () => {
  try {
    bmosScanCode({
      success,
      fail: (error) => {
        emit('fail', error);
      },
      complete: (res) => {
        emit('complete', res);
      },
    });
  }
  catch (error) {
    console.log('scan error', error);
  }
};

const onSelect = () => {
  if (props.type === 'select') {
    emit('select');
  }
};
const onClear = () => {
  value.value = '';
  if (props.type === 'select') {
    emit('clear');
  }
};
watch(() => value.value, (newValue, oldValue) => {
  if (props.type === 'input' && newValue?.length < oldValue?.length) {
    emit('clear');
  }
});
</script>

<style lang="scss" scoped>
.scan-input {
  background-color: #f2f7ff;
  border: 0.59rpx solid #b5d4ff;
  :deep(.wd-input__inner) {
    height: 32.81rpx;
    font-size: 11.72rpx;
    .uni-input-wrapper {
      padding: 9.38rpx;
    }
  }
}
.scan-button {
  display: flex;
  align-items: center;
  z-index: 9;
  position: relative;
  .select-item {
    width: 17.58rpx;
    text-align: right;
  }
  .scan-item {
    padding: 0 0 0 9.38rpx;
    display: flex;
    align-items: center;
  }
}
</style>
