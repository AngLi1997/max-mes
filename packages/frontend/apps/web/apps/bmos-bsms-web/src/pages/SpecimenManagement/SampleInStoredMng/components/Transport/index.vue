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
  import { getWaitInStorageTransport } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();

  const dataList = ref<any>([]);

  const openModal = async (rows: any) => {
    dataList.value = rows.map((item: any) => item.syncBatchNo);
    open.value = true;
    await nextTick();
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const approvalParams = {
        syncBatchNoList: dataList.value,
        stopTime: formModal.stopTime,
      };

      return await getWaitInStorageTransport(approvalParams);
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
