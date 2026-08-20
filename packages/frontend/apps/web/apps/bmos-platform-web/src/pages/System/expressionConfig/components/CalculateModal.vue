<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('验证公式')"
    :showCancelButton="showBtn"
    :showOkButton="showBtn"
    :cancelButtonText="t('未通过')"
    :okButtonText="t('通过')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="formConfirm"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, Recordable, FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
  import { Button, Input, InputGroup, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import ExpressionParseTable from './ExpressionParseTable.vue';
  import { isEmpty } from '@bmos/utils';
  import { reqExpressionCalculate, expressionVerify } from '@/api/system/expression';

  const emit = defineEmits(['update:open', 'submit']);

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData: Recordable;
    }>(),
    {
      open: false,
      rowData: () => ({}),
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      showBtn.value = false;
      emit('update:open', val);
    },
  });
  const showBtn = ref(false);

  const modalFormRef = ref<ModalFormInstance>();
  const submit = async () => {
    try {
      const formData = await modalFormRef.value?.formRef?.validate();
      const { expression, roundingCode, scale, expressionParse } = formData;
      const { data } = await reqExpressionCalculate({
        expression,
        roundingCode,
        scale,
        keyValueList: expressionParse.map((item: any) => {
          return {
            key: item.key,
            value: item.result,
          };
        }),
      });
      modalFormRef.value?.formRef?.setFormModels({
        calculateResult: data,
      });
      showBtn.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const formProps: Ref<FormProps> = ref({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        field: 'calculateFormula',
        component: 'Divider',
        label: t('计算公式'),
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
        field: 'name',
        component: 'Span',
        label: t('公式名称'),
        noFormItemMarginBottom: true,
      },
      {
        field: 'expression',
        component: 'Span',
        label: t('表达式'),
        noFormItemMarginBottom: true,
      },
      {
        field: 'expressionParse',
        noLabel: true,
        label: '11',
        colProps: {
          span: 24,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return <ExpressionParseTable v-model:expressionParse={formModel['expressionParse']} />;
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              trigger: [],
              validator: async (rule, value) => {
                let flag = false;
                value.forEach((item: any) => {
                  if (isEmpty(item.result)) {
                    flag = true;
                  }
                });
                if (flag) {
                  return Promise.reject('参数值不能为空');
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'roundingCode',
        component: 'Select',
        label: t('修约方式'),
        required: ({ formModel }: RenderCallbackParams) => formModel.scale,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('四舍五入'),
                value: 'roundingFive',
              },
              {
                label: t('四舍六入五成双'),
                value: 'roundingSix',
              },
              {
                label: t('向上舍入'),
                value: 'roundingUp',
              },
              {
                label: t('向下舍入'),
                value: 'roundingDown',
              },
            ],
            onChange: () => {
              formInstance.clearValidate(['scale']);
            },
          };
        },
      },
      {
        field: 'scale',
        component: 'InputNumber',
        label: t('精度'),
        required: ({ formModel }: RenderCallbackParams) => formModel.roundingCode,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            min: 0,
            max: 15,
            step: 1,
            precision: 0,
            style: {
              width: '100%',
            },
            onChange: (val: number) => {
              if (isEmpty(val)) {
                formInstance?.clearValidate(['roundingCode']);
              }
            },
          };
        },
      },
      {
        field: 'calculateResultDivider',
        component: 'Divider',
        label: t('计算结果'),
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
        field: 'calculateResult',
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <InputGroup compact>
                <Input v-model:value={formModel.calculateResult} style='width: calc(100% - 100px)' />
                <Button
                  type='primary'
                  onClick={() => {
                    submit();
                  }}
                  style='margin-left: var(--bmos-margin-small); border-radius: 4px'>
                  {t('计算')}
                </Button>
              </InputGroup>
            </>
          );
        },
        label: t('计算结果'),
      },
    ],
  });

  const formConfirm = async () => {
    try {
      await expressionVerify(props.rowData.id);
      open.value = false;
      message.success(t('验证通过'));
      emit('submit');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (!val) return;
      try {
        modalFormRef.value?.formRef?.setFormModels(props.rowData);
      } catch (error) {}
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less" scoped></style>
