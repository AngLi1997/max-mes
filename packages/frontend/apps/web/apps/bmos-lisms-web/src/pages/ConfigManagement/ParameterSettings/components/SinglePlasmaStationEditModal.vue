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
  import { postStaticDataConfigStationEdit } from '@/services';

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
    return `${t('单采血浆站')}${t('编辑')}`;
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          centreName: props.rowData?.centreName,
          telNumber: props.rowData?.telNumber,
          address: props.rowData?.address,
          status: props.rowData?.status,
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    labelWidth: 100,
    schemas: [
      {
        field: 'centreName',
        component: 'Input',
        label: t('单采中心名称'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'telNumber',
        component: 'Input',
        label: t('联系电话'),
        componentProps: {
          maxlength: 20,
          showCount: true,
        },
      },
      {
        field: 'address',
        component: 'Input',
        label: t('地址'),
        componentProps: {
          maxlength: 50,
          showCount: true,
        },
      },
      {
        field: 'status',
        component: 'Switch',
        label: t('启用'),
        componentProps: {
          checkedValue: 1,
          unCheckedValue: 0,
        },
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      await postStaticDataConfigStationEdit({
        id: props.rowData?.id,
        menuIdentify: props.rowData?.menuIdentify,
        ...formModal,
        stationCode: props.rowData?.stationCode,
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
