<!-- 创建/编辑报告 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="actionType === 'create' ? t('创建报告') : t('编辑报告')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge">
    <template #footer>
      <Button @click="cancel">
        {{ t('取消') }}
      </Button>
      <Button @click="save">
        {{ t('保存') }}
      </Button>
      <Button type="primary" @click="submit">
        {{ t('提交') }}
      </Button>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="903" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks/useForm';
  import { Sign } from '@/components/Sign';
  import { message } from 'ant-design-vue';
  import { getQuarantineReport, saveQuarantineReport, submitQuarantineReport } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const signRef = ref();

  const open = ref(false);
  const actionType = ref('create');

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (row: any, type: 'create' | 'edit' = 'create') => {
    actionType.value = type;
    open.value = true;
    try {
      const { data } = await getQuarantineReport(row.checkNo);
      await nextTick();

      setFormModels({
        rowId: row.id,
        overDue: row.overDue,
        ...data,
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = formModal;
      // return await submitQuarantineReport(formModal);
      await signRef.value.openSign([
        {
          reportId: formModal.rowId,
          reportName: t('检疫期核查报告'),
          reportNo: formModal.reportNo,
        },
      ]);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const saveRequest = async (formModal: any) => {
    try {
      return await saveQuarantineReport(formModal);
    } catch (error) {
      return Promise.reject(error);
    }
  };
  // 保存
  const save = async () => {
    try {
      await modalFormRef.value?.submit(saveRequest);
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 提交
  const submit = async () => {
    try {
      await modalFormRef.value?.submit(request);
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await submitQuarantineReport({
        ...submitObj.value,
        createSignatureId: signUrl,
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
