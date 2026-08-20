<!-- 文件读取 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('文件读取')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { Button, message } from 'ant-design-vue';
  import { FormProps, ModalFormInstance } from '@bmos/components';
  import { postInspectFourEnzymeFileRead } from '@/services';
  import { BMModalForm } from '@bmos/components';
  import { InspectionProjectEnum } from '@/types';

  const open = ref(false);
  const emits = defineEmits(['ok']);

  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('上传文件'),
        field: 'file',
        required: true,
        component: 'Upload',
        componentProps: {
          accept: '.txt',
          maxCount: 1,
          beforeUpload: (file: any) => {
            setFormModels({
              file: file ? [file] : [],
            });
            // formModel.file = file ? [file] : [];
            return false;
          },
          onRemove: (_file: any) => {
            setFormModels({
              file: [],
            });
          },
        },
        componentSlots: {
          default: () => <Button type='primary'>{t('上传文件')}</Button>,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const inspectItemCode = ref<InspectionProjectEnum>();

  const openModal = async (type: InspectionProjectEnum) => {
    inspectItemCode.value = type;
    open.value = true;
  };

  const cancel = () => {
    open.value = false;
    // tableRef.value?.fetchData();
  };

  const request = async (formModal: any) => {
    try {
      const formData = new FormData();
      console.log('formModal.file', formModal.file);
      formData.append('file', formModal.file[0]?.originFileObj);
      formData.append('inspectItemCode', inspectItemCode.value as string);

      return await postInspectFourEnzymeFileRead(formData);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      const res = await request(formModal);
      message.success(res.data ?? t('操作成功'));
      emits('ok');
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
