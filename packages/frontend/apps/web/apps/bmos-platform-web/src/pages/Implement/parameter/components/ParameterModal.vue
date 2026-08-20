<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('编辑系统参数')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    class="edit-parameter-modal"
    @okModal="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, Recordable } from '@bmos/components';
  import { message, Tooltip } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ParameterValueType } from '../types';
  import { InfoCircleOutlined } from '@ant-design/icons-vue';
  import { reqBusinessParameterUpdatePUT } from '@/api';
  import DataDescription from './dataDescriptions.vue';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: Recordable;
      businessTypeOptions?: Recordable[];
      valueTypeOptions?: Recordable[];
      belongOptions?: Recordable[];
    }>(),
    {
      open: false,
      rowData: () => ({}),
      businessTypeOptions: () => [],
      valueTypeOptions: () => [],
      belongOptions: () => [],
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const request = async (formModal: any) => {
    const params = {
      id: props.rowData?.id,
      value: formModal.value,
    };
    const data: any = await reqBusinessParameterUpdatePUT(params);
    if (data.code === 0) {
      return Promise.resolve();
    } else {
      return Promise.reject({
        message: data.message,
      });
    }
  };
  const submit = async () => {
    try {
      console.log('ddddddd');
      await modalFormRef.value?.submit(request);
      message.success(t('编辑成功'));
      emit('updateTable');
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();

  const getHelpTitle = (): string => {
    switch (props.rowData?.valueType?.value) {
      case ParameterValueType.STRING:
        return t('请输入任意类型参数');
      case ParameterValueType.NUMBER:
        return t('请输入数值');
      case ParameterValueType.JSON:
        return t('请输入Json语法格式文本');
      default:
        return t('请输入任意类型参数');
    }
  };

  const getLabel = (): string => {
    return `${t('值')}(${props.rowData?.valueType?.label})`;
  };

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        field: 'description',
        noLabel: true,
        colProps: {
          span: 24,
        },
        component: () => {
          return <DataDescription rowData={props.rowData} />;
        },
      },
      {
        field: 'value',
        component: 'InputTextArea',
        label: getLabel(),
        required: true,
        useMaxLengthRule: false,
        componentSlots: {
          suffix: () => (
            <Tooltip title={getHelpTitle()}>
              <InfoCircleOutlined style='color: rgba(0, 0, 0, 0.45)' />
            </Tooltip>
          ),
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_: any, value: any) => {
                // 如果 length 大y于 2000 则报错
                if (value.length > 2000) {
                  return Promise.reject(t('内容不能超过2000个字符'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
  });
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          ...props.rowData,
          businessType: props.rowData?.businessType?.value,
          valueType: props.rowData?.valueType?.value,
        });
        switch (props.rowData?.valueType?.value) {
          case ParameterValueType.STRING:
            modalFormRef.value?.formRef?.updateSchema({
              field: 'value',
              component: 'InputTextArea',
              label: getLabel(),
              componentSlots: {
                suffix: () => (
                  <Tooltip title={getHelpTitle()}>
                    <InfoCircleOutlined style='color: rgba(0, 0, 0, 0.45)' />
                  </Tooltip>
                ),
              },
            });
            break;
          case ParameterValueType.NUMBER:
            modalFormRef.value?.formRef?.updateSchema({
              field: 'value',
              component: 'InputNumber',
              label: getLabel(),
              required: true,
              componentSlots: {
                suffix: () => (
                  <Tooltip title={getHelpTitle()}>
                    <InfoCircleOutlined style='color: rgba(0, 0, 0, 0.45)' />
                  </Tooltip>
                ),
              },
              dynamicRules: () => {
                return [
                  { required: true, message: t('请输入数值'), trigger: 'blur' },
                  {
                    validator: async (_: any, value: any) => {
                      if (isNaN(value)) {
                        return Promise.reject(t('请输入数值'));
                      }
                      return Promise.resolve();
                    },
                  },
                ];
              },
            });
            break;
          case ParameterValueType.BOOLEAN:
            modalFormRef.value?.formRef?.updateSchema({
              field: 'value',
              component: 'Switch',
              label: getLabel(),
              componentProps: {
                checkedValue: true,
                unCheckedValue: false,
                checkedChildren: t('开'),
                unCheckedChildren: t('关'),
              },
            });
            modalFormRef.value?.formRef?.setFormModel('value', props.rowData?.value === 'true' ? true : false);
            break;
          case ParameterValueType.ENUM:
            modalFormRef.value?.formRef?.updateSchema({
              field: 'value',
              component: 'Select',
              label: getLabel(),
              componentProps: {
                options: JSON.parse(props.rowData?.valueRange),
              },
            });
            break;
          case ParameterValueType.JSON:
            modalFormRef.value?.formRef?.updateSchema({
              field: 'value',
              component: 'InputTextArea',
              label: getLabel(),
              componentSlots: {
                suffix: () => (
                  <Tooltip title={getHelpTitle()}>
                    <InfoCircleOutlined style='color: rgba(0, 0, 0, 0.45)' />
                  </Tooltip>
                ),
              },
              dynamicRules: () => {
                return [
                  {
                    required: true,
                    // message: t('请输入Json语法格式文本'),
                    message: t(''),
                    trigger: 'blur',
                  },
                  {
                    validator: async (_: any, value: any) => {
                      try {
                        JSON.parse(value);
                        return Promise.resolve();
                      } catch (error) {
                        return Promise.reject(t('请输入Json语法格式文本'));
                      }
                    },
                  },
                ];
              },
            });
            break;
          default:
            modalFormRef.value?.formRef?.setFieldsValue({
              value: props.rowData?.value,
            });
            break;
        }
      }
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less">
  .edit-parameter-modal {
    .plat-input-number {
      width: 100%;
    }
  }
</style>
