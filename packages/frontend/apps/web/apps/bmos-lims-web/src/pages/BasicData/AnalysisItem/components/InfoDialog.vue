<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :okButtonProps="okButtonProps"
    @okModal="ok"></BMModalForm>
</template>

<script setup lang="ts">
import { createVNode, reactive, ref } from 'vue';
import { t } from '@bmos/i18n';
import { BMModalForm, ModalFormInstance, FormProps } from '@bmos/components';
import { MODAL_STATUS } from '../types/enum';
import {
  saveAnalyze,
  updateAnalyze
} from '@/services/index';
import { message } from 'ant-design-vue';

const modalFormRef = ref<ModalFormInstance>();
const open = ref<boolean>(false);
const title = ref<string>(t('新增'));
const modalType = ref<string>(MODAL_STATUS.ADD);
const okButtonProps = ref({});

const emit = defineEmits(['submitSuccess']);

const formProps = reactive<FormProps>({
  initialValues: {},
  labelCol: {
    span: 6,
  },
  disabled: false,
  schemas: [
    {
      field: 'name',
      component: 'Input',
      label: t('分析项名称'),
      required: true,
      componentProps: {
        maxLength: 30,
      },
    },
    {
      field: 'code',
      component: 'Input',
      label: t('分析项编码'),
      required: true,
      componentProps: {
        maxLength: 30,
        disabled: true,
      },
    },
    {
      field: 'standard',
      component: 'InputTextArea',
      label: t('默认标准规定'),
      componentProps: {
        maxLength: 100,
      },
    },
  ],
});

const openModal = (row: any, type: MODAL_STATUS) => {
  formProps.initialValues = row;
  modalType.value = type;
  switch (type) {
    case MODAL_STATUS.EDIT:
      title.value = t('编辑');
      okButtonProps.value = {
        disabled: false,
      };
      break;
    case MODAL_STATUS.ADD:
      title.value = t('新增');
      okButtonProps.value = {
        disabled: false,
      };
      break;
    case MODAL_STATUS.VIEW:
      title.value = t('查看');
      okButtonProps.value = {
        disabled: true,
      }
      break;
    default:
  }
  formProps.disabled = type === MODAL_STATUS.VIEW;
  formProps.schemas[1].componentProps.disabled = type !== MODAL_STATUS.ADD;
  open.value = true;
};

const request = async (formModal: any) => {
  try {
    const params = {
      ...formModal
    }
    if (modalType.value === MODAL_STATUS.EDIT) {
      return await updateAnalyze(params);
    } else if (modalType.value === MODAL_STATUS.ADD) {
      return await saveAnalyze(params);
    }
  } catch (error) {
    return Promise.reject(error);
  }
}

const ok = async () => {
  try {
    await modalFormRef.value?.submit(request);
    message.success(modalType.value === MODAL_STATUS.EDIT ? t('编辑成功') : t('新增成功'));
    emit('submitSuccess');
    cancel()
  } catch(error) {
    error.message && message.error(error.message);
  }
}

const cancel = () => {
  open.value = false;
}

defineExpose({
  openModal,
});

</script>

<style scoped>

</style>