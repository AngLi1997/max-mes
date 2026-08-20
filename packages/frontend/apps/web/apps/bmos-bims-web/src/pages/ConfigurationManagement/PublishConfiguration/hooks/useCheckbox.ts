import { t } from '@bmos/i18n';

interface OptionsType {
  label: string;
  value: string | number;
  disabled?: boolean;
  indeterminate?: boolean;
  onChange?: Function;
}

export const useCheckbox = () => {
  const checkValue = ref<any[]>([]);

  const options = reactive<Array<OptionsType>>([
    {
      label: t('蛋白质含量'),
      value: 1,
      disabled: false,
      onChange: (value: string) => {
        console.log(value);
      },
    },
    {
      label: 'ALT',
      value: 2,
      disabled: false,
      onChange: (value: string) => {
        console.log(value);
      },
    },
    {
      label: `HBsAg/${t('抗-HCV')}/${t('抗-HIV')}/${t('抗-TP')}`,
      value: 3,
      disabled: false,
      onChange: (value: string) => {
        console.log(value);
      },
    },
  ]);

  const companyOptions = reactive<Array<OptionsType>>([
    ...options,
    {
      label: 'PCR',
      value: 4,
      disabled: false,
      onChange: (value: string) => {
        console.log(value);
      },
    },
    {
      label: t('抗体效价'),
      value: 4,
      disabled: false,
      onChange: (value: string) => {
        console.log(value);
      },
    },
  ]);

  const returnVisitOptions = reactive<Array<OptionsType>>([
    ...options,
    {
      label: 'PCR',
      value: 4,
      disabled: false,
      onChange: (value: string) => {
        console.log(value);
      },
    },
  ]);

  return {
    checkValue,
    companyOptions,
    returnVisitOptions,
  };
};
