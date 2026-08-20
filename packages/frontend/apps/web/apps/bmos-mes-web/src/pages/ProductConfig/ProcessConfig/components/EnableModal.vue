<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="enableOpen"
    :title="t('是否发起工艺审核？')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message, RadioGroup } from 'ant-design-vue';
  import { processVersionAuditReq } from '@/services';

  const emit = defineEmits<{
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      versionId: string;
    }>(),
    {
      versionId: '',
    },
  );

  const enableOpen = defineModel<boolean>('enableOpen', { default: false });
  const submit = async (formValues: Recordable) => {
    try {
      const { date } = formValues;
      await processVersionAuditReq(props.versionId, date);
      message.success(t('发起审核成功'));
      sendMessage(MessageType.UpdateMessageCount);
      enableOpen.value = false;
      emit('updateTable');
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      enableOpen.value = false;
      return Promise.reject();
    }
  };
  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    initialValues: {},
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    schemas: [
      {
        field: 'type',
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <RadioGroup
                v-model:value={formModel.type}
                options={[
                  {
                    label: t('立即生效'),
                    value: 1,
                  },
                  {
                    label: t('固定日期'),
                    value: 2,
                  },
                ]}
              />
              <div
                style={{
                  background: 'var(--bmos-background-color)',
                  fontSize: '12px',
                  color: 'var(--bmos-fourth-level-text-color)',
                  lineHeight: '24px',
                  marginTop: '8px',
                  padding: '0 8px',
                }}>
                {formModel.type === 1 ? t('审核通过后, 立即生效') : t('审核通过后, 在指定日期生效')}
              </div>
            </>
          );
        },
        label: t('生效类型'),
        required: true,
        defaultValue: 1,
      },
      {
        field: 'date',
        component: 'DatePicker',
        label: t('生效时间'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel?.type === 2,
      },
    ],
  });
</script>
