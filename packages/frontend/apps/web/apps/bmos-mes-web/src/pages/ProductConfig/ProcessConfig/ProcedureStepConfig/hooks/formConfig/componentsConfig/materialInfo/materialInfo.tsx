import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';

export type UseMaterialInfoConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 物料件信息
export const useMaterialInfoConfig = ({ props, hasChange }: UseMaterialInfoConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle: false });
  const materialInfoConfig = ref<FormSchema[]>([...stationConfig]);
  return {
    materialInfoConfig,
  };
};
