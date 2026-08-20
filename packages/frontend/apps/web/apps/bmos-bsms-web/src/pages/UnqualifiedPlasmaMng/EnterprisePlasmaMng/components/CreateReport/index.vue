<!-- 创建/编辑报告 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="actionType === 'create' ? t('创建不合格核查报告') : t('编辑不合格核查报告')"
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
  <Sign ref="signRef" :signatureAction="909" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks/useForm';
  import { Button, message } from 'ant-design-vue';
  import { Sign } from '@/components/Sign';
  import {
    generateUnqualifiedPlasmaReportNo,
    getUnqualifiedPlasmaReportInfo,
    unqualifiedPlasmaReportEdit,
    unqualifiedPlasmaReportSave,
  } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const signRef = ref();

  const open = ref(false);
  const actionType = ref('create');

  const apiCmp = computed(() => {
    return actionType.value === 'create' ? unqualifiedPlasmaReportSave : unqualifiedPlasmaReportEdit;
  });

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const rowData = ref<any>({});

  const openModal = async (row: any, type: 'create' | 'edit' = 'create') => {
    actionType.value = type;
    rowData.value = row;
    open.value = true;
    try {
      const { data } = await generateUnqualifiedPlasmaReportNo();
      await nextTick();

      if (type === 'edit') {
        const { data: tempData } = await getUnqualifiedPlasmaReportInfo(row.id);
        setFormModels({
          unqualifiedPlasmaInfoId: row.id,
          reportBillNo: row.reportNo,
          ...tempData,
        });
        return;
      }
      setFormModels({
        unqualifiedPlasmaInfoId: row.id,
        reportBillNo: data,
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
      submitObj.value = {
        ...formModal,
        draftFlag: 0,
      };
      // return await submitQuarantineReport({
      //   ...formModal,
      //   draftFlag: 0,
      // });
      await signRef.value.openSign([
        {
          reportId: rowData.value.id,
          reportName: t('不合格血浆核查记录'),
          reportNo: rowData.value.reportNo,
        },
      ]);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const saveRequest = async (formModal: any) => {
    try {
      return await apiCmp.value({
        ...formModal,
        draftFlag: 1,
      });
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
      // message.success(t('操作成功'));
      // emits('submitSuccess');
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await apiCmp.value({
        ...submitObj.value,
        createSignature: signUrl,
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
