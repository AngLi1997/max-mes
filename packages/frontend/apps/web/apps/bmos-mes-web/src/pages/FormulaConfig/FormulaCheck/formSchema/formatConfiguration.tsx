// 数值判定公式(文字、单选、多选)

import { getParameter, recordFunctionPreview, recordRoundingList } from '@/services';
import type { FormSchema, RenderCallbackParams } from '@bmos/components';
import { format } from 'date-fns';
import { computed, ref } from 'vue';
import { UseFormParams } from '../useFormSchema';
export const formatConfig = (useFormContext: UseFormParams) => {
  const { component, isShow } = useFormContext;
  const show = ({ formModel }: RenderCallbackParams) => {
    return formModel?.formulaId && formModel.formulaId === '2';
  };

  const titleSchema: FormSchema = {
    field: 'optionsTitle1',
    label: t('格式配置'),
    component: 'TableTitle',
    vIf: show,
  };
  const allTimeFormat = ref<any>({});

  const typeSchema: FormSchema = {
    field: 'formulaConfig.associationPatternConfig.numberPatternConfig.style',
    label: t('数值样式'),
    component: 'Select',
    vIf: show,
    componentProps: ({ formModel }: RenderCallbackParams) => {
      return {
        options: [
          {
            label: t('修约数'),
            value: 0,
          },
          {
            label: t('百分比'),
            value: 1,
          },
          {
            label: t('科学计数法'),
            value: 2,
          },
        ],
        disabled: isShow,
        onChange: (value: string) => {
          formModel.formulaConfig = {
            associationPatternConfig: {
              numberPatternConfig: {
                style: value,
                exponentLower: false,
              },
            },
          };
        },
      };
    },
  };

  // 数字表单选项
  const numberSchemas: FormSchema[] = [
    // --------------------------科学计数法------------------------
    {
      field: 'formulaConfig.associationPatternConfig.numberPatternConfig.exponentLower',
      label: t('指数符号'),
      component: 'Select',
      required: true,
      vIf: ({ formModel }: RenderCallbackParams) => {
        return (
          formModel?.formulaId &&
          formModel.formulaId === '2' &&
          formModel.formulaConfig?.associationPatternConfig?.numberPatternConfig?.style == 2
        );
      },
      componentProps: () => {
        return {
          disabled: isShow,
          options: [
            {
              label: 'E',
              value: false,
            },
            {
              label: 'e',
              value: true,
            },
          ],
        };
      },
    },
    // -------------------------------------修约数,百分比
    {
      field: 'formulaConfig.associationPatternConfig.numberPatternConfig.scale',
      label: ({ formModel }: RenderCallbackParams) => {
        return formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style == 2
          ? t('有效数字')
          : t('精度');
      },
      component: 'Input',
      vIf: ({ formModel }: RenderCallbackParams) => {
        return (
          formModel?.formulaId &&
          formModel.formulaId === '2' &&
          (formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style ?? -1) * 1 >= 0
        );
      },
      componentProps: {
        disabled: isShow,
      },
      dynamicRules({ formModel }: RenderCallbackParams) {
        return [
          {
            required:
              formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style == 0
                ? true
                : !!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.roundCode,
            trigger: 'blur',
            validator: async (_rule: any, value: any) => {
              const number = value * 1;
              if (
                (formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style == 0
                  ? true
                  : !!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.roundCode) &&
                !value
              ) {
                return Promise.reject(t('请输入精度'));
              } else if (!value) {
                // 非必填,为填值不校验
                return Promise.resolve();
              }
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
    },
    // -------------------------------------------------
    {
      field: 'formulaConfig.associationPatternConfig.numberPatternConfig.roundCode',
      label: t('修约方式'),
      component: 'Select',
      vIf: ({ formModel }: RenderCallbackParams) => {
        return (
          formModel?.formulaId &&
          formModel.formulaId === '2' &&
          (formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style ?? -1) * 1 >= 0
        );
      },
      componentProps: () => {
        return {
          request: async () => {
            // 获取设备数据
            try {
              const { data } = await recordRoundingList();
              return data;
            } catch (error: any) {
              console.log(error);
            }
          },
          disabled: isShow,
        };
      },
      dynamicRules({ formModel }: RenderCallbackParams) {
        return [
          {
            required:
              formModel.formulaConfig.associationPatternConfig.numberPatternConfig.style == 0
                ? true
                : !!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.roundCode ||
                  !!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.scale,
            trigger: 'blur',
            validator: async (_rule: any, value: any) => {
              if (
                formModel.formulaConfig.associationPatternConfig.numberPatternConfig.style == 0
                  ? true
                  : !!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.roundCode ||
                    !!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.scale
              ) {
                if (!value) {
                  return Promise.reject(t('请选择修约方式'));
                }
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'formulaConfig.associationPatternConfig.numberPatternConfig.preInput',
      label: t('数值预览'),
      component: 'Input',
      vIf: ({ formModel }: RenderCallbackParams) => {
        return (
          formModel?.formulaId &&
          formModel.formulaId === '2' &&
          (formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style ?? -1) * 1 >= 0
        );
      },
      dynamicRules() {
        return [
          {
            required: true,
            trigger: 'blur',
            validator: async (_rule: any, value: any) => {
              if (!value) {
                return Promise.reject(t('请输入'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
      componentProps: ({ formModel }: RenderCallbackParams) => {
        return {
          disabled: isShow,
          onChange: async () => {
            // 获取设备数据
            if (!formModel.formulaConfig.associationPatternConfig.numberPatternConfig.preInput) {
              formModel.formulaConfig.associationPatternConfig.numberPatternConfig.preview = '';
              return;
            }
            const { data } = await recordFunctionPreview({
              formulaConfig: formModel.formulaConfig,
              functionValue: '2',
              input: formModel.formulaConfig.associationPatternConfig.numberPatternConfig.preInput,
            });
            formModel.formulaConfig.associationPatternConfig.numberPatternConfig.preview = data;
          },
        };
      },
    },
    {
      field: 'formulaConfig.associationPatternConfig.numberPatternConfig.preview',
      label: t('格式预览'),
      noLabel: true,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      vIf: ({ formModel }: RenderCallbackParams) => {
        return (
          formModel?.formulaId &&
          formModel.formulaId === '2' &&
          (formModel.formulaConfig.associationPatternConfig?.numberPatternConfig.style ?? -1) * 1 >= 0
        );
      },
    },
  ];

  // 日期
  const dateSchemas: FormSchema[] = [
    {
      field: 'formulaConfig.associationPatternConfig.datePatternConfig.datePattern',
      label: t('日期样式'),
      component: 'Select',
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel?.formulaId && formModel.formulaId === '2';
      },
      componentProps: ({ formModel }: RenderCallbackParams) => {
        return {
          disabled: isShow,
          request: async () => {
            // 获取设备数据
            try {
              const { data } = await getParameter('platform.sys.time-format');
              const options = JSON.parse(data.value);
              allTimeFormat.value = options;
              let newOptions = [];
              for (let key in options) {
                let label = key;
                label = label.replaceAll('y', t('年'));
                label = label.replaceAll('M', t('月'));
                label = label.replaceAll('d', t('日'));
                label = label.replaceAll('H', t('时'));
                label = label.replaceAll('m', t('分'));
                label = label.replaceAll('s', t('秒'));
                newOptions.push({
                  value: key,
                  label,
                });
              }
              return newOptions;
            } catch (error: any) {
              console.log(error);
            }
          },
          onChange: async (value: any) => {
            formModel.formulaConfig.associationPatternConfig.datePatternConfig.dateStyle = allTimeFormat.value[value];
            const { data } = await recordFunctionPreview({
              formulaConfig: formModel.formulaConfig,
              functionValue: '2',
              input: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            });
            formModel.formulaConfig.associationPatternConfig.datePatternConfig.datePreview = data;
          },
        };
      },
    },
    {
      field: 'formulaConfig.associationPatternConfig.datePatternConfig.dateStyle',
      label: t('日期格式'),
      component: 'Input',
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel?.formulaId && formModel.formulaId === '2';
      },
      componentProps: ({ formModel }: RenderCallbackParams) => {
        return {
          disabled: isShow,
          onInput: async () => {
            const { data } = await recordFunctionPreview({
              formulaConfig: formModel.formulaConfig,
              functionValue: '2',
              input: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            });
            formModel.formulaConfig.associationPatternConfig.datePatternConfig.datePreview = data;
          },
        };
      },
    },
    {
      field: 'formulaConfig.associationPatternConfig.datePatternConfig.datePreview',
      label: t('日期预览'),
      component: 'Input',
      vIf: ({ formModel }: RenderCallbackParams) => {
        return formModel?.formulaId && formModel.formulaId === '2';
      },
      componentProps: {
        disabled: true,
      },
    },
  ];

  const schemas = computed(() => {
    if (component.value?.componentType === 'NUMBER') {
      return [titleSchema, typeSchema, ...numberSchemas];
    }
    if (component.value?.componentType === 'DATE') {
      return [titleSchema, ...dateSchemas];
    }
    return [];
  });
  return {
    formatConfigSchema: schemas,
  };
};
