<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('备注信息')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, FormSchema, Recordable } from '@bmos/components';
  import { RemarkDetail } from '.';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const props = withDefaults(
    defineProps<{
      details?: RemarkDetail[];
    }>(),
    {
      details: () => [],
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        const fieldsValue: Recordable = {};
        const sachems: FormSchema[] = props.details.map((item: RemarkDetail) => {
          fieldsValue[item.field] = item.value;
          return {
            field: item.field,
            component: 'InputTextArea',
            label: item.label,
            componentProps: {
              disabled: true,
              placeholder: t('空'),
            },
          };
        });
        modalFormRef.value?.formRef?.appendSchemasByField(sachems);
        modalFormRef.value?.formRef?.setFieldsValue(fieldsValue);
      }
    },
  );
  const formProps = reactive<FormProps>({
    layout: 'vertical',
    schemas: [],
  });
</script>

<style scoped lang="less"></style>
