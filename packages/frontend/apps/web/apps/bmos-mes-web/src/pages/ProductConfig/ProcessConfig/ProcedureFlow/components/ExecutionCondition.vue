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
  import { ConditionItem, ConditionType, ConditionTypeValue, SegmentedType, SegmentedTypeValue } from '../types';
  import { BMForm, FormProps, RenderCallbackParams, Recordable } from '@bmos/components';
  import {
    getProcedureNodeListReq,
    reqAllProcedureGetProcedureModel,
    reqAllProcedureStepEquipmentList,
    reqAllProcedureStepRoomsListAll,
    reqProductFormulaMaterialListByProcedureId,
  } from '@/services';
  import { loopSelectableNotValueTree } from '@bmos/utils';

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
      settingNodeFormData?: Record<string, any>;
      procedureModelId: string;
      isView?: boolean;
      versionId?: string;
    }>(),
    {
      conditionList: () => [],
      processDetail: () => ({}),
      segmentedValue: SegmentedType.ExecutionCondition,
      procedureModelId: '',
      isView: false,
      settingNodeFormData: () => ({}),
      versionId: '',
    },
  );

  const commonFilterOption = (input: string, option: any, labelField: string = 'label') =>
    option[labelField].toLowerCase().indexOf(input.toLowerCase()) >= 0;

  const conditionTypeList = [
    { label: t('步骤节点完成'), value: ConditionType.StepNodeComplete },
    { label: t('任务节点完成'), value: ConditionType.TaskNodeComplete },
    { label: t('设备使用状态'), value: ConditionType.EquipmentUseState },
    { label: t('房间状态'), value: ConditionType.RoomState },
    { label: t('物料预定量'), value: ConditionType.MaterialReserveNumber },
  ];
  const conditionTypeOptions = computed(() => {
    if (props.segmentedValue === SegmentedType.ExecutionCondition) {
      return conditionTypeList;
    }
    return [
      ...conditionTypeList,
      { label: t('配料称量签名'), value: ConditionType.DosingSignature },
      { label: t('中间品产出签名'), value: ConditionType.OutputSignature },
    ];
  });

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
    // if (props.conditionList.length === 1) {
    //   message.error(t('至少保留一个条件'));
    //   return;
    // }
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

  const onNumberChange = (val: number | string, key: string, item: ConditionItem) => {
    triggerChange({
      ...item,
      [key]: val,
    });
  };
  const getTaskOptions = async (procedureId: string | undefined, type: boolean) => {
    if (!procedureId) {
      return [];
    }
    try {
      const { data } = await getProcedureNodeListReq(procedureId, type, props.settingNodeFormData?.id);
      return data;
    } catch (error) {
      return [];
    }
  };
  const getTaskOptionsAndSetOptions = async (
    procedureId: string,
    formInstance: any,
    conditionType: ConditionTypeValue,
  ) => {
    try {
      const options = await getTaskOptions(
        procedureId,
        conditionType === ConditionType.StepNodeComplete ? true : false,
      );
      if (conditionType === ConditionType.StepNodeComplete) {
        formInstance.updateSchema([
          {
            field: 'stepId',
            componentProps: {
              options,
            },
          },
        ]);
      } else {
        formInstance.updateSchema({
          field: 'taskNodeId',
          componentProps: {
            options,
          },
        });
      }
    } catch (error) {
      formInstance.updateSchema([
        {
          field: 'taskNodeId',
          componentProps: {
            options: [],
          },
        },
        {
          field: 'stepId',
          componentProps: {
            options: [],
          },
        },
      ]);
    }
  };

  // 设备使用状态
  const equipmentUseStatusOptions = [
    { label: t('可用'), value: 1 },
    { label: t('不可用'), value: 2 },
    { label: t('故障'), value: 4 },
    { label: t('占用'), value: 3 },
  ];

  const RoomStateOptions = [
    { label: t('已清场'), value: 3 },
    { label: t('待清场'), value: 2 },
    { label: t('在用'), value: 1 },
  ];

  // 物料信息
  // 可选范围：大于或等于、大于、等于、小于、小于或等于
  const checkRuleOptions = [
    { label: t('大于或等于'), value: '>=' },
    { label: t('大于'), value: '>' },
    { label: t('等于'), value: '==' },
    { label: t('小于'), value: '<' },
    { label: t('小于或等于'), value: '<=' },
  ];

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
          if (conditionItem?.procedureId) {
            if (conditionItem) {
              getTaskOptionsAndSetOptions(
                conditionItem?.procedureId as string,
                formRefs.value[key],
                conditionItem.conditionType as ConditionTypeValue,
              );
            }
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
            options: conditionTypeOptions.value,
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
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (
            (formModel as ConditionItem).conditionType === ConditionType.TaskNodeComplete ||
            (formModel as ConditionItem).conditionType === ConditionType.StepNodeComplete
          );
        },
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            fieldNames: { label: 'name', value: 'id' },
            options: props.processDetail?.procedures,
            request: async () => {
              try {
                const { data } = await reqAllProcedureGetProcedureModel(props.versionId, props.settingNodeFormData?.id);
                return data;
              } catch (error) {
                return [];
              }
            },
            filterOption: (input: string, option: any) => commonFilterOption(input, option, 'name'),
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'procedureId', {
                ...formModel,
                taskNodeId: undefined,
                stepId: undefined,
              } as ConditionItem);
              if (value) {
                // getTaskOptionsAndSetOptions(value as string, formInstance, formModel.conditionType);
              } else {
                formInstance?.updateSchema([
                  {
                    field: 'taskNodeId',
                    componentProps: {
                      options: [],
                    },
                  },
                  {
                    field: 'stepId',
                    componentProps: {
                      options: [],
                    },
                  },
                ]);
              }
            },
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
                if (!formModel.procedureId) {
                  return [];
                }
                const { data } = await getProcedureNodeListReq(
                  formModel.procedureId as string,
                  false,
                  props.settingNodeFormData?.id,
                );
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
      {
        component: 'Select',
        label: t('步骤节点'),
        field: 'stepId',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.StepNodeComplete;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: { label: 'name', value: 'id' },
            request: async () => {
              try {
                if (!formModel.procedureId) {
                  return [];
                }
                const { data } = await getProcedureNodeListReq(
                  formModel.procedureId as string,
                  true,
                  props.settingNodeFormData?.id,
                );
                return data;
              } catch (error) {
                return [];
              }
            },
            filterOption: (input: string, option: any) => commonFilterOption(input, option, 'name'),
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'stepId', formModel as ConditionItem);
            },
            onDropdownVisibleChange: (visible: boolean) => {
              if (visible && !formModel.procedureId) {
                message.error(t('请选择工序节点'));
              }
            },
          };
        },
        formItemProps: {
          htmlFor: 'stepId' + Math.random(),
        },
      },
      {
        component: 'TreeSelect',
        label: t('设备信息'),
        field: 'equipmentId',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.EquipmentUseState;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: { label: 'name', value: 'id' },
            request: async () => {
              try {
                const { data } = await reqAllProcedureStepEquipmentList(props.settingNodeFormData?.id);
                return loopSelectableNotValueTree(data, 'flag', true);
              } catch (error) {
                return [];
              }
            },
            showSearch: true,
            treeNodeFilterProp: 'name',
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'equipmentId', formModel as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'equipmentId' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('使用状态'),
        required: true,
        field: 'deviceState',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.EquipmentUseState;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: equipmentUseStatusOptions,
            filterOption: commonFilterOption,
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'deviceState', formModel as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'deviceState' + Math.random(),
        },
      },
      {
        component: 'TreeSelect',
        label: t('房间信息'),
        field: 'procedureName',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.RoomState;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: { label: 'showName', value: 'roomIdPath' },
            request: async () => {
              try {
                const { data } = await reqAllProcedureStepRoomsListAll({
                  procedureModelId: props.procedureModelId,
                  stepModelId: props.settingNodeFormData?.id,
                });
                return loopSelectableNotValueTree(data, 'roomFlag', true);
              } catch (error) {
                return [];
              }
            },
            showSearch: true,
            treeNodeFilterProp: 'showName',
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'procedureName', {
                ...formModel,
                roomId: value?.toString()?.split('-')?.pop() as string,
              } as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'procedureName' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('使用状态'),
        field: 'roomState',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.RoomState;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: RoomStateOptions,
            filterOption: commonFilterOption,
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'roomState', formModel as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'roomState' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('物料信息'),
        field: 'materialId',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.MaterialReserveNumber;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            request: async () => {
              try {
                const { data } = await reqProductFormulaMaterialListByProcedureId(
                  props.procedureModelId,
                  null,
                  props.settingNodeFormData.id,
                );
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
              onSelectChange(value, 'materialId', formModel as ConditionItem);
              if (value) {
                // 设置单位
                formModel.unit = option.unitName;
                onChange(formModel as ConditionItem);
              }
            },
          };
        },
        formItemProps: {
          htmlFor: 'materialId' + Math.random(),
        },
      },
      {
        component: 'Select',
        label: t('校验规则'),
        field: 'checkRule',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.MaterialReserveNumber;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: checkRuleOptions,
            filterOption: commonFilterOption,
            onChange: (value: SelectValue) => {
              onSelectChange(value, 'checkRule', formModel as ConditionItem);
            },
          };
        },
        formItemProps: {
          htmlFor: 'checkRule' + Math.random(),
        },
      },
      {
        component: 'InputNumber',
        label: t('物料量'),
        field: 'number',
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (formModel as ConditionItem).conditionType === ConditionType.MaterialReserveNumber;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            stringMode: true,
            onChange: (value: string) => {
              onNumberChange(value, 'number', formModel as ConditionItem);
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
              {formModel.unit}
            </span>
          ),
        },
        formItemProps: {
          htmlFor: 'number' + Math.random(),
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入为正数'));
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
