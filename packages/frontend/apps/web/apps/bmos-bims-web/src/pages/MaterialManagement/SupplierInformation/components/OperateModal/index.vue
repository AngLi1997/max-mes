<!-- 新增/编辑弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[dialogType]"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { getSupplierDetail, createSupplier, updateSupplier } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);
  const dialogType = ref<'create' | 'update'>('create');
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const titleMap = {
    create: t('新增'),
    update: t('编辑'),
  };

  const openModal = async (row: any, type: 'create' | 'update') => {
    dialogType.value = type;
    open.value = true;
    await nextTick();
    let formData = {};
    if (type === 'update') {
      const { data } = await getSupplierDetail(row.id);
      formData = {
        ...data,
        type: data.type?.value,
      };
      setFormModels(formData);
    }
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      if (dialogType.value === 'create') {
        return await createSupplier(formModal);
      } else {
        return await updateSupplier(formModal);
      }
      // return await auditOutboundProcess(formModal);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
