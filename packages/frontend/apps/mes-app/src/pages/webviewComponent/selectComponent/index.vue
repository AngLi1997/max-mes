<template>
  <BMLayout>
    <BMModal v-model="open" :title="t('数据选择')" size="large" closable :default-padding="false" @close="cancel">
      <scroll-view scroll-y="auto" class=" radio-box">
        <wd-radio-group v-model="value" shape="dot" @change="radioChange">
          <wd-radio v-for="option in options" :key="option.id" :value="option.label">
            {{ option.label }}
          </wd-radio>
        </wd-radio-group>
        <wd-input v-model="content" :disabled="value !== manualValue" :placeholder="t('请输入')" />
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
import { reqDictDownApi } from '@/api/webViewApi.js';
import { BMLayout, BMModal } from '@/BMComponents';
import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
import { selectPopupConfirm } from '@/pages/webview/logic/fn/index.js';
import {
  setComponentNull,
  setComponentReset,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { showSelectComponentRef } from '@/pages/webview/utils/index.js';
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
const content = ref('');
const componentData = ref(null);
const options = ref([]);
const manualValue = ref(t('以上内容都没有，需手动填写'));
const dataSource = ref(1);

const init = async () => {
  const data = componentData.value;
  dataSource.value = data.configInfo ? data.configInfo.dataSource || 1 : 1;
  if (dataSource.value === 1) {
    options.value = data.configInfo ? data.configInfo.options || [] : [];
    options.value = options.value.map((item) => {
      return {
        id: item.value,
        label: item.text,
      };
    });
  }
  if (dataSource.value === 2) {
    const res = await reqDictDownApi({
      dictId: data.configInfo.dataDictionary,
    });
    options.value = res.data || [];
  }
  options.value.push({
    id: '',
    label: manualValue.value,
  });
  if (data.value) {
    value.value = manualValue.value;
    content.value = data.value;
    options.value.forEach((item) => {
      if (item.label === data.value) {
        value.value = item.label;
        content.value = '';
      }
    });
  }

  open.value = true;
};

useSubNvueLinster('page-selectComponent', (data) => {
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

// 单选值改变
const radioChange = () => {
  if (value.value !== manualValue.value) {
    content.value = '';
  }
};

// 获取扩展值
const getValueExtension = () => {
  let valueExtension = { dataSource: dataSource.value };
  options.value.forEach((item) => {
    if (item.label === value.value) {
      valueExtension = { ...valueExtension, ...item };
    }
  });
  return JSON.stringify(valueExtension);
};
// 重置
const reset = () => {
  setComponentReset(componentData.value);
};
// 录入空值
const enterNull = () => {
  if (props.isRevise) {
    emit('confirm', {
      value: nullValueRef.value,
      valueExtension: getValueExtension(),
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
      value: content.value || value.value,
      valueExtension: getValueExtension(),
      emptyValue: false,
    });
    return;
  }
  if (
    (!value.value && !content.value)
    || (value.value === manualValue.value && !content.value)
  ) {
    uni.showToast({
      title: t('请选择数据或录入内容'),
      icon: 'none',
    });
    return;
  }
  const data = {
    ...componentData.value,
    value: content.value || value.value,
    valueExtension: getValueExtension(),
    state: 'default',
    emptyValue: false,
  };
  selectPopupConfirm(data);
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
  showSelectComponentRef.value = false;
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
