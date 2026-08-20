import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseOutputWeighingConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料称量
export const useOutputWeighingConfig = ({ props, hasChange }: UseOutputWeighingConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, materialType: 1 });
  const { stationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle: true });
  const outputWeighingConfig = ref<FormSchema[]>([
    ...commonMaterialConfig,
    ...stationConfig,
    {
      field: 'weightModeConfig',
      label: t('称量模式'),
      component: 'TableTitle',
    },
    {
      field: 'weightMode',
      component: 'Select',
      label: t('称量模式'),
      componentProps: () => {
        return {
          mode: 'multiple',
          showSearch: true,
          options: [
            { label: t('产出称量'), value: 0 },
            { label: t('手动称量'), value: 1 },
            { label: t('手动产出'), value: 2 },
            { label: t('扫码去皮'), value: 3 },
          ],
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
  ]);
  return {
    outputWeighingConfig,
  };
};
