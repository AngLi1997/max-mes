import { getParameter } from '@/services';
import { FormSchema, RenderCallbackParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import { isEmpty, isObject, throttle } from '@bmos/utils';
import { format } from 'date-fns';
import { ref } from 'vue';
import { ConfigFormProps } from '../../types';
import { useStationWithRequiredConfig } from './stationWithRequiredConfig';

const isValidFormatString = (formatString: string) => {
  const regex =
    /^(?:[^a-zA-Z]*[ayMMDdhHmms]?)*[^a-zA-Z]*$|^[ayMMDdhHmms]*d{0,2}h{0,2}H{0,2}m{0,2}s{0,2}[ayMMDdhHmms]*[^a-zA-Z]*$/;
  return regex.test(formatString);
};

export type UseDateConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
export const useDateConfig = ({ props, hasChange }: UseDateConfigParams) => {
  const { stationWithRequiredConfig } = useStationWithRequiredConfig({ props, hasChange, showStationTitle: true });
  const dateConfig = ref<FormSchema[]>([
    {
      field: 'dateStyle',
      component: 'Select',
      label: t('日期样式'),
      required: true,
      componentProps: ({ formInstance, formModel }: RenderCallbackParams) => {
        return {
          // ● 年月日时分秒 yyyy-MM-dd HH:mm:ss
          // ● 年月日时分 yyyy-MM-dd HH:mm
          // ● 年月日时 yyyy-MM-dd HH
          // ● 年月日 yyyy-MM-dd
          // ● 年月 yyyy-MM
          // ● 年 yyyy
          // ● 月日时分秒 MM-dd HH:mm:ss
          // ● 月日时分 MM-dd HH:mm
          // ● 月日时 MM-dd HH
          // ● 月日 MM-dd
          // ● 月 MM
          // ● 日时分秒 dd HH:mm:ss
          // ● 日时分 dd HH:mm
          // ● 日时 dd HH
          // ● 日 dd
          // ● 时分秒 HH:mm:ss
          // ● 时分 HH:mm
          // ● 时 HH
          // ● 分秒 mm:ss
          // ● 分 mm
          // ● 秒 ss
          // options: [
          //   {
          //     label: t('年月日时分秒'),
          //     value: 'yyyy-MM-dd HH:mm:ss',
          //   },
          //   {
          //     label: t('年月日时分'),
          //     value: 'yyyy-MM-dd HH:mm',
          //   },
          //   {
          //     label: t('年月日时'),
          //     value: 'yyyy-MM-dd HH',
          //   },
          //   {
          //     label: t('年月日'),
          //     value: 'yyyy-MM-dd',
          //   },
          //   {
          //     label: t('年月'),
          //     value: 'yyyy-MM',
          //   },
          //   {
          //     label: t('年'),
          //     value: 'yyyy',
          //   },
          //   {
          //     label: t('月日时分秒'),
          //     value: 'MM-dd HH:mm:ss',
          //   },
          //   {
          //     label: t('月日时分'),
          //     value: 'MM-dd HH:mm',
          //   },
          //   {
          //     label: t('月日时'),
          //     value: 'MM-dd HH',
          //   },
          //   {
          //     label: t('月日'),
          //     value: 'MM-dd',
          //   },
          //   {
          //     label: t('月'),
          //     value: 'MM',
          //   },
          //   {
          //     label: t('日时分秒'),
          //     value: 'dd HH:mm:ss',
          //   },
          //   {
          //     label: t('日时分'),
          //     value: 'dd HH:mm',
          //   },
          //   {
          //     label: t('日时'),
          //     value: 'dd HH',
          //   },
          //   {
          //     label: t('日'),
          //     value: 'dd',
          //   },
          //   {
          //     label: t('时分秒'),
          //     value: 'HH:mm:ss',
          //   },
          //   {
          //     label: t('时分'),
          //     value: 'HH:mm',
          //   },
          //   {
          //     label: t('时'),
          //     value: 'HH',
          //   },
          //   {
          //     label: t('分秒'),
          //     value: 'mm:ss',
          //   },
          //   {
          //     label: t('分'),
          //     value: 'mm',
          //   },
          //   {
          //     label: t('秒'),
          //     value: 'ss',
          //   },
          // ],
          request: async () => {
            try {
              const { data } = await getParameter('platform.sys.time-format');
              const { data: defaultFormat } = await getParameter('platform.sys.time.default-format');
              const dataJson: Record<string, string> = JSON.parse(data.value);
              const defaultFormatKey = isEmpty(defaultFormat?.value) ? 'yMdHms' : defaultFormat.value;
              if (dataJson && isObject(dataJson)) {
                if (!formModel.format)
                  if (isValidFormatString(dataJson[defaultFormatKey])) {
                    formInstance?.setFieldsValue({
                      previewDate: format(new Date(), dataJson[defaultFormatKey]),
                    });
                    formInstance?.setFieldsValue({
                      format: dataJson[defaultFormatKey],
                      dateStyle: dataJson[defaultFormatKey],
                    });
                  }
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
          onChange: (value: string) => {
            // 防抖
            throttle(() => {
              {
                try {
                  formInstance.setFieldsValue({ format: value, previewDate: format(new Date(), value) });
                  formInstance?.clearValidate(['format', 'previewDate']);
                } catch (error) {
                  formInstance.setFieldsValue({ format: value, previewDate: undefined });
                }
              }
            }, 500)();
            hasChange.value = true;
          },
        };
      },
    },
    {
      field: 'format',
      component: 'Input',
      label: t('日期格式'),
      required: true,
      componentProps: ({ formInstance, formModel }: RenderCallbackParams) => {
        return {
          onChange: () => {
            try {
              if (isValidFormatString(formModel['format']) && format(new Date(), formModel['format'])) {
                formInstance?.setFieldsValue({
                  previewDate: format(new Date(), formModel['format']),
                });
              } else {
                formInstance?.setFieldsValue({
                  previewDate: undefined,
                });
              }
            } catch (error) {
              formInstance?.setFieldsValue({
                previewDate: undefined,
              });
            }
            hasChange.value = true;
          },
        };
      },
      dynamicRules: ({ formModel }) => {
        return [
          {
            required: true,
            trigger: 'change',
            validator: async (rule: any, value: string) => {
              if (!value) {
                return Promise.reject(t('请输入日期格式'));
              }
              try {
                if (isValidFormatString(formModel['format']) && format(new Date(), formModel['format'])) {
                  return Promise.resolve();
                } else {
                  return Promise.reject(t('日期格式错误'));
                }
              } catch (error) {
                return Promise.reject(t('日期格式错误'));
              }
            },
          },
        ];
      },
    },
    {
      field: 'previewDate',
      component: 'Input',
      label: t('日期预览'),
      dynamicDisabled: true,
      componentProps: () => {
        return {
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
    {
      field: 'entryMethod',
      component: 'Select',
      label: t('录入方式'),
      required: true,
      componentProps: () => {
        return {
          // 录入方式分别为“录入当前时间”和“手动选择时间”
          options: [
            {
              label: t('录入当前时间'),
              value: 0,
            },
            {
              label: t('手动选择时间'),
              value: 1,
            },
          ],
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
    ...stationWithRequiredConfig,
  ]);
  return {
    dateConfig,
  };
};
