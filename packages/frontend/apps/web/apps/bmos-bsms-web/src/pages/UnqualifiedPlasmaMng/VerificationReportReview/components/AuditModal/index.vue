<!-- 审核弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格核查报告审核')"
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
          <div>{{ t('确定后数据将退回至「不合格血浆管理/不合格核查报告送审」') }}</div>
        </template>
      </Alert>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="910" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { Alert, message } from 'ant-design-vue';
  import { Sign } from '@/components/Sign';
  import { unqualifiedPlasmaReportAudit } from '@/services';
  import { BMModalForm } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const signRef = ref();

  const open = ref(false);
  const dialogType = ref<'audit' | 'return'>('audit');
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, showCheck } = useForm();

  const rowData = ref<any>({});

  const openModal = async (rows: any, type: 'audit' | 'return') => {
    showCheck(type);
    rowData.value = rows;
    dialogType.value = type;
    open.value = true;
    await nextTick();
    setFormModels({
      reportBillNoList: rowData.value?.map((row: any) => row.reportBillNo),
      auditResult: type == 'audit' ? 3 : 4,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = { ...formModal };
      // return await unqualifiedPlasmaReportAudit(formModal);
      await signRef.value.openSign(
        rowData.value?.map((row: any) => ({
          reportId: row.id,
          reportName: t('不合格血浆核查报告'),
          reportNo: row.reportBillNo,
        })),
      );
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      // message.success(t('操作成功'));
      // emits('submitSuccess');
      cancel();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await unqualifiedPlasmaReportAudit({
        ...submitObj.value,
        auditSignature: signUrl,
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
