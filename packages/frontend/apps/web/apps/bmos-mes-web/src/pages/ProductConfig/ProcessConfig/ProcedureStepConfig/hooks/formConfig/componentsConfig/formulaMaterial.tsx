import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../types';
import { useCommonMaterialConfig } from '../hooks';

export type UseFormulaMaterialConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
export const useFormulaMaterialConfig = ({ props, hasChange }: UseFormulaMaterialConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const formulaMaterialConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    formulaMaterialConfig,
  };
};
