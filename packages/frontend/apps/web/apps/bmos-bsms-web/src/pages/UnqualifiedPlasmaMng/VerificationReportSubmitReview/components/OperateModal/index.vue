<!-- 送审和撤销 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="dialogType == 'audit' ? t('不合格核查报告送审') : t('不合格核查报告撤销')"
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
          :scroll="{ x: 800, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="913" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { unqualifiedPlasmaReportSendToAudit, unqualifiedPlasmaReportRevocation } from '@/services';
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
        // await unqualifiedPlasmaReportSendToAudit(data);
        await signRef.value.openSign(
          dataList.value.map((item: any) => {
            return {
              reportId: item.id,
              reportName: t('不合格血浆核查报告'),
              reportNo: item.reportBillNo,
            };
          }),
        );
      } else {
        const data = dataList.value.map((item: any) => item.reportBillNo);
        await unqualifiedPlasmaReportRevocation(data);
        message.success(t('操作成功'));
        emits('submitSuccess');
      }
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await unqualifiedPlasmaReportSendToAudit({
        reportBillNoList: dataList.value.map((item: any) => item.reportBillNo),
        sendToAuditSignature: signUrl,
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
