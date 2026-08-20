<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import RoundingParameterTable from './RoundingParameterTable.vue';
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
    return `${t('修约参数')}${t('编辑')}`;
  });

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          enumsValues: [
            {
              description: props.rowData?.description,
              label: props.rowData?.label,
              value: props.rowData?.value,
            },
          ],
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    labelWidth: 100,
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('修约规则'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'enumsValues',
        noLabel: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <RoundingParameterTable v-model:tableList={formModel.enumsValues} />
              </FormItemRest>
            </>
          );
        },
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      const { enumsValues } = formModal;
      await postStaticDataConfigEdit({
        id: props.rowData?.id,
        label: enumsValues[0]?.label,
        value: enumsValues[0]?.value,
        enumsValue: props.rowData?.enumsValue,
        menuIdentify: props.rowData?.menuIdentify,
        projectName: props.rowData?.description,
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
