<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { postStaticDataConfigEdit } from '@/services';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      treeNode?: Recordable;
      rowData?: Recordable;
    }>(),
    {
      treeNode: () => ({}),
      rowData: () => ({}),
    },
  );

  const title = computed(() => {
    return `${t('修约规则')}${t('编辑')}`;
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          label: props.rowData?.label,
          enumsValue: props.rowData?.enumsValue,
          description: props.rowData?.description,
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    labelWidth: 100,
    schemas: [
      {
        field: 'label',
        component: 'Input',
        label: t('修约规则名称'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'enumsValue',
        component: 'Input',
        label: t('修约编码'),
        required: true,
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('修约描述'),
        required: true,
        useMaxLengthRule: false,
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      await postStaticDataConfigEdit({
        id: props.rowData?.id,
        enumsValue: formModal.enumsValue,
        description: formModal.description,
        menuIdentify: props.rowData?.menuIdentify,
        projectName: props.rowData?.label,
      });
      emit('ok');
      message.success(`${t('编辑成功')}`);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>
