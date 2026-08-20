<!-- eslint-disable prettier/prettier -->
<template>
  <Collapse v-model:activeKey="activeKey" class="dataAttr-collapse">
    <CollapsePanel v-for="item in attrList" :key="item.fieldId" :header="item.componentName" forceRender>
      <BMForm :ref="(el: any) => getFormRefs(el, item)" v-bind="formProps" />
    </CollapsePanel>
  </Collapse>
</template>

<script lang="tsx" setup>
  import { Form, CollapsePanel, Collapse, RadioGroup, RadioButton } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMForm, FormProps, RenderCallbackParams, Recordable } from '@bmos/components';
  import ScopeNumber from '../../../../components/ScopeNumber.vue';

  defineOptions({
    name: 'DataAttrList',
    inheritAttrs: false,
  });

  const emit = defineEmits<{
    (e: 'update:dataAttrList', dataAttr: Array<Recordable>): void;
    (e: 'change', dataAttr: Array<Recordable>): void;
  }>();

  const props = withDefaults(
    defineProps<{
      dataAttrList: Array<Recordable>;
      activeNodeData: any;
      isView?: boolean;
    }>(),
    {
      dataAttrList: () => [],
      activeNodeData: () => {},
      isView: false,
    },
  );

  const activeKey = ref<string[]>([]);

  const attrList = computed(() => {
    const result: any = [];
    props.activeNodeData.children.forEach((group: Recordable) => {
      group.children.forEach((item: Recordable) => {
        if (item.componentType === 'CUSTOM_FIELD') {
          try {
            const componentDetail = JSON.parse(item.componentDetail);
            if (componentDetail.dataType === 'NUMBER') {
              if (
                result.findIndex((i: any) => JSON.parse(i.componentDetail).fieldData === componentDetail.fieldData) ===
                -1
              ) {
                result.push({
                  ...item,
                });
              }
            }
          } catch (error) {}
        }
      });
    });
    return result;
  });

  const formItemContext = Form.useInjectFormItemContext();
  const triggerChange = (changedValue: Recordable) => {
    try {
      const newMaterialList: any[] = cloneDeep(props.dataAttrList);
      const editItem = newMaterialList.find((item: Recordable) => {
        return JSON.parse(item.componentDetail).fieldData === JSON.parse(changedValue.componentDetail).fieldData;
      });
      if (editItem) {
        Object.assign(editItem, changedValue);
      } else {
        newMaterialList.push(changedValue);
      }
      emit('update:dataAttrList', newMaterialList);
      emit('change', newMaterialList);
      formItemContext.onFieldChange();
    } catch (error) {}
  };
  const onChange = (value: string, key: string, item: Recordable) => {
    triggerChange({
      ...item,
      [key]: value,
    });
  };

  const formRefs = ref<Recordable>({});
  const getFormRefs = (el: any, item: Recordable) => {
    if (el) {
      try {
        const componentDetail = JSON.parse(item.componentDetail);
        componentDetail.fieldData && (formRefs.value[componentDetail.fieldData] = el);
      } catch (error) {}
    }
  };
  watch(
    () => props.dataAttrList,
    async () => {
      await nextTick();
      try {
        Object.keys(formRefs.value).forEach(fieldData => {
          const materialItem = props.dataAttrList.find(item => {
            return JSON.parse(item.componentDetail).fieldData?.toString() === fieldData?.toString();
          });
          if (materialItem) {
            formRefs.value[fieldData].setFormModels(materialItem);
          } else {
            const attrItem = attrList.value.find((item: any) => {
              return JSON.parse(item.componentDetail).fieldData?.toString() === fieldData?.toString();
            });
            formRefs.value[fieldData].setFormModels({
              fieldId: attrItem.fieldId,
              componentDetail: attrItem.componentDetail,
            });
          }
        });
      } catch (error) {}
    },
    {
      immediate: true,
      deep: true,
    },
  );

  watch(
    () => props.isView,
    async (val: boolean) => {
      await nextTick();
      if (val) {
        await nextTick();
        Object.keys(formRefs.value).forEach(fieldData => {
          formRefs.value[fieldData].setFormProps({
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
        field: 'precision',
        component: 'InputNumber',
        label: t('数值精度（小数位数）'),
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            min: 0,
            max: 15,
            step: 1,
            precision: 0,
            onChange: () => {
              onChange(formModel.precision, 'precision', formModel as Recordable);
            },
          };
        },
        formItemProps: {
          htmlFor: 'precision' + Math.random(),
        },
      },
      {
        field: 'roundCode',
        component: 'Select',
        label: t('数值修约'),
        formItemProps: {
          htmlFor: 'roundCode' + Math.random(),
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('四舍五入'),
                value: 'HALF_UP',
              },
              {
                label: t('四舍六入五成双'),
                value: 'HALF_EVEN',
              },
            ],
            onChange: () => {
              onChange(formModel.roundCode, 'roundCode', formModel as Recordable);
            },
          };
        },
      },
      {
        field: 'limitTitle',
        label: t('阈值设置'),
        component: 'TableTitle',
      },
      {
        field: 'limit',
        component: 'Select',
        label: t('限制方式'),
        formItemProps: {
          htmlFor: 'limit' + Math.random(),
        },
        defaultValue: 0,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            allowClear: false,
            options: [
              {
                label: t('范围限制'),
                value: 0,
              },
              {
                label: t('数值相等'),
                value: 1,
              },
            ],
            onChange: () => {
              formInstance.validate(['scope']);
              onChange(formModel.limit, 'limit', formModel as Recordable);
            },
          };
        },
      },
      {
        field: 'scope',
        formItemProps: {
          htmlFor: 'scope' + Math.random(),
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel['limit'] !== 1;
        },
        defaultValue: {
          lowerLimit: 1,
          upperLimit: 1,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <ScopeNumber
              v-model:limit={formModel['scope']}
              onUpdate:limit={(val: any) => {
                formModel['scope'] = val;
                onChange(formModel.scope, 'scope', formModel as Recordable);
              }}
            />
          );
        },
        label: t('阈值设置'),
        dynamicRules: ({ formModel }) => {
          return [
            {
              required: false,
              validator: async () => {
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
                if (scopeMin === null) return Promise.resolve();
                if (scopeMin === undefined) return Promise.resolve();
                if (scopeMax === null) return Promise.resolve();
                if (scopeMax === undefined) return Promise.resolve();
                if (Number(scopeMin) >= Number(scopeMax)) {
                  return Promise.reject(t('最小值不能大于等于最大值'));
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
        formItemProps: {
          htmlFor: 'numericalValue' + Math.random(),
        },
        label: t('数值'),
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel['limit'] === 1;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            stringMode: true,
            onChange: () => {
              onChange(formModel.numericalValue, 'numericalValue', formModel as Recordable);
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              required: false,
              validator: async (rule, value) => {
                if (!value) return Promise.resolve();
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
        field: 'waringAutoRecordTitle',
        label: t('异常是否自动记录'),
        component: 'TableTitle',
      },
      {
        field: 'waringAutoRecord',
        noLabel: true,
        formItemProps: {
          htmlFor: 'waringAutoRecord' + Math.random(),
        },
        defaultValue: false,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <RadioGroup
                v-model:value={formModel['waringAutoRecord']}
                onChange={() => {
                  onChange(formModel.waringAutoRecord, 'waringAutoRecord', formModel as Recordable);
                }}>
                <RadioButton value={true}>{t('是')}</RadioButton>
                <RadioButton value={false} class='waring-false'>
                  {t('否')}
                </RadioButton>
              </RadioGroup>
            </>
          );
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

<style lang="less" scoped>
  .dataAttr-collapse.mes-collapse {
    border: none;
    .mes-collapse-item {
      border: none;
      margin-bottom: 8px;
      border-radius: 4px;
    }
  }
  :deep(.mes-collapse-item > .mes-collapse-header) {
    padding: 6px 8px;
    border-radius: 4px;
    background-color: var(--bmos-primary-color-background);
  }
  :deep(.mes-collapse-content) {
    border: none;
    .mes-collapse-content-box {
      padding: 0;
    }
  }
</style>
