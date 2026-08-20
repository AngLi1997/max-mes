<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge">
    <template #footer>
      <Button type="primary" @click="cancelModal">{{ t('确定') }}</Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { reqInventoryQueryInventoryBatchById } from '@/services';
  import { BMModalForm, FormProps, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      treeData?: any[];
      currentNodes: any;
      isView?: boolean;
    }>(),
    {
      rowData: () => ({}),
      treeData: () => [],
      currentNodes: () => [],
      isView: false,
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

  const title = ref<string>(t('查看货品批次'));

  const cancelModal = () => {
    open.value = false;
  };

  const modalFormRef = ref<ModalFormInstance>();
  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 12,
    },
    disabled: true,
    schemas: [
      {
        field: 'cargoName',
        component: 'Input',
        label: t('货品名称'),
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('货品编码'),
      },
      {
        field: 'inventoryBatchNo',
        component: 'Input',
        label: t('货品批号'),
      },
      {
        field: 'availableSize',
        component: 'Input',
        label: t('结存件数'),
      },
      {
        field: 'availableQuantity',
        component: 'Input',
        label: t('可用量'),
      },
      {
        field: 'reserveQuantity',
        component: 'Input',
        label: t('预定量'),
      },
      {
        field: 'unit',
        component: 'Input',
        label: t('单位'),
      },
      {
        field: 'factoryBatchNo',
        component: 'Input',
        label: t('原厂批号'),
      },
      {
        field: 'produceDate',
        component: 'DatePicker',
        label: t('生产日期'),
      },
      {
        field: 'expiredDate',
        component: 'DatePicker',
        label: t('有效期至'),
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
        field: 'supplier',
        component: 'Input',
        label: t('供应商'),
      },
      {
        field: 'producer',
        component: 'Input',
        label: t('生产商'),
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
          const { data } = await reqInventoryQueryInventoryBatchById(props.rowData?.id as string);
          modalFormRef.value?.formRef?.setFieldsValue({
            ...data,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
