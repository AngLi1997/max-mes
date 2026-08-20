import { BMFormSelect } from '@/BMComponents';
import SelectComponent from '@/pages/webviewComponent/selectComponent/index.vue';
// 时间日期组件
import TimeDateComponentNew from '@/pages/webviewComponent/timeDateComponentNew/index.vue';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useDataRevision = ({ props }) => {
  const show = ref(false);
  const formRef = ref();

  const openSelectModal = () => {
    show.value = true;
  };

  const selectCancel = () => {
    show.value = false;
  };

  const selectConfirm = (data, valueExtension) => {
    show.value = false;
    if (props.componentData.componentType === 'DATE' || props.componentData.componentType === 'TIME') {
      formRef.value.setFormModels({
        newValue: data,
        valueExtension,
      });
      return;
    }
    formRef.value.setFormModels({
      newValue: data.value,
      valueExtension: data.valueExtension,
    });
  };
  const formProps = reactive({
    schemas: [
      {
        field: 'valueExtension',
        vShow: false,
      },
      {
        field: 'newValueMode',
        component: 'BMFormRadio',
        label: t('新值'),
        required: true,
        defaultValue: 'newValue',
        vIf: () => ['NUMBER', 'TEXT'].includes(props.componentData.componentType),
        colProps: {
          span: 24,
        },
        componentProps: () => {
          return {
            options: [
              {
                label: t('录入新值'),
                value: 'newValue',
              },
              {
                label: t('录入空值'),
                value: 'nullValue',
              },
            ],
            onChange: ({ value }) => {
              formRef.value.setFormModels({
                newValue: value === 'newValue' ? '' : nullValueRef.value,
              });
            },
          };
        },
      },
      {
        field: 'newValue',
        component: 'Input',
        vIf: () => ['NUMBER', 'TEXT'].includes(props.componentData.componentType),
        colProps: {
          span: 24,
        },
        noUseMaxLengthRule: true,
        componentProps: ({ formModel }) => {
          return {
            disabled: formModel.newValueMode === 'nullValue',
            onInput: () => {
              if (props.componentData.componentType === 'NUMBER') {
                setTimeout(() => {
                  function filterNumber(str) {
                    // 创建一个空字符串来存储结果
                    let result = '';
                    // 遍历输入字符串的每个字符
                    for (let i = 0; i < str.length; i++) {
                      // 获取当前字符
                      const char = str[i];
                      // 如果当前字符是数字，就将它添加到结果字符串中
                      if (
                        (char >= '0' && char <= '9')
                        || char === '.'
                        || char === '+'
                        || char === '-'
                        || char === 'e'
                        || char === 'E'
                      ) {
                        result += char;
                      }
                    }

                    // 返回结果字符串
                    return result;
                  }
                  formModel.newValue = filterNumber(formModel.newValue);
                }, 0);
              }
            },
          };
        },
        dynamicRules: ({ formModel }) => [
          {
            message: t('请输入新值'),
            validator: () => {
              if (formModel.newValue) {
                if (formModel.newValue.length > 200) {
                  return Promise.reject(t('输入内容过长, 不能超过200字符'));
                }
                return Promise.resolve();
              }
              return Promise.reject(t('请输入新值'));
            },
          },
        ],
      },
      {
        field: 'newValue',
        component: ({ formModel }) => {
          return (
            <BMFormSelect
              v-model={formModel.newValue}
              onSelect={openSelectModal}
            >
              {{
                modal: show.value
                  ? (
                      <SelectComponent
                        component={{
                          ...props.componentData,
                          value: formModel.newValue,
                        }}
                        isRevise={true}
                        onCancel={selectCancel}
                        onConfirm={selectConfirm}
                      />
                    )
                  : (
                      ''
                    ),
              }}
            </BMFormSelect>
          );
        },
        label: t('新值'),
        colProps: {
          span: 24,
        },
        vIf: () => props.componentData.componentType === 'SELECT',
        dynamicRules: ({ formModel }) => [
          {
            required: true,
            message: t('请选择新值'),
            validator: () => {
              if (formModel.newValue) {
                return Promise.resolve();
              }
              return Promise.reject(t('请选择新值'));
            },
          },
        ],
      },
      {
        field: 'newValue',
        component: ({ formModel }) => {
          return (
            <BMFormSelect
              v-model={formModel.newValue}
              closable={true}
              options={props.componentData.componentDetail}
              title={t('选项列表')}
              field-names={{
                label: 'field',
                value: 'field',
              }}
              showEmptyValue={formModel.newValue === nullValueRef.value}
              cancelText={t('录入空值')}
              onCancel={() => {
                formModel.newValue = nullValueRef.value;
              }}
            />
          );
        },
        label: t('新值'),
        colProps: {
          span: 24,
        },
        vIf: () => props.componentData.componentType === 'RADIO',
        dynamicRules: ({ formModel }) => [
          {
            required: true,
            message: t('请选择新值'),
            validator: () => {
              if (formModel.newValue) {
                return Promise.resolve();
              }
              return Promise.reject(t('请选择新值'));
            },
          },
        ],
      },
      {
        field: 'newValue',
        component: ({ formModel }) => {
          return (
            <BMFormSelect
              v-model={formModel.newValue}
              options={props.componentData.componentDetail}
              title={t('选项列表')}
              type="checkbox"
              closable={true}
              cancelText={t('录入空值')}
              showEmptyValue={formModel.newValue === nullValueRef.value}
              field-names={{
                label: 'field',
                value: 'field',
              }}
              onCancel={() => {
                formModel.newValue = nullValueRef.value;
              }}
            />
          );
        },
        label: t('新值'),
        colProps: {
          span: 24,
        },
        vIf: () => props.componentData.componentType === 'CHECKBOX',
        dynamicRules: ({ formModel }) => [
          {
            required: true,
            message: t('请选择新值'),
            validator: () => {
              if (formModel.newValue) {
                return Promise.resolve();
              }
              return Promise.reject(t('请选择新值'));
            },
          },
        ],
      },
      {
        vIf: () => props.componentData.componentType === 'DATE' || props.componentData.componentType === 'TIME',
        field: 'newValue',
        component: ({ formModel }) => {
          return (
            <BMFormSelect
              v-model={formModel.newValue}
              onSelect={openSelectModal}
              placeholder={t('选择日期')}
            >
              {{
                modal: show.value
                  ? (
                      <TimeDateComponentNew
                        component={{
                          ...props.componentData,
                          value: formModel.newValue,
                        }}
                        isRevise={true}
                        onClose={selectCancel}
                        onConfirm={selectConfirm}
                      />
                    )
                  : (
                      ''
                    ),
              }}
            </BMFormSelect>
          );
        },
        label: t('新值'),
        colProps: {
          span: 24,
        },
        dynamicRules: ({ formModel }) => [
          {
            required: true,
            message: t('选择日期'),
            validator: () => {
              if (formModel.newValue) {
                return Promise.resolve();
              }
              return Promise.reject(t('请选择日期'));
            },
          },
        ],
      },
      {
        field: 'remark',
        component: 'Input',
        label: t('备注'),
        colProps: {
          span: 24,
        },
        required: true,
      },
    ],
  });

  return {
    formRef,
    formProps,
  };
};
