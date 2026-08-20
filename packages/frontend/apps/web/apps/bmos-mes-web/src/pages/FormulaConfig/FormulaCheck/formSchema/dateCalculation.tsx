// 日期计算公式

import { getParameter } from '@/services';
import type { FormSchema, RenderCallbackParams } from '@bmos/components';
import { isObject } from '@bmos/utils';
import { Col, FormItem, FormItemRest, InputGroup, InputNumber, Row, Select } from 'ant-design-vue';
import { computed } from 'vue';
import { UseFormParams } from '../useFormSchema';

export const useDateCalculation = (useFormContext: UseFormParams) => {
  const { isShow, component, changeStatus } = useFormContext;
  const show = ({ formModel }: RenderCallbackParams) => {
    return formModel?.formulaId && formModel.formulaId === '10';
  };

  //表单选项
  const dateSchemas: FormSchema[] = [
    {
      field: 'formulaConfig.dateCalculateConfig.dateStyle',
      component: 'Select',
      required: true,
      label: t('日期样式'),
      vIf: show,
      componentProps: ({ formModel, formInstance }: any) => {
        return {
          request: async () => {
            try {
              const { data } = await getParameter('platform.sys.time-format');
              const dataJson: Record<string, string> = JSON.parse(data.value);
              if (dataJson && isObject(dataJson)) {
                return Object.keys(dataJson).map(key => {
                  return {
                    label: t(key),
                    value: dataJson[key as keyof typeof dataJson],
                  };
                });
              }
              return [];
            } catch (error) {
              return [];
            }
          },

          disabled: isShow,
          onChange: (val: any) => {
            formModel['formulaConfig']['dateCalculateConfig']['datePattern'] = val;
            formInstance?.validateFields([['formulaConfig', 'dateCalculateConfig', 'datePattern']]); //校验formulaConfig.dateCalculateConfig.datePattern
            changeStatus();
          },
        };
      },
    },
    {
      field: 'formulaConfig.dateCalculateConfig.datePattern',
      component: 'Input',
      required: true,
      label: t('日期格式'),
      vIf: show,
      componentProps: () => {
        return {
          readonly: false,
          disabled: isShow,
        };
      },
    },
    {
      field: 'formulaConfig.dateCalculateConfig.addTime',
      component: 'Select',
      required: true,
      label: t('计算方式'),
      defaultValue: true,
      vIf: show,
      componentProps: () => {
        return {
          disabled: isShow,
          options: [
            {
              label: t('增加时长'),
              value: true,
            },
            {
              label: t('减少时长'),
              value: false,
            },
          ],
          onChange: () => {
            changeStatus();
          },
        };
      },
    },
    {
      field: 'sendBackList',
      noLabel: true,
      vIf: show,
      component: ({ formModel, formInstance }: any) => {
        return (
          <>
            <Row>
              <Col span={24}>
                <FormItem
                  name={['formulaConfig']}
                  label={t('时间差')}
                  rules={[
                    {
                      required: true,
                      message: t('请输入'),
                    },
                    {
                      trigger: 'blur',
                      validator: async (_rule: any) => {
                        if (!formModel['formulaConfig']['dateCalculateConfig']['timeDiff']) {
                          return Promise.reject(t('请输入时间差'));
                        }
                        if (
                          formModel['formulaConfig']['dateCalculateConfig']['timeDiff'] > 999999999 ||
                          formModel['formulaConfig']['dateCalculateConfig']['timeDiff'] == 0
                        ) {
                          return Promise.reject(t('请输入10位以下的正整数'));
                        }
                        if (!formModel['formulaConfig']['dateCalculateConfig']['timeUnit']) {
                          return Promise.reject(t('请选择单位'));
                        }

                        return Promise.resolve();
                      },
                    },
                  ]}>
                  <InputGroup compact>
                    <InputNumber
                      v-model:value={formModel.formulaConfig.dateCalculateConfig.timeDiff}
                      stringMode={true}
                      style={{ width: '70%' }}
                      min={0}
                      precision={0}
                      disabled={isShow}
                      placeholder={t('请输入时间差')}
                    />
                    <FormItemRest>
                      <Select
                        v-model:value={formModel.formulaConfig.dateCalculateConfig.timeUnit}
                        style={{ width: '30%' }}
                        placeholder={t('单位')}
                        disabled={isShow}
                        options={[
                          {
                            label: t('秒'),
                            value: 'Seconds',
                          },
                          {
                            label: t('分'),
                            value: 'Minutes',
                          },
                          {
                            label: t('时'),
                            value: 'Hours',
                          },
                          {
                            label: t('日'),
                            value: 'Days',
                          },
                        ]}
                        onChange={() => {
                          formInstance?.validateFields([['formulaConfig']]);
                        }}></Select>
                    </FormItemRest>
                  </InputGroup>
                </FormItem>
              </Col>
            </Row>
          </>
        );
      },
    },
  ];

  const schemas = computed(() => {
    if (component.value?.componentType === 'DATE') {
      return [...dateSchemas];
    }
    return [];
  });
  return {
    dateCalculationSchemas: schemas,
  };
};
