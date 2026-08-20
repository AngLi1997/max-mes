import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig, useCommonStationConfig } from '../../hooks';

export type UseWeighingIngredientsConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 配料称量
export const useWeighingIngredientsConfig = ({ props, hasChange }: UseWeighingIngredientsConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange });
  const { stationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle: true });
  const weighingIngredientsConfig = ref<FormSchema[]>([
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
            { label: t('配料称量'), value: 0 },
            { label: t('手动称量'), value: 1 },
          ],
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
  ]);
  return {
    weighingIngredientsConfig,
  };
};
