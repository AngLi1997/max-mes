<!-- 生成报告 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('生成报告')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
  <Sign ref="signRef" :signatureAction="901" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { Sign } from '@/components/Sign';
  import { message } from 'ant-design-vue';
  import { createInspectionReportManagement } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const signRef = ref();

  const rowData = ref<any>({});

  const { modalFormRef, formProps } = useForm();

  const openModal = async (row: any) => {
    rowData.value = row;
    open.value = true;
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = {
        id: rowData.value.id,
        ...formModal,
      };
      // return await createInspectionReportManagement(params);
      await signRef.value.openSign([
        {
          reportId: rowData.value.id,
          reportName: t('血浆检验报告'),
          reportNo: formModal.reportNo,
        },
      ]);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await createInspectionReportManagement({
        ...submitObj.value,
        reportSignature: signUrl,
      });
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
