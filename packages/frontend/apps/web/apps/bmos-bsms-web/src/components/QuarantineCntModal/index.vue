<!-- 检疫期数据详情列表 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[modalType]"
    wrapClassName="modalSizeLarge"
    :cancelButtonText="t('关闭')"
    :showOkButton="false">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :search="true"
          :data-request="loadData"
          :columns="modalType == 2 ? columns2 : columns1"
          :formProps="formProps"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall">
          <template #toolbar>
            <Button type="primary" :loading="loading" @click="exportExcel">{{ t('导出') }}</Button>
          </template>
        </BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks/useTable';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { getCheckQueryDetailList, exportCheckQueryDetail } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    inheritAttrs: false,
  });

  const open = ref(false);

  const modalFormRef = ref();
  const { tableRef, columns1, columns2, formProps } = useTable();

  const modalType = ref<1 | 2 | 3>(1); // 1:：合格 2：不合格 3：未通过

  const titleMap = ref({
    1: t('检疫期合格数据'),
    2: t('检疫期不合格数据'),
    3: t('检疫期未通过数据'),
  });

  const rowData = ref<any>({});

  const openModal = async (row: any, type: 1 | 2 | 3) => {
    rowData.value = row;
    modalType.value = type;
    open.value = true;
  };

  const loadData = async (params: any) => {
    return await getCheckQueryDetailList({
      ...params,
      plasmaStatus: modalType.value,
      checkNo: rowData.value.checkNo,
    });
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

  const loading = ref(false);
  const exportExcel = async () => {
    try {
      loading.value = true;
      const params = {
        ...tableRef.value?.getQueryFormRef().formModel,
        plasmaStatus: modalType.value,
        checkNo: rowData.value.checkNo,
      };
      const res = await exportCheckQueryDetail(params);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
