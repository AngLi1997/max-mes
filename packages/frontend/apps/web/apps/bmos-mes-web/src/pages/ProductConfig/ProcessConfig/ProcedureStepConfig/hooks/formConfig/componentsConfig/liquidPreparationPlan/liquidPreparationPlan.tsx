import { reqProductFormulaMaterialListByProcessReq } from '@/services';
import { BMTableTitle, FormSchema, RenderCallbackParams } from '@bmos/components';
import { Button } from 'ant-design-vue';
import { SelectValue } from 'ant-design-vue/es/select';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';
import MaterialInput from './MaterialInput.vue';

export type UseIngredientsPlanConfigParams = {
  props: ConfigFormProps;
  isView: ComputedRef<boolean>;
  hasChange: Ref<boolean>;
};
// 配液计划
export const useLiquidPreparationPlanConfig = ({ props, isView, hasChange }: UseIngredientsPlanConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const addMaterial = (formModel: any) => {
    if (!formModel.materialList) {
      formModel.materialList = [];
    }
    formModel.materialList.push({
      key: new Date().getTime(),
    });
  };
  const liquidPreparationPlanConfig = ref<FormSchema[]>([
    {
      field: 'middleProductTableTitle',
      label: t('产出中间品'),
      component: 'TableTitle',
    },
    {
      field: 'formulaMaterialId',
      component: 'Select',
      label: t('中间品'),
      required: true,
      componentProps: ({ formModel }) => {
        return {
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          request: async () => {
            try {
              const { data } = await reqProductFormulaMaterialListByProcessReq({
                processVersionId: props.versionId,
                categoryType: 1,
              });
              if (formModel.formulaMaterialId) {
                // 找到当前选中的中间品
                const currentMaterial = data.find((item: any) => item.id === formModel.formulaMaterialId);
                if (currentMaterial) {
                  formModel.unitName = currentMaterial.unitName;
                }
              }
              return data.map((item: any) => ({
                label: item.materialMergeCode + '-' + item.materialName,
                value: item.id,
                ...item,
              }));
            } catch (error) {
              return [];
            }
          },
          onChange: (value: SelectValue, option: any) => {
            if (value) {
              // 设置单位
              formModel.unitName = option.unitName;
            } else {
              formModel.unitName = undefined;
            }
            hasChange.value = true;
          },
        };
      },
    },
    {
      component: 'Input',
      label: t('目标体积'),
      field: 'targetVolume',
      required: true,
      componentProps: () => {
        return {
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
      componentSlots: {
        addonAfter: ({ formModel }: RenderCallbackParams) => {
          return (
            <span
              style={{
                display: 'inline-block',
                width: '80px',
              }}>
              {formModel.unitName}
            </span>
          );
        },
      },
      dynamicRules: () => {
        return [
          {
            required: true,
            validator: async (_rule: any, value: any) => {
              if (isNaN(Number(value)) || Number(value) <= 0) {
                return Promise.reject(t('请输入正数'));
              }
              // 如果值 整数部分最多为10位，小数位数最多为9位
              const reg = /^\d{1,10}(\.\d{1,9})?$/;
              if (!reg.test(value)) {
                return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'materialList',
      formItemProps: {
        labelCol: { span: 24 },
      },
      labelFullWidth: true,
      disabledLabelWidth: true,
      noLabelTip: true,
      defaultValue: [
        {
          key: new Date().getTime(),
        },
      ],
      label: ({ formModel }: RenderCallbackParams) => {
        return (
          <div
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              width: '100%',
            }}>
            <BMTableTitle title={t('配液物料')} />
            <Button
              type='link'
              onClick={() => {
                hasChange.value = true;
                addMaterial(formModel);
              }}>
              {isView.value ? '' : t('添加物料')}
            </Button>
          </div>
        );
      },
      component: ({ formModel }: RenderCallbackParams) => {
        return (
          <MaterialInput
            v-model:materials={formModel.materialList}
            procedureModelId={props.procedureId}
            versionId={props.versionId}
            isView={isView.value}
            onDeleteItem={() => {
              hasChange.value = true;
            }}
            onChange={() => {
              hasChange.value = true;
            }}
          />
        );
      },
      dynamicRules({ formInstance }: RenderCallbackParams) {
        return [
          {
            required: false,
            trigger: 'blur',
            validator: async () => {
              try {
                const materialRef = formInstance?.compRefMap.get('materialList');
                await materialRef?.validateForm();
                return Promise.resolve();
              } catch (error) {
                return Promise.reject();
              }
            },
          },
        ];
      },
    },
    ...stationConfig,
  ]);
  return {
    liquidPreparationPlanConfig,
  };
};
