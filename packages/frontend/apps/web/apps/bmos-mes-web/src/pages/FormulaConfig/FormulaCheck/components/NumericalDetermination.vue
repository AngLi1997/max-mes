<template>
  <div v-for="(item, index) in numericalJudgmentConfig" :key="item.field">
    <div v-if="showOptionTitle">{{ t('选项') }}{{ index }}：{{ item.field }}</div>
    <BMForm :ref="el => getFormRefs(el, item)" v-bind="formProps" />
  </div>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { NumericalJudgmentItem } from '../types';
  import { BMForm, FormProps, RenderCallbackParams, Recordable } from '@bmos/components';
  import ScopeNumber from '@/pages/ProductConfig/ProcessConfig/ProcedureStepConfig/components/ScopeNumber.vue';

  defineOptions({
    name: 'NumericalJudgmentConfig',
    inheritAttrs: false,
  });

  const emit = defineEmits<{
    (e: 'update:numericalJudgmentConfig', numericalJudgmentConfig: NumericalJudgmentItem[]): void;
    (e: 'change', numericalJudgmentConfig: NumericalJudgmentItem[]): void;
  }>();

  const props = withDefaults(
    defineProps<{
      numericalJudgmentConfig: NumericalJudgmentItem[];
      isShow: boolean;
      showOptionTitle: boolean;
      componentDetail?: any;
      isText?: boolean;
    }>(),
    {
      numericalJudgmentConfig: () => [],
      isShow: false,
      showOptionTitle: true,
      componentDetail: () => [{ field: 'text' }],
      isText: false,
    },
  );

  const formRefs = ref<Recordable>({});

  const getFormRefs = (el: any, item: { field: string }) => {
    if (el) {
      formRefs.value[item.field] = el;
    }
  };

  const setOptions = () => {
    let data = [];
    if (props.componentDetail && Array.isArray(props.componentDetail)) {
      data = props.componentDetail;
    } else if (props.componentDetail && !Array.isArray(props.componentDetail)) {
      data = JSON.parse(props.componentDetail);
    } else {
      data = [{ field: 'text' }];
    }
    data = data.map((item: any) => {
      return {
        field: item.field,
        limitType: '0',
        scope: { lowerLimit: 1, upperLimit: 1, scopeMax: null, scopeMin: null },
        satisfiedValue: '',
        unsatisfiedValue: '',
      };
    });
    emit('update:numericalJudgmentConfig', data);
  };

  const triggerChange = (changedValue: NumericalJudgmentItem) => {
    const newNumericalJudgmentConfig = props.numericalJudgmentConfig.map((item: NumericalJudgmentItem) => {
      if (!props.isText) {
        item.satisfiedValue = item.field;
      }
      if (item.field === changedValue.field) {
        return { ...item, ...changedValue };
      }
      return item;
    });
    emit('update:numericalJudgmentConfig', newNumericalJudgmentConfig);
    emit('change', newNumericalJudgmentConfig);
  };

  // 限制方式改变
  const limitTypeChange = (changedValue: NumericalJudgmentItem) => {
    triggerChange(changedValue);
  };

  watch(
    () => props.numericalJudgmentConfig,
    async () => {
      if (!props.numericalJudgmentConfig) {
        return;
      }
      // 没有配置项时，初始化配置项
      if (props.numericalJudgmentConfig.length === 0) {
        setOptions();
      }
      await nextTick();
      Object.keys(formRefs.value).forEach(key => {
        const numericalJudgmentConfigItem = props.numericalJudgmentConfig.find(item => item.field === key);
        if (numericalJudgmentConfigItem) {
          formRefs.value[key].setFormModels(numericalJudgmentConfigItem);
        }
      });
    },
    {
      immediate: true,
      deep: true,
    },
  );

  watch(
    () => props.isShow,
    async (val: boolean) => {
      await nextTick();
      if (val) {
        await nextTick();
        Object.keys(formRefs.value).forEach(key => {
          formRefs.value[key].setFormProps({
            disabled: true,
          });
        });
      }
    },
    { immediate: true },
  );

  const formProps: Ref<FormProps> = ref({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'limitType',
        component: 'Select',
        label: t('限制方式'),
        required: true,
        defaultValue: 1,
        componentProps: ({ formInstance, formModel }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('范围限制'),
                value: '0',
              },
              {
                label: t('数值相等'),
                value: '1',
              },
            ],
            onChange: (val: number) => {
              if (val! == 0) {
                formModel.numericalValue = null;
              } else {
                formModel.scope = { lowerLimit: 1, upperLimit: 1, scopeMax: null, scopeMin: null };
              }
              limitTypeChange({
                field: formModel.field,
                limitType: formModel.limitType,
                numericalValue: formModel.numericalValue,
                scope: formModel.scope,
              });
            },
          };
        },
      },
      {
        field: 'scope',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel['limitType'].value ?? formModel['limitType']) === '0';
        },
        defaultValue: {
          lowerLimit: 1,
          upperLimit: 1,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <ScopeNumber
              v-model:limit={formModel.scope}
              onUpdate:limit={() => {
                triggerChange({
                  field: formModel.field,
                  scope: formModel.scope,
                });
              }}
            />
          );
        },
        label: t('范围'),
        dynamicRules: ({ formModel }) => {
          return [
            {
              required: true,
              validator: async (rule, value) => {
                let scopeMin = formModel['scope']?.['scopeMin'];
                let scopeMax = formModel['scope']?.['scopeMax'];
                const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
                if (!(scopeMin === null || scopeMin === undefined)) {
                  if (!reg.test(scopeMin)) {
                    return Promise.reject(t('最小值整数或小数不能超过15位'));
                  }
                }
                if (!(scopeMax === null || scopeMax === undefined)) {
                  if (!reg.test(scopeMax)) {
                    return Promise.reject(t('最大值整数或小数不能超过15位'));
                  }
                }
                if ((scopeMin === null || scopeMin === undefined) && (scopeMax === null || scopeMax === undefined))
                  return Promise.reject(t('请输入最小值/最大值'));
                // 如果最小值和最大值都有值，则最大值大于最小值
                if (scopeMin !== null && scopeMin !== undefined && scopeMax !== null && scopeMax !== undefined) {
                  if (Number(scopeMin) >= Number(scopeMax)) {
                    return Promise.reject(t('最大值大于最小值'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'numericalValue',
        component: 'InputNumber',
        label: t('数值'),
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel['limitType'].value ?? formModel['limitType']) === '1';
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            stringMode: true,
            style: {
              width: '100%',
            },
            onChange: () => {
              triggerChange({
                field: formModel.field,
                numericalValue: formModel.numericalValue,
              });
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (rule, value) => {
                if (!value) return Promise.reject(t('请输入数值'));
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数或小数不能超过15位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'satisfiedValue',
        label: t('满足时录入值'),
        component: 'Input',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return props.isText;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              triggerChange({
                field: formModel.field,
                satisfiedValue: formModel.satisfiedValue,
              });
            },
          };
        },
      },
      {
        field: 'unsatisfiedValue',
        label: t('不满足时录入值'),
        component: 'Input',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return props.isText;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              triggerChange({
                field: formModel.field,
                unsatisfiedValue: formModel.unsatisfiedValue,
              });
            },
          };
        },
      },
    ],
  });

  const validateForm = async () => {
    try {
      const formRefList = Object.values(formRefs.value);
      const validateResult = await Promise.all(formRefList.map((formInstance: any) => formInstance.validate()));
      return Promise.resolve(validateResult);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  defineExpose({
    validateForm,
    formRefs,
  });
</script>

<style lang="less" scoped></style>
