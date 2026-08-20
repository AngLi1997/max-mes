import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseIngredientsInputSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料投入汇总
export const useIngredientsInputSummaryConfig = ({ props, hasChange }: UseIngredientsInputSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const ingredientsInputSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    ingredientsInputSummaryConfig,
  };
};
