import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseMaterialReserveSummaryConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 物料预定 - 物料汇总
export const useMaterialReserveSummaryConfig = ({ props, hasChange }: UseMaterialReserveSummaryConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const materialReserveSummaryConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    materialReserveSummaryConfig,
  };
};
