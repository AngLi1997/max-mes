import { BMTableTitle, FormSchema, Recordable, RenderCallbackParams } from '@bmos/components';
import { Button } from 'ant-design-vue';
import { useCommonStationConfig } from '../../hooks';
import EquipmentDataDraw from './EquipmentDataDraw.vue';

// 设备信息
export const useEquipmentDataDraw = ({ props, isView, hasChange }: any) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const formCode = ref(1);
  const addCcExecutionCondition = (formModel: Recordable) => {
    formModel.equipmentPictureConfigList.push({
      formCode: ++formCode.value,
    })
  };
  const drawConfig: FormSchema[] = [
    {
      field: 'equipmentPictureConfigList',
      formItemProps: {
        labelCol: { span: 24 },
      },
      labelFullWidth: true,
      disabledLabelWidth: true,
      noLabelTip: true,
      label: ({ formModel }: RenderCallbackParams) => {
        return (
          <div class='condition-label'>
            <BMTableTitle title={t('设备数据')} />
            <Button
              type='link'
              onClick={() => {
                addCcExecutionCondition(formModel);
              }}>
              {t('添加设备数据')}
            </Button>
          </div>
        );
      },
      component: ({ formModel }: RenderCallbackParams) => {
        return <EquipmentDataDraw 
          v-model:equipmentPictureConfigList={formModel.equipmentPictureConfigList}
          isView={isView.value}
        />;
      },
      dynamicRules: ({ formInstance }: RenderCallbackParams) => {
        return [
          {
            validator: async () => {
              try {
                const formRef = formInstance?.compRefMap.get('equipmentPictureConfigList');
                await formRef?.validateForm();
                return Promise.resolve();
              } catch (error) {
                return Promise.reject('');
              }
            },
          },
        ];
      },
    },
  ];
  const equipmentDataDrawConfig = ref<FormSchema[]>([...stationConfig, ...drawConfig]);
  return {
    equipmentDataDrawConfig,
  };
};
