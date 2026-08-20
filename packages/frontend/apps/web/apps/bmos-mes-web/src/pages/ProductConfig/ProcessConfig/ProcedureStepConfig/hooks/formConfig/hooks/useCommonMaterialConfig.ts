import { reqProductFormulaMaterialListByProcedureId } from '@/services';
import { FormSchema } from '@bmos/components';
import { t } from '@bmos/i18n';
import { ConfigFormProps } from '../../../types';

export type UseCommonMaterialConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
  showMaterialTitle?: boolean;
  materialType?: number | undefined;
  multiple?: boolean;
  label?: string;
};
// 配料计划
export const useCommonMaterialConfig = ({ props, hasChange, materialType, multiple = true, showMaterialTitle = false, label = t('生产BOM物料') }: UseCommonMaterialConfigParams) => {
  const commonMaterialConfig: FormSchema[] = [
    {
      field: 'materialConfig',
      label: t('物料信息'),
      component: 'TableTitle',
      vIf: showMaterialTitle,
    },
    {
      field: multiple ? 'formulaMaterialIds' : 'formulaMaterialId',
      component: 'Select',
      label: label,
      componentProps: () => {
        return {
          ...(multiple ? { mode: 'multiple' } : {}), // (mode: 'multiple') if multiple is true
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          request: async () => {
            try {
              const { data } = await reqProductFormulaMaterialListByProcedureId(props.procedureId, materialType);
              return data.map((item: any) => ({
                label: item.materialMergeCode + '-' + item.materialName,
                value: item.id,
              }));
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
  ];
  return {
    commonMaterialConfig,
  };
};
