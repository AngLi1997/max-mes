import { FormSchema } from '@bmos/components';
import { ref } from 'vue';
import { ConfigFormProps } from '../../types';
import { useCommonStationConfig } from './hooks';

export type UseStationConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
  showStationTitle?: boolean;
};
export const useStationConfig = ({ props, hasChange, showStationTitle }: UseStationConfigParams) => {
  const { stationConfig: commonStationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle });
  const stationConfig = ref<FormSchema[]>([...commonStationConfig]);
  return {
    stationConfig,
  };
};
