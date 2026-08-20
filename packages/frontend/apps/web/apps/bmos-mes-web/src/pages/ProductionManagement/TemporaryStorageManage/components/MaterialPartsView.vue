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
  import { reqStorageMaterialInfoByNo } from '@/services';
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

  const title = ref<string>(t('查看物料件'));

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
        field: 'materialNo',
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
        field: 'container',
        component: 'Input',
        label: t('容器'),
      },
      {
        field: 'materialPositionName',
        component: 'Input',
        label: t('暂存货位'),
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
      {
        field: 'field7',
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
        field: 'productName',
        component: 'Input',
        label: t('预定产品'),
      },
      {
        field: 'batchNo',
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
        try {
          const { data } = await reqStorageMaterialInfoByNo(props.rowData.materialNo);
          modalFormRef.value?.formRef?.setFormModels({
            ...data,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
