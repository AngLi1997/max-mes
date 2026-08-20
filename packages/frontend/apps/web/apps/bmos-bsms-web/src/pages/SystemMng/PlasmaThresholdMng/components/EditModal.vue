<!-- 新增/编辑阈值 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[dialogType]"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useForm } from './hooks/useForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ref } from 'vue';
  import { getPlasmaThresholdById, updatePlasmaThreshold } from '@/services';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const dialogType = ref<'create' | 'edit'>('create');
  const titleMap = {
    create: t('新增'),
    edit: t('编辑'),
  };

  const openModal = async (row?: any, type: 'create' | 'edit' = 'create') => {
    try {
      dialogType.value = type;
      console.log('type', type);
      open.value = true;
      await nextTick();
      if (row?.id) {
        // 编辑，进行数据回显
        const res = await getPlasmaThresholdById(row.id);
        setFormModels({
          ...res.data,
        });
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        ...formModal,
      };
      return await updatePlasmaThreshold(params);
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
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
