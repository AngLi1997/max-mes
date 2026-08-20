<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('批量添加数据点')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('showBatchAddModal', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'number',
        component: 'InputNumber',
        label: t('添加数量'),
        required: true,
        componentProps: {
          min: 1,
          precision: 0,
          max: 999,
          style: {
            width: '100%',
          },
        },
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      const { number } = formModal;
      emit('ok', number);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
