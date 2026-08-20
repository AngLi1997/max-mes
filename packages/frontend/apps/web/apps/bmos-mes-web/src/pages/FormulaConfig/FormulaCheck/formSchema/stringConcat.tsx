// 字符串拼接公式

import type { FormSchema, RenderCallbackParams } from '@bmos/components';
import { computed } from 'vue';
import { UseFormParams } from '../useFormSchema';

export const useStringConcat = (useFormContext: UseFormParams) => {
  const { isShow, component, changeStatus } = useFormContext;
  const show = ({ formModel }: RenderCallbackParams) => {
    return formModel?.formulaId && formModel.formulaId === '11';
  };

  //表单选项
  const dateSchemas: FormSchema[] = [
    {
      field: 'formulaConfig.stringJoinConfig.join',
      component: 'Input',
      label: t('字符配置'),
      vIf: show,
      required: true,
      componentProps: ({ formModel }: any) => {
        return {
          disabled: isShow,
          onChange: () => {
            changeStatus();
          },
        };
      },
      dynamicRules: ({ formModel }: RenderCallbackParams) => {
        return [
          {
            required: true,
            message: t('请输入字符配置'),
            trigger: 'change',
            validator: (_rule: any, val: string) => {
              console.log(val);
              if (!val) {
                return Promise.reject(t('请输入字符配置'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
  ];

  const schemas = computed(() => {
    if (component.value?.componentType === 'TEXT') {
      return [...dateSchemas];
    }
    return [];
  });
  return {
    stringConcatSchemas: schemas,
  };
};
