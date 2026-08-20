import { FormSchema } from '@bmos/components';
import { t } from '@bmos/i18n';
import { RadioButton, RadioGroup } from 'ant-design-vue';
import { ref } from 'vue';
import ScopeNumber from '../../components/ScopeNumber.vue';
import { ConfigFormProps } from '../../types';
import { useStationWithRequiredConfig } from './stationWithRequiredConfig';

export type UseNumberConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
export const useNumberConfig = ({ props, hasChange }: UseNumberConfigParams) => {
  const { stationWithRequiredConfig } = useStationWithRequiredConfig({ props, hasChange, showStationTitle: true });
  const numberConfig = ref<FormSchema[]>([
    {
      field: 'limit',
      component: 'Select',
      label: t('限制方式'),
      required: true,
      componentProps: ({ formInstance }) => {
        return {
          options: [
            {
              label: t('范围限制'),
              value: 0,
            },
            {
              label: t('数值相等'),
              value: 1,
            },
          ],
          onChange: () => {
            hasChange.value = true;
            formInstance.validate(['scope']);
          },
        };
      },
    },
    {
      field: 'scope',
      vIf: ({ formModel }) => {
        return formModel['limit'] !== 1;
      },
      defaultValue: {
        lowerLimit: 1,
        upperLimit: 1,
      },
      component: ({ formModel }) => {
        return (
          <ScopeNumber
            v-model:limit={formModel['scope']}
            onUpdate:limit={(val: any) => {
              hasChange.value = true;
              formModel['scope'] = val;
            }}
          />
        );
      },
      label: t('阈值设置'),
      dynamicRules: ({ formModel }) => {
        return [
          {
            required: false,
            validator: async (rule, value) => {
              let scopeMin = formModel['scope']?.['scopeMin'];
              let scopeMax = formModel['scope']?.['scopeMax'];
              const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
              if (!(scopeMin === null || scopeMin === undefined)) {
                if (!reg.test(scopeMin)) {
                  return Promise.reject(t('最小值整数或小数不能超过15位'));
                }
              }
              if (!(scopeMax === null || scopeMax === undefined)) {
                if (!reg.test(scopeMax)) {
                  return Promise.reject(t('最大值整数或小数不能超过15位'));
                }
              }
              if (scopeMin === null) return Promise.resolve();
              if (scopeMin === undefined) return Promise.resolve();
              if (scopeMax === null) return Promise.resolve();
              if (scopeMax === undefined) return Promise.resolve();
              if (Number(scopeMin) > Number(scopeMax)) {
                return Promise.reject(t('最小值不能大于最大值'));
              }
              // 如果限制方式为范围限制(开区间)，则最小值和最大值不能相等
              if (formModel.limit === 2) {
                if (Number(scopeMin) === Number(scopeMax)) {
                  return Promise.reject(t('最大值需大于最小值'));
                }
              }

              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'numericalValue',
      component: 'InputNumber',
      label: t('数值'),
      vIf: ({ formModel }) => {
        return formModel['limit'] === 1;
      },
      componentProps: {
        stringMode: true,
        onChange: () => {
          hasChange.value = true;
        },
      },
      dynamicRules: ({ formModel }) => {
        return [
          {
            required: false,
            validator: async (rule, value) => {
              if (!value) return Promise.resolve();
              // 如果值 整数或小数不能超过15位 则报错，否则通过
              const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
              if (!reg.test(value)) {
                return Promise.reject(t('整数或小数不能超过15位'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'waringAutoRecordTitle',
      label: t('异常是否自动记录'),
      component: 'TableTitle',
    },
    {
      field: 'waringAutoRecord',
      defaultValue: false,
      noLabel: true,
      component: ({ formModel }) => {
        return (
          <>
            <RadioGroup v-model:value={formModel['waringAutoRecord']}>
              <RadioButton value={true}>{t('是')}</RadioButton>
              <RadioButton value={false} class='waring-false'>
                {t('否')}
              </RadioButton>
            </RadioGroup>
          </>
        );
      },
      componentProps: {
        onChange: () => {
          hasChange.value = true;
        },
      },
    },
    // {
    //   field: 'waring',
    //   component: ({ formModel }) => {
    //     return (
    //       <>
    //         <RadioGroup v-model:value={formModel['waring']}>
    //           <RadioButton value={true}>{t('是')}</RadioButton>
    //           <RadioButton value={false} class='waring-false'>
    //             {t('否')}
    //           </RadioButton>
    //         </RadioGroup>
    //       </>
    //     );
    //   },
    //   componentProps: {
    //     onChange: () => {
    //       hasChange.value = true;
    //     },
    //   },
    //   label: t('异常告警'),
    // },
    ...stationWithRequiredConfig,
  ]);

  return {
    numberConfig,
  };
};
