import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseIngredientsPlanConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料计划
export const useIngredientsPlanConfig = ({ props, hasChange }: UseIngredientsPlanConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const ingredientsPlanConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    ingredientsPlanConfig,
  };
};
