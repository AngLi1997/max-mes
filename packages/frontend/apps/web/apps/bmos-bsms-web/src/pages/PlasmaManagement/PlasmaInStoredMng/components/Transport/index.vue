<!-- 运输弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('获取运输信息')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { getPlasmaToStorageTransport } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const props = defineProps({
    rows: {
      type: Array<any>,
      default: () => [],
    },
  });

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();

  const openModal = async () => {
    open.value = true;
    await nextTick();
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const approvalParams = {
        batchNoList: props.rows.map((row: any) => row.syncBatchNo),
        stopTime: formModal.stopTime,
      };

      return await getPlasmaToStorageTransport(approvalParams);
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
