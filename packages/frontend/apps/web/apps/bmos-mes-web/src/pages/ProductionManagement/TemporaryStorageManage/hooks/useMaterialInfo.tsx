import {
  getMesUnitExtendListBoundApi,
  getProductMaterialDetailApi,
  reqProductMaterialProductTreeReq,
} from '@/services';
import type { Recordable } from '@bmos/components';
import { t } from '@bmos/i18n';
import { SelectProps } from 'ant-design-vue';
import { loopTree } from '../utils';

export type UseMaterialInfoParams = {
  modalFormRef: Ref<any>;
};

export const useMaterialInfo = ({ modalFormRef }: UseMaterialInfoParams) => {
  const setMaterialOptions = async (value: number) => {
    try {
      const { data } = await reqProductMaterialProductTreeReq(value);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialId',
        componentProps: {
          treeData: loopTree(data) || [],
        },
      });
    } catch (error) {
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialId',
        componentProps: {
          treeData: [],
        },
      });
      unitOptions.value = []
    }
  };

  // 当前物料信息
  const curMaterialInfo = ref<Recordable>({});

  const setCodeAndSpecification = async (value: string) => {
    try {
      if (!value) {
        unitOptions.value = []
        return;
      }
      const { data } = await getProductMaterialDetailApi({
        id: value,
      });
      modalFormRef.value?.formRef?.setFieldsValue({
        code: data.mergeCode,
        specification: data.specification,
      });
      curMaterialInfo.value = data;
      getUnitOptions(data.id, data.unitId);
    } catch (error) {
      unitOptions.value = []
    }
  };

  const unitOptions = ref<SelectProps['options']>([]);
  const getUnitOptions = async (materialId: string, unitId: string) => {
    try {
      const extendRes = await getMesUnitExtendListBoundApi({materialId});
      (extendRes.data || []).forEach((item: any) => {
        item.unitId = item.id;
        item.name = item.extendUnitName;
      });
      unitOptions.value = [
        {
          unitId: unitId,
          expression: t('标准单位'),
          name: curMaterialInfo.value.unitName,
          isUnit: true,
        },
        ...extendRes.data,
      ];
    } catch (error) {}
  };

  return {
    setMaterialOptions,
    setCodeAndSpecification,
    unitOptions,
    curMaterialInfo,
  };
};
