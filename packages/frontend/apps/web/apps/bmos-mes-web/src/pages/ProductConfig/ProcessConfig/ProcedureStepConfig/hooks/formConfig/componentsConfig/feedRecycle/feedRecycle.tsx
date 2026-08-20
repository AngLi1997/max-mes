import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseFeedRecycleConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 生产投料
export const useFeedRecycleConfig = ({ props, hasChange }: UseFeedRecycleConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const feedRecycleConfig = ref<FormSchema[]>([...commonMaterialConfig, ...stationConfig]);
  return {
    feedRecycleConfig,
  };
};
