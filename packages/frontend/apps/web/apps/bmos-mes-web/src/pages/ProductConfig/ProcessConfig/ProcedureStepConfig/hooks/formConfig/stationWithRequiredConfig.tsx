import { BMTableTitle, FormSchema } from '@bmos/components';
import { t } from '@bmos/i18n';
import { RadioButton, RadioGroup } from 'ant-design-vue';
import { ConfigFormProps } from '../../types';
import { useCommonStationConfig } from './hooks';

export type UseStationWithRequiredConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
  showStationTitle?: boolean;
};
export const useStationWithRequiredConfig = ({
  props,
  hasChange,
  showStationTitle,
}: UseStationWithRequiredConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange, showStationTitle });
  const stationWithRequiredConfig: FormSchema[] = [
    ...stationConfig,
    {
      label: () => (
        <>
          <span>
            <BMTableTitle title={t('是否必填')} />
          </span>
        </>
      ),
      field: 'required',
      defaultValue: false,
      component: ({ formModel }) => {
        return (
          <>
            <RadioGroup v-model:value={formModel['required']}>
              <RadioButton value={true}>{t('是')}</RadioButton>
              <RadioButton value={false} class='waring-false'>
                {t('否')}
              </RadioButton>
            </RadioGroup>
          </>
        );
      },
      componentProps: {
        onChange: () => {
          hasChange.value = true;
        },
      },
    },
  ];
  return {
    stationWithRequiredConfig,
  };
};
