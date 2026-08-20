import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseLiquidPreparationMeasureConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液量取
export const useLiquidPreparationMeasureConfig = ({ props, hasChange }: UseLiquidPreparationMeasureConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({
    props,
    hasChange,
    multiple: true,
    showMaterialTitle: true,
  });
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const liquidPreparationMeasureConfig = ref<FormSchema[]>([
    ...commonMaterialConfig,
    ...stationConfig,
    // {
    //   field: 'equipmentDataAcquisitionConfig',
    //   label: t('设备数采'),
    //   component: 'TableTitle',
    // },
    // {
    //   field: 'equipmentDataAcquisitionParams',
    //   component: 'Select',
    //   label: t('数采参数'),
    //   componentProps: () => {
    //     return {
    //       showSearch: true,
    //       filterOption: (input: string, option: any) => {
    //         return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    //       },
    //       request: async () => {
    //         try {
    //           // const { data } = await reqProductFormulaMaterialListByProcedureId(props.procedureId, materialType);
    //           // return data.map((item: any) => ({
    //           //   label: item.materialMergeCode + '-' + item.materialName,
    //           //   value: item.id,
    //           // }));
    //           return [];
    //         } catch (error) {
    //           return [];
    //         }
    //       },
    //       onChange: () => {
    //         hasChange.value = true;
    //       },
    //     };
    //   },
    // },
  ]);
  return {
    liquidPreparationMeasureConfig,
  };
};
