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
  import { NoTypeEnum, OperationStatusMap } from '@/types';
  import { postStaticDataConfigCreate, postStaticDataConfigEdit } from '@/services';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      status: OperationStatusMap;
      treeNode?: Recordable;
      rowData?: Recordable;
    }>(),
    {
      status: OperationStatusMap.ADD,
      treeNode: () => ({}),
      rowData: () => ({}),
    },
  );

  const title = computed(() => {
    return `${t('领用库')}${props.status === OperationStatusMap.ADD ? t('添加') : t('编辑')}`;
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        props.status === OperationStatusMap.EDIT &&
          modalFormRef.value?.formRef?.setFieldsValue({
            substNo: props.rowData?.substNo,
            enumsValue: props.rowData?.enumsValue,
            remark: props.rowData?.remark,
          });
      }
    },
  );
  const formProps = reactive<FormProps>({
    labelWidth: 100,
    schemas: [
      {
        field: 'substNo',
        component: 'Input',
        label: t('领用库编号'),
        required: true,
        componentProps: {
          maxlength: 10,
          showCount: true,
        },
      },
      {
        field: 'enumsValue',
        component: 'Input',
        label: t('领用库名称'),
        required: true,
        componentProps: {
          maxlength: 10,
          showCount: true,
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      if (props.status === OperationStatusMap.ADD) {
        await postStaticDataConfigCreate({
          ...formModal,
          menuIdentify: props.treeNode?.menuIdentify,
          noType: NoTypeEnum.RECEIVE_STORE_CODE,
          receiveStoreNo: formModal?.substNo,
        });
      } else {
        await postStaticDataConfigEdit({
          id: props.rowData?.id,
          menuIdentify: props.rowData?.menuIdentify,
          receiveStoreNo: props.rowData?.substNo,
          ...formModal,
        });
      }
      emit('ok');
      message.success(`${props.status === OperationStatusMap.ADD ? t('添加') : t('编辑')}${t('成功')}`);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>
