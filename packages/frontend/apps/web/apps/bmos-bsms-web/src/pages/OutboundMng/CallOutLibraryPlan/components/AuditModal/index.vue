<!-- 审核弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[dialogType]"
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
          <div>{{ t('确定后数据将退回至「出库管理/出库计划」') }}</div>
        </template>
      </Alert>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { Alert, message } from 'ant-design-vue';
  import { auditOutboundProcess } from '@/services';
  import { BMModalForm } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const open = ref(false);
  const dialogType = ref<'audit' | 'return'>('audit');
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, showCheck } = useForm();

  const titleMap = {
    audit: t('出库审核'),
    return: t('退回审核'),
  };

  const openModal = async (row: any, type: 'audit' | 'return') => {
    showCheck(type);
    dialogType.value = type;
    open.value = true;
    await nextTick();
    setFormModels({
      menuCode: '170100003',
      batchNo: row.batchNo,
      type: row.type?.value,
      auditStatus: type == 'audit' ? 1 : 2,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await auditOutboundProcess(formModal);
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
