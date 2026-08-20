<!-- 审核弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[dialogType]"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
  <Sign ref="signRef" :signatureAction="908" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { Sign } from '@/components/Sign';
  import { message } from 'ant-design-vue';
  import { unqualifiedCheckRecordAudit } from '@/services';
  import { BMModalForm } from '@bmos/components';

  // ----------------签名相关-----------------
  const signRef = ref();

  const open = ref(false);
  const dialogType = ref<'audit' | 'return'>('audit');
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const titleMap = {
    audit: t('出库审核'),
    return: t('退回审核'),
  };

  const rowList = ref<any[]>([]);

  const openModal = async (rows: any, type: 'audit' | 'return') => {
    dialogType.value = type;
    rowList.value = rows;
    open.value = true;
    await nextTick();
    setFormModels({
      ids: rows?.map((item: any) => item.id) ?? [],
      auditStatus: type == 'audit' ? 1 : 2,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = {
        ...formModal,
      };

      // return await unqualifiedCheckRecordAudit(formModal);
      await signRef.value.openSign(
        rowList.value.map((item: any) => ({
          reportId: item.id,
          reportName: t('不合格血浆核查记录'),
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
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await unqualifiedCheckRecordAudit({
        ...submitObj.value,
        auditSignature: signUrl,
      });
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
