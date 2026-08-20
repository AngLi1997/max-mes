<!-- 审核弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('放行单审核')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="dataList"
          :columns="columns"
          auto-height
          row-key="id"
          :show-tool-bar="false"
          :scroll="{ x: 800, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="906" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { auditQualityGuaranteeRelease } from '@/services';
  import { Sign } from '@/components/Sign';
  import { BMModalForm, BMTable } from '@bmos/components';

  const signRef = ref();

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref([]);

  const openModal = async (rows: any, type: 'audit' | 'return') => {
    dataList.value = rows;
    open.value = true;
    await nextTick();
    setFormModels({
      auditResult: type == 'audit' ? 1 : 2,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = {
        ids: dataList.value?.map((item: any) => item.noteId),
        auditResult: formModal.auditResult,
        remark: formModal.remark,
      };

      // return await auditQualityGuaranteeRelease(params);
      await signRef.value.openSign(
        dataList.value.map((item: any) => ({
          reportId: item.noteId,
          reportName: t('原料血浆放行审核单'),
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
      await auditQualityGuaranteeRelease({
        ...submitObj.value,
        signature: signUrl,
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
