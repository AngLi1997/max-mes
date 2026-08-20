<!-- 出库审核弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('出库审核')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit">
    <template v-if="dialogType === 'return'" #formBefore>
      <Alert class="approval-alert" type="warning" showIcon>
        <template #icon>
          <ExclamationCircleOutlined />
        </template>
        <template #message>
          <div>{{ t('是否退回该数据') }}</div>
          <div>{{ t('确定后数据将退回至「标本管理/标本出库计划」') }}</div>
        </template>
      </Alert>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { auditSampleDeliveryPlan } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, showCheck } = useForm();

  const dialogType = ref<'audit' | 'return'>('audit');

  const data = ref<any>({});

  const openModal = async (row: any, type: 'audit' | 'return') => {
    showCheck(type);
    dialogType.value = type;
    data.value = { ...row };
    console.log('data', data.value);
    open.value = true;
    await nextTick();
    setFormModels({
      auditStatus: type === 'audit' ? 1 : 2,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        outPlanBatchNo: data.value?.outPlanBatchNo,
        auditStatus: formModal.auditStatus,
        remark: formModal.remark,
      };

      return await auditSampleDeliveryPlan(params);
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
