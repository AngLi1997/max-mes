<template>
  <BMModal v-model="open" :title="showData.title" size="medium" @cancel="toBack" @confirm="confirm">
    <view class="form_box">
      <BMForm v-if="showData.type === 'handling'" ref="formRef" v-bind="handling_formProps" />
      <BMForm v-if="showData.type === 'toVoid'" ref="formRef" v-bind="toVoid_formProps" />
      <BMForm v-if="showData.type === 'reinvestigate'" ref="formRef" v-bind="reinvestigate_formProps" />
    </view>
  </BMModal>
  <BMSignModal
    v-model:show="showSign"
    v-model="signValue"
    :signature-data="submitData"
    :label-list="labelList"
    @confirm="signConfirm"
  />
</template>

<script lang="ts" setup>
import {
  exceptionCancel,
  exceptionHandle,
  exceptionReInvestigate,
} from '@/api';
import { BMForm, BMModal, BMSignModal } from '@/BMComponents';
import { timestampToTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, ref, watch } from 'vue';

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  showData: {
    type: Object,
    default: () => {},
  },
  rowData: {
    type: Object,
    default: () => {},
  },
});
const emit = defineEmits(['update:open', 'submit']);
const open = computed({
  get: () => props.open,
  set: (val) => {
    emit('update:open', val);
  },
});
const formRef = ref();
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('操作人'),
    // 签名动作
    signatureAction: 119,
    menuId: 121040001000007,
    currentUser: true,
  },
]);
const submitData = ref();
watch(
  () => props.open,
  () => {
    formRef.value?.resetForm();

    switch (props.showData.type) {
      case 'handling': // 异常处理
        labelList.value[0].signatureAction = 119;
        labelList.value[0].menuId = 121040001000009;
        break;
      case 'toVoid': // 异常作废
        labelList.value[0].signatureAction = 120;
        labelList.value[0].menuId = 121040001000010;
        break;
      case 'reinvestigate': // 重新调查
        labelList.value[0].signatureAction = 121;
        labelList.value[0].menuId = 121040001000011;
        break;
      default:
        break;
    }
  },
);

const confirm = async () => {
  // 表单校验
  formRef.value?.submit();
  const params = await formRef.value?.validate();
  submitData.value = { ...params };
  showSign.value = true;
};
const toBack = () => {
  emit('update:open', false);
};
const signConfirm = async () => {
  try {
    switch (props.showData.type) {
      case 'handling': // 异常处理
        await exceptionHandle({
          ...submitData.value,
          handleUserId: signValue.value.userId1,
          handleTime: timestampToTime(submitData.value.handleTime),
          id: props.rowData.id,
        });
        break;
      case 'toVoid': // 异常作废
        await exceptionCancel({
          ...submitData.value,
          cancelUserId: signValue.value.userId1,
          id: props.rowData.id,
        });
        break;
      case 'reinvestigate': // 重新调查
        await exceptionReInvestigate({
          ...submitData.value,
          reInvestigateUserId: signValue.value.userId1,
          id: props.rowData.id,
        });
        break;
      default:
        break;
    }

    showSign.value = false;
    emit('submit');
    toBack();
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

  // 表单配置
const handling_formProps = reactive({
  schemas: [
    {
      field: 'handleTime',
      component: 'BMFormDatePicker',
      label: t('处理时间'),
      required: true,
      colProps: {
        span: 24,
      },
      componentProps: {
        formatDate: 'yyyy-MM-dd HH:mm:ss',
      },
    },
    {
      field: 'handleResult',
      component: 'Textarea',
      label: t('处理结果'),
      required: true,
      colProps: {
        span: 24,
      },
      componentProps: {
        size: 'large',
      },
    },
  ],
});
const toVoid_formProps = reactive({
  schemas: [
    {
      field: 'cancelReason',
      component: 'Textarea',
      label: t('作废原因'),
      required: true,
      colProps: {
        span: 24,
      },
    },
  ],
});
const reinvestigate_formProps = reactive({
  schemas: [
    {
      field: 'reInvestigateReason',
      component: 'Textarea',
      label: t('重新调查原因'),
      required: true,
      colProps: {
        span: 24,
      },
    },
  ],
});
</script>

<style lang="scss" scoped>
.form_box {
  padding: 13rpx 0;
}
</style>
