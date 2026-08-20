import { postProcedureExpressionCheckoutExpressionReq } from '@/services';
import { BMTableTitle, Recordable, RenderCallbackParams, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { BMIcons } from '@bmos/icons';
import { isNull } from '@bmos/utils';
import { Button, FormProps, Input, InputGroup, Popover, message } from 'ant-design-vue';
import { SegmentedOption, SegmentedValue } from 'ant-design-vue/es/segmented/src/segmented';
import { ref } from 'vue';
import { ConditionItem, SegmentedType, SegmentedTypeValue } from '../../ProcedureFlow/types';
import ExecutionCondition from './ExecutionCondition.vue';

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

export type UseRightDrawerParams = {
  props: any;
  ccFormRef: Ref<formInstance> | undefined;
  isFormChange: Ref<boolean>;
  emit: any;
};

export const useRightDrawerForm = ({ props, ccFormRef, isFormChange, emit }: UseRightDrawerParams) => {
  const segmentedValue = ref<SegmentedTypeValue>(SegmentedType.FunctionConfig);
  const segmentedData = ref<SegmentedOption[]>([
    { title: t('功能配置'), value: SegmentedType.FunctionConfig },
    { title: t('完成条件'), value: SegmentedType.CompletionCondition },
  ]);

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
    setCcExecutionConditionNameStr(formModel);
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
              validator: async (_rule: any, value: string) => {
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
              processDetail={props.settingNodeFormData}
              segmentedValue={segmentedValue.value}
              isView={props.isView}
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
    ccFormClearValue,
    ccFormInitValue,
    addCcExecutionCondition,
    ccTest,
    ccFormProps,
  };
};
