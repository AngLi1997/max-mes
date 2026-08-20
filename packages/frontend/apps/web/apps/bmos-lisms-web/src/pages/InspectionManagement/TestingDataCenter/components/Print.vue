<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('打印检测记录单')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { getReportFile } from '@/services';
  import { pdfPreview } from '@/utils';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  const formProps = reactive<FormProps>({
    schemas: [
      {
        label: t('标本批号'),
        field: 'sampleBatchNo',
        component: 'Input',
        required: true,
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      const res = await getReportFile({
        fileType: 'INSPECT_RECORD',
        params: JSON.stringify({
          sampleBatchNo: formModal.sampleBatchNo,
        }),
        sampleBatchNo: formModal.sampleBatchNo,
      });
      await pdfPreview(res);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
