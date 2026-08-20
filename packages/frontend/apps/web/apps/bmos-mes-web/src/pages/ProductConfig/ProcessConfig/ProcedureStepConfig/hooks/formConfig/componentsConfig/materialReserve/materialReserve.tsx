import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseMaterialReserveConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 物料预定
export const useMaterialReserveConfig = ({ props, hasChange }: UseMaterialReserveConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const materialReserveConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    materialReserveConfig,
  };
};
