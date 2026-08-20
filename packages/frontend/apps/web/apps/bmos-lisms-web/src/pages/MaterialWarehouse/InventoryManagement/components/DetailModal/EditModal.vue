<!-- 编辑质控品含量 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('编辑')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { editMaterialInventory } from '@/services';
  import { useDict } from '@/stores/dictStore';
  import { BMModalForm, ModalFormInstance, FormProps } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const { getDict } = useDict();

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        label: t('质控品含量'),
        field: 'qualityControlNumerical',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: { label: 'label', value: 'label' },
          request: async () => {
            return await getDict('质控品含量');
          },
        },
      },
    ],
  });

  const openModal = async (row: any) => {
    open.value = true;
    await nextTick();
    modalFormRef.value?.formRef?.setFormModels({
      identify: row.identify,
      qualityControlNumerical: row.qualityControlNumerical,
    });
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        ...formModel,
      };
      await editMaterialInventory(params);
      message.success(t('操作成功'));
      open.value = false;
      emit('submitSuccess');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  defineExpose({
    openModal,
  });
</script>

<style lang="less" scoped>
  :deep(.delete-icon) {
    color: var(--bmos-danger-color);
  }
</style>
