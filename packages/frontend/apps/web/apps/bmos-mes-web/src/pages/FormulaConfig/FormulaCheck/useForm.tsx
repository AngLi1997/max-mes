import { getParameter, recordManageSaveFormula, recordRoundingList, recordSaveFormula } from '@/services';
import { getExpressionFullList, recordDeleteFormula } from '@/services/index';
import type { BMFormType, FormProps, FormSchema, RenderCallbackParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import { isEmpty, isObject } from '@bmos/utils';
import { Modal, message } from 'ant-design-vue';
import { storeToRefs } from 'pinia';
import { Ref } from 'vue';
import { ComponentNode } from '../../../components/Record/NodeList/type';
import { NODE_MATH } from '../enum';
import { useCheckComponent } from '../store/useCheckComponent';
import { EmitFn, FormulaParsesType } from '../type';
import { useFormSchema } from './useFormSchema';

export const emitTypes = ['confirm', 'cancel', 'clear', 'add', 'delete', 'formula-change', 'before-formula-change'];

export const useForm = (
  component: Ref<ComponentNode | undefined>,
  emit: EmitFn<typeof emitTypes>,
  isShow: boolean,
  changeStatus: Function,
) => {
  const store = useCheckComponent();
  const { setFormulaParses, deleteFormulaParamTarget, RESET_CHECK_STATUS, endCheck } = store;
  const { formulaParses } = storeToRefs(store);
  const formRef = ref<BMFormType>();
  const route = useRoute();

  const dateStyle = ref<any>();
  const formulaOptions = ref<any[]>([]);

  const currentFormula = ref<any>({});
  const setCurrentFormula = (val: string | undefined | null | number) => {
    const {
      expression: formulaExpression,
      expressionCategoryId,
      name,
      id: formulaId,
      expressionParse,
      indefiniteParam,
    } = leafNodes.value.find(item => item.id === val) || {};

    currentFormula.value = {
      formulaExpression,
      formulaId,
      expressionCategoryId,
      expressionParse,
      name,
      indefiniteParam,
    };
  };
  const validateFormulaParses = (): boolean => {
    const validate = formulaParses.value.every(item => item.target && item.target.fieldId);
    if (!validate) message.error(t('请选择公式参数'));
    return validate;
  };
  const formSubmit = async (currentComponent: ComponentNode) => {
    endCheck();
    const validate = await formRef.value?.validate();
    if (!validate || !validateFormulaParses()) return;
    try {
      if (formulaParses.value && formulaParses.value.length === 0) {
        message.error(t('请添加公式参数'));
        return;
      }
      const formulaDetailList = formulaParses.value.map((item: FormulaParsesType) => {
        const { key, value, target } = item;
        return {
          fieldId: target?.fieldId as string,
          detail: JSON.stringify({
            id: target?.id,
            recordItemId: target?.recordItemId,
            fieldId: target?.fieldId,
            componentType: target?.componentType,
            componentNumber: target?.componentNumber,
            componentName: target?.componentName,
            relevance: target?.relevance,
          }),
          key,
          value,
        };
      });
      const data = {
        ...{
          ...validate,
          formulaConfig: {
            ...validate.formulaConfig,
            numericalJudgmentConfig: validate.formulaConfig?.numericalJudgmentConfig?.map((item: any) => {
              return {
                ...item,
                ...(isObject(item.limitType)
                  ? {
                      limitType: item.limitType.value,
                    }
                  : { limitType: item.limitType }),
              };
            }),
          },
        },
        id: currentComponent.id,
        formulaType: validate.formulaId,
        formulaDetailList,
        recordVersionId: route.params.record_id,
      };
      const res = route.params.implement === '1' ? await recordManageSaveFormula(data) : await recordSaveFormula(data);
      if (res.code === 0) {
        formRef.value?.initFormValues();
        Object.assign(currentComponent, data);
        emit('confirm');
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const cancelCheck = () => {
    emit('cancel');
  };

  const clearFormula = () => {
    if (!component || !component.value) return;

    Modal.confirm({
      title: t('是否清除公式配置信息？'),
      async onOk() {
        try {
          await recordDeleteFormula({
            componentId: component.value?.id,
          });
          emit('clear', component.value);
          return Promise.resolve();
        } catch (error) {
          message.error(t('操作失败'));
        }
      },
    });
  };

  const { formSchema } = useFormSchema({ component, isShow, changeStatus });

  // 叶子节点数组
  const leafNodes = ref<any[]>([]);
  const createTreeData = (data: any) => {
    const loop = (data: any[]) => {
      return data.map(item => {
        if (item.categoryFlag) {
          item.selectable = false;
        } else {
          item.selectable = !item.cancelBound;
          leafNodes.value.push(item);
        }
        if (item.children) {
          loop(item.children);
        }
        return item;
      });
    };
    return loop(data);
  };

  // 根据类型获取公式树
  const getFormulaTree = async (componentType: string, formulaId: string) => {
    try {
      const res = await getExpressionFullList({
        componentType,
        formulaId: formulaId || undefined,
        recordId: route.params.recordId,
      });
      formulaOptions.value = createTreeData(res.data || []);
    } catch (error: any) {
      formulaOptions.value = [];
      message.error(error.message);
    }
    formRef.value?.updateSchema({
      field: 'formulaId',
      componentProps: {
        treeData: formulaOptions.value,
      },
    });
  };

  // 获取参数配置日期格式
  const getDateStyle = async () => {
    try {
      const { data } = await getParameter('platform.sys.time-format');
      const { data: defaultFormat } = await getParameter('platform.sys.time.default-format');
      const dataJson: Record<string, string> = JSON.parse(data.value);
      const defaultFormatKey = isEmpty(defaultFormat?.value) ? 'yMdHms' : defaultFormat.value;
      dateStyle.value = dataJson[defaultFormatKey] || '';
    } catch (error: any) {}
  };

  // 默认配置
  const defaultSchema: FormSchema = {
    field: 'formulaId',
    component: 'TreeSelect',
    label: t('公式名称'),
    required: true,
    componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
      return {
        fieldNames: { label: 'name', value: 'id' },
        treeData: [],
        disabled: isShow,
        showSearch: true,
        treeNodeFilterProp: 'name',
        onSelect: () => {
          setCurrentFormula(formModel.formulaId);
          // 除了时间差公式，其他公式清空修约方式
          if (formModel.formulaId !== '3') {
            formInstance.setFormModels({ roundCode: undefined });
          }
          // 当是求和公式时（'4'）|| 字符串拼接公式（'11'），设置取值方式为最新有效
          if (['4', '11'].includes(formModel.formulaId)) {
            formInstance.setFormModels({
              formulaConfig: {
                valueTakeType: 'LATEST_EFFECTIVE',
              },
            });
          }
          // 当是数值判断公式时（'9'） 且是文字组件
          if (formModel.formulaId === '9' && component.value?.componentType === 'TEXT') {
            formInstance.setFormModels({
              formulaConfig: {
                numericalJudgmentConfig: [],
              },
            });
          }
          // 当是日期计算公式时('10')
          if (formModel.formulaId === '10') {
            formRef.value?.setFormModels({
              formulaConfig: {
                dateCalculateConfig: {
                  addTime: true,
                  dateStyle: dateStyle.value,
                  datePattern: dateStyle.value,
                  timeDiff: '',
                  timeUnit: undefined,
                },
              },
            });
          }
          // 当是关联引用公式（'2'） 且是 数值组件时
          if (formModel.formulaId === '2' && component.value?.componentType === 'NUMBER') {
            formRef.value?.setFormModels({
              formulaConfig: {
                associationPatternConfig: {
                  numberPatternConfig: {
                    exponentLower: false,
                  },
                },
              },
            });
          }
          // 当是关联引用公式（'2'） 且是 日期组件时
          if (formModel.formulaId === '2' && component.value?.componentType === 'DATE') {
            formRef.value?.setFormModels({
              formulaConfig: {
                associationPatternConfig: {
                  datePatternConfig: {},
                },
              },
            });
          }
          // 清空精度
          formInstance.setFormModels({
            formulaPrecision: '',
          });
          // 设置修约方式得options
          setRoundCodeOptions(formModel.formulaId);
          const before = currentFormula.value;
          emit('before-formula-change', { ...before }, { ...formModel });
          Object.assign(formModel, before);
          setFormulaParses(before?.expressionParse || [], before);
          RESET_CHECK_STATUS();
          emit('formula-change', { ...before }, { ...formModel });
        },
      };
    },
  };
  // 时间差公式配置 (formulaId === '3'时展示)
  const timeFormSchemas: FormSchema = {
    field: 'dateType',
    component: 'Select',
    label: t('时间格式'),
    vIf: ({ formModel }: RenderCallbackParams) => {
      return formModel.formulaId && formModel.formulaId === '3';
    },
    componentProps: () => {
      return {
        // ● 日时分秒 dd HH:mm:ss
        // ● 时分秒 HH:mm:ss
        // ● 分秒 mm:ss
        // ● 秒 ss
        // ● 日时分 dd HH:mm
        // ● 日时 dd HH
        // ● 日 dd
        // ● 时分 HH:mm
        // ● 时 HH
        // ● 分 mm
        options: [
          {
            label: t('日时分秒'),
            value: 'dd HH:mm:ss',
          },
          {
            label: t('时分秒'),
            value: 'HH:mm:ss',
          },
          {
            label: t('分秒'),
            value: 'mm:ss',
          },
          {
            label: t('秒'),
            value: 'ss',
          },
          {
            label: t('日时分'),
            value: 'dd HH:mm',
          },
          {
            label: t('日时'),
            value: 'dd HH',
          },
          {
            label: t('日'),
            value: 'dd',
          },
          {
            label: t('时分'),
            value: 'HH:mm',
          },
          {
            label: t('时'),
            value: 'HH',
          },
          {
            label: t('分'),
            value: 'mm',
          },
        ],
        disabled: isShow,
        allowClear: false,
        defaultValue: 'dd HH:mm:ss',
        onChange: () => {
          changeStatus();
        },
      };
    },
  };
  // 标准公式配置
  const standardFormSchemas: FormSchema[] = [
    {
      field: 'formulaExpression',
      component: 'Input',
      label: t('计算表达式'),
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel.formulaId && !NODE_MATH.includes(formModel.formulaId);
      },
      componentProps: () => {
        return {
          readonly: true,
          disabled: isShow,
        };
      },
    },
    {
      field: 'roundCode',
      component: 'Select',
      required: ({ formModel }: RenderCallbackParams) => {
        return !['1'].includes(formModel.formulaId) && formModel.formulaPrecision;
      },
      label: ({ formModel }: RenderCallbackParams) => {
        return formModel?.formulaId === '3' ? t('时间修约') : t('公式修约');
      },
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel.formulaId && !['1', '8', '9', '10', '11'].includes(formModel.formulaId);
      },
      componentProps: ({ formModel }: RenderCallbackParams) => {
        return {
          disabled: isShow,
          onChange: () => {
            changeStatus();
            formRef.value?.clearValidate();
          },
          request: async () => {
            try {
              if (formModel?.formulaId === '3') {
                return [
                  {
                    label: t('向上舍入'),
                    value: 'roundingUp',
                  },
                  {
                    label: t('向下舍入'),
                    value: 'roundingDown',
                  },
                ];
              }
              const { data } = await recordRoundingList();
              return data || [];
            } catch (error: any) {
              console.log(error);
              return [];
            }
          },
        };
      },
    },
    {
      field: 'formulaConfig.valueTakeType',
      component: 'Select',
      required: true,
      label: t('取值方式'),
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel.formulaId && ['4', '11'].includes(formModel.formulaId);
      },
      componentProps: () => {
        return {
          disabled: isShow,
          options: [
            {
              label: t('最新有效'),
              value: 'LATEST_EFFECTIVE',
            },
            {
              label: t('所有有效'),
              value: 'ALL_EFFECTIVE',
            },
          ],
          onChange: () => {
            changeStatus();
          },
        };
      },
    },

    {
      field: 'formulaPrecision',
      component: 'InputNumber',
      label: t('精度(小数位数)'),
      required: ({ formModel }: RenderCallbackParams) => {
        return !['1'].includes(formModel.formulaId) && formModel.roundCode;
      },
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel.formulaId && !['1', '3', '8', '9', '10', '11'].includes(formModel.formulaId);
      },
      componentProps: () => {
        return {
          style: {
            width: '100%',
          },
          disabled: isShow,
          min: 0,
          max: 15,
          step: 1,
          precision: 0,
          onChange: () => {
            changeStatus();
            formRef.value?.clearValidate();
          },
        };
      },
    },
  ];

  const formProps = computed<FormProps>(() => {
    const key = new Date().getTime();
    return {
      layout: 'vertical',
      baseColProps: {
        span: 24,
      },
      showActionButtonGroup: false,
      showAdvancedButton: false,
      initialValues: {},
      formKey: key,
      schemas: [defaultSchema, ...standardFormSchemas, timeFormSchemas, ...formSchema.value],
    } as FormProps;
  });

  const setRoundCodeOptions = async (formulaId: string) => {
    if (formulaId === '3') {
      formRef.value?.updateSchema({
        field: 'roundCode',
        componentProps: {
          options: [
            {
              label: t('向上舍入'),
              value: 'roundingUp',
            },
            {
              label: t('向下舍入'),
              value: 'roundingDown',
            },
          ],
        },
      });
      return;
    }
    const { data } = await recordRoundingList();
    formRef.value?.updateSchema({
      field: 'roundCode',
      componentProps: {
        options: data || [],
      },
    });
  };
  watch(
    component,
    async val => {
      if (val) {
        const { formulaId, formulaPrecision, formulaExpression, roundCode, dateType, componentType, formulaConfig } =
          val;
        // 点击组件时获取组件的公式树
        await getFormulaTree(componentType!, formulaId);
        nextTick(() => {
          formRef.value?.setFormModels({
            formulaId,
            formulaPrecision,
            formulaExpression,
            dateType: formulaId ? dateType : 'dd HH:mm:ss',
            roundCode: formulaId ? roundCode : 'roundingUp',
            formulaConfig: formulaConfig || {},
          });
          setRoundCodeOptions(formulaId as string);
          setCurrentFormula(formulaId);
          formRef.value?.clearValidate();
        });
      }
    },
    { immediate: true },
  );

  onMounted(() => {
    document.body.style.cursor = 'auto';
    getDateStyle();
  });

  const deleteParam = (type: boolean, target: FormulaParsesType) => {
    if (!target) return;
    Modal.confirm({
      title: t('是否取消该参数组件关联？'),
      onOk() {
        const nodeId = target.target?.fieldId;
        deleteFormulaParamTarget(target.key);
        emit('delete', nodeId);
        return Promise.resolve();
      },
    });
  };

  onUnmounted(() => {
    document.body.style.cursor = 'auto';
  });

  return {
    formSubmit,
    cancelCheck,
    clearFormula,
    formulaOptions,
    formRef,
    currentFormula,
    deleteParam,
    formProps,
  };
};
