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
    return `${props.rowData?.description}${t('设置')}`;
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          enumsValue: props.rowData?.enumsValue,
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'enumsValue',
        component: 'InputNumber',
        label: t('参数'),
        required: true,
        componentProps: {
          min: 1,
          max: 99999,
          precision: 0,
          style: { width: '100%' },
        },
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      await postStaticDataConfigEdit({
        id: props.rowData?.id,
        menuIdentify: props.rowData?.menuIdentify,
        ...formModal,
        projectName: props.rowData?.description,
      });
      emit('ok');
      message.success(`${t('编辑')}${t('成功')}`);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>
