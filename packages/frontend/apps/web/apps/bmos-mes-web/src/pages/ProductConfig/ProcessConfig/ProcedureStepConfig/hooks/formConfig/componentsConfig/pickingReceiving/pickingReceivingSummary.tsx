import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UsePickingReceivingSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 物料批次领料汇总
export const usePickingReceivingSummaryConfig = ({ props, hasChange }: UsePickingReceivingSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const pickingReceivingSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    pickingReceivingSummaryConfig,
  };
};
