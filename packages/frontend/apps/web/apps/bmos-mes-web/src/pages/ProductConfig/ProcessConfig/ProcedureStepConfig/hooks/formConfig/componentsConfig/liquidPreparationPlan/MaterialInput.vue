<template>
  <div v-for="(item, index) in materials" :key="item.key" class="material-item">
    <BMForm :ref="(el: any) => getFormRefs(el, item)" v-bind="formProps" />
    <div class="delete-btn">
      <BMIcons
        v-show="!isView && index > 0"
        class="delete-icon"
        icon="Delete"
        @click="() => deleteMaterialList(item)" />
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { Form, message, Modal } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { BMIcons } from '@bmos/icons';
  import { BMForm, FormProps, RenderCallbackParams, Recordable } from '@bmos/components';
  import { reqMaterialFieldInfoListReq, reqProductFormulaMaterialListByProcedureId } from '@/services';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    name: 'MaterialItem',
    inheritAttrs: false,
  });

  const emit = defineEmits<{
    (e: 'update:materials', material: Array<Recordable>): void;
    (e: 'change', material: Array<Recordable>): void;
    (e: 'deleteItem', item: Recordable): void;
  }>();

  const props = withDefaults(
    defineProps<{
      materials: Array<Recordable>;
      procedureModelId: string;
      versionId: string;
      isView?: boolean;
    }>(),
    {
      materials: () => [],
      procedureModelId: '',
      versionId: '',
      isView: false,
    },
  );

  const commonFilterOption = (input: string, option: any, labelField: string = 'label') =>
    option[labelField].toLowerCase().indexOf(input.toLowerCase()) >= 0;

  const formItemContext = Form.useInjectFormItemContext();
  const triggerChange = (changedValue: Recordable) => {
    const newMaterialList = props.materials.map((item: Recordable) => {
      if (item.key === changedValue.key) {
        return { ...item, ...changedValue };
      }
      return item;
    });
    emit('update:materials', newMaterialList);
    emit('change', newMaterialList);
    formItemContext.onFieldChange();
  };
  const deleteMaterialList = (item: Recordable) => {
    if (props.materials.length === 1) {
      message.error(t('至少保留一个条件'));
      return;
    }
    Modal.confirm({
      title: t('删除物料信息'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否删除该条物料信息'),
      async onOk() {
        try {
          const newMaterialList = props.materials.filter((conditionItem: Recordable) => conditionItem.key !== item.key);
          emit('update:materials', newMaterialList);
          emit('change', newMaterialList);
          emit('deleteItem', item);
          formItemContext.onFieldChange();
          return Promise.resolve();
        } catch (error: any) {
          return Promise.reject();
        }
      },
    });
  };
  const onChange = (value: SelectValue, key: string, item: Recordable) => {
    triggerChange({
      ...item,
      [key]: value,
    });
  };

  const formRefs = ref<Recordable>({});
  const getFormRefs = (el: any, item: Recordable) => {
    if (el) {
      formRefs.value[item.key] = el;
    }
  };

  watch(
    () => props.materials,
    async () => {
      await nextTick();
      Object.keys(formRefs.value).forEach(key => {
        const materialItem = props.materials.find(item => {
          return item.key?.toString() === key?.toString();
        });
        if (materialItem) {
          formRefs.value[key].setFormModels(materialItem);
        }
      });
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
        component: 'Select',
        label: t('物料信息'),
        field: 'formulaMaterialId',
        required: true,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            showSearch: true,
            request: async () => {
              try {
                const { data } = await reqProductFormulaMaterialListByProcedureId(props.procedureModelId);
                // 如果其他块选择了该物料，不允许再次选择
                return data.map((item: any) => ({
                  label: item.materialMergeCode + '-' + item.materialName,
                  value: item.id,
                  ...item,
                  disabled: props.materials.some(
                    (materialItem: Recordable) => materialItem.formulaMaterialId === item.id,
                  ),
                }));
              } catch (error) {
                return [];
              }
            },
            onFocus: async () => {
              try {
                const { data } = await reqProductFormulaMaterialListByProcedureId(props.procedureModelId);
                // 如果其他块选择了该物料，不允许再次选择
                const options = data.map((item: any) => ({
                  label: item.materialMergeCode + '-' + item.materialName,
                  value: item.id,
                  ...item,
                  disabled: props.materials.some(
                    (materialItem: Recordable) => materialItem.formulaMaterialId === item.id,
                  ),
                }));
                formInstance?.updateSchema({
                  field: 'formulaMaterialId',
                  componentProps: {
                    options,
                  },
                });
              } catch (error) {
                return [];
              }
            },
            filterOption: (input: string, option: any) => commonFilterOption(input, option),
            onChange: (value: SelectValue, option: any) => {
              onChange(value, 'formulaMaterialId', formModel as Recordable);
              triggerChange({
                ...formModel,
                formulaMaterialId: value,
                materialId: option?.materialId,
                concentrationParameter: undefined,
                field: undefined,
                fieldName: undefined,
              });
            },
          };
        },
        formItemProps: {
          htmlFor: 'formulaMaterialId' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('浓度参数'),
        field: 'concentrationParameter',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            showSearch: true,
            fieldNames: { label: 'showName', value: 'id' },
            filterOption: (input: string, option: any) => commonFilterOption(input, option, 'showName'),
            request: {
              watchFields: ['formulaMaterialId'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.materialId) {
                    return [];
                  }
                  const { data } = await reqMaterialFieldInfoListReq(formModel.materialId, 'MaterialBatchCustomFields');
                  return data.map((item: any) => ({
                    showName: item.fieldName + '-' + item.field,
                    ...item,
                  }));
                } catch (error) {
                  return [];
                }
              },
            },
            onChange: (value: SelectValue, option: any) => {
              onChange(value, 'concentrationParameter', formModel as Recordable);
              triggerChange({
                ...formModel,
                concentrationParameter: value,
                field: option.field,
                fieldName: option.fieldName,
              });
            },
          };
        },
        formItemProps: {
          htmlFor: 'concentrationParameter' + Math.random(),
        },
      },
      {
        component: 'Input',
        label: t('目标浓度'),
        field: 'targetConcentration',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              onChange(formModel.targetConcentration, 'targetConcentration', formModel as Recordable);
            },
          };
        },
        formItemProps: {
          htmlFor: 'targetConcentration' + Math.random(),
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 如果值 整数部分最多为10位，小数位数最多为9位
                const reg = /^\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                return Promise.resolve();
              },
            },
          ];
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
  .material-item {
    background-color: var(--bmos-background-color);
    padding: var(--bmos-padding-mini);
    margin-bottom: var(--bmos-margin-large);
    .label {
      display: inline-block;
      color: var(--bmos-third-level-text-color);
      margin-bottom: var(--bmos-margin-small);
      margin-top: var(--bmos-margin-large);
    }
    .delete-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      margin-top: var(--bmos-margin-large);
      .delete-icon {
        color: var(--bmos-danger-color);
      }
    }
  }
</style>
