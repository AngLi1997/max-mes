import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseBatchQuantityPickSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 物料批次领料汇总
export const useBatchQuantityPickSummaryConfig = ({ props, hasChange }: UseBatchQuantityPickSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const batchQuantityPickSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    batchQuantityPickSummaryConfig,
  };
};
