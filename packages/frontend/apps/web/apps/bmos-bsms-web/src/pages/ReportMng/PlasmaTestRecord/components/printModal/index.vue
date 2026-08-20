<!-- 打印血浆检测记录表 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('打印血浆检测记录表')"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { printPlasmaTestRecord } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useModalForm();

  const openModal = async () => {
    open.value = true;
  };

  const cancel = () => {
    open.value = false;
  };

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const exportExcel = async (formModal: any) => {
    const res = await printPlasmaTestRecord(formModal);
    let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
    // 文件名解码
    fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
    downloadFn(res.data, fileName);
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        ...formModal,
      };

      return await exportExcel(params);
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
