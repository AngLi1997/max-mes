<!-- 计划申请 / 更改出库批次 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="type == 'apply' ? t('计划申请') : t('更改出库批次')"
    :formProps="formProps"
    :submit="submit">
    <template v-if="type == 'apply'" #formBefore>
      <div>
        {{ t('是否提交该计划申请') }}
        <br />
        {{ t('确定后数据将提交至「标本管理/标本出库审核」') }}
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { attentionSampleDeliveryPlan, updateSampleDeliveryPlanBatchNo } from '@/services';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, type, changeType } = useModalForm();

  const openModal = async (row: any, type: 'apply' | 'editNo') => {
    changeType(type);
    open.value = true;
    await nextTick();

    if (type == 'apply') {
      setFormModels({
        outPlanBatchNo: row.outPlanBatchNo,
      });
    } else {
      setFormModels({
        oldNo: row.outPlanBatchNo,
        newNo: row.outPlanBatchNo,
      });
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
      if (type.value === 'apply') {
        return await attentionSampleDeliveryPlan(params);
      } else {
        return await updateSampleDeliveryPlanBatchNo(params);
      }
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
