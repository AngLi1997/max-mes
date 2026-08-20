<!-- 计划申请/更改出库批次 弹窗-->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="actionType == 'apply' ? t('计划申请') : t('更改出库批次')"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { applyDeliveryPlan, editDeliveryPlanLotNo } from '@/services';

  const open = ref(false);
  const actionType = ref<'apply' | 'change'>('apply');

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, changeSchema } = useModalForm();

  const openModal = async (row: any, type: 'apply' | 'change') => {
    changeSchema(type);
    actionType.value = type;
    open.value = true;
    await nextTick();
    let formData: any = {
      id: row.id,
    };
    if (type === 'change') {
      const [outType, batchNo] = row.batchNo.split(/(\d+)/);
      formData = {
        ...formData,
        outType,
        batchNo,
      };
    }
    setFormModels(formData);
  };

  const cancel = () => {
    open.value = false;
    setFormModels({
      id: undefined,
      outType: undefined,
      batchNo: undefined,
      remark: undefined,
    });
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        id: formModal.id,
        batchNo: formModal.batchNo ? `${formModal.outType}${formModal.batchNo}` : undefined,
        remark: formModal.remark,
      };

      if (actionType.value === 'change') {
        return await editDeliveryPlanLotNo(params);
      } else {
        return await applyDeliveryPlan(params);
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
