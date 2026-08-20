<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验项目编辑')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { getLaboratoryInstrumentQueryConfig, postStaticDataConfigInspectEdit } from '@/services';
  import { useDict } from '@/stores/dictStore';

  defineOptions({
    inheritAttrs: false,
  });
  const { getDict } = useDict();
  const { projectTypeDict, yesOrNoDictOther } = getDicts();
  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      rowData?: Recordable;
    }>(),
    {
      rowData: () => ({}),
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          ...props.rowData,
          itemType: props.rowData?.itemType?.value,
          reInspect: props.rowData?.reInspect?.value,
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    labelWidth: 110,
    schemas: [
      {
        field: 'itemNo',
        component: 'Input',
        label: t('检验项目编号'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'description',
        component: 'Input',
        label: t('检验项目描述'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'inspectType',
        component: 'Input',
        label: t('检验类型'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'itemType',
        component: 'Select',
        label: t('项目类型'),
        componentProps: {
          options: projectTypeDict,
        },
      },
      {
        field: 'itemName',
        component: 'Input',
        label: t('检验项目名称'),
        required: true,
        componentProps: {
          maxLength: 20,
          showCount: true,
        },
      },
      {
        field: 'inspectMethod',
        component: 'Select',
        label: t('检验方式'),
        required: true,
        componentProps: {
          request: async () => {
            return getDict('检验方式');
          },
        },
      },
      {
        field: 'reInspect',
        component: 'Select',
        label: t('是否复检'),
        required: true,
        componentProps: {
          options: yesOrNoDictOther,
        },
      },
      {
        field: 'defaultInstrument',
        component: 'Select',
        label: t('默认设备'),
        componentProps: {
          request: async () => {
            const { data } = await getLaboratoryInstrumentQueryConfig(props.rowData?.itemNo);
            return data;
          },
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
      const { itemNo, itemType, itemName, inspectMethod, reInspect, defaultInstrument, remark } = formModal;
      await postStaticDataConfigInspectEdit({
        id: props.rowData?.id,
        itemNo,
        itemType,
        itemName,
        inspectMethod,
        reInspect,
        defaultInstrument,
        remark,
        inspectItemNo: itemNo,
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
