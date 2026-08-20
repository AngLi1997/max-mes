import { getPlatformQueryListDictDownReq } from '@/services';
import { PlusCircleOutlined } from '@ant-design/icons-vue';
import { BMIcon, FormSchema, RenderCallbackParams } from '@bmos/components';
import { FormItem, Input, Space } from 'ant-design-vue';
import { ComputedRef, ref } from 'vue';
import { ConfigFormProps } from '../../types';
import { useStationWithRequiredConfig } from './stationWithRequiredConfig';

export type UseSelectConfigParams = {
  props: ConfigFormProps;
  isView: ComputedRef<boolean>;
  hasChange: Ref<boolean>;
};

export const useSelectConfig = ({ props, isView, hasChange }: UseSelectConfigParams) => {
  const { stationWithRequiredConfig } = useStationWithRequiredConfig({ props, hasChange, showStationTitle: true });
  const removeOption = (formModel: any, index: number) => {
    hasChange.value = true;
    formModel.options.splice(index, 1);
  };

  // 获取选项的 label 0 => 选项一 1 => 选项二 ，以此类推
  const getLabel = (index: number) => {
    return `${t('选项')}${index + 1}`;
  };
  const addOption = (formModel: any) => {
    hasChange.value = true;
    if (formModel.options.length > 29) return;
    formModel.options.push({
      value: undefined,
      text: undefined,
    });
  };
  const selectConfig = ref<FormSchema[]>([
    {
      field: 'dataSource',
      component: 'Select',
      label: t('数据来源'),
      componentProps: ({ formModel }) => {
        return {
          options: [
            { label: t('选项内容'), value: 1 },
            { label: t('数据字典'), value: 2 },
          ],
          onChange: (val: string[]) => {
            formModel['options'] = [
              {
                text: undefined,
                value: undefined,
              },
            ];
            formModel['dataDictionary'] = undefined;
            hasChange.value = true;
          },
        };
      },
    },
    {
      field: 'optionsTitle1',
      label: t('选项内容'),
      component: 'TableTitle',
      vIf: ({ formModel }) => formModel['dataSource'] === 1,
    },
    {
      field: 'optionsTitle2',
      label: t('数据字典'),
      component: 'TableTitle',
      vIf: ({ formModel }) => formModel['dataSource'] === 2,
    },
    {
      field: 'options',
      noLabel: true,
      vIf: ({ formModel }) => formModel['dataSource'] === 1,
      dynamicRules: ({ formModel }: RenderCallbackParams) => {
        return [
          {
            required: false,
            type: 'array',
            validator: (rule: any, value: any) => {
              try {
                // 选项不能为空
                if (formModel['options'].length === 0) {
                  return Promise.reject(t('选项不能为空'));
                }
                let allValue = [] as any;
                for (let i = 0; i < formModel['options'].length; i++) {
                  let item = formModel['options'][i];
                  if (!item.value) {
                    return Promise.reject(t('选项不能为空'));
                  }
                  if (allValue.indexOf(item.value) >= 0) {
                    return Promise.reject(t('选项不能重复'));
                  } else {
                    allValue.push(item.value);
                  }
                }
                return Promise.resolve();
              } catch (error) {
                return Promise.reject(t('选项不能为空'));
              }
            },
          },
        ];
      },
      component: ({ formModel }) => {
        return (
          <>
            {formModel['options'].map((_item: any, index: number) => {
              return (
                <>
                  <FormItem label={getLabel(index)}>
                    <Input
                      v-model:value={formModel['options'][index]['value']}
                      placeholder={t('请输入选项')}
                      style='width: 86%'
                      onChange={(e: any) => {
                        formModel['options'][index]['text'] = formModel['options'][index]['value'];
                        hasChange.value = true;
                      }}
                    />
                    <BMIcon
                      class={`delete-option-icon ${index > 0 && !isView.value ? 'show-delete-option-icon' : ''}`}
                      type='Delete'
                      onClick={() => removeOption(formModel, index)}
                    />
                  </FormItem>
                </>
              );
            })}
            {!isView.value && formModel.options.length < 30 && (
              <span class='add-icon' onClick={() => addOption(formModel)}>
                <Space size={8}>
                  <PlusCircleOutlined />
                  {t('新增')}
                </Space>
              </span>
            )}
          </>
        );
      },
    },
    {
      field: 'dataDictionary',
      label: t('数据字典'),
      component: 'Select',
      vIf: ({ formModel }) => formModel['dataSource'] === 2,
      componentProps: ({ formModel }) => {
        return {
          fieldNames: { value: 'id' },
          request: async () => {
            const { data } = await getPlatformQueryListDictDownReq();
            return (data || []).map((item: any) => {
              return {
                id: item.id,
                label: `${item.label}-${item.value}`,
              };
            });
          },
          onChange: (val: string[]) => {
            hasChange.value = true;
          },
        };
      },
    },
    ...stationWithRequiredConfig,
  ]);

  return {
    selectConfig,
  };
};
