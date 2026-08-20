import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseMaterialInputSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料投入 汇总
export const useMaterialInputSummaryConfig = ({ props, hasChange }: UseMaterialInputSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const materialInputSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    materialInputSummaryConfig,
  };
};
