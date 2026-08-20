import {
  getCargoDetailApi,
  reqCargoCategoryQueryTreeWithCargo,
  reqInventoryListByCargoIdAndBatchNo,
  reqPlatformMaterialExtendUnitList,
} from '@/services';
import type { Recordable } from '@bmos/components';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import { SelectProps } from 'ant-design-vue';
import { loopTree } from '../utils';

export type UseMaterialInfoParams = {
  modalFormRef: Ref<any>;
};

export const useCargoInfo = ({ modalFormRef }: UseMaterialInfoParams) => {
  const setCargoOptions = async (_value: number) => {
    try {
      // const { data } = await reqProductMaterialProductTreeReq(value);
      const data: any = [];
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
      unitOptions.value = [];
    }
  };

  const getCargoCategoryQueryTree = async () => {
    try {
      const { data } = await reqCargoCategoryQueryTreeWithCargo();
      return loopTree(data) || [];
    } catch (error) {
      unitOptions.value = [];
      return [];
    }
  };

  // 当前货品信息
  const curCargoInfo = ref<Recordable>({});

  const setCodeAndSpecification = async (value: string) => {
    try {
      if (!value) {
        unitOptions.value = [];
        return;
      }
      const { data } = await getCargoDetailApi({
        id: value,
      });
      curCargoInfo.value = data;
      await getUnitOptions(data.platformMaterialId);
      modalFormRef.value?.formRef?.setFormModels({
        mergeCode: data.mergeCode,
        specification: data.specification,
        singleQuantity: data.singleQuantity,
        singleUnitId: data.unitId,
        singleUnit: data.unit,
      });
    } catch (error) {
      unitOptions.value = [];
    }
  };

  const unitOptions = ref<SelectProps['options']>([]);
  const getUnitOptions = async (platformMaterialId: string, cargoInfo?: Recordable) => {
    try {
      const extendRes = await reqPlatformMaterialExtendUnitList(platformMaterialId);
      (extendRes.data || []).forEach((item: any) => {
        item.unitId = item.id;
        item.name = item.extendUnitName;
      });
      unitOptions.value = [
        {
          unitId: cargoInfo?.unitId || curCargoInfo.value.unitId,
          expression: t('标准单位'),
          name: cargoInfo?.unit || curCargoInfo.value.unit,
          isUnit: true,
        },
        ...extendRes.data,
      ];
      return Promise.resolve(unitOptions.value);
    } catch (error) {
      return Promise.resolve([]);
    }
  };

  const getBatchNumberOptions = debounce(async (value: string, cargoId: string) => {
    try {
      if (!cargoId) throw new Error(t('请选择货品名称'));
      const { data } = await reqInventoryListByCargoIdAndBatchNo(cargoId, value);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'batchNo',
        componentProps: {
          options: data.map((item: any) => {
            return {
              label: item.inventoryBatchNo,
              value: item.inventoryBatchNo,
              ...item,
            };
          }),
        },
      });
    } catch (error) {
      modalFormRef.value?.formRef?.updateSchema({
        field: 'batchNo',
        componentProps: {
          options: [],
        },
      });
    }
  }, 500);

  const setBatchNoAndDateDisable = (flag: boolean = false) => {
    modalFormRef.value?.formRef?.updateSchema([
      {
        field: 'factoryBatchNo',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'produceDate',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'expiredDate',
        componentProps: {
          disabled: flag,
        },
      },
    ]);
  };

  return {
    setCargoOptions,
    getCargoCategoryQueryTree,
    setCodeAndSpecification,
    getBatchNumberOptions,
    setBatchNoAndDateDisable,
    getUnitOptions,
    unitOptions,
    curCargoInfo,
  };
};
