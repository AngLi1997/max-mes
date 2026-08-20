import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseLiquidPreparationMeasureSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液量取 汇总
export const useLiquidPreparationMeasureSummaryConfig = ({
  props,
  hasChange,
}: UseLiquidPreparationMeasureSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const liquidPreparationMeasureSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    liquidPreparationMeasureSummaryConfig,
  };
};
