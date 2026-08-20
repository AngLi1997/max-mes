<template>
  <BMModal
    v-model="show" :title="t('数据修订')" size="large" :close-on-click-modal="false" overflow="hidden"
    max-height="unset"
    @cancel="show = false" @confirm="reviseConfirm"
  >
    <view style="height: 336.91rpx">
      <view v-if="productionRevision" class="sub-title">
        {{ t("在生产修订功能修订数据将自动记录数据异常，是否修订？") }}
      </view>

      <BMForm ref="formRef" v-bind="formProps" />
      <BMSign
        ref="signRef"
        v-model="signValue"
        v-model:current-time="currentTime"
        :label-list="labelList"
        :signature-data="params"
      />
    </view>
  </BMModal>
</template>

<script setup>
import { postModifyExecuteDataApi } from '@/api/webViewApi.js';
import { BMForm, BMModal, BMSign } from '@/BMComponents/index.js';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  newSignOptionsRef,
  pageBasicDataRef,
  productionRevision,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  getSignFormat,
} from '@/pages/webview/utils/fns.js';
import { isArray } from '@/utils/is.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { computed, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useDataRevision } from './hooks/useDataRevision.jsx';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  componentData: {
    type: Object,
    default: () => ({}),
  },
});

const emit = defineEmits(['update:modelValue', 'update']);

const { showNotify } = useNotify();

const show = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});

const currentTime = ref('');
const signFormat = ref('yyyy-MM-dd HH:mm:ss');
const signRef = ref();
const signValue = ref({
  userName1: '',
  userName2: '',
  loginName1: '',
  loginName2: '',
  password1: '',
  password2: '',
  userId1: '',
  userId2: '',
});
const params = computed(() => {
  const copyRecordItem = getCurrentCopyRecordItem();
  const data = {};
  if (productionRevision.value) {
    if (Array.isArray(props.componentData.value)) {
      data.originalValue = JSON.stringify(props?.componentData?.value);
    }
    else {
      data.originalValue = props?.componentData.value || '';
    }
  }
  return {
    ...data,
    batchNo: urlQueryRef.value.batchNo,
    componentType: props.componentData.componentType,
    copyVersion: copyRecordItem.version,
    fieldId: props.componentData.fieldId,
    procedureStepId: pageBasicDataRef.value.procedureStepId,
    processId: urlQueryRef.value.processId,
    processVersion: urlQueryRef.value.processVersion,
    productPlanId: urlQueryRef.value.productPlanId,
    recordItemId: pageBasicDataRef.value.recordItemId,
    recordVersionId: pageBasicDataRef.value.recordVersionId,
    processChangeNumber: urlQueryRef.value.processChangeNumber,
    procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
    procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
    reuse: pageBasicDataRef.value.reusable,
    operationTime: currentTime.value,
    operationUser: signValue.value.userId1,
    reviewUser: signValue.value.userId2,
    reviewTime: currentTime.value,
  };
});

const labelList = computed(() => {
  if (productionRevision.value) {
    return [
      {
        label: t('修订人'),
        // 签名动作
        signatureAction: 115,
        menuId: '121010004000003',
      },
      {
        label: t('复核人'),
        // 签名动作
        signatureAction: 116,
        menuId: '121010004000004',
      },
    ];
  }
  else {
    return [
      {
        label: t('修订人'),
        // 签名动作
        signatureAction: 3,
        options: newSignOptionsRef.value,
      },
      {
        label: t('复核人'),
        // 签名动作
        signatureAction: 4,
        options: newSignOptionsRef.value,
      },
    ];
  }
});

const { formRef, formProps } = useDataRevision({ props });

const reviseConfirm = async () => {
  await formRef.value.validate();
  try {
    await signRef.value?.checkSign();
    const values = formRef.value.getFormValues();
    if (values.newValue === props.componentData.value) {
      showNotify({
        type: 'warning',
        message: t('新值与原值一致'),
      });
      return;
    }
    // 复选框的新值与原值一致校验
    if (props.componentData.componentType === 'CHECKBOX') {
      if (
        isArray(values.newValue)
        && values.newValue?.every(item =>
          props.componentData.value.includes(item),
        )
        && values.newValue.length === props.componentData.value.length
      ) {
        showNotify({
          type: 'warning',
          message: t('新值与原值一致'),
        });
        return;
      }
    }
    let value = values.newValue;
    if (Array.isArray(value)) {
      value = JSON.stringify(value);
    }
    if (props.componentData.componentType === 'SUBMIT_SIGN' || props.componentData.componentType === 'REVIEW_SIGN') {
      value = `${signValue.value.userName1} ${format(currentTime.value, signFormat.value)}`;
    }
    await postModifyExecuteDataApi({
      ...params.value,
      remark: values.remark,
      value,
      valueExtension: values.valueExtension,
      ...(value === nullValueRef.value ? { emptyValue: true } : { emptyValue: false }),
    });
    initFillData2();
    show.value = false;
    emit('update', {
      ...props.componentData,
      value: values.newValue,
    });
  }
  catch (error) {
    console.log(error);
  }
};
watch(
  () => props.modelValue,
  async (val) => {
    if (val) {
      formRef.value?.resetForm();
      signValue.value = {
        userName1: '',
        userName2: '',
        loginName1: '',
        loginName2: '',
        password1: '',
        password2: '',
        userId1: '',
        userId2: '',
      };
      signFormat.value = await getSignFormat();
      currentTime.value = '';
    }
  },
);
</script>

<style lang="scss" scoped>
.sub-title {
  font-size: 11.72rpx;
  font-weight: 400;
  line-height: 14.06rpx;
  text-align: center;
  color: var(--bmos-color-text-desc);
  margin-bottom: 14.06rpx;
}
</style>
