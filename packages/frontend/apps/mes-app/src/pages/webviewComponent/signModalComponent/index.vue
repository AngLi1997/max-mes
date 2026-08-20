<template>
  <BMLayout>
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      v-model:current-time="currentTime"
      :label-list="labelList"
      :title="title"
      :sub-title="productionRevision ? t('在生产修订保存数据将自动记录异常，是否保存？') : ''"
      :signature-data="signatureData"
      @cancel="cancelSign"
      @confirm="confirmSign"
    />
    <BMMessageBox
      v-model="showRefresh"
      :title="t('记录数据已存在，是否刷新')"
      size="small"
      :cancel-text="t('否')"
      :confirm-text="t('刷新')"
      @cancel="cancelRefresh"
      @confirm="confirmRefresh"
    />
  </BMLayout>
</template>

<script setup>
import { saveRecordDataApi } from '@/api/webViewApi.js';
import { BMLayout, BMMessageBox, BMSignModal } from '@/BMComponents';
import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
import {
  clearCacheComponentsData,
  constructBatchSaveData,
  getSignComponentData,
  initFillData2,
  newSignOptionsRef,
  productionRevision,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  getSignFormat,
} from '@/pages/webview/utils/fns.js';
import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { computed, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();

const SignatureActionEnum = {
  SAVE: 0,
  SUBMIT_SIGN: 1,
  REVIEW_SIGN: 2,
};

const showSign = ref(false);
const showRefresh = ref(false);
const componentData = ref(null);
const componentType = ref('SAVE');
const currentTime = ref('');
const signFormat = ref('yyyy-MM-dd HH:mm:ss');

const signValue = ref({
  loginName1: '',
  userName1: '',
  password1: '',
  userId1: '',
  loginName2: '',
  userName2: '',
  password2: '',
  userId2: '',
});
const labelList = computed(() => {
  if (productionRevision.value) {
    return [
      {
        label: t('保存人'),
        // 签名动作
        signatureAction: 113,
        menuId: '121010004000001',
      },
      {
        label: t('复核人'),
        // 签名动作
        signatureAction: 114,
        menuId: '121010004000002',
      },
    ];
  }
  return [
    {
      label: t('账号'),
      // 签名动作
      signatureAction: SignatureActionEnum[componentType.value],
      options: newSignOptionsRef.value,
    },
  ];
});
const signatureData = computed(() => {
  try {
    if (componentType.value === 'SAVE') {
      return constructBatchSaveData(signValue.value);
    }
    return getSignComponentData({
      fieldId: componentData.value.fieldId,
      account: signValue.value.loginName1,
      password: signValue.value.password1,
      userId: signValue.value.userId1,
      componentType: componentType.value,
      state: 'default',
      value: `${signValue.value.userName1} ${format(currentTime.value, signFormat.value)}`,
      appTime: currentTime.value,
    });
  }
  catch (error) {
    return {};
  }
});
const titleMap = new Map([
  ['SAVE', t('数据保存')],
  ['SUBMIT_SIGN', t('提交签名')],
  ['REVIEW_SIGN', t('复核签名')],
]);
const title = computed(() => {
  return titleMap.get(componentType.value);
});

const init = async () => {
  showSign.value = true;
  const data = componentData.value;
  componentType.value = data.componentType;
  signFormat.value = await getSignFormat();
};

useSubNvueLinster('page-signModalComponent', (data) => {
  componentData.value = data;
  init();
});

const saveFn = async () => {
  try {
    const data = constructBatchSaveData(signValue.value);
    await saveRecordDataApi(data);
    clearCacheComponentsData();
    initFillData2();
    showNotify({
      message: t('记录保存成功'),
      type: 'success',
    });
    setTimeout(() => {
      H5AppNavigateBack();
    }, 500);
  }
  catch (e) {
    if (e.code === 8208003) {
      showRefresh.value = true;
    }
    else {
      showNotify({
        message: e.message || t('保存失败'),
        type: 'danger',
      });
    }
  }
};
const signFn = async () => {
  const data = getSignComponentData({
    fieldId: componentData.value.fieldId,
    account: signValue.value.loginName1,
    password: signValue.value.password1,
    userId: signValue.value.userId1,
    componentType: componentType.value,
    state: 'default',
    value: `${signValue.value.userName1} ${format(currentTime.value, signFormat.value)}`,
    appTime: currentTime.value,
  });
  try {
    await saveRecordDataApi(data);
    initFillData2();
    uni.showToast({
      title: t('签名保存成功'),
      icon: 'none',
    });
    H5AppNavigateBack();
  }
  catch (e) {
    if (e.code === 8208003) {
      showRefresh.value = true;
    }
    else {
      uni.showToast({
        title: t('保存失败'),
        icon: 'none',
      });
    }
  }
};
const cancelRefresh = () => {
  showRefresh.value = false;
  H5AppNavigateBack();
};
const confirmRefresh = () => {
  showRefresh.value = false;
  initFillData2();
  H5AppNavigateBack();
};
const actionsMap = new Map([
  ['SAVE', saveFn],
  ['SUBMIT_SIGN', signFn],
  ['REVIEW_SIGN', signFn],
]);

// 取消签名
const cancelSign = () => {
  H5AppNavigateBack();
};
  // 确认签名
const confirmSign = () => {
  actionsMap.get(componentType.value)();
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
