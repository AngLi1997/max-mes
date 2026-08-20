import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';

export type UseEquipmentInfoConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 设备信息
export const useEquipmentInfoConfig = ({ props, hasChange }: UseEquipmentInfoConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const equipmentInfoConfig = ref<FormSchema[]>([...stationConfig]);
  return {
    equipmentInfoConfig,
  };
};
