import { getCargoPrincipalListApi, getMesUnitExtendListApi, getWmsUnitListApi } from '@/services';

import type { FormProps, RenderCallbackParams } from '@bmos/components';

import { t } from '@bmos/i18n';

import type { CascaderProps, TreeProps } from 'ant-design-vue';

import { Cascader, Select, message } from 'ant-design-vue';

import { Ref, reactive, ref } from 'vue';

type AddFormProps = {
  categoryTreeData: Ref<TreeProps['treeData']>;
  initialValues: Ref<any>;
  categoryType: number; //  0: 原辅包 1: 中间品 2: 产品
  addModalFormRef: Ref<any>;
};

export const useAddModalForm = (props: AddFormProps) => {
  const { addModalFormRef } = props;
  const unitList = ref<any>([]);
  const getUnitList = async () => {
    //回显时，获取对应的拓展单位
    if (props.initialValues.value.unitId) {
      // console.log('编辑或查看...');
      try {
        const unitId = props.initialValues.value.unitId;
        const extendRes = await getMesUnitExtendListApi(
          props.initialValues.value.parentUnitId || props.initialValues.value.unitId,
        );
        (extendRes.data || []).forEach((item: any) => {
          item.unitId = item.id;
          item.unitName = `${item.name}(${item.expression})`;
        });
        unitList.value = [
          {
            unitId: props.initialValues.value.parentUnitId || props.initialValues.value.unitId,
            unitName: `${props.initialValues.value.parentUnitName || props.initialValues.value.unit}(${t('标准单位')})`,
            name: props.initialValues.value.parentUnitName || props.initialValues.value.unit,
            isUnit: true,
          },
          ...extendRes.data,
        ];
        // 如果拓展单位不在下拉框中，添加到下拉框中
        if (
          props.initialValues.value.unitExtendId &&
          unitList.value.every((item: any) => {
            return item.unitId !== props.initialValues.value.unitExtendId;
          })
        ) {
          unitList.value.push({
            unitId: props.initialValues.value.unitExtendId,
            unitName: `${props.initialValues.value.unitExtendName}`,
            name: props.initialValues.value.unitExtendName,
            parentId: unitId,
            disabled: true,
          });
        }
      } catch (error: any) {
        message.error(error.message);
      }
    } else {
      // console.log('新增时候....', props.initialValues.value);
      try {
        const res = await getWmsUnitListApi();

        unitList.value = res.data || [];
        unitList.value?.forEach((item: any) => {
          item.isLeaf = false;
          if (item.unitId === props.initialValues.value.unitId) {
            item.children = [
              {
                id: item.id,
                unitName: `${item.unitName}(${t('标准单位')})`,
                // name: item.unitName,
                name: `${item.name}(${t('标准单位')})`,
                isUnit: true,
              },
            ];
          } else {
            item.children = [];
          }
        });
      } catch (error: any) {
        message.error(error.message);
      }
    }
  };
  const loadData: CascaderProps['loadData'] = selectedOptions => {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    // // load options lazily
    getMesUnitExtendListApi(targetOption.id).then((res: any) => {
      targetOption.loading = false;
      (res.data || []).forEach((item: any) => {
        item.unitName = `${item.extendUnitName}(${item.expression})`;
        item.showName = item.name;
        // item.name = item.extendUnitName;
        item.name = `${item.name}(${item.expression})`;
        item.parentId = targetOption.id;
      });
      targetOption.children = [
        {
          id: targetOption.id,
          showName: targetOption.name,
          unitName: `${targetOption.name}(标准单位)`,
          name: `${targetOption.name}(标准单位)`,
          isUnit: true,
        },
        ...res.data,
      ];
      unitList.value = [...unitList.value];
    });
  };
  getUnitList();

  const itemDisabled = !!props.initialValues.value.id;

  const addModalFormProps = reactive<FormProps>({
    initialValues: props.initialValues.value,
    schemas: [
      {
        field: 'cargoCategoryId',
        component: 'TreeSelect',
        label: t('所属分类'),
        required: true,
        componentProps: ({ formModel, formInstance }: any) => {
          return {
            treeData: props.categoryTreeData.value?.[0]?.children,
            fieldNames: {
              label: 'fullName',
              value: 'id',
            },
            disabled: itemDisabled,
            onSelect: (value: any, node: any) => {
              formInstance?.updateSchema({
                field: 'subMaterialId',
                componentProps: {
                  options: [],
                },
              });
              formModel.subMaterialId = undefined;
              formModel.categoryCode = '';
              if (value) {
                getMaterialPrincipalListFn(value);
                formModel.categoryCode = node.cargoCategoryCode; //可能待改cargoCategoryMergeCode?
              }
            },
          };
        },
      },
      {
        field: 'cargoName',
        component: 'Input',
        label: t('名称'),
        required: true,
        componentProps: {
          disabled: itemDisabled,
        },
      },
      {
        field: 'cargoCode',
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
        vIf: !props.initialValues.value.id, //新增时候展示
        component: ({ formModel }: any) => {
          return (
            <Cascader
              options={unitList.value}
              fieldNames={{ label: 'name', value: 'id' }}
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
                formModel.unitName = selectedOptions[1].showName;
              }}></Cascader>
          );
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
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
        vIf: !!props.initialValues.value.id, //编辑及查看时候展示
        component: ({ formModel }: any) => {
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
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
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
        field: 'isMember',
        component: 'RadioGroup',
        label: t('成员物料'),
        required: true,
        componentProps: ({ formModel, formInstance }: any) => {
          return {
            options: [
              { label: t('是'), value: true },
              { label: t('否'), value: false },
            ],
            disabled: itemDisabled,
            onChange: (e: any) => {
              if (!e.target.value) {
                formModel.subMaterialId = undefined;
              }
              formInstance.clearValidate(['subMaterialId']);
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              type: 'boolean',
              message: t('请选择成员物料'),
              validator: (_rule: any, value: any) => {
                if (value === undefined) {
                  return Promise.reject(t('请选择成员物料'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'subMaterialId',
        component: 'Select',
        label: t('所属物料'),
        required: ({ formModel }: any) => {
          return formModel.isMember;
        },
        componentProps: ({ formModel }: any) => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            disabled: !formModel.isMember === true || itemDisabled,
          };
        },
      },
      {
        field: 'singleQuantity',
        label: t('单件量'),
        component: 'InputNumber',
        componentProps: () => {
          return {
            stringMode: true,
            controls: false,
            step: 1,
            style: 'width:100%',
          };
        },
        componentSlots: ({ formModel }: RenderCallbackParams) => {
          return {
            addonAfter: () => <div style='min-width:20px;'>{formModel.unitName || formModel.unit}</div>,
          };
        },
        dynamicRules: () => {
          return [
            {
              type: 'number',
              validator: (_rule: any, value: any) => {
                if (!value) return Promise.resolve();
                // 输入正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 判断是否整数，且整数部分最多10位，小数部分最多9位
                if (!/^\d{1,10}(\.\d{0,9})?$/.test(value) && value !== undefined && value !== null) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'supplier',
        component: 'Input',
        label: t('供应商'),
      },
      {
        field: 'producer',
        component: 'Input',
        label: t('生产商'),
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
    labelWidth: 100,
  });
  // 通过所属分类查所属物料
  const getMaterialPrincipalListFn = async (id: string) => {
    try {
      const res = await getCargoPrincipalListApi({
        categoryId: id,
        // filter: !props.initialValues.value.id,
      });
      (res.data || []).forEach((item: any) => (item.name = `${item.mergeCode}-${item.cargoName}`));
      addModalFormRef.value?.formRef.updateSchema({
        field: 'subMaterialId',
        componentProps: {
          options: res.data || [],
        },
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };

  return {
    addModalFormProps,
    getMaterialPrincipalListFn,
  };
};
