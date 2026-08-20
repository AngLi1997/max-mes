<template>
  <div v-for="item in (equipmentPictureConfigList as any)" :key="item.formCode" class="draw_config_box">
    <BMForm :ref="el => getFormRefs(el, item)" v-bind="formProps"></BMForm>
    <div class="delete_box">
      <BMIcons v-if="!isView" class="delete-icon" icon="Delete" @click="() => deleteConditionList(item)" />
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { BMForm, FormProps, BMSelect } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  import type { RenderCallbackParams } from '@bmos/components';
  import { recordRoundingList } from '@/services';
  import ScopeNumber from './ScopeNumber.vue';
  import { Form } from 'ant-design-vue';
  defineOptions({
    name: 'EquipmentDataDraw',
    inheritAttrs: false,
  });
  const emit = defineEmits(['update:equipmentPictureConfigList', 'change', 'deleteItem']);
  const props = defineProps({
    equipmentPictureConfigList: {
      type: Array,
      default: () => [],
    },
    isView: {
      type: Boolean,
      default: false,
    },
  });
  import { dictListDictCode } from '@/services';

  const isSetValue = ref(false);
  const allAcquisitionDataCode = ref<any>({});
  const formItemContext = Form.useInjectFormItemContext();
  watch(
    () => props.equipmentPictureConfigList,
    () => {
      if (isSetValue.value) {
        isSetValue.value = false;
        return;
      }
      nextTick(() => {
        props.equipmentPictureConfigList.forEach((item: any) => {
          formRefs.value[item.formCode]?.setFieldsValue(item);
        });
      });
    },
  );
  watch(
    () => props.equipmentPictureConfigList.length,
    () => {
      nextTick(() => {
        formModelChange();
      });
    },
  );
  watch(
    () => props.isView,
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
  onMounted(() => {
    formModelChange();
  });

  const formModelChange = async () => {
    isSetValue.value = true;
    let newList = [] as any;
    allAcquisitionDataCode.value = [];
    for (let key in formRefs.value) {
      const data = await formRefs.value[key]?.getFormValues();
      if (!allAcquisitionDataCode.value[data.acquisitionDataCode]) {
        allAcquisitionDataCode.value[data.acquisitionDataCode] = 1;
      } else {
        allAcquisitionDataCode.value[data.acquisitionDataCode]++;
      }
      newList.push({ ...data, formCode: key });
    }
    emit('update:equipmentPictureConfigList', newList);
    formItemContext.onFieldChange();
  };
  const formRefs = ref<any>({});

  const getFormRefs = (el: any, item: any) => {
    if (el) {
      formRefs.value[item.formCode] = el;
    }
  };
  const deleteConditionList = (formItem: any) => {
    const newConditionList = props.equipmentPictureConfigList.filter(
      (item: any) => item.formCode !== formItem.formCode,
    );
    delete formRefs.value[formItem.formCode];
    emit('update:equipmentPictureConfigList', newConditionList);
    emit('change', newConditionList);
    emit('deleteItem', formItem);
  };

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

  // 表单属性
  const formProps: Ref<FormProps> = ref({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    initialValues: {
      requirementTime: {
        value: '',
        type: 'minute',
      },
      collectionTime: {
        value: '',
        type: 'minute',
      },
      standardLineConfig: {},
      warningLineConfig: {},
      correctionLineConfig: {},
    },
    schemas: [
      {
        field: 'acquisitionDataCode',
        component: 'Select',
        formItemProps: {
          labelCol: { span: 24 },
          htmlFor: 'name' + Math.random(),
        },
        required: true,
        label: t('设备数采数据'),
        componentProps: () => {
          return {
            getPopupContainer: (triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box'),
            request: async () => {
              // 获取设备数据
              try {
                const { data } = await dictListDictCode({
                  code: 'DeviceDataFields',
                });
                return data.map((item: any) => {
                  return {
                    label: `${item.label}-${item.value}`,
                    value: item.value,
                  };
                });
              } catch (error: any) {
                console.log('设备数采数据error:', error);
              }
            },
            onChange: () => {
              formModelChange();
            },
            onClear: () => {
              formModelChange();
            },
          };
        },
        dynamicRules() {
          return [
            {
              required: true,
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (!value) {
                  return Promise.reject(t('请选择设备数采数据'));
                }
                if (allAcquisitionDataCode.value[value] > 1) {
                  return Promise.reject(t('设备数采数据配置重复'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'scale',
        label: t('数值精度(小数位数)'),
        component: 'Input',
        dynamicRules({ formModel }: RenderCallbackParams) {
          return [
            {
              required: !!formModel.roundCode,
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (!!formModel.roundCode && !value) {
                  return Promise.reject(t('请输入精度'));
                } else if (!value) {
                  // 非必填,为填值不校验
                  return Promise.resolve();
                }
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (number < 0) {
                  return Promise.reject(t('请输入大于等于0的整数'));
                } else if (number > 15) {
                  return Promise.reject(t('请输入小于等于15的整数'));
                } else if (!Number.isInteger(number)) {
                  return Promise.reject(t('请输入整数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          onChange: () => {
            formModelChange();
          },
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
      },
      {
        field: 'roundCode',
        label: t('修约方式'),
        component: 'Select',
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentProps: () => {
          return {
            getPopupContainer: (triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box'),
            request: async () => {
              // 获取设备数据
              try {
                const { data } = await recordRoundingList();
                return data;
              } catch (error: any) {
                console.log(error);
              }
            },
            onChange: () => {
              formModelChange();
            },
          };
        },
        dynamicRules({ formModel }: RenderCallbackParams) {
          return [
            {
              required: !!formModel.scale,
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (formModel.scale && !value) {
                  return Promise.reject(t('请选择修约方式'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        component: 'Input',
        label: t('数据采集时长'),
        field: 'requirementTime.value',
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentSlots: {
          addonAfter: ({ formModel }: RenderCallbackParams) => {
            return (
              <BMSelect
                v-model:value={formModel.requirementTime.type}
                style='width: 80px;background: #fff;'
                onChange={formModelChange}
                getPopupContainer={(triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box')}
                options={[
                  { label: t('日'), value: 'day' },
                  { label: t('时'), value: 'hour' },
                  { label: t('分'), value: 'minute' },
                  { label: t('秒'), value: 'second' },
                ]}></BMSelect>
            );
          },
        },
        dynamicRules() {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (!value) {
                  return Promise.resolve();
                }
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (!Number.isInteger(number)) {
                  return Promise.reject(t('请输入正整数'));
                } else if (number <= 0) {
                  return Promise.reject(t('请输入正整数'));
                }
                if (`${value}`.length > 10) {
                  return Promise.reject(t('请输入小于11位正整数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        component: 'Input',
        label: t('采集间隔时长'),
        field: 'collectionTime.value',
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentSlots: {
          addonAfter: ({ formModel }: RenderCallbackParams) => {
            return (
              <BMSelect
                v-model:value={formModel.collectionTime.type}
                style='width: 80px;background: #fff;'
                getPopupContainer={(triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box')}
                onChange={formModelChange}
                options={[
                  { label: t('日'), value: 'day' },
                  { label: t('时'), value: 'hour' },
                  { label: t('分'), value: 'minute' },
                  { label: t('秒'), value: 'second' },
                ]}></BMSelect>
            );
          },
        },
        dynamicRules() {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (!value) {
                  return Promise.resolve();
                }
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (!Number.isInteger(number)) {
                  return Promise.reject(t('请输入整数'));
                } else if (number <= 0) {
                  return Promise.reject(t('请输入正整数'));
                }
                if (`${value}`.length > 10) {
                  return Promise.reject(t('请输入小于11位正整数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      // 标准线
      {
        field: 'drawConfig',
        noLabel: true,
        component: () => {
          return (
            <div class='line-label' style='color: #59BF78;'>
              {t('标准线')}
            </div>
          );
        },
      },
      {
        field: 'standardLineConfig.limitType',
        label: t('限制方式'),
        component: 'Select',
        defaultValue: 0,
        required: true,
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('范围限制'),
                value: 0,
              },
              {
                label: t('固定数值'),
                value: 1,
              },
            ],
            getPopupContainer: (triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box'),
            onChange: () => {
              formModel.standardLineConfig.scopeConfig = {
                lowerValue: null,
                lowerLimit: undefined,
                upperLimit: undefined,
                upperValue: null,
              };
              formModel.standardLineConfig.fixedValue = '';
              formModelChange();
            },
          };
        },
      },
      {
        field: 'standardLineConfig.scopeConfig',
        defaultValue: {
          lowerValue: null,
          lowerLimit: undefined,
          upperLimit: undefined,
          upperValue: null,
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.standardLineConfig.limitType == 0;
        },
        component: ({ formModel }: any) => {
          return (
            <ScopeNumber
              v-model:limit={formModel.standardLineConfig.scopeConfig}
              onUpdate:limit={(val: any) => {
                formModel.standardLineConfig.scopeConfig = val;
                formModelChange();
              }}
            />
          );
        },
        label: t('范围'),
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: false,
              validator: async () => {
                let lowerValue = formModel.standardLineConfig.scopeConfig?.lowerValue;
                let upperValue = formModel.standardLineConfig.scopeConfig?.upperValue;
                const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
                if (!(lowerValue === null || lowerValue === undefined)) {
                  if (!reg.test(lowerValue)) {
                    return Promise.reject(t('最小值整数或小数不能超过15位'));
                  }
                }

                if (!(upperValue === null || upperValue === undefined)) {
                  if (!reg.test(upperValue)) {
                    return Promise.reject(t('最大值整数或小数不能超过15位'));
                  }
                }
                if (lowerValue === null) return Promise.resolve();
                if (lowerValue === undefined) return Promise.resolve();
                if (upperValue === null) return Promise.resolve();
                if (upperValue === undefined) return Promise.resolve();
                if (Number(lowerValue) > Number(upperValue)) {
                  return Promise.reject(t('最小值不能大于最大值'));
                }
                // 如果限制方式为范围限制(开区间)，则最小值和最大值不能相等
                if (Number(lowerValue) === Number(upperValue)) {
                  return Promise.reject(t('最大值需大于最小值'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'standardLineConfig.fixedValue',
        label: t('数值'),
        component: 'Input',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.standardLineConfig.limitType == 1;
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        dynamicRules() {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (value.includes('.')) {
                  const int = value.split('.')[0];
                  const float = value.split('.')[1];
                  if (int.length > 15) {
                    return Promise.reject(t('整数部分不能超过15位'));
                  }
                  if (float.length > 15) {
                    return Promise.reject(t('小数部分不能超过15位'));
                  }
                } else {
                  if (value.length > 15) {
                    return Promise.reject(t('整数部分不能超过15位'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          onChange: formModelChange,
        },
      },
      // 警戒线
      {
        field: 'alertDrawConfig',
        noLabel: true,
        component: () => {
          return (
            <div class='line-label' style='color: #FF9A2F;'>
              {t('警戒线')}
            </div>
          );
        },
      },
      {
        field: 'warningLineConfig.limitType',
        label: t('限制方式'),
        component: 'Select',
        defaultValue: 0,
        required: true,
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            getPopupContainer: (triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box'),
            options: [
              {
                label: t('范围限制'),
                value: 0,
              },
              {
                label: t('固定数值'),
                value: 1,
              },
            ],
            onChange: () => {
              formModel.warningLineConfig.scopeConfig = {
                lowerValue: null,
                lowerLimit: undefined,
                upperLimit: undefined,
                upperValue: null,
              };
              formModel.warningLineConfig.fixedValue = '';
              formModelChange();
            },
          };
        },
      },
      {
        field: 'warningLineConfig.scopeConfig',
        defaultValue: {
          lowerValue: null,
          lowerLimit: undefined,
          upperLimit: undefined,
          upperValue: null,
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.warningLineConfig.limitType == 0;
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        component: ({ formModel }: any) => {
          return (
            <ScopeNumber
              v-model:limit={formModel.warningLineConfig.scopeConfig}
              onUpdate:limit={(val: any) => {
                formModel.warningLineConfig.scopeConfig = val;
                formModelChange();
              }}
            />
          );
        },
        label: t('范围'),
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: false,
              validator: async () => {
                let lowerValue = formModel.warningLineConfig.limitType?.['lowerValue'];
                let upperValue = formModel.warningLineConfig.limitType?.['upperValue'];
                const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
                if (!(lowerValue === null || lowerValue === undefined)) {
                  if (!reg.test(lowerValue)) {
                    return Promise.reject(t('最小值整数或小数不能超过15位'));
                  }
                }

                if (!(upperValue === null || upperValue === undefined)) {
                  if (!reg.test(upperValue)) {
                    return Promise.reject(t('最大值整数或小数不能超过15位'));
                  }
                }
                if (lowerValue === null) return Promise.resolve();
                if (lowerValue === undefined) return Promise.resolve();
                if (upperValue === null) return Promise.resolve();
                if (upperValue === undefined) return Promise.resolve();
                if (Number(lowerValue) > Number(upperValue)) {
                  return Promise.reject(t('最小值不能大于最大值'));
                }
                // 如果限制方式为范围限制(开区间)，则最小值和最大值不能相等
                if (Number(lowerValue) === Number(upperValue)) {
                  return Promise.reject(t('最大值需大于最小值'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'warningLineConfig.fixedValue',
        label: t('数值'),
        component: 'Input',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.warningLineConfig.limitType == 1;
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        dynamicRules() {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (value.includes('.')) {
                  const int = value.split('.')[0];
                  const float = value.split('.')[1];
                  if (int.length > 15) {
                    return Promise.reject(t('整数部分不能超过15位'));
                  }
                  if (float.length > 15) {
                    return Promise.reject(t('小数部分不能超过15位'));
                  }
                } else {
                  if (value.length > 15) {
                    return Promise.reject(t('整数部分不能超过15位'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          onChange: formModelChange,
        },
      },
      // 纠偏线
      {
        field: 'correctionDrawConfig',
        noLabel: true,
        component: () => {
          return (
            <div class='line-label' style='color: #FF5633;'>
              {t('纠偏线')}
            </div>
          );
        },
      },
      {
        field: 'correctionLineConfig.limitType',
        label: t('限制方式'),
        component: 'Select',
        defaultValue: 0,
        required: true,
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            getPopupContainer: (triggerNode: any) => triggerNode.parentNode.closest('.draw_config_box'),
            options: [
              {
                label: t('范围限制'),
                value: 0,
              },
              {
                label: t('固定数值'),
                value: 1,
              },
            ],
            onChange: () => {
              formModel.correctionLineConfig.scopeConfig = {
                lowerValue: null,
                lowerLimit: undefined,
                upperLimit: undefined,
                upperValue: null,
              };
              formModel.correctionLineConfig.fixedValue = '';
              formModelChange();
            },
          };
        },
      },
      {
        field: 'correctionLineConfig.scopeConfig',
        defaultValue: {
          lowerValue: null,
          lowerLimit: undefined,
          upperLimit: undefined,
          upperValue: null,
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.correctionLineConfig.limitType == 0;
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        component: ({ formModel }: any) => {
          return (
            <ScopeNumber
              v-model:limit={formModel.correctionLineConfig.scopeConfig}
              onUpdate:limit={(val: any) => {
                formModel.correctionLineConfig.scopeConfig = val;
                formModelChange();
              }}
            />
          );
        },
        label: t('范围'),
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: false,
              validator: async () => {
                let lowerValue = formModel.correctionLineConfig.scopeConfig?.['lowerValue'];
                let upperValue = formModel.correctionLineConfig.scopeConfig?.['upperValue'];
                const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
                if (!(lowerValue === null || lowerValue === undefined)) {
                  if (!reg.test(lowerValue)) {
                    return Promise.reject(t('最小值整数或小数不能超过15位'));
                  }
                }

                if (!(upperValue === null || upperValue === undefined)) {
                  if (!reg.test(upperValue)) {
                    return Promise.reject(t('最大值整数或小数不能超过15位'));
                  }
                }
                if (lowerValue === null) return Promise.resolve();
                if (lowerValue === undefined) return Promise.resolve();
                if (upperValue === null) return Promise.resolve();
                if (upperValue === undefined) return Promise.resolve();
                if (Number(lowerValue) > Number(upperValue)) {
                  return Promise.reject(t('最小值不能大于最大值'));
                }
                // 如果限制方式为范围限制(开区间)，则最小值和最大值不能相等
                if (Number(lowerValue) === Number(upperValue)) {
                  return Promise.reject(t('最大值需大于最小值'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'correctionLineConfig.fixedValue',
        label: t('数值'),
        component: 'Input',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.correctionLineConfig.limitType == 1;
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        dynamicRules() {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (value.includes('.')) {
                  const int = value.split('.')[0];
                  const float = value.split('.')[1];
                  if (int.length > 15) {
                    return Promise.reject(t('整数部分不能超过15位'));
                  }
                  if (float.length > 15) {
                    return Promise.reject(t('小数部分不能超过15位'));
                  }
                } else {
                  if (value.length > 15) {
                    return Promise.reject(t('整数部分不能超过15位'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          onChange: formModelChange,
        },
      },
    ],
  });
</script>
<style scoped lang="less">
  :deep(.mes-input-group-addon .mes-select-selector) {
    border: 1px solid #d4d7d9 !important;
    box-sizing: border-box;
  }
  .draw_config_box {
    background: #f5f7fa;
    margin-bottom: 20px;
    padding: 0 16px 16px;
    .delete_box {
      display: flex;
      justify-content: end;
      .delete-icon {
        color: #6c7380;
      }
    }
  }
  .line-label {
    background-color: #f5f7fa;
  }
  :deep(.no-label) {
    margin: 0;
  }
</style>
