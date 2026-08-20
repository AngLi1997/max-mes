<!-- 审核/退回弹窗 -->
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
          <div>{{ t('确定后数据将退回至「检疫期管理/检疫期报告送审」') }}</div>
        </template>
      </Alert>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="904" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { Sign } from '@/components/Sign';
  import { Alert, message } from 'ant-design-vue';
  import { auditQuarantineReportAudit } from '@/services';
  import { BMModalForm } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const signRef = ref();

  const open = ref(false);
  const dialogType = ref<'audit' | 'return'>('audit');
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, showCheck } = useForm();

  const titleMap = {
    audit: t('出库审核'),
    return: t('退回审核'),
  };

  const rowList = ref<string[]>([]);

  const openModal = async (rows: any, type: 'audit' | 'return') => {
    rowList.value = rows;
    showCheck(type);
    dialogType.value = type;
    open.value = true;
    await nextTick();
    setFormModels({
      auditStatus: type == 'audit' ? 3 : 4,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = {
        ids: rowList.value.map((item: any) => item.id),
        ...formModal,
      };

      // return await auditQuarantineReportAudit(params);
      await signRef.value.openSign(
        rowList.value.map((item: any) => ({
          reportId: item.id,
          reportName: t('检疫期核查报告'),
          reportNo: item.reportNo,
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
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await auditQuarantineReportAudit({
        ...submitObj.value,
        auditSignatureId: signUrl,
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
