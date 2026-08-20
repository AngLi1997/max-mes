<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验结果批量发布')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
  <Publish v-model:modalOpen="publishModal" :tableData="tableData" hasRequest :sampleBatchNo="pramsSampleBatchNo" />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { postInspectAlldataCheck } from '@/services';
  import Publish from './Publish.vue';

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
  const publishModal = ref<boolean>(false);
  const tableData = ref<Recordable[]>([]);
  const pramsSampleBatchNo = ref<string>('');
  const submit = async (formModal: Recordable) => {
    try {
      await postInspectAlldataCheck({
        sampleBatchNo: formModal.sampleBatchNo,
      });
      pramsSampleBatchNo.value = formModal.sampleBatchNo;
      publishModal.value = true;
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
