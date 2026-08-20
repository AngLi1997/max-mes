import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseOutputWeighingSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料称量汇总
export const useOutputWeighingSummaryConfig = ({ props, hasChange }: UseOutputWeighingSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, materialType: '1', multiple: false });
  const outputWeighingSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    outputWeighingSummaryConfig,
  };
};
