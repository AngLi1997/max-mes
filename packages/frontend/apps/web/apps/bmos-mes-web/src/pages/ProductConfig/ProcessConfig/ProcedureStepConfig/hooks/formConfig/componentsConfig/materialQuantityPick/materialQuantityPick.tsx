import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseMaterialQuantityPickConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 按物料量领料
export const useMaterialQuantityPickConfig = ({ props, hasChange }: UseMaterialQuantityPickConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const materialQuantityPickConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    materialQuantityPickConfig,
  };
};
