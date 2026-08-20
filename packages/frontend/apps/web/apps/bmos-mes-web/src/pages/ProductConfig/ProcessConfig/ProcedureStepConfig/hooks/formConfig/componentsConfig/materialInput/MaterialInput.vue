<template>
  <div v-for="item in materials" :key="item.key" class="material-item">
    <BMForm :ref="(el: any) => getFormRefs(el, item)" v-bind="formProps" />
    <div class="delete-btn">
      <BMIcons v-show="!isView" class="delete-icon" icon="Delete" @click="() => deleteMaterialList(item)" />
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { Form, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { BMIcons } from '@bmos/icons';
  import { BMForm, FormProps, RenderCallbackParams, Recordable } from '@bmos/components';
  import { reqProductFormulaMaterialListByProcedureId, reqWeighingCenterTree } from '@/services';
  import { loopSelectableNotValueTree } from '@bmos/utils';

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
      isView?: boolean;
    }>(),
    {
      materials: () => [],
      procedureModelId: '',
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
    const newMaterialList = props.materials.filter((conditionItem: Recordable) => conditionItem.key !== item.key);
    emit('update:materials', newMaterialList);
    emit('change', newMaterialList);
    emit('deleteItem', item);

    formItemContext.onFieldChange();
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

  const getProductionPreparationCenterOptions = async () => {
    try {
      const { data } = await reqWeighingCenterTree();
      return loopSelectableNotValueTree(data, 'isCategory', false);
    } catch (error) {
      return [];
    }
  };

  const getProductionPreparationCenterOptionsAndSetOptions = async (procedureId: string, formInstance: any) => {
    try {
      const treeData = await getProductionPreparationCenterOptions();
      formInstance?.updateSchema({
        field: 'productionPreparationCenter',
        componentProps: {
          treeData,
        },
      });
    } catch (error) {
      formInstance?.updateSchema([
        {
          field: 'productionPreparationCenter',
          componentProps: {
            treeData: [],
          },
        },
      ]);
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
          if (materialItem?.source) {
            getProductionPreparationCenterOptionsAndSetOptions(materialItem?.source as string, formRefs.value[key]);
          }
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
        label: t('物料来源'),
        field: 'source',
        required: true,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            showSearch: true,
            options: [
              {
                label: t('称量中心'),
                value: 1,
              },
            ],
            filterOption: commonFilterOption,
            onChange: (value: SelectValue) => {
              onChange(value, 'source', formModel as Recordable);
              onChange(undefined, 'productionPreparationCenter', formModel as Recordable);
              if (value) {
                //
              } else {
                formInstance?.updateSchema([
                  {
                    field: 'productionPreparationCenter',
                    componentProps: {
                      treeData: [],
                    },
                  },
                ]);
              }
            },
          };
        },
        formItemProps: {
          htmlFor: 'source' + Math.random(),
        },
      },
      {
        component: 'TreeSelect',
        label: t('生产准备中心'),
        field: 'productionPreparationCenter',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            showSearch: true,
            fieldNames: { label: 'name', value: 'id' },
            request: async () => {
              try {
                if (!formModel.source) {
                  return [];
                }
                return await getProductionPreparationCenterOptions();
              } catch (error) {
                return [];
              }
            },
            filterOption: (input: string, option: any) => commonFilterOption(input, option, 'name'),
            onChange: (value: SelectValue, _option: any) => {
              onChange(value, 'productionPreparationCenter', formModel as Recordable);
            },
            onDropdownVisibleChange: (visible: boolean) => {
              if (visible && !formModel.source) {
                message.error(t('请先选择物料来源'));
              }
            },
          };
        },
        formItemProps: {
          htmlFor: 'productionPreparationCenter' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('物料'),
        field: 'formulaMaterialId',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            showSearch: true,
            request: async () => {
              try {
                const { data } = await reqProductFormulaMaterialListByProcedureId(props.procedureModelId);
                // 如果有 formulaMaterialId 则更新 unitName unitId
                if (formModel.formulaMaterialId) {
                  const material = data.find((item: any) => item.id === formModel.formulaMaterialId);
                  if (material) {
                    triggerChange({
                      ...formModel,
                      unitName: material.unitName,
                      unitId: material.unitId,
                    });
                  }
                }
                return data.map((item: any) => ({
                  label: item.materialMergeCode + '-' + item.materialName,
                  value: item.id,
                  ...item,
                }));
              } catch (error) {
                return [];
              }
            },
            filterOption: (input: string, option: any) => commonFilterOption(input, option),
            onChange: (value: SelectValue, option: any) => {
              onChange(value, 'formulaMaterialId', formModel as Recordable);
              if (value) {
                triggerChange({
                  ...formModel,
                  unitName: option.unitName,
                  unitId: option.unitId,
                });
              } else {
                triggerChange({
                  ...formModel,
                  unitName: '',
                  unitId: '',
                });
              }
            },
          };
        },
        formItemProps: {
          htmlFor: 'formulaMaterialId' + Math.random(),
        },
      },
      {
        component: 'Input',
        label: t('需求量'),
        field: 'demand',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              onChange(formModel.demand, 'demand', formModel as Recordable);
            },
          };
        },
        componentSlots: {
          addonAfter: ({ formModel }: any) => (
            <span
              style={{
                display: 'inline-block',
                width: '80px',
              }}>
              {formModel.unitName}
            </span>
          ),
        },
        formItemProps: {
          htmlFor: 'demand' + Math.random(),
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
      {
        component: 'Input',
        label: t('物料需求时间'),
        field: 'requirementTime',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              onChange(formModel.requirementTime, 'requirementTime', formModel as Recordable);
            },
          };
        },
        componentSlots: {
          addonAfter: () => (
            <span
              style={{
                display: 'inline-block',
                width: '80px',
              }}>
              {t('日')}
            </span>
          ),
        },
        formItemProps: {
          htmlFor: 'requirementTime' + Math.random(),
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                if (isNaN(Number(value)) || Number(value) < 0) {
                  return Promise.reject(t('请输入自然数'));
                }
                // 如果值 整数部分最多为10位，小数位数最多为9位
                const reg = /^\d{1,3}$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('物料需求时间最大999的自然数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        component: 'Input',
        label: t('需求失效时间'),
        field: 'demandExpirationTime',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              onChange(formModel.demandExpirationTime, 'demandExpirationTime', formModel as Recordable);
            },
          };
        },
        componentSlots: {
          addonAfter: () => (
            <span
              style={{
                display: 'inline-block',
                width: '80px',
              }}>
              {t('日')}
            </span>
          ),
        },
        formItemProps: {
          htmlFor: 'demandExpirationTime' + Math.random(),
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入正整数'));
                }
                // 正则判断 最大 999 的正整数
                const reg = /^\d{1,3}$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('需求失效时间最大999的正整数'));
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
