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
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { enumsTypeEnum } from '../types';
  import ColorPicker from '@/components/ColorPicker/index.vue';
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
        switch (props.rowData?.enumsType) {
          case enumsTypeEnum.SWITCH:
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: 'enumsValue',
              component: 'Select',
              label: t('参数'),
              required: true,
              componentProps: {
                options: [
                  {
                    label: t('开'),
                    value: 'true',
                  },
                  {
                    label: t('关'),
                    value: 'false',
                  },
                ],
              },
            });
            break;
          case enumsTypeEnum.COLOUR:
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: 'enumsValue',
              component: ({ formModel }: RenderCallbackParams) => {
                return <ColorPicker v-model={formModel.enumsValue} />;
              },
              label: t('参数'),
              required: true,
            });
            break;
          case enumsTypeEnum.DATE:
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: 'enumsValue',
              component: 'Select',
              label: t('参数'),
              required: true,
              componentProps: {
                options: [
                  {
                    label: 'YYYY-MM-DD',
                    value: 'YYYY-MM-DD',
                  },
                  {
                    label: 'YYYY-MM-DD HH:mm:ss',
                    value: 'YYYY-MM-DD HH:mm:ss',
                  },
                ],
              },
            });
            break;
          case enumsTypeEnum.HOUR:
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: 'enumsValue',
              component: 'InputNumber',
              label: t('参数'),
              required: true,
              componentProps: {
                min: 1,
                max: 9999,
                precision: 0,
                style: {
                  width: '100%',
                },
              },
            });
            break;
          case enumsTypeEnum.NUMBER:
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: 'enumsValue',
              component: 'InputNumber',
              label: t('参数'),
              required: true,
              componentProps: {
                min: 1,
                max: 10,
                precision: 0,
                style: {
                  width: '100%',
                },
              },
            });
            break;
          default:
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: 'enumsValue',
              component: 'Input',
              label: t('参数'),
              required: true,
            });
            break;
        }
        modalFormRef.value?.formRef?.setFieldsValue({
          enumsValue: props.rowData?.enumsValue,
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    schemas: [],
  });

  const submit = async (formModal: Recordable) => {
    try {
      await postStaticDataConfigEdit({
        id: props.rowData?.id,
        ...formModal,
        menuIdentify: props.rowData?.menuIdentify,
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
