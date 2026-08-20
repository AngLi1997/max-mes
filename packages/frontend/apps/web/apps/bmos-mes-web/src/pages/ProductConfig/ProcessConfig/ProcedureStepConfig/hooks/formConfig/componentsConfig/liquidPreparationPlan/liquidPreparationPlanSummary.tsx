import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseLiquidPreparationPlanSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液计划 汇总
export const useLiquidPreparationPlanSummaryConfig = ({
  props,
  hasChange,
}: UseLiquidPreparationPlanSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const liquidPreparationPlanSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    liquidPreparationPlanSummaryConfig,
  };
};
