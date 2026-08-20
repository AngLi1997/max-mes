<template>
  <div v-for="item in conditionList" :key="item.code" class="condition-item">
    <div>{{ conditionTitle }}：{{ item.code }}</div>
    <BMForm :ref="el => getFormRefs(el, item)" v-bind="formProps" />
    <div class="test-delete">
      <div class="default-test">
        <span class="default-test-label">{{ t('默认测试条件') }}：</span>
        <Switch
          :checked="item.defaultResult"
          checked-children="true"
          un-checked-children="false"
          @change="(val: any, _e: any) => onSwitchChange(val, 'defaultResult', item)" />
      </div>
      <BMIcons v-show="!isView" class="delete-icon" icon="Delete" @click="() => deleteConditionList(item)" />
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { Form, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { BMIcons } from '@bmos/icons';
  import { ConditionItem, ConditionType, SegmentedType, SegmentedTypeValue } from '../../ProcedureFlow/types';
  import { BMForm, FormProps, RenderCallbackParams, Recordable } from '@bmos/components';
  import { reqAllProcedureCompleteNodeList } from '@/services';

  defineOptions({
    name: 'ExecutionCondition',
    inheritAttrs: false,
  });

  const emit = defineEmits<{
    (e: 'update:conditionList', conditionList: ConditionItem[]): void;
    (e: 'change', conditionList: ConditionItem[]): void;
    (e: 'deleteItem', item: ConditionItem): void;
  }>();

  const props = withDefaults(
    defineProps<{
      conditionList: ConditionItem[];
      processDetail: Record<string, any>;
      segmentedValue?: SegmentedTypeValue;
      isView?: boolean;
    }>(),
    {
      conditionList: () => [],
      processDetail: () => ({}),
      segmentedValue: SegmentedType.ExecutionCondition,
      isView: false,
    },
  );

  const commonFilterOption = (input: string, option: any, labelField: string = 'label') =>
    option[labelField].toLowerCase().indexOf(input.toLowerCase()) >= 0;

  const conditionTypeList = [{ label: t('任务节点完成'), value: ConditionType.TaskNodeComplete }];

  const conditionTitle = computed(() => {
    if (props.segmentedValue === SegmentedType.ExecutionCondition) {
      return t('执行条件');
    }
    return t('完成条件');
  });

  const formItemContext = Form.useInjectFormItemContext();
  const triggerChange = (changedValue: ConditionItem) => {
    const newConditionList = props.conditionList.map((item: ConditionItem) => {
      if (item.code === changedValue.code) {
        return { ...item, ...changedValue };
      }
      return item;
    });
    emit('update:conditionList', newConditionList);
    emit('change', newConditionList);
    formItemContext.onFieldChange();
  };
  const deleteConditionList = (item: ConditionItem) => {
    const newConditionList = props.conditionList.filter(
      (conditionItem: ConditionItem) => conditionItem.code !== item.code,
    );
    emit('update:conditionList', newConditionList);
    emit('change', newConditionList);
    emit('deleteItem', item);

    formItemContext.onFieldChange();
  };
  const onChange = (item: ConditionItem) => {
    triggerChange({
      ...item,
    });
  };
  const onSelectChange = (value: SelectValue, key: string, item: ConditionItem) => {
    triggerChange({
      ...item,
      [key]: value,
    });
  };
  const onConditionTypeSelectChange = (value: SelectValue, key: string, item: ConditionItem) => {
    const newItem = {
      ...item,
      [key]: value,
    };
    const newConditionList = props.conditionList.map((item: ConditionItem) => {
      if (item.code === newItem.code) {
        return {
          ...newItem,
          [key]: value,
        };
      }
      return item;
    });
    emit('update:conditionList', newConditionList);
    emit('change', newConditionList);
    formItemContext.onFieldChange();
  };

  const onSwitchChange = (val: boolean, key: string, item: ConditionItem) => {
    triggerChange({
      ...item,
      [key]: val,
    });
  };

  const formRefs = ref<Recordable>({});
  const getFormRefs = (el: any, item: ConditionItem) => {
    if (el) {
      formRefs.value[item.code] = el;
    }
  };

  watch(
    () => props.conditionList,
    async () => {
      await nextTick();
      Object.keys(formRefs.value).forEach(key => {
        const conditionItem = props.conditionList.find(item => item.code === key);
        if (conditionItem) {
          formRefs.value[key].setFormModels(conditionItem);
          if (!conditionItem?.procedureId) {
            formRefs.value[key].setFormModel('procedureId', props.processDetail?.id);
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
        component: 'Input',
        label: t('条件名称'),
        field: 'name',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            onChange: () => {
              onChange(formModel as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('条件类型'),
        field: 'conditionType',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: conditionTypeList,
            filterOption: commonFilterOption,
            onChange: (value: SelectValue) => {
              onConditionTypeSelectChange(value, 'conditionType', {
                code: formModel.code,
                name: formModel.name,
                conditionType: value,
                defaultResult: formModel.defaultResult,
                procedureId: undefined,
              } as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'conditionType' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('工序节点'),
        field: 'procedureId',
        required: true,
        defaultValue: props.processDetail?.id,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (
            (formModel as ConditionItem).conditionType === ConditionType.TaskNodeComplete ||
            (formModel as ConditionItem).conditionType === ConditionType.StepNodeComplete
          );
        },
        componentProps: () => {
          return {
            disabled: true,
            fieldNames: { label: 'name', value: 'id' },
            options: [
              {
                name: props.processDetail?.label,
                id: props.processDetail?.id,
              },
            ],
          };
        },
        formItemProps: {
          htmlFor: 'procedureId' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('任务节点'),
        field: 'taskNodeId',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.TaskNodeComplete;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: { label: 'name', value: 'id' },
            request: async () => {
              try {
                if (!props.processDetail?.id) {
                  return [];
                }
                const { data } = await reqAllProcedureCompleteNodeList(props.processDetail?.id as string);
                return data;
              } catch (error) {
                return [];
              }
            },
            filterOption: (input: string, option: any) => commonFilterOption(input, option, 'name'),
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'taskNodeId', formModel as ConditionItem);
            },
            onDropdownVisibleChange: (visible: boolean) => {
              if (visible && !formModel.procedureId) {
                message.error(t('请选择工序节点'));
              }
            },
          };
        },
        formItemProps: {
          htmlFor: 'taskNodeId' + Math.random(),
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
  .condition-item {
    background-color: var(--bmos-background-color);
    padding: var(--bmos-padding-mini);
    margin-bottom: var(--bmos-margin-large);
    .label {
      display: inline-block;
      color: var(--bmos-third-level-text-color);
      margin-bottom: var(--bmos-margin-small);
      margin-top: var(--bmos-margin-large);
    }
    .test-delete {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: var(--bmos-margin-large);
      .delete-icon {
        color: var(--bmos-danger-color);
      }
    }
  }
</style>
