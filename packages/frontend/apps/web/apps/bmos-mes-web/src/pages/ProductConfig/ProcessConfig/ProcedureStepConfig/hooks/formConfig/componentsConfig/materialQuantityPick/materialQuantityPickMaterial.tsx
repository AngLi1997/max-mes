import { FormSchema } from '@bmos/components';
import { ConfigFormProps } from '../../../../types';
import { useCommonMaterialConfig } from '../../hooks';

export type UseMaterialQuantityPickMaterialConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 按物料量领料 分组
export const useMaterialQuantityPickMaterialConfig = ({
  props,
  hasChange,
}: UseMaterialQuantityPickMaterialConfigParams) => {
  const { commonMaterialConfig } = useCommonMaterialConfig({ props, hasChange, multiple: false });
  const materialQuantityPickMaterialConfig = ref<FormSchema[]>([...commonMaterialConfig]);
  return {
    materialQuantityPickMaterialConfig,
  };
};
