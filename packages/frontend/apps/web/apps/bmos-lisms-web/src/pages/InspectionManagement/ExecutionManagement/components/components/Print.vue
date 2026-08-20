<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('打印蛋白电泳检测报告')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
  <PrintHasNoCheck v-model:modalOpen="printHasNoCheckModal" :params :errorMsg />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { getInspectFileProteinelecCheck, getReportFile } from '@/services';
  import PrintHasNoCheck from './PrintHasNoCheck.vue';
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
        label: t('检验日期'),
        field: 'inspectDate',
        component: 'DatePicker',
        required: true,
        componentProps: {
          format: 'YYYYMMDD',
          valueFormat: 'YYYYMMDD',
        },
      },
    ],
  });
  const printHasNoCheckModal = ref<boolean>(false);
  const params = ref<string>('');
  const errorMsg = ref<string>('');
  const submit = async (formModal: Recordable) => {
    try {
      params.value = JSON.stringify({ ...formModal });
      const { data } = await getInspectFileProteinelecCheck(formModal);
      if (data.length) {
        printHasNoCheckModal.value = true;
        open.value = false;
        errorMsg.value = data;
      } else {
        const res = await getReportFile({
          fileType: 'INSPECT_PROTEIN_ELEC',
          params: params.value,
          inspectDate: formModal.inspectDate,
        });
        await pdfPreview(res);
      }
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
