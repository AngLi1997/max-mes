import { FormSchema, RenderCallbackParams } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';
import DataAttrList from './DataAttrList.vue';

export type UseEquipmentDataAcquisitionConfigParams = {
  props: ConfigFormProps;
  isView: ComputedRef<boolean>;
  hasChange: Ref<boolean>;
};
// 设备数采
export const useEquipmentDataAcquisitionConfig = ({
  props,
  isView,
  hasChange,
}: UseEquipmentDataAcquisitionConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle: false });
  const equipmentDataAcquisitionConfig = ref<FormSchema[]>([
    ...stationConfig,
    {
      field: 'dateFormat',
      component: 'Select',
      label: t('日期格式'),
      required: true,
      componentProps: () => {
        return {
          // ● 年月日时分
          // ● 年月日
          // ● 年月日时分
          // ● 时分
          // ● 时分
          options: [
            {
              label: t('年月日时分秒'),
              value: 'yyyy-MM-dd HH:mm:ss',
            },
            {
              label: t('年月日时分'),
              value: 'yyyy-MM-dd HH:mm',
            },
            {
              label: t('年月日'),
              value: 'yyyy-MM-dd',
            },
            {
              label: t('年月'),
              value: 'yyyy-MM',
            },
            {
              label: t('月日时分'),
              value: 'MM-dd HH:mm',
            },
            {
              label: t('时分秒'),
              value: 'HH:mm:ss',
            },
            {
              label: t('时分'),
              value: 'HH:mm',
            },
          ],

          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
    {
      field: 'equipmentDataAttr',
      label: t('设备数据属性配置'),
      component: 'TableTitle',
    },
    {
      field: 'equipmentDataAttrList',
      label: '',
      noLabel: true,
      component: ({ formModel }: RenderCallbackParams) => {
        return (
          <DataAttrList
            v-model:dataAttrList={formModel.equipmentDataAttrList}
            activeNodeData={props.activeNodeData}
            isView={isView.value}
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
                const materialRef = formInstance?.compRefMap.get('equipmentDataAttrList');
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
  ]);
  return {
    equipmentDataAcquisitionConfig,
  };
};
