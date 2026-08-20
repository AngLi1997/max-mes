import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseLiquidPreparationInputSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液投入 汇总
export const useLiquidPreparationInputSummaryConfig = ({
  props,
  hasChange,
}: UseLiquidPreparationInputSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({
    props,
    hasChange,
    multiple: false,
    showMaterialTitle: true,
  });
  const liquidPreparationInputSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    liquidPreparationInputSummaryConfig,
  };
};
