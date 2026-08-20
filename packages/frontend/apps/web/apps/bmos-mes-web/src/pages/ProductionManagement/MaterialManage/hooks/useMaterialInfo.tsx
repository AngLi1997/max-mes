import { getProductMaterialDetailApi, reqMaterialFieldInfo, reqProductMaterialProductTreeReq } from '@/services';
import { ref } from 'vue';
import { loopTree } from '../utils';
export type UseMaterialInfoParams = {
  modalFormRef: Ref<any>;
};

export const useMaterialInfo = ({ modalFormRef }: UseMaterialInfoParams) => {
  let arrFlag: any = []; //缓存接口所返
  const materialFieldList = ref<any>([]);
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
    }
  };
  // 物料名称改变时回显物料编码及物料规格
  const setCodeAndSpecification = async (value: string) => {
    try {
      if (!value) {
        modalFormRef.value?.formRef?.removeSchemaByFiled(arrFlag);
        materialFieldList.value = [];
        return;
      }
      const { data } = await getProductMaterialDetailApi({
        id: value,
      });
      modalFormRef.value?.formRef?.setFieldsValue({
        mergeCode: data.mergeCode,
        materialSpecification: data.specification,
      });
      // 根据生产物料id查对应自定义字段信息
      const res = await reqMaterialFieldInfo(value);
      modalFormRef.value?.formRef?.removeSchemaByFiled(arrFlag);
      materialFieldList.value = res.data;
      res.data.forEach((item: any, index: any) => {
        modalFormRef.value?.formRef?.appendSchemaByField({
          field: item.field.replace('.', '*'),
          component: 'Input',
          label: item.fieldName,
          defaultValue: item.fieldValue,
          componentProps: {
            disabled: item.fieldType === 'MaterialCustomFields' || item.fieldType === 'MaterialPieceCustomFields',
          },
          colProps:
            (index + 1) % 2 == 1 && index + 1 == res.data?.length
              ? {
                  style: {
                    marginRight: 'auto',
                  },
                }
              : {},
        });
      });
      arrFlag = res.data.map((item: any) => item.field.replace('.', '*'));
    } catch (error) {}
  };

  return {
    materialFieldList,
    setMaterialOptions,
    setCodeAndSpecification,
  };
};
