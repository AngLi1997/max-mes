import {
  getOperateListReq,
  postProcedureExpressionCheckoutExpressionReq,
  reqAllPlanTeamProcedureStepListByProcessVersionId,
} from '@/services';
import { ClockCircleOutlined, InfoCircleOutlined } from '@ant-design/icons-vue';
import { BMTableTitle, Recordable, RenderCallbackParams, formInstance } from '@bmos/components';
import { BMIcons } from '@bmos/icons';
import { cloneDeep, isNull, loopSelectableNotValueTree } from '@bmos/utils';
import { Button, FormProps, Input, InputGroup, Popover, Tag, Tooltip, message } from 'ant-design-vue';
import { SegmentedOption, SegmentedValue } from 'ant-design-vue/es/segmented/src/segmented';
import ExecutionCondition from '../components/ExecutionCondition.vue';
import {
  ConditionItem,
  NodeFunctionEnum,
  NodeType,
  RightDrawerProps,
  SegmentedType,
  SegmentedTypeValue,
} from '../types';
const codeArr = [
  'A',
  'B',
  'C',
  'D',
  'E',
  'F',
  'G',
  'H',
  'I',
  'J',
  'K',
  'L',
  'M',
  'N',
  'O',
  'P',
  'Q',
  'R',
  'S',
  'T',
  'U',
  'V',
  'W',
  'X',
  'Y',
  'Z',
];

export type UseFormParams = {
  props: RightDrawerProps;
  emit: any;
  setFormRef: Ref<formInstance> | undefined;
  ecFormRef: Ref<formInstance> | undefined;
  ccFormRef: Ref<formInstance> | undefined;
  recordItemCascaderChange: (value: string[] | undefined, selectedOptions: any) => void;
};

export const useForm = ({ props, emit, ecFormRef, ccFormRef, recordItemCascaderChange }: UseFormParams) => {
  // form 是否change
  const isFormChange = ref<boolean>(false);

  // 选择记录弹窗
  const curSelectRecordItemId = ref<string>('');
  const selectRecordOpen = ref<boolean>(false);
  const selectedKeys = ref<string[]>([]);
  const handleClickSelectRecordItem = (formModel: Recordable) => {
    if (!formModel.recordItemId || !formModel.recordVersionId) {
      selectedKeys.value = [];
      curSelectRecordItemId.value = '';
    }
    selectRecordOpen.value = true;
  };

  const isTask = computed(() => {
    return props.currentNodeType === NodeType.TaskNode;
  });

  const segmentedValue = ref<SegmentedTypeValue>(SegmentedType.FunctionConfig);
  const segmentedData = ref<SegmentedOption[]>([
    { title: t('功能配置'), value: SegmentedType.FunctionConfig },
    { title: t('执行条件'), value: SegmentedType.ExecutionCondition },
    { title: t('完成条件'), value: SegmentedType.CompletionCondition },
  ]);

  // 执行条件 逻辑表达式名称 字符串
  const setEcExecutionConditionNameStr = (formModel: Recordable) => {
    const { executeCondition } = formModel;
    if (!executeCondition?.conditionList || !executeCondition.expression || !executeCondition.conditionList?.length) {
      ecFormRef?.value?.setFormModel('executeCondition.executionConditionNameStr', '');
      return;
    }
    ecFormRef?.value?.setFormModel(
      'executeCondition.executionConditionNameStr',
      executeCondition.expression
        .split('')
        .map((item: string) => {
          const condition = executeCondition.conditionList?.find((condition: ConditionItem) => condition.code === item);
          return condition?.name || item;
        })
        .join(''),
    );
  };
  const ecExecutionConditionCodeArr = ref<string[]>([...codeArr]);
  const addEcExecutionCondition = (formModel: Recordable) => {
    // 最多 20 个条件
    if (formModel.executeCondition?.conditionList?.length >= 20) {
      message.error(t('最多添加20个执行条件'));
      return;
    }
    let code = ecExecutionConditionCodeArr.value.shift();
    if (!code) {
      // 没有 code, 找 没有使用的 code
      const index = codeArr.findIndex(
        (item: string) =>
          !formModel.executeCondition?.conditionList?.find((condition: ConditionItem) => condition.code === item),
      );
      if (index > -1) {
        code = codeArr[index];
      } else {
        return;
      }
    }
    if (!formModel.executeCondition?.conditionList) {
      formModel.executeCondition.conditionList = [];
    }
    formModel.executeCondition?.conditionList.push({
      code,
      name: `${t('执行条件')} ${code}`,
      defaultResult: true,
    });
    setEcExecutionConditionNameStr(formModel);
  };
  const ecTest = async (formModel: Recordable) => {
    const { executeCondition } = formModel;
    if (!executeCondition?.expression) {
      message.error(t('请输入逻辑表达式'));
      return;
    }
    if (!executeCondition?.conditionList || !executeCondition?.conditionList.length) {
      message.error(t('请先添加执行条件'));
      return;
    }
    try {
      const params = {
        expression: executeCondition?.expression,
        conditionList: executeCondition?.conditionList?.map((item: ConditionItem) => {
          return {
            code: item.code,
            result: item.defaultResult || false,
          };
        }),
      };
      const { data } = await postProcedureExpressionCheckoutExpressionReq(params);
      formModel.executeCondition.result = data?.toString() || 'false';
      ecFormRef?.value?.clearValidate([['executeCondition', 'result']]);
    } catch (error) {
      formModel.executeCondition.result = undefined;
      message.error(t('逻辑表达式配置错误'));
    }
  };

  // 完成条件 逻辑表达式名称 字符串
  const setCcExecutionConditionNameStr = (formModel: Recordable) => {
    const { completeCondition } = formModel;
    if (
      !completeCondition?.conditionList ||
      !completeCondition?.expression ||
      !completeCondition?.conditionList?.length
    ) {
      ccFormRef?.value?.setFormModel('completeCondition.ccExecutionConditionNameStr', '');
      return;
    }
    ccFormRef?.value?.setFormModel(
      'completeCondition.ccExecutionConditionNameStr',
      completeCondition?.expression
        .split('')
        .map((item: string) => {
          const condition = completeCondition?.conditionList?.find(
            (condition: ConditionItem) => condition.code === item,
          );
          return condition?.name || item;
        })
        .join(''),
    );
  };
  const ccExecutionConditionCodeArr = ref<string[]>([...codeArr]);
  const addCcExecutionCondition = (formModel: Recordable) => {
    // 最多 20 个条件
    if (formModel.completeCondition?.conditionList?.length >= 20) {
      message.error(t('最多添加20个完成条件'));
      return;
    }
    let code = ccExecutionConditionCodeArr.value.shift();
    // 没有 code, 找 没有使用的 code
    const index = codeArr.findIndex(
      (item: string) =>
        !formModel.completeCondition?.conditionList?.find((condition: ConditionItem) => condition.code === item),
    );
    if (index > -1) {
      code = codeArr[index];
    } else {
      return;
    }
    if (!formModel.completeCondition?.conditionList) {
      formModel.completeCondition.conditionList = [];
    }
    formModel.completeCondition?.conditionList.push({
      code,
      name: `${t('完成条件')} ${code}`,
      defaultResult: true,
    });
    setEcExecutionConditionNameStr(formModel);
  };
  const ccTest = async (formModel: Recordable) => {
    const { completeCondition } = formModel;
    if (!completeCondition?.expression) {
      message.error(t('请输入逻辑表达式'));
      return;
    }
    if (!completeCondition?.conditionList || !completeCondition?.conditionList?.length) {
      message.error(t('请先添加完成条件'));
      return;
    }
    try {
      const params = {
        expression: completeCondition?.expression,
        conditionList: completeCondition?.conditionList?.map((item: ConditionItem) => {
          return {
            code: item.code,
            result: item.defaultResult || false,
          };
        }),
      };
      const { data } = await postProcedureExpressionCheckoutExpressionReq(params);
      formModel.completeCondition.result = data?.toString() || 'false';
      ccFormRef?.value?.clearValidate([['completeCondition', 'result']]);
    } catch (error) {
      formModel.completeCondition.result = undefined;
      message.error(t('逻辑表达式配置错误'));
    }
  };

  const ecFormInitValue = async (val: SegmentedValue) => {
    segmentedValue.value = val as SegmentedTypeValue;
    await nextTick();
    const formModel = props.settingNodeFormData;
    ecExecutionConditionCodeArr.value = [...codeArr];
    if (!formModel.executeCondition?.conditionList || !formModel.executeCondition?.conditionList?.length) {
      const code = ecExecutionConditionCodeArr.value.shift();
      ecFormRef?.value?.setFormModel('executeCondition', {
        expression: formModel.executeCondition?.expression || undefined,
        result: formModel.executeCondition?.result || undefined,
        conditionList: [
          {
            code,
            name: `${t('执行条件')} ${code}`,
            defaultResult: true,
          },
        ],
      });
    } else {
      const { executeCondition } = formModel;
      // 如果已经使用了 code 则从 ecExecutionConditionCodeArr 中移除
      executeCondition?.conditionList?.forEach((item: ConditionItem) => {
        const index = ecExecutionConditionCodeArr.value.findIndex((code: string) => code === item.code);
        if (index > -1) {
          ecExecutionConditionCodeArr.value.splice(index, 1);
        }
      });
      ecFormRef?.value?.setFormModel('executeCondition', cloneDeep(executeCondition));
      setEcExecutionConditionNameStr(cloneDeep(formModel));
    }
  };
  const ecFormClearValue = async () => {
    await nextTick();
    ecFormRef?.value?.setFormModel('executeCondition', {
      expression: undefined,
      result: undefined,
      ecExecutionConditionNameStr: undefined,
      conditionList: [],
    });
    emit('updateFormValue', props.settingNodeId, {
      executeCondition: {
        expression: null,
        result: null,
        ecExecutionConditionNameStr: null,
        conditionList: [],
      },
    });
    isFormChange.value = false;
  };
  const ccFormClearValue = async () => {
    await nextTick();
    ccFormRef?.value?.setFormModel('completeCondition', {
      expression: undefined,
      result: undefined,
      ccExecutionConditionNameStr: undefined,
      conditionList: [],
    });
    emit('updateFormValue', props.settingNodeId, {
      completeCondition: {
        expression: null,
        result: null,
        ccExecutionConditionNameStr: null,
        conditionList: [],
      },
    });
    isFormChange.value = false;
  };
  const ccFormInitValue = async (val: SegmentedValue) => {
    segmentedValue.value = val as SegmentedTypeValue;
    await nextTick();
    const formModel = props.settingNodeFormData;
    ccExecutionConditionCodeArr.value = [...codeArr];
    if (!formModel.completeCondition || !formModel.completeCondition?.conditionList?.length) {
      const code = ccExecutionConditionCodeArr.value.shift();
      ccFormRef?.value?.setFormModel('completeCondition', {
        expression: formModel.completeCondition?.expression || undefined,
        result: formModel.completeCondition?.result || undefined,
        conditionList: [
          {
            code,
            name: `${t('完成条件')} ${code}`,
            defaultResult: true,
          },
        ],
      });
    } else {
      const { completeCondition } = formModel;
      // 如果已经使用了 code 则从 ccExecutionConditionCodeArr 中移除
      completeCondition?.conditionList?.forEach((item: ConditionItem) => {
        const index = ccExecutionConditionCodeArr.value.findIndex((code: string) => code === item.code);
        if (index > -1) {
          ccExecutionConditionCodeArr.value.splice(index, 1);
        }
      });
      ccFormRef?.value?.setFormModel('completeCondition', cloneDeep(completeCondition));
      setCcExecutionConditionNameStr(cloneDeep(formModel));
    }
  };

  const setFormProps: Ref<FormProps> = ref({
    disabled: props.isView,
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: () => {
          return (
            <div>
              <span>{isTask.value ? t('任务别名') : t('步骤别名')}</span>
            </div>
          );
        },
        required: true,
        componentProps: () => {
          return {
            placeholder: isTask.value ? t('请输入任务别名') : t('请输入工序步骤别名'),
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: string) => {
                if (!value) {
                  return Promise.reject(isTask.value ? t('请输入任务别名') : t('请输入工序步骤别名'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'label',
        label: () => {
          return (
            <div>
              <span>{isTask.value ? t('任务名称') : t('步骤名称')}</span>
              <Tooltip placement='top'>
                {{
                  default: () => <InfoCircleOutlined style={{ marginLeft: '5px' }} />,
                  title: () => (
                    <div class='mes-tooltip-inner'>
                      <p>{isTask.value ? t('输入任务名称新建任务') : t('输入步骤名称新建步骤')}</p>
                      <p>
                        {t('点击')}
                        <ClockCircleOutlined />
                        {isTask.value ? t('图标后，可选择历史的任务') : t('图标后，可选择历史的步骤')}
                      </p>
                    </div>
                  ),
                }}
              </Tooltip>
            </div>
          );
        },
        required: true,
        slot: 'label',
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: string) => {
                if (!value) {
                  return Promise.reject(isTask.value ? t('请输入任务名称') : t('请输入工序步骤名称'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'nodeFunction',
        component: 'Select',
        label: () => {
          return isTask.value ? t('任务功能') : t('步骤功能');
        },
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            // 可选“记录作业”、“工序审核”、“工艺审核”、“工序换班”、“工艺换班”、“归档”等
            options: isTask.value
              ? [
                  {
                    label: t('记录作业'),
                    value: NodeFunctionEnum.RecordOperation,
                  },
                  {
                    label: t('工序审核'),
                    value: NodeFunctionEnum.ProcedureAudit,
                  },
                  {
                    label: t('工艺审核'),
                    value: NodeFunctionEnum.ProcessAudit,
                  },
                  {
                    label: t('工序换班'),
                    value: NodeFunctionEnum.ProcedureShift,
                  },
                  {
                    label: t('工艺换班'),
                    value: NodeFunctionEnum.ProcessShift,
                  },
                  {
                    label: t('辅助记录'),
                    value: NodeFunctionEnum.AuxiliaryRecord,
                  },
                  {
                    label: t('发起请验'),
                    value: NodeFunctionEnum.Inspection,
                  },
                ]
              : [
                  {
                    label: t('记录作业'),
                    value: NodeFunctionEnum.RecordOperation,
                  },
                  {
                    label: t('工序换班'),
                    value: NodeFunctionEnum.ProcedureShift,
                  },
                  {
                    label: t('工艺换班'),
                    value: NodeFunctionEnum.ProcessShift,
                  },
                  {
                    label: t('辅助记录'),
                    value: NodeFunctionEnum.AuxiliaryRecord,
                  },
                  {
                    label: t('发起请验'),
                    value: NodeFunctionEnum.Inspection,
                  },
                ],
            onChange: (value: string) => {
              isFormChange.value = true;
              if (
                value === NodeFunctionEnum.ProcedureShift ||
                value === NodeFunctionEnum.ProcessShift ||
                value === NodeFunctionEnum.Inspection
              ) {
                recordItemCascaderChange(undefined, {});
                formModel.reusable = undefined;
              } else {
                formModel.reusable = true;
              }
            },
          };
        },
      },
      {
        field: 'recordItemId',
        formItemProps: {
          labelCol: { span: 24 },
        },
        noLabelTip: true,
        labelFullWidth: true,
        disabledLabelWidth: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (
            formModel.nodeFunction !== NodeFunctionEnum.ProcedureShift &&
            formModel.nodeFunction !== NodeFunctionEnum.ProcessShift &&
            formModel.nodeFunction !== NodeFunctionEnum.Inspection
          );
        },
        label: ({ formModel }: RenderCallbackParams) => {
          return (
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                width: '100%',
              }}>
              <span>{t('记录项')}</span>
              <Button
                type='link'
                onClick={() => {
                  handleClickSelectRecordItem(formModel);
                }}>
                {t('预览选择')}
              </Button>
            </div>
          );
        },
        slot: 'recordItem',
        required: true,
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请选择记录项'),
              trigger: 'change',
              validator: async (rule: any, value: any) => {
                if (value.length === 0) {
                  return Promise.reject(t('请选择记录项'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'reusable',
        component: 'RadioGroup',
        label: t('记录复用'),
        required: true,
        defaultValue: true,
        vIf: ({ formModel }: RenderCallbackParams) => {
          return (
            formModel.nodeFunction !== NodeFunctionEnum.ProcedureShift &&
            formModel.nodeFunction !== NodeFunctionEnum.ProcessShift &&
            formModel.nodeFunction !== NodeFunctionEnum.Inspection
          );
        },
        componentProps: {
          options: [
            {
              label: t('复用'),
              value: true,
            },
            {
              label: t('不复用'),
              value: false,
            },
          ],
          onChange: () => {
            isFormChange.value = true;
          },
        },
      },
      {
        field: 'operationSopId',
        component: 'TreeSelect',
        label: t('操作规程'),
        componentProps: {
          multiple: true,
          fieldNames: { value: 'id', label: 'name' },
          showSearch: true,
          treeNodeFilterProp: 'name',
          request: async () => {
            try {
              const { data } = await getOperateListReq();
              return loopSelectableNotValueTree(data, 'flag', true);
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            isFormChange.value = true;
          },
        },
      },
      {
        field: 'roles',
        component: 'Select',
        label: t('执行班组'),
        componentProps: {
          mode: 'multiple',
          fieldNames: { value: 'id' },
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          request: async () => {
            const { data } = await reqAllPlanTeamProcedureStepListByProcessVersionId(
              props.versionId,
              props.procedureId,
            );
            return data.map((item: any) => ({
              label: item.name + '-' + item.code,
              ...item,
            }));
          },
          onChange: () => {
            isFormChange.value = true;
          },
        },
        componentSlots: {
          tagRender: ({ slotData }: any) => {
            if (!slotData) return;
            const { label, closable, onClose, disabled } = slotData;
            return (
              <Tag closable={closable || disabled} class='dynamicSelectTag' onClose={onClose}>
                {label}
              </Tag>
            );
          },
        },
      },
      {
        field: 'duration',
        label: t('执行时长'),
        slot: 'duration',
      },
    ],
  });
  const ecFormProps: Ref<FormProps> = ref({
    disabled: props.isView,
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      // 执行条件
      {
        field: 'ecExpressionTableTitle',
        label: t('逻辑表达式'),
        component: 'TableTitle',
      },
      {
        field: 'executeCondition.expression',
        noLabelTip: true,
        label: () => {
          return (
            <div class='operator-help'>
              <span>{t('逻辑表达式')}</span>
              <Popover title={t('帮助')} overlayClassName='operator-help-popover'>
                {{
                  default: () => <BMIcons icon='Exclamation' class='operator-help-icon' />,
                  content: () => (
                    <div class='container'>
                      {t('逻辑运算符的优先级为：！运算级别最高，& 运算高于 | 运算。支持的逻辑运算符：')}
                      {['!', '&', '|'].map((item: string) => {
                        return <span class='operators'>{item}</span>;
                      })}
                    </div>
                  ),
                }}
              </Popover>
            </div>
          );
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <Input
                v-model:value={formModel.executeCondition.expression}
                onChange={() => {
                  isFormChange.value = true;
                  setEcExecutionConditionNameStr(formModel);
                }}
              />
              <div class='condition'>{formModel.executeCondition.executionConditionNameStr}</div>
            </>
          );
        },
        required: true,
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: async (_rule: any) => {
                if (!formModel.executeCondition?.expression) {
                  return Promise.reject(t('请输入逻辑表达式'));
                }
                if (!formModel.executeCondition?.conditionList?.length) return Promise.reject(t('请添加执行条件'));
                let flag = false;
                formModel.executeCondition?.conditionList?.forEach((item: ConditionItem) => {
                  // 如果条件有 code 但表达式中没有则提示
                  if (item.code && !formModel.executeCondition?.expression?.includes(item.code)) {
                    flag = true;
                  }
                });
                // formModel.executeCondition?.expression  其中 A-Z 不存在于 conditionList 中的code 则 flag 为 true
                const regex = /[A-Z]/g;
                const codeArr = formModel.executeCondition?.expression?.match(regex);
                if (codeArr && codeArr.length) {
                  codeArr.forEach((code: string) => {
                    if (!formModel.executeCondition?.conditionList?.find((item: ConditionItem) => item.code === code)) {
                      flag = true;
                    }
                  });
                }
                if (flag) return Promise.reject(t('逻辑表达式配置错误'));
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'executeCondition.result',
        label: t('测试结果'),
        required: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <InputGroup compact>
                <Input v-model:value={formModel.executeCondition.result} style='width: calc(100% - 100px)' readonly />
                <Button
                  onClick={() => {
                    isFormChange.value = true;
                    ecTest(formModel);
                  }}
                  style='margin-left: var(--bmos-margin-small); border-radius: 4px'>
                  {t('测试')}
                </Button>
              </InputGroup>
            </>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: string) => {
                if (isNull(formModel.executeCondition.result)) {
                  return Promise.reject(t('逻辑表达式配置错误'));
                }
                if (!value) {
                  return Promise.reject(t('请点击测试'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'executeCondition.conditionList',
        formItemProps: {
          labelCol: { span: 24 },
        },
        labelFullWidth: true,
        disabledLabelWidth: true,
        noLabelTip: true,
        label: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <div class='condition-label'>
              <BMTableTitle title={t('执行条件')} />
              <Button
                type='link'
                onClick={() => {
                  isFormChange.value = true;
                  addEcExecutionCondition(formModel);
                  formInstance?.validate([[`executeCondition`, 'expression']]);
                }}>
                {t('添加条件')}
              </Button>
            </div>
          );
        },
        component: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <ExecutionCondition
              v-model:conditionList={formModel.executeCondition.conditionList}
              processDetail={props.processDetail}
              procedureModelId={props.procedureId}
              settingNodeFormData={props.settingNodeFormData}
              isView={props.isView}
              versionId={props.versionId}
              onDeleteItem={(item: ConditionItem) => {
                isFormChange.value = true;
                if (item.code) {
                  ecExecutionConditionCodeArr.value.push(item.code);
                  setEcExecutionConditionNameStr(formModel);
                }
                formInstance?.validate([[`executeCondition`, 'expression']]);
              }}
              onChange={() => {
                isFormChange.value = true;
                setEcExecutionConditionNameStr(formModel);
                formInstance?.validate([['executeCondition', 'expression']]);
              }}
            />
          );
        },
      },
    ],
  });
  const ccFormProps: Ref<FormProps> = ref({
    disabled: props.isView,
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      // 完成条件
      {
        field: 'ccExpressionTableTitle',
        label: t('逻辑表达式'),
        component: 'TableTitle',
      },
      {
        field: 'completeCondition.expression',
        noLabelTip: true,
        label: () => {
          return (
            <div class='operator-help'>
              <span>{t('逻辑表达式')}</span>
              <Popover title={t('帮助')} overlayClassName='operator-help-popover'>
                {{
                  default: () => <BMIcons icon='Exclamation' class='operator-help-icon' />,
                  content: () => (
                    <div class='container'>
                      {t('逻辑运算符的优先级为：！运算级别最高，& 运算高于 | 运算。支持的逻辑运算符：')}
                      {['!', '&', '|'].map((item: string) => {
                        return <span class='operators'>{item}</span>;
                      })}
                    </div>
                  ),
                }}
              </Popover>
            </div>
          );
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <Input
                v-model:value={formModel.completeCondition.expression}
                onChange={() => {
                  isFormChange.value = true;
                  setCcExecutionConditionNameStr(formModel);
                }}
              />
              <div class='condition'>{formModel.completeCondition.ccExecutionConditionNameStr}</div>
            </>
          );
        },
        required: true,
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: async (_rule: any) => {
                if (!formModel.completeCondition?.expression) {
                  return Promise.reject(t('请输入逻辑表达式'));
                }
                if (!formModel.completeCondition?.conditionList?.length) return Promise.reject(t('请添加执行条件'));
                let flag = false;
                formModel.completeCondition?.conditionList?.forEach((item: ConditionItem) => {
                  // 如果条件有 code 但表达式中没有则提示
                  if (item.code && !formModel.completeCondition?.expression?.includes(item.code)) {
                    flag = true;
                  }
                });

                // formModel.completeCondition?.expression  其中 A-Z 不存在于 conditionList 中的code 则 flag 为 true`
                const regex = /[A-Z]/g;
                const codeArr = formModel.completeCondition?.expression?.match(regex);
                if (codeArr && codeArr.length) {
                  codeArr.forEach((code: string) => {
                    if (
                      !formModel.completeCondition?.conditionList?.find((item: ConditionItem) => item.code === code)
                    ) {
                      flag = true;
                    }
                  });
                }
                if (flag) return Promise.reject(t('逻辑表达式配置错误'));
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'completeCondition.result',
        label: t('测试结果'),
        required: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <InputGroup compact>
                <Input v-model:value={formModel.completeCondition.result} style='width: calc(100% - 100px)' readonly />
                <Button
                  onClick={() => {
                    isFormChange.value = true;
                    ccTest(formModel);
                  }}
                  style='margin-left: var(--bmos-margin-small); border-radius: 4px'>
                  {t('测试')}
                </Button>
              </InputGroup>
            </>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: string) => {
                if (isNull(formModel.completeCondition?.result)) {
                  return Promise.reject(t('逻辑表达式配置错误'));
                }
                if (!value) {
                  return Promise.reject(t('请点击测试'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'completeCondition.conditionList',
        formItemProps: {
          labelCol: { span: 24 },
        },
        labelFullWidth: true,
        disabledLabelWidth: true,
        noLabelTip: true,
        label: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <div class='condition-label'>
              <BMTableTitle title={t('完成条件')} />
              <Button
                type='link'
                onClick={() => {
                  isFormChange.value = true;
                  addCcExecutionCondition(formModel);
                  formInstance?.validate([[`completeCondition`, 'expression']]);
                }}>
                {t('添加条件')}
              </Button>
            </div>
          );
        },
        component: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <ExecutionCondition
              v-model:conditionList={formModel.completeCondition.conditionList}
              processDetail={props.processDetail}
              segmentedValue={segmentedValue.value}
              procedureModelId={props.procedureId}
              settingNodeFormData={props.settingNodeFormData}
              isView={props.isView}
              versionId={props.versionId}
              onDeleteItem={(item: ConditionItem) => {
                isFormChange.value = true;
                if (item.code) {
                  ccExecutionConditionCodeArr.value.push(item.code);
                  setCcExecutionConditionNameStr(formModel);
                }
                formInstance?.validate([[`completeCondition`, 'expression']]);
              }}
              onChange={() => {
                isFormChange.value = true;
                setCcExecutionConditionNameStr(formModel);
                formInstance?.validate([['completeCondition', 'expression']]);
              }}
            />
          );
        },
      },
    ],
  });
  return {
    segmentedValue,
    segmentedData,
    setFormProps,
    ecFormInitValue,
    ccFormInitValue,
    isFormChange,

    handleClickSelectRecordItem,
    selectRecordOpen,
    selectedKeys,
    curSelectRecordItemId,
    ecFormProps,
    ccFormProps,
    ecFormClearValue,
    ccFormClearValue,
  };
};
