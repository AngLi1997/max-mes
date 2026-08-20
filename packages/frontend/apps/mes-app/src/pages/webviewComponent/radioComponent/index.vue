<template>
  <BMLayout>
    <BMModal v-model="open" :title="t('数据单选')" size="large" closable :default-padding="false" @close="cancel">
      <scroll-view scroll-y="auto" class=" radio-box">
        <wd-radio-group v-model="value" shape="dot" @change="radioChange">
          <wd-radio v-for="option in options" :key="option.id" :value="option.label">
            {{ option.label }}
          </wd-radio>
        </wd-radio-group>
      </scroll-view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col v-if="!isRevise" :span="6">
            <wd-button type="info" block @click="reset">
              {{ t("重置") }}
            </wd-button>
          </wd-col>
          <wd-col :span="6">
            <wd-button type="info" block @click="enterNull">
              {{ t("录入空值") }}
            </wd-button>
          </wd-col>
          <wd-col :span="isRevise ? 18 : 12">
            <wd-button block @click="confirm">
              {{ t("确定") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMModal>
  </BMLayout>
</template>

<script setup>
import { BMLayout, BMModal } from '@/BMComponents';
import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
import { radioPopupConfirm } from '@/pages/webview/logic/fn/index.js';
import {
  setComponentNull,
  setComponentReset,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { showRadioComponentRef } from '@/pages/webview/utils/index.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { nextTick, ref, watch } from 'vue';

const props = defineProps({
  // 是否为修订
  isRevise: {
    type: Boolean,
    default: false,
  },
  component: {
    type: Object,
    default: () => ({}),
  },
});

const emit = defineEmits(['cancel', 'confirm']);
const open = ref(false);
const value = ref();
const componentData = ref(null);
const options = ref([]);

const init = async () => {
  const data = componentData.value;
  options.value = [];
  data?.componentDetail?.forEach((item) => {
    if (item.field === data.value) {
      value.value = item.field;
    }
    options.value.push({
      id: item.field,
      label: item.field,
    });
  });
  open.value = true;
};

useSubNvueLinster('page-radioComponent', (data) => {
  componentData.value = data;
  init();
});
watch(
  () => props.component,
  (val) => {
    if (val && val.fieldId) {
      componentData.value = val;
      init();
    }
  },
  {
    immediate: true,
  },
);

// 重置
const reset = () => {
  setComponentReset(componentData.value);
};
// 录入空值
const enterNull = () => {
  if (props.isRevise) {
    emit('confirm', {
      value: nullValueRef.value,
      emptyValue: true,
    });
    return;
  }
  setComponentNull(componentData.value);
};
// 确定
const confirm = () => {
  if (props.isRevise) {
    emit('confirm', {
      value: value.value,
      emptyValue: false,
    });
    return;
  }
  if (
    !value.value
  ) {
    uni.showToast({
      title: t('请选择数据'),
      icon: 'none',
    });
    return;
  }
  const data = {
    ...componentData.value,
    value: value.value,
    state: 'default',
    emptyValue: false,
  };
  radioPopupConfirm(data);
};
// 取消
const cancel = () => {
  if (props.isRevise) {
    emit('cancel');
    nextTick(() => {
      open.value = true;
    });
    return;
  }
  // #ifdef APP-PLUS
  uni.navigateBack();
  // #endif
  // #ifdef H5
  showRadioComponentRef.value = false;
  // #endif
};
</script>

<style>
page {
  background: transparent;
}
</style>

<style lang="scss" scoped>
.radio-box {
  padding: 0 9.38rpx;
  box-sizing: border-box;
}
</style>
