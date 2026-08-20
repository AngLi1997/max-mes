import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseIngredientsPlanSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 按物料量领料 物料
export const useIngredientsPlanSummaryConfig = ({ props, hasChange }: UseIngredientsPlanSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const ingredientsPlanSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    ingredientsPlanSummaryConfig,
  };
};
