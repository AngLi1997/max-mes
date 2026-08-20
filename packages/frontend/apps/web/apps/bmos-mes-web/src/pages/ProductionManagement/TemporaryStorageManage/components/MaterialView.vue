<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge">
    <template #footer>
      <Button type="primary" @click="handleOk">{{ t('确定') }}</Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { MaterialTypeMap } from '@/pages/ProductionMaterials/PageComponentNew/const';
  import { reqStorageMaterialBatchBatchDetail } from '@/services';
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
  import { t } from '@bmos/i18n';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
    }>(),
    {
      rowData: {},
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('查看物料批次'));

  const handleOk = () => {
    open.value = false;
  };

  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    baseColProps: {
      span: 12,
    },
    disabled: true,
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('物料信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'categoryType',
        component: 'Select',
        label: t('物料类型'),
        required: true,
        componentProps: () => {
          return {
            options: [
              { label: t('原辅包'), value: 0 },
              { label: t('中间品'), value: 1 },
            ],
          };
        },
      },
      {
        field: 'materialName',
        component: 'Input',
        label: t('物料名称'),
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('物料编码'),
      },
      {
        field: 'materialSpecification',
        component: 'Input',
        label: t('物料规格'),
      },
      {
        field: 'materialBatchNo',
        component: 'Input',
        label: t('物料批号'),
      },
      {
        field: 'size',
        component: 'Input',
        label: t('结存件数'),
      },
      {
        field: 'reserveQuantity',
        component: 'Input',
        label: t('预定量'),
      },
      {
        field: 'availableQuantity',
        component: 'Input',
        label: t('可用量'),
      },
      {
        field: 'unit',
        component: 'Input',
        label: t('单位'),
      },
      {
        field: 'qualityStatus',
        component: 'Select',
        label: t('质量状态'),
        defaultValue: 'QUARANTINE',
        componentProps: () => {
          return {
            options: [
              { label: t('待验'), value: 'QUARANTINE' },
              { label: t('合格'), value: 'QUALIFIED' },
              { label: t('不合格'), value: 'UNQUALIFIED' },
              { label: t('已取样'), value: 'SAMPLED' },
              { label: t('限制性放行'), value: 'RESTRICTED_RELEASE' },
            ],
          };
        },
      },
      {
        field: 'expiredDate',
        component: 'Input',
        label: t('有效期至'),
      },
      {
        field: 'productDate',
        component: 'Input',
        label: t('生产日期'),
      },
      {
        field: 'hydration',
        component: 'Input',
        label: t('水分') + '(%)',
      },
      {
        field: 'noHydrationContent',
        component: 'Input',
        label: t('含量') + '(%)',
      },
      {
        field: 'reportNo',
        component: 'Input',
        label: t('报告单编号'),
      },
      {
        field: 'licenceNo',
        component: 'Input',
        label: t('放行单编号'),
      },
      {
        field: 'originalBatchNo',
        component: 'Input',
        label: t('原始编码'),
      },
      {
        field: 'factoryBatchNo',
        component: 'Input',
        label: t('原厂批号'),
      },
      {
        field: 'expandInfo.supplier',
        component: 'Input',
        label: t('供应商'),
        vIf: ({ formModel }: RenderCallbackParams) => formModel.categoryType === MaterialTypeMap.RawMaterial,
      },
      {
        field: 'expandInfo.producer',
        component: 'Input',
        label: t('生产商'),
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
      {
        field: 'expandInfo.level',
        component: 'Input',
        label: t('级别'),
        vIf: ({ formModel }: RenderCallbackParams) => formModel.categoryType === MaterialTypeMap.RawMaterial,
      },
      {
        field: 'expandInfo.formulation',
        component: 'Input',
        label: t('剂型'),
        vIf: ({ formModel }: RenderCallbackParams) => formModel.categoryType === MaterialTypeMap.RawMaterial,
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
      {
        field: 'field7',
        component: 'Divider',
        label: t('扩展信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        try {
          const { data } = await reqStorageMaterialBatchBatchDetail(props.rowData?.id);
          data?.fieldList?.forEach((item: any, index: any) => {
            modalFormRef.value?.formRef?.appendSchemaByField({
              field: item.field,
              component: 'Input',
              label: item.fieldName,
              defaultValue: item.fieldValue,
              colProps:
                (index + 1) % 2 == 1 && index + 1 == data?.fieldList?.length
                  ? {
                      style: {
                        marginRight: 'auto',
                      },
                    }
                  : {},
            });
          });
          modalFormRef.value?.formRef?.setFormModels({
            ...props.rowData,
            ...data,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
