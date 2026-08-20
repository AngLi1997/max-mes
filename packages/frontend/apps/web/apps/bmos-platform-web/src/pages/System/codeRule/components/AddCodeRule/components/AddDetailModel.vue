<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    :disabled="status=='view'"
    :okButtonProps="okButtonProps"
    wrapClassName="modalSizeMedium"
    class="add-code-rule-data-modal"
    @okModal="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    ModalFormType,
    FormProps,
    ModalFormInstance,
    Recordable,
    RenderCallbackParams,
  } from '@bmos/components';
  import { Modal, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { MODAL_STATUS } from '@/pages/System/dict/types';
  import { DetailsType } from '@/pages/System/codeRule/types';
  import { typeMap } from '../utils';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTableData', params: Recordable, id: string): void;
    (e: 'addTableData', params: Recordable): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      tableData: any[];
      rowData?: any;
      status?: MODAL_STATUS;
      selectDictId?: string;
      parameterIdOptions: any[];
    }>(),
    {
      rowData: {},
      status: MODAL_STATUS.ADD,
    },
  );

  const parameterIdOptionsCom = computed(() => props.parameterIdOptions);

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('添加属性'));

  const request = async (formModal: any) => {
    const params = {
      ...formModal,
    };

    // 如果是流水号，且已有流水号，则不允许添加
    if (
      params.type === DetailsType.SEQUENCE &&
      props.tableData.some(item => item.type === DetailsType.SEQUENCE && item.type !== props.rowData.type)
    ) {
      return Promise.reject({
        message: t('编号规则流水号属性唯一'),
      });
    }
    if (params.type === DetailsType.SEQUENCE && params.startNo?.length > params.maxLength) {
      return Promise.reject({
        message: t('起始流水号长度超过最大位数，请确认'),
      });
    }
    switch (props.status) {
      case MODAL_STATUS.ADD:
        emit('addTableData', params);
        return Promise.resolve();
      case MODAL_STATUS.EDIT:
        emit('updateTableData', params, props.rowData.id);
        return Promise.resolve();
      default:
        return Promise.reject();
    }
  };
  const submit = async (modalFormType: ModalFormType) => {
    try {
      await modalFormRef.value?.submit(request);
      if (props.status === MODAL_STATUS.EDIT) {
        message.success(t('编辑成功'));
      } else {
        message.success(t('新增成功'));
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();

  const getParameterIdOptions = async () => {
    try {
      modalFormRef.value?.formRef?.updateSchema({
        field: 'parameterId',
        componentProps: {
          options: props.parameterIdOptions,
        },
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const dateType = [
    [
      //年
      {
        label: 'yy',
        value: 'yy',
      },
      {
        label: 'yyyy',
        value: 'yyyy',
      },
    ],
    [
      //月
      {
        label: 'MM',
        value: 'MM',
      },
    ],
    [
      //日
      {
        label: 'dd',
        value: 'dd',
      },
    ],
    [
      //年月
      {
        label: 'yyMM',
        value: 'yyMM',
      },
      {
        label: 'yyyyMM',
        value: 'yyyyMM',
      },
      {
        label: 'yy-MM',
        value: 'yy-MM',
      },
      {
        label: 'yyyy-MM',
        value: 'yyyy-MM',
      },
      {
        label: 'yy/MM',
        value: 'yy/MM',
      },
      {
        label: 'yyyy/MM',
        value: 'yyyy/MM',
      },
    ],
    [
      //年月日
      {
        label: 'yyMMdd',
        value: 'yyMMdd',
      },
      {
        label: 'yyyyMMdd',
        value: 'yyyyMMdd',
      },
      {
        label: 'yy-MM-dd',
        value: 'yy-MM-dd',
      },
      {
        label: 'yyyy-MM-dd',
        value: 'yyyy-MM-dd',
      },
      {
        label: 'yy/MM/dd',
        value: 'yy/MM/dd',
      },
      {
        label: 'yyyy/MM/dd',
        value: 'yyyy/MM/dd',
      },
    ],
  ];
  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        field: 'type',
        component: 'Select',
        label: t('属性类型'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: Array.from(typeMap, ([key, value]) => ({
              label: value,
              value: key,
            })),
            onChange: (val: string) => {
              modalFormRef.value?.formRef?.resetForm();
              modalFormRef.value?.formRef?.setFormModel('type', val);
              if (val === DetailsType.PARAMETER) {
                getParameterIdOptions();
              }
              if (val === DetailsType.SEQUENCE) {
                modalFormRef.value?.formRef?.setFormModel('fillZero', 'TRUE');
                // modalFormRef.value?.formRef?.setFormModel('maxLength', 1);
              }
            },
          };
        },
      },
      {
        field: 'startNo',
        component: 'Input',
        label: t('起始流水号'),
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.SEQUENCE,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            // onInput: (val: string) => {
            //   const max = !formModel.maxLength ? 10 : formModel.maxLength;
            //   if (formModel.startNo?.length >= max) {
            //     formModel.startNo = formModel.startNo.slice(
            //       0,
            //       formModel.maxLength,
            //     );
            //   }
            // },
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: async (_: any, value: any) => {
                const pattern = /^[0-9]+$/;
                if (!value) {
                  return Promise.reject(t('请输起始流水号'));
                }
                if (!pattern.test(value)) {
                  formModel.startNo = '';
                  return Promise.reject(t('请输自然数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'maxLength',
        component: 'InputNumber',
        label: t('最大位数'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.SEQUENCE,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            min: 1,
            max: 10,
            placeholder: t('起始流水号'),
            // onChange: (val: string) => {
            //   if (
            //     formModel.startNo?.length > formModel.maxLength &&
            //     formModel.maxLength
            //   ) {
            //     Modal.error({
            //       title: t('起始流水号长度超过最大位数，请确认'),
            //     });
            //     formModel.maxLength = formModel.startNo.length;
            //   }
            // },
          };
        },
      },
      {
        field: 'step',
        component: 'InputNumber',
        label: t('增量'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.SEQUENCE,
        componentProps: {
          min: 1,
          stringMode: true,
          placeholder: t('增量'),
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请输入增量'),
              trigger: 'blur',
            },
            {
              trigger: 'blur',
              validator: async (_: any, value: any) => {
                if (!isNaN(value) && value != parseInt(value)) {
                  formModel.step = '';
                  return Promise.reject(t('请输正整数'));
                }
                // 超过 10 位报错
                if (value > 9999999999) {
                  return Promise.reject(t('最多输入10位正整数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'fillZero',
        component: 'RadioGroup',
        label: t('补零'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.SEQUENCE,
        componentProps: {
          options: [
            {
              label: t('是'),
              value: 'TRUE',
            },
            {
              label: t('否'),
              value: 'FALSE',
            },
          ],
        },
      },
      {
        field: 'value',
        component: 'Input',
        label: t('常量'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.CONSTANT,
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请选择常量'),
              trigger: 'change',
            },
            {
              min: 1,
              max: 100,
              message: t('常量不能超过100长度字符'),
              trigger: 'blur',
            },
          ];
        },
      },
      {
        field: 'parameterId',
        component: 'Select',
        label: t('参数'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.PARAMETER,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'label',
              value: 'id',
            },
            options: props.parameterIdOptions,
          };
        },
      },
      {
        field: 'isShow',
        component: 'RadioGroup',
        label: t('展示'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.PARAMETER,
        componentProps: {
          options: [
            {
              label: t('是'),
              value: true,
            },
            {
              label: t('否'),
              value: false,
            },
          ],
        },
      },
      {
        field: 'dateType',
        component: 'Select',
        label: t('日期类型'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.DATE,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('年'),
                value: '0',
              },
              {
                label: t('月'),
                value: '1',
              },
              {
                label: t('日'),
                value: '2',
              },
              {
                label: t('年月'),
                value: '3',
              },
              {
                label: t('年月日'),
                value: '4',
              },
            ],
            onChange: (val: string) => {
              modalFormRef.value?.formRef?.bmFormRef.clearValidate()
              modalFormRef.value?.formRef?.setFormModel('dateFormat', dateType[formModel.dateType][0].value);
            },
          };
        },
      },
      {
        field: 'dateFormat',
        component: 'Select',
        label: t('日期格式'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => formModel.type === DetailsType.DATE,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: dateType[formModel.dateType],
          };
        },
      },
    ],
  });

  const okButtonProps = ref({});
  watch(
    () => open.value,
    async val => {
      await nextTick();
      switch (props.status) {
        case MODAL_STATUS.ADD:
          title.value = t('添加属性');
          okButtonProps.value = {
            disabled: false,
          };
          break;
        case MODAL_STATUS.EDIT:
          title.value = t('编辑属性');
          okButtonProps.value = {
            disabled: false,
          };
          modalFormRef.value?.formRef?.setFormProps({
            disabled: false,
          });
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
          });
          break;
        case MODAL_STATUS.VIEW:
          title.value = t('查看属性');
          okButtonProps.value = {
            disabled: true,
          };
          modalFormRef.value?.formRef?.setFormProps({
            disabled: true,
          });
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
          });
          break;
        default:
          break;
      }
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less" scoped>
  .add-code-rule-data-modal {
  }
</style>
