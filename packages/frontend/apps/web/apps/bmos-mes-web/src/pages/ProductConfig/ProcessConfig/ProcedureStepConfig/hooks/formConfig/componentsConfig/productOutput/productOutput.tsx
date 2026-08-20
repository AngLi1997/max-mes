import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';

export type UseProductOutputConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 成品产出
export const useProductOutputConfig = ({ props, hasChange }: UseProductOutputConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle: false });
  const productOutputConfig = ref<FormSchema[]>([...stationConfig]);
  return {
    productOutputConfig,
  };
};
