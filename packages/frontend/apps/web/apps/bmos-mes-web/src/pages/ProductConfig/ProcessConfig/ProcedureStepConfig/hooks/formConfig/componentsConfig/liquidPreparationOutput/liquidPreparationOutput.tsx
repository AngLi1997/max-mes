import { MaterialTypeMap } from '@/pages/ProductionMaterials/PageComponentNew/const';
import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseIngredientsOutputConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液产出
export const useLiquidPreparationOutputConfig = ({ props, hasChange }: UseIngredientsOutputConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const { commonMaterialConfig } = useCommonMaterialConfig({
    props,
    hasChange,
    showMaterialTitle: true,
    label: t('中间品物料'),
    materialType: MaterialTypeMap.MiddleProduct,
  });
  const liquidPreparationOutputConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    liquidPreparationOutputConfig,
  };
};
