<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="inspectItem.label + t('批量核对')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
  <Check v-model:modalOpen="checkModal" :inspectItem hasRequest :sampleBatchNo="pramsSampleBatchNo" />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { postInspectSingledataList } from '@/services';
  import Check from './Check.vue';

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
  const checkModal = ref<boolean>(false);
  const pramsSampleBatchNo = ref<string>('');
  const submit = async (formModal: Recordable) => {
    try {
      const { data } = await postInspectSingledataList({
        sampleBatchNo: formModal.sampleBatchNo,
        inspectItemCode: props.inspectItem.value,
        inspectDataStatus: 'CHECKED',
      });
      if (data?.list?.length) {
        checkModal.value = true;
        pramsSampleBatchNo.value = formModal.sampleBatchNo;
        open.value = false;
        return Promise.resolve();
      } else {
        message.error('未查询到可核对检验数据');
        return Promise.reject();
      }
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
