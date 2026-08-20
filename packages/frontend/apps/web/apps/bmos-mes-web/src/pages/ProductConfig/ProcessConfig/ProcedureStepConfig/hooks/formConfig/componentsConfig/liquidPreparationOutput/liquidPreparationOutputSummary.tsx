import { MaterialTypeMap } from '@/pages/ProductionMaterials/PageComponentNew/const';
import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseLiquidPreparationOutputSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配液产出 汇总
export const useLiquidPreparationOutputSummaryConfig = ({
  props,
  hasChange,
}: UseLiquidPreparationOutputSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({
    props,
    hasChange,
    multiple: false,
    showMaterialTitle: true,
    label: t('中间品物料'),
    materialType: MaterialTypeMap.MiddleProduct,
  });
  const liquidPreparationOutputSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    liquidPreparationOutputSummaryConfig,
  };
};
