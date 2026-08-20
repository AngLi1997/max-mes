import { BMTableTitle, FormSchema, Recordable, RenderCallbackParams } from '@bmos/components';
import { Button } from 'ant-design-vue';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';
import MaterialInput from './MaterialInput.vue';

export type UseMaterialInputConfigParams = {
  props: ConfigFormProps;
  isView: ComputedRef<boolean>;
  hasChange: Ref<boolean>;
};
// 配料投入
export const useMaterialInputConfig = ({ props, isView, hasChange }: UseMaterialInputConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const addMaterial = (formModel: any) => {
    if (!formModel.materialList) {
      formModel.materialList = [];
    }
    formModel.materialList.push({
      key: new Date().getTime(),
    });
  };
  const materialInputConfig = ref<FormSchema[]>([
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
            <BMTableTitle title={t('物料信息')} />
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
            isView={isView.value}
            onDeleteItem={(item: Recordable) => {
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
            validator: async (rule: any, value: any) => {
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
    materialInputConfig,
  };
};
