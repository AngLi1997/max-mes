import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseWeighingIngredientsSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料称量汇总
export const useWeighingIngredientsSummaryConfig = ({
  props,
  hasChange,
}: UseWeighingIngredientsSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const weighingIngredientsSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    weighingIngredientsSummaryConfig,
  };
};
