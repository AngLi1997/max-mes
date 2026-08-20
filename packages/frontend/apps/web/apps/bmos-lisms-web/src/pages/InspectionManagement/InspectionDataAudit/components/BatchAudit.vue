<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验结果批量审核')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
  <Audit v-model:modalOpen="publishModal" :tableData="tableData" hasRequest :sampleBatchNo="pramsSampleBatchNo" />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import Audit from './Audit.vue';
  import { postInspectDatapubCheck } from '@/services';

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
      const { data } = await postInspectDatapubCheck({
        sampleBatchNo: formModal.sampleBatchNo,
        auditStatus: 'TO_AUDIT',
        pageNum: 1,
        pageSize: 10,
        fetchSampleDetail: true,
      });
      if (data?.list?.length) {
        publishModal.value = true;
        pramsSampleBatchNo.value = formModal.sampleBatchNo;
        open.value = false;
        return Promise.resolve();
      } else {
        message.error('未查询到待审核检验数据');
        return Promise.reject();
      }
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
