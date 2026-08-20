<!-- 送审和撤销 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="dialogType == 'audit' ? t('检疫期报告送审') : t('检疫期报告撤销')"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          :show-tool-bar="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="912" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { submitQuarantineReportAudit, cancelQuarantineReportAudit } from '@/services';
  import { t } from '@bmos/i18n';
  import { Sign } from '@/components/Sign';
  import { useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const signRef = ref();

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();

  const dataList = ref<any>([]);
  const dialogType = ref<'audit' | 'cancel'>('audit');

  const openModal = async (rows: any, type: 'audit' | 'cancel') => {
    dataList.value = rows;
    dialogType.value = type;
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (): Promise<any> => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: [...dataList.value],
          total: dataList.value?.length || 0,
        });
      }, 100);
    });
  };

  const cancel = () => {
    open.value = false;
  };

  // 提交
  const submit = async () => {
    try {
      if (dialogType.value === 'audit') {
        cancel();
        await signRef.value.openSign(
          dataList.value.map((item: any) => ({
            reportId: item.id,
            reportName: t('检疫期核查报告'),
            reportNo: item.reportNo,
          })),
        );
      } else {
        const data = {
          ids: dataList.value.map((item: any) => item.id),
        };
        await cancelQuarantineReportAudit(data);
        message.success(t('操作成功'));
        emits('submitSuccess');
        cancel();
      }
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      const data = {
        ids: dataList.value.map((item: any) => item.id),
        sendSignatureId: signUrl,
      };
      await submitQuarantineReportAudit(data);
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
