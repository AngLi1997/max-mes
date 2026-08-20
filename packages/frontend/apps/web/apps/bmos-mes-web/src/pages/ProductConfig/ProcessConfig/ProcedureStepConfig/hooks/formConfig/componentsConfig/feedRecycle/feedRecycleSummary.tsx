import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseFeedRecycleSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料称量汇总
export const useFeedRecycleSummaryConfig = ({ props, hasChange }: UseFeedRecycleSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const feedRecycleSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    feedRecycleSummaryConfig,
  };
};
