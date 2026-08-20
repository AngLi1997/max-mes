import {
  getMesUnitExtendListApi,
  getMesUnitExtendListBoundApi,
  getMesUnitListApi,
  getProductMaterialPrincipalListApi,
  getUnitExtendDetailApi,
} from '@/services';
import type { FormProps, FormSchema, RenderCallbackParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import type { CascaderProps, TreeProps } from 'ant-design-vue';
import { Cascader, Select, message } from 'ant-design-vue';
import { Ref, reactive, ref } from 'vue';
type AddFormProps = {
  categoryTreeData: Ref<TreeProps['treeData']>;
  initialValues: Ref<any>;
  categoryType: number; //  0: 原辅包 1: 中间品 2: 产品
};

export const useAddModalForm = (props: AddFormProps) => {
  const materialPrincipalList = ref([]);
  const unitList = ref<CascaderProps['options']>([]);
  const getUnitList = async () => {
    //回显时，获取对应的拓展单位
    if (props.initialValues.value.unitId) {
      try {
        const unitId = props.initialValues.value.unitId;
        const extendRes = await getMesUnitExtendListBoundApi({
          materialId: props.initialValues.value.id,
        });
        (extendRes.data || []).forEach(item => {
          item.unitId = item.id;
          item.unitName = `${item.extendUnitName}(${item.expression})`;
          item.name = item.extendUnitName;
          item.parentId = unitId;
        });
        unitList.value = [
          {
            unitId,
            unitName: `${props.initialValues.value.unitName}(${t('标准单位')})`,
            name: props.initialValues.value.unitName,
            isUnit: true,
          },
          ...extendRes.data,
        ];
        // 如果拓展单位不在下拉框中，添加到下拉框中
        if (
          props.initialValues.value.unitExtendId &&
          unitList.value.every(item => {
            return item.unitId !== props.initialValues.value.unitExtendId;
          })
        ) {
          const extendDetail = await getUnitExtendDetailApi({ id: props.initialValues.value.unitExtendId });
          unitList.value.push({
            unitId: props.initialValues.value.unitExtendId,
            unitName: `${props.initialValues.value.unitExtendName}(${extendDetail.data.expression})`,
            name: props.initialValues.value.unitExtendName,
            parentId: unitId,
            disabled: true,
          });
        }
      } catch (error) {
        message.error(error.message);
      }
    } else {
      try {
        const res = await getMesUnitListApi();
        unitList.value = res.data || [];
        unitList.value.forEach((item: any) => {
          item.isLeaf = false;
          if (item.unitId === props.initialValues.value.unitId) {
            item.children = [
              {
                unitId: item.unitId,
                unitName: `${item.unitName}(${t('标准单位')})`,
                name: item.unitName,
                isUnit: true,
              },
            ];
          } else {
            item.children = [];
          }
        });
      } catch (error) {
        message.error(error.message);
      }
    }
  };
  const loadData: CascaderProps['loadData'] = selectedOptions => {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    // // load options lazily
    getMesUnitExtendListApi(targetOption.unitId).then(res => {
      targetOption.loading = false;
      (res.data || []).forEach(item => {
        item.unitId = item.id;
        item.unitName = `${item.extendUnitName}(${item.expression})`;
        item.name = item.extendUnitName;
        item.parentId = targetOption.unitId;
      });
      targetOption.children = [
        {
          unitId: targetOption.unitId,
          unitName: `${targetOption.unitName}(标准单位)`,
          name: targetOption.unitName,
          isUnit: true,
        },
        ...res.data,
      ];
      unitList.value = [...unitList.value];
    });
  };
  getUnitList();

  const itemDisabled = !!props.initialValues.value.id;
  const extraSchemas: FormSchema[][] = [
    [
      {
        field: 'dyingPeriod',
        component: 'Input',
        label: t('临期提醒'),
        componentSlots: () => {
          return {
            addonAfter: () => <div style='min-width:20px;'>{t('日')}</div>,
          };
        },
      },
      {
        field: 'expandInfo.presetTareWeight',
        label: t('预制皮重'),
        component: 'InputNumber',
        componentProps: ({ formModel, formInstance }) => {
          return {
            stringMode: true,
            controls: false,
            min: 0,
            step: 1,
            style: 'width: 100%',
          };
        },
        componentSlots: ({ formModel }: RenderCallbackParams) => {
          return {
            addonAfter: () => <div style='min-width:20px;'>{formModel.unitExtendName || formModel.unitName}</div>,
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              type: 'number',
              validator: (rule: any, value: any) => {
                // 判断是否整数，且整数部分最多10位，小数部分最多9位
                if (!/^\d{1,10}(\.\d{0,9})?$/.test(value) && value !== undefined && value !== null && value !== '') {
                  return Promise.reject(t('只能输入正数，且整数部分最多10位，小数部分最多9位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'expandInfo.supplier',
        component: 'Input',
        label: t('供应商'),
      },
      {
        field: 'expandInfo.producer',
        component: 'Input',
        label: t('生产商'),
      },
      {
        field: 'expandInfo.level',
        component: 'Input',
        label: t('级别'),
      },
      {
        field: 'expandInfo.formulation',
        component: 'Input',
        label: t('剂型'),
      },
    ],
    [
      {
        field: 'dyingPeriod',
        component: 'Input',
        label: t('临期提醒'),
        componentSlots: () => {
          return {
            addonAfter: () => <div style='min-width:20px;'>{t('日')}</div>,
          };
        },
      },
      {
        field: 'expandInfo.producer',
        component: 'Input',
        label: t('生产商'),
      },
      {
        field: 'expandInfo.defaultExpiration',
        label: t('默认效期'),
        component: 'InputNumber',
        componentProps: ({ formModel, formInstance }) => {
          return {
            controls: false,
            min: 0,
            step: 1,
            style: 'width: 100%',
          };
        },
        componentSlots: ({ formModel }: RenderCallbackParams) => {
          return {
            addonAfter: () => (
              <Select
                style='width: 60px'
                v-model:value={formModel.expandInfo.timeUnit}
                options={[
                  { label: t('时'), value: 0 },
                  { label: t('天'), value: 1 },
                  { label: t('月'), value: 2 },
                ]}></Select>
            ),
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              type: 'number',
              validator: (rule: any, value: any) => {
                // 判断是否输入自然数
                if ((!Number.isInteger(value) || value < 0) && value !== undefined && value !== null) {
                  return Promise.reject(t('默认效期只能输入自然数'));
                }
                if (value > 9999) {
                  return Promise.reject(t('默认效期最大位数4位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
    [
      {
        field: 'innerPackingSpecification',
        component: 'Input',
        label: t('内包规格'),
      },
      {
        field: 'packingSpecification',
        component: 'Input',
        label: t('包装规格'),
      },
      {
        field: 'productionCycle',
        component: 'InputNumber',
        label: t('生产周期(天)'),
        componentProps: ({ formModel, formInstance }) => {
          return {
            stringMode: true,
            controls: false,
            min: 0,
            step: 1,
            maxlength: 10,
            style: 'width: 100%',
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              type: 'number',
              validator: (rule: any, value: string) => {
                if (value === undefined || value === null || value === '') {
                  return Promise.resolve();
                }
                // 将value转化为数字
                const result = Number(value);
                // 判断是否输入自然数
                if (!Number.isInteger(result) || result < 0) {
                  return Promise.reject(t('生产周期（天）只能输入自然数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'productMark',
        component: 'Input',
        label: t('产品标识'),
      },
    ],
  ];
  const addModalFormProps = reactive<FormProps>({
    initialValues: props.initialValues.value,
    schemas: [
      {
        field: 'materialCategoryId',
        component: 'TreeSelect',
        label: t('所属分类'),
        required: true,
        componentProps: ({ formModel, formInstance }) => {
          return {
            treeData: props.categoryTreeData.value[0].children,
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            disabled: itemDisabled,
            onSelect: (value, node) => {
              materialPrincipalList.value = [];
              formModel.principalMaterialId = undefined;
              formModel.categoryCode = '';
              if (value) {
                getMaterialPrincipalListFn(value);
                formModel.categoryCode = node.code;
              }
            },
          };
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('名称'),
        required: true,
        componentProps: {
          disabled: itemDisabled,
        },
      },
      {
        field: 'code',
        component: 'Input',
        label: t('编码'),
        required: true,
        componentProps: {
          disabled: itemDisabled,
        },
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('规格'),
        required: true,
        componentProps: {
          disabled: itemDisabled,
        },
      },
      {
        field: 'unitIds',
        label: t('单位'),
        vIf: !props.initialValues.value.id,
        component: ({ formModel, formInstance, field }) => {
          return (
            <Cascader
              options={unitList.value}
              fieldNames={{ label: 'unitName', value: 'unitId' }}
              loadData={loadData}
              placeholder={t('请选择单位')}
              onChange={(value, selectedOptions: any) => {
                if (!value) {
                  formModel.unitId = '';
                  formModel.unitExtendId = '';
                  formModel.unitName = '';
                  return;
                }
                formModel.unitId = value[0];
                formModel.unitExtendId = value[0] === value[1] ? '' : value[1];
                formModel.unitName = selectedOptions[1].name;
              }}></Cascader>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (rule: any, value: any) => {
                if (!value) {
                  return Promise.reject(t('请选择单位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'echoUnitId',
        label: t('单位'),
        vIf: !!props.initialValues.value.id,
        component: ({ formModel, formInstance, field }) => {
          return (
            <Select
              options={unitList.value}
              fieldNames={{ label: 'unitName', value: 'unitId' }}
              onChange={(value, option: any) => {
                if (!value) {
                  formModel.unitId = '';
                  formModel.unitExtendId = '';
                  formModel.unitName = '';
                  return;
                }
                formModel.unitId = option.isUnit ? option.unitId : option.parentId;
                formModel.unitExtendId = option.isUnit ? '' : option.unitId;
                formModel.unitName = option.name;
              }}></Select>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (rule: any, value: any) => {
                if (!value) {
                  return Promise.reject(t('请选择单位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'finishProduct',
        component: 'RadioGroup',
        label: t('成品'),
        required: true,
        vIf: props.categoryType === 2,
        componentProps: ({ formModel, formInstance }) => {
          return {
            options: [
              { label: t('是'), value: true },
              { label: t('否'), value: false },
            ],
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              type: 'boolean',
              message: t('请选择成品'),
              validator: (rule: any, value: any) => {
                if (value === undefined) {
                  return Promise.reject(t('请选择成品'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'subMaterial',
        component: 'RadioGroup',
        label: props.categoryType === 2 ? t('成员产品') : t('成员物料'),
        required: true,
        defaultValue: false,
        componentProps: ({ formModel, formInstance }) => {
          return {
            options: [
              { label: t('是'), value: true },
              { label: t('否'), value: false },
            ],
            disabled: itemDisabled,
            onChange: e => {
              if (!e.target.value) {
                formModel.principalMaterialId = undefined;
              }
              formInstance.clearValidate(['principalMaterialId']);
            },
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              type: 'boolean',
              message: props.categoryType === 2 ? t('请选择成员产品') : t('请选择成员物料'),
              validator: (rule: any, value: any) => {
                if (value === undefined) {
                  return Promise.reject(props.categoryType === 2 ? t('请选择成员产品') : t('请选择成员物料'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'principalMaterialId',
        component: 'Select',
        label: props.categoryType === 2 ? t('所属产品') : t('所属物料'),
        required: ({ formModel }) => {
          return formModel.subMaterial;
        },
        componentProps: ({ formModel }) => {
          return {
            options: materialPrincipalList.value,
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            disabled: !formModel.subMaterial === true || itemDisabled,
          };
        },
      },
      ...extraSchemas[props.categoryType],
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
    labelWidth: 100,
  });

  const getMaterialPrincipalListFn = async (id: string) => {
    try {
      const res = await getProductMaterialPrincipalListApi({
        materialCategoryId: id,
        filter: !props.initialValues.value.id,
      });
      (res.data || []).forEach(item => (item.name = `${item.mergeCode}-${item.name}`));
      materialPrincipalList.value = res.data || [];
    } catch (error) {
      message.error(error.message);
    }
  };

  return {
    addModalFormProps,
    getMaterialPrincipalListFn,
  };
};
