<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="inspectItem.label + t('批量发布')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
  <Publish v-model:modalOpen="publishModal" :inspectItem hasRequest :sampleBatchNo="pramsSampleBatchNo" />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { postInspectSingledataList } from '@/services';
  import Publish from './Publish.vue';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const props = withDefaults(
    defineProps<{
      inspectItem: Recordable;
    }>(),
    {
      inspectItem: () => ({}),
    },
  );

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
  const pramsSampleBatchNo = ref<string>('');
  const submit = async (formModal: Recordable) => {
    try {
      const { data } = await postInspectSingledataList({
        sampleBatchNo: formModal.sampleBatchNo,
        inspectItemCode: props.inspectItem.value,
        inspectDataStatus: 'TO_CHECK',
      });
      if (data?.list?.length) {
        publishModal.value = true;
        pramsSampleBatchNo.value = formModal.sampleBatchNo;
        open.value = false;
        return Promise.resolve();
      } else {
        message.error('未查询到待发布检验数据');
        return Promise.reject();
      }
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
