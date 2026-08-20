<!-- 新增分拣计划 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('计划信息')"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { getSortingPlanManage, createSortingPlan } from '@/services';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useModalForm();

  const openModal = async (type: 1 | 2) => {
    open.value = true;
    await nextTick();
    try {
      const { data } = await getSortingPlanManage({ itemType: type });
      modalFormRef.value?.formRef?.updateSchema({
        field: 'sortingTypeName',
        componentProps: {
          options: data,
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }

    setFormModels({
      itemType: type,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        ...formModal,
      };

      return await createSortingPlan(params);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      const batchNo = formModal?.batchNo;
      await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess', batchNo);
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
