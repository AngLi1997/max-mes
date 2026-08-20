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
  import { BMModalForm, FormProps, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reqInventoryQueryInventoryById } from '@/services';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      treeData?: any[];
      currentNodes: any;
    }>(),
    {
      rowData: () => ({}),
      treeData: () => [],
      currentNodes: () => [],
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

  const title = ref<string>(t('查看货品件'));

  const handleOk = () => {
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
        field: 'batchNo',
        component: 'Input',
        label: t('货品批号'),
      },
      {
        field: 'inventoryNo',
        component: 'Input',
        label: t('货品件号'),
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
        field: 'position',
        component: 'Input',
        label: t('暂存货位'),
        colProps: {
          style: {
            marginRight: 'auto',
          },
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
          const { data } = await reqInventoryQueryInventoryById(props.rowData?.id);
          modalFormRef.value?.formRef?.setFormModels({
            ...data,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
