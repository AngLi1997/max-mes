import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseIngredientsInputConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液投入
export const useLiquidPreparationInputConfig = ({ props, hasChange }: UseIngredientsInputConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, showMaterialTitle: true });
  const liquidPreparationInputConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    liquidPreparationInputConfig,
  };
};
