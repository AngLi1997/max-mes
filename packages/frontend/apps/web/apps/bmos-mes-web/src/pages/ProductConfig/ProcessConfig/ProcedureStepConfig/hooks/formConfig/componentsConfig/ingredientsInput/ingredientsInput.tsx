import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseIngredientsInputConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料投入
export const useIngredientsInputConfig = ({ props, hasChange }: UseIngredientsInputConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const ingredientsInputConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    ingredientsInputConfig,
  };
};
