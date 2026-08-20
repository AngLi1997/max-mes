<!-- 献浆者管理 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :titles="[t('献浆者管理')]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationFirst]"
    :requests="[getBloodDonorList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0="{ instance }">
      <Button
        v-hasAuth="170040010000001"
        type="primary"
        :loading="loading"
        @click="exportExcel(instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel))">
        {{ t('导出') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { getBloodDonorList, plasmaDonorInfoExport } from '@/services';
  import { useTable } from './hooks/useTable';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'PlasmaDonorManagementPage',
    inheritAttrs: false,
  });

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable();

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

  const loading = ref(false);
  const exportExcel = async (data: any) => {
    try {
      loading.value = true;
      const res = await plasmaDonorInfoExport(data);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };
</script>

<style scoped></style>
