import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseBatchQuantityPickConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 物料批次领料
export const useBatchQuantityPickConfig = ({ props, hasChange }: UseBatchQuantityPickConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const batchQuantityPickConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    batchQuantityPickConfig,
  };
};
