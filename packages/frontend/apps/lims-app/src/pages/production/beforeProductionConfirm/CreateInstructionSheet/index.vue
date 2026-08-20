<template>
  <BMLayout>
    <BMBasicPage
      :title="t('创建指令单')"
      :default-padding="false"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirm"
    >
      <BMForm
        ref="formRef"
        v-bind="formProps"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  getDetailByProcessByVersion,
  getDirectlyCreateBuildNo,
  getListByProcessVersion,
  getProductTreeApi,
  productionDirectlyCreate,
  reqProcessListAll,
} from '@/api';
import {
  BMBasicPage,
  BMForm,
  BMLayout,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();

const formRef = ref();

const toBack = () => {
  uni.navigateBack();
};
const confirm = async () => {
  // 表单校验
  formRef.value?.submit();
  const params = await formRef.value?.validate();
  try {
    await productionDirectlyCreate({ ...params });
    showNotify({ type: 'success', message: t('创建成功') });
    toBack();
  }
  catch (error) {
    error.message && showNotify({ type: 'warning', message: error.message });
  }
};

const getChildrenData = (arr) => {
  const newArr = [];
  arr.forEach((item) => {
    item.categoryFlag = !item.categoryFlag;
    if (item.children.length > 0) {
      item.children = getChildrenData(item.children);
    }
    newArr.push(item);
  });
  return newArr;
};

// 获取指令单编号和生产批号
const getDirectlyCreate = async (formData) => {
  const { processId, processVersion, productPlanType, productionLineId } = formData;
  if (!processId || !processVersion || !productPlanType || !productionLineId) {
    return;
  }
  const { data } = await getDirectlyCreateBuildNo({
    processId,
    processVersion,
    productPlanType,
    productionLineId,
  });
  formData.planNo = data.planNo;
  formData.batchNo = data.batchNo;
  formData.planNoCode = data.planNoCode;
  formData.batchNoCode = data.batchNoCode;
};

// 获取批量和单位
const getDetailByProcess = async (formData) => {
  const { processId, processVersion } = formData;
  const { data } = await getDetailByProcessByVersion({
    processId,
    processVersion,
  });
  formData.batchQuantity = data?.batchQuantity;
  formData.unitName = data?.unitName;
  formData.unitId = data?.unitId;
};

// 表单配置
const formProps = reactive({
  schemas: [
    {
      field: 'formTitle1',
      component: 'FormTitle',
      label: t('生产信息'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'productId',
      component: 'BMFormSelect',
      required: true,
      label: t('产品信息'),
      colProps: {
        span: 12,
      },
      componentProps: ({ formModel, formInstance }) => {
        return {
          request: async () => {
            const { data } = await getProductTreeApi({ categoryType: 2 });
            return getChildrenData(data);
          },
          title: t('产品信息'),
          type: 'tree',
          fieldNames: {
            name: 'showName',
            key: 'id',
            checkKey: 'categoryFlag',
            checkKeyValue: true,
            parentId: 'parentId',
            children: 'children',
          },
          treeData: [],
          onConfirm: async (product) => {
            // 获取所属工艺
            const { data } = await reqProcessListAll({
              productId: product.id,
              active: true,
            });
            formInstance.updateSchema({
              field: 'processId',
              componentProps: {
                options: data.map((item) => {
                  item.showName = `${item.name}-${item.activeVersion}`;
                  return item;
                }),
              },
            });
          },
          onClear: () => {
            formModel.processId = '';
            formModel.productionLineId = '';
            // 删除工艺下拉框
            formInstance.updateSchema({
              field: 'processId',
              componentProps: {
                options: [],
              },
            });
            // 删除产线下拉
            formInstance.updateSchema({
              field: 'productionLineId',
              componentProps: {
                options: [],
              },
            });
          },
          onChange: () => {
            formModel.processId = '';
            formModel.productionLineId = '';
            formModel.planNo = '';
            formModel.batchNo = '';
            formModel.batchQuantity = '';
            formModel.unitName = '';
          },
        };
      },
    },
    {
      field: 'processId',
      component: 'BMFormSelect',
      label: t('生产工艺'),
      colProps: {
        span: 12,
      },
      required: true,
      componentProps: ({ formModel, formInstance }) => {
        return {
          placeholder: t('请选择所属工艺'),
          options: [],
          title: t('所属工艺'),
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          onConfirm: async ({ id, activeVersion }) => {
            const { data } = await getListByProcessVersion({
              id,
              version: activeVersion,
            });
            formModel.processVersion = activeVersion;
            // 获取指令单编号和生产批号
            getDirectlyCreate(formModel);
            // 获取批量和单位
            getDetailByProcess(formModel);
            const options = data.map((item) => {
              item.showName = `${item.code}-${item.name}`;
              return item;
            });
            formInstance.updateSchema({
              field: 'productionLineId',
              componentProps: {
                options: [...options],
              },
            });
          },
          onChange: async () => {
            formModel.productionLineId = '';
            formInstance.updateSchema({
              field: 'productionLineId',
              componentProps: {
                options: [],
              },
            });
          },
          onClear: () => {
            formModel.productionLineId = '';
            formModel.planNo = '';
            formModel.batchNo = '';
            formModel.batchQuantity = '';
            formModel.unitName = '';
          },
        };
      },
    },
    {
      field: 'productionLineId',
      component: 'BMFormSelect',
      label: t('产线信息'),
      colProps: {
        span: 12,
      },
      required: true,
      componentProps: ({ formModel }) => {
        return {
          title: t('产线名称'),
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          onConfirm: async () => {
            // 获取指令单编号和生产批号
            getDirectlyCreate(formModel);
          },
          onClear: () => {
            formModel.planNo = '';
            formModel.batchNo = '';
          },
        };
      },
    },
    {
      field: 'productPlanType',
      component: 'BMFormSelect',
      label: t('指令单类型'),
      defaultValue: 'PRODUCT',
      required: true,
      colProps: {
        span: 12,
      },
      componentProps: ({ formModel }) => {
        return {
          title: t('指令单类型'),
          placeholder: t('请选择指令单类型'),
          options: [
            {
              label: t('生产批次'),
              value: 'PRODUCT',
            },
            {
              label: t('实验批次'),
              value: 'EXPERIMENT',
            },
            {
              label: t('验证批次'),
              value: 'VERIFY',
            },
          ],
          onConfirm: async () => {
            // 获取指令单编号和生产批号
            getDirectlyCreate(formModel);
          },
          onClear: () => {
            formModel.planNo = '';
            formModel.batchNo = '';
          },
        };
      },
    },
    {
      field: 'planNo',
      component: 'Input',
      label: t('指令单编号'),
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'batchNo',
      component: 'Input',
      label: t('生产批号'),
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'batchQuantity',
      component: 'Input',
      label: t('生产批量'),
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'unitName',
      component: 'Input',
      label: t('批量单位'),
      required: true,
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
  ],
});
</script>

<style lang="scss" scoped>
  :deep(.bm-form) {
  width: 98.1% !important;
}
</style>
