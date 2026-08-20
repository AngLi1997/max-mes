<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('查看物料件')"
    :formProps="formProps"
    :show-cancel-button="false"
    wrapClassName="modalSizeLarge"
    @okModal="open = false"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps } from '@bmos/components';
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
      rowData: () => ({}),
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

  const modalFormRef = ref<any>();

  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 12,
    },
    disabled: true,
    schemas: [
      {
        field: 'cargoInfo',
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
        field: 'materialBatchNo',
        component: 'Input',
        label: t('物料批号'),
      },
      {
        field: 'storageMaterialNo',
        component: 'Input',
        label: t('物料件号'),
      },
      {
        field: 'initQuantity',
        component: 'Input',
        label: t('初始量'),
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
        field: 'consumeQuantity',
        component: 'Input',
        label: t('消耗量'),
      },
      {
        field: 'unit',
        component: 'Input',
        label: t('单位'),
      },
      {
        field: 'containerName',
        component: 'Input',
        label: t('容器'),
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
      {
        field: 'inboundInfo',
        component: 'Divider',
        label: t('预定信息'),
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
        field: 'reserveProductName',
        component: 'Input',
        label: t('预定产品'),
      },
      {
        field: 'reserveBatchNo',
        component: 'Input',
        label: t('预定批次'),
      },
      {
        field: 'reserveUserName',
        component: 'Input',
        label: t('预定人员'),
      },
      {
        field: 'reserveTime',
        component: 'Input',
        label: t('预定时间'),
      },
    ],
  });
  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        modalFormRef.value?.formRef?.setFieldsValue({
          ...props.rowData,
        });
      }
    },
  );
</script>
