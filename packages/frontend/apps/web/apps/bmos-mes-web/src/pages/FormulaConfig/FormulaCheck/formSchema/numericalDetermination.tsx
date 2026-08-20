// 数值判定公式(文字、单选、多选)

import type { FormSchema, RenderCallbackParams } from '@bmos/components';
import { computed } from 'vue';
import NumericalDetermination from '../components/NumericalDetermination.vue';
import { UseFormParams } from '../useFormSchema';

export const useNumericalDetermination = (useFormContext: UseFormParams) => {
  const { isShow, component, changeStatus } = useFormContext;
  const show = ({ formModel }: RenderCallbackParams) => {
    return formModel?.formulaId && formModel.formulaId === '9';
  };

  const titleSchema: FormSchema = {
    field: 'optionsTitle1',
    label: t('判定设置'),
    component: 'TableTitle',
    vIf: show,
  };

  // 文字表单选项
  const textSchemas: FormSchema[] = [
    {
      field: 'formulaConfig.numericalJudgmentConfig',
      noLabel: true,
      vIf: show,
      component: ({ formModel }) => {
        return (
          <NumericalDetermination
            v-model:numericalJudgmentConfig={formModel.formulaConfig.numericalJudgmentConfig}
            isShow={isShow}
            showOptionTitle={false}
            isText={true}
            onChange={() => {
              changeStatus();
            }}
          />
        );
      },
      dynamicRules({ formInstance }: RenderCallbackParams) {
        return [
          {
            required: false,
            trigger: 'blur',
            validator: async (rule: any, value: any) => {
              try {
                const numericalJudgmentRef = formInstance?.compRefMap.get('formulaConfig.numericalJudgmentConfig');
                await numericalJudgmentRef?.validateForm();
                return Promise.resolve();
              } catch (error) {
                return Promise.reject();
              }
            },
          },
        ];
      },
    },
  ];

  // 单选、多选表单选项
  const radioCheckboxSchemas: Ref<FormSchema[]> = computed(() => {
    return [
      {
        field: 'formulaConfig.numericalJudgmentConfig',
        noLabel: true,
        vIf: show,
        component: ({ formModel }) => {
          return (
            <NumericalDetermination
              v-model:numericalJudgmentConfig={formModel.formulaConfig.numericalJudgmentConfig}
              isShow={isShow}
              componentDetail={component.value?.componentDetail}
              onChange={() => {
                changeStatus();
              }}
            />
          );
        },
        dynamicRules({ formInstance }: RenderCallbackParams) {
          return [
            {
              required: false,
              trigger: 'blur',
              validator: async (rule: any, value: any) => {
                try {
                  const numericalJudgmentRef = formInstance?.compRefMap.get('formulaConfig.numericalJudgmentConfig');
                  await numericalJudgmentRef?.validateForm();
                  return Promise.resolve();
                } catch (error) {
                  return Promise.reject();
                }
              },
            },
          ];
        },
      },
    ];
  });

  const schemas = computed(() => {
    if (component.value?.componentType === 'TEXT') {
      return [titleSchema, ...textSchemas];
    }
    if (component.value?.componentType === 'RADIO' || component.value?.componentType === 'CHECKBOX') {
      return [titleSchema, ...radioCheckboxSchemas.value];
    }
    return [];
  });
  return {
    numericalDeterminationSchemas: schemas,
  };
};
