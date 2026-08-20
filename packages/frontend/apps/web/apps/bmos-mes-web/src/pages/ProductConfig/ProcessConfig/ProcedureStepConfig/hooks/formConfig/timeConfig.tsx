import { FormSchema, RenderCallbackParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import { RadioButton, RadioGroup } from 'ant-design-vue';
import { ref } from 'vue';
import TimeNumber from '../../components/timeNumber.vue';

import { ConfigFormProps } from '../../types';
import { useStationWithRequiredConfig } from './stationWithRequiredConfig';

export type UseTimeConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
export const useTimeConfig = ({ props, hasChange }: UseTimeConfigParams) => {
  const { stationWithRequiredConfig } = useStationWithRequiredConfig({ props, hasChange, showStationTitle: true });
  const timeConfig = ref<FormSchema[]>([
    {
      field: 'format',
      component: 'Select',
      label: t('时间格式'),
      componentProps: () => {
        return {
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
          defaultValue: 'dd HH:mm:ss',
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
    {
      field: 'round',
      component: 'Select',
      label: t('时间修约'),
      componentProps: () => {
        return {
          defaultValue: 'roundingUp',
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
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
    {
      field: 'optionsTitle2',
      label: t('阈值设置'),
      component: 'TableTitle',
    },
    {
      field: 'limit',
      component: 'Select',
      defaultValue: 0,
      label: t('限制方式'),
      componentProps: () => {
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
          },
        };
      },
    },
    {
      field: 'min',
      vIf: ({ formModel }) => {
        return formModel['limit'] == 0;
      },
      defaultValue: {
        lowerLimit: null,
        day: '',
        hour: 0,
        minute: 0,
        second: 0,
      },
      noLabel: true,
      component: ({ formModel }) => {
        return (
          <TimeNumber
            label={t('最小值')}
            v-model:limit={formModel['min']}
            onUpdate:limit={(val: any) => {
              hasChange.value = true;
              formModel['scope'] = {
                ...formModel['scope'],
                lowerLimit: val.lowerLimit,
                scopeMin:
                  Number(val.day) * 24 * 60 * 60 +
                  Number(val.hour) * 60 * 60 +
                  Number(val.minute) * 60 +
                  Number(val.second),
              };
            }}
          />
        );
      },
      dynamicRules: ({ formModel }) => {
        return [
          {
            required: false,
            validator: async (rule, value) => {
              if (!value) return Promise.resolve();
              if (!formModel.scope) {
                return Promise.resolve();
              }
              if (value.lowerLimit == 0 || value.lowerLimit == 1) {
                if (
                  value.day === null ||
                  value.day === '' ||
                  value.hour === null ||
                  value.hour === '' ||
                  value.minute === null ||
                  value.minute === '' ||
                  value.second === null ||
                  value.second === ''
                ) {
                  return Promise.reject(t('配置不能为空'));
                }
              }
              if (
                (!formModel.min.lowerLimit && formModel.min.lowerLimit != 0) ||
                (!formModel.max.lowerLimit && formModel.max.lowerLimit != 0)
              ) {
                return Promise.resolve();
              }
              if (
                !Number.isNaN(formModel.scope.scopeMin * 1) &&
                !Number.isNaN(formModel.scope.scopeMax * 1) &&
                formModel.scope.scopeMin * 1 >= formModel.scope.scopeMax * 1
              ) {
                return Promise.reject(t('最大值需大于最小值'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'max',
      vIf: ({ formModel }) => {
        return formModel['limit'] == 0;
      },
      defaultValue: {
        lowerLimit: null,
        day: '',
        hour: 0,
        minute: 0,
        second: 0,
      },
      noLabel: true,
      component: ({ formModel }) => {
        return (
          <TimeNumber
            label={t('最大值')}
            v-model:limit={formModel['max']}
            type='max'
            onUpdate:limit={(val: any) => {
              hasChange.value = true;
              formModel['scope'] = {
                ...formModel['scope'],
                upperLimit: val.lowerLimit,
                scopeMax:
                  Number(val.day) * 24 * 60 * 60 +
                  Number(val.hour) * 60 * 60 +
                  Number(val.minute) * 60 +
                  Number(val.second),
              };
            }}
          />
        );
      },
      dynamicRules: ({ formModel }) => {
        return [
          {
            required: false,
            validator: async (rule, value) => {
              if (!value) return Promise.resolve();
              if (!formModel.scope) {
                return Promise.resolve();
              }
              if (value.lowerLimit == 0 || value.lowerLimit == 1) {
                if (
                  value.day === null ||
                  value.day === '' ||
                  value.hour === null ||
                  value.hour === '' ||
                  value.minute === null ||
                  value.minute === '' ||
                  value.second === null ||
                  value.second === ''
                ) {
                  return Promise.reject(t('配置不能为空'));
                }
              }
              // 如果值 整数或小数不能超过15位 则报错，否则通过
              if (
                (!formModel.min.lowerLimit && formModel.min.lowerLimit != 0) ||
                (!formModel.max.lowerLimit && formModel.max.lowerLimit != 0)
              ) {
                return Promise.resolve();
              }
              if (
                !Number.isNaN(formModel.scope.scopeMin * 1) &&
                !Number.isNaN(formModel.scope.scopeMax * 1) &&
                formModel.scope.scopeMin * 1 >= formModel.scope.scopeMax * 1
              ) {
                return Promise.reject(t('最大值需大于最小值'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'numericalSaveValue',
      vIf: ({ formModel }) => {
        return formModel['limit'] == 1;
      },
      defaultValue: {
        day: '',
        hour: 0,
        minute: 0,
        second: 0,
      },
      label: t('数值'),
      component: ({ formModel }) => {
        return (
          <TimeNumber
            v-model:limit={formModel['numericalSaveValue']}
            type='numericalValue'
            onUpdate:limit={(val: any) => {
              hasChange.value = true;
              formModel['numericalValue'] =
                Number(val.day) * 24 * 60 * 60 +
                Number(val.hour) * 60 * 60 +
                Number(val.minute) * 60 +
                Number(val.second);
            }}
          />
        );
      },
      dynamicRules: () => {
        return [
          {
            required: false,
            validator: async (rule, value) => {
              if (!value) return Promise.resolve();
              if (
                value.day === null ||
                value.day === '' ||
                value.hour === null ||
                value.hour === '' ||
                value.minute === null ||
                value.minute === '' ||
                value.second === null ||
                value.second === ''
              ) {
                return Promise.reject(t('配置不能为空'));
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
      formItemProps: {
        htmlFor: 'waringAutoRecord' + Math.random(),
      },
      defaultValue: false,
      noLabel: true,
      component: ({ formModel }: RenderCallbackParams) => {
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
      componentProps: () => {
        return {
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
    ...stationWithRequiredConfig,
  ]);
  return {
    timeConfig,
  };
};
