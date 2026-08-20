<!-- 审核 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('审核')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="dataList"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="902" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { Sign } from '@/components/Sign';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { auditInspectionReportManagement } from '@/services';

  // const props = defineProps({
  //   rows: {
  //     type: Array<any>,
  //     default: () => [],
  //   },
  // });

  const signRef = ref();

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const dataList = ref<any>([]);

  const { modalFormRef, formProps, setFormModels } = useForm();
  const { tableRef, columns } = useTable();

  const openModal = async (rows: any, status: number) => {
    dataList.value = JSON.parse(JSON.stringify(rows));
    open.value = true;
    await nextTick();
    // console.log('props.rows', props.rows);
    setFormModels({
      auditResult: status,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const submitObj = ref<any>({});

  const request = async (formModal: any) => {
    try {
      submitObj.value = {
        ids: dataList.value.map((item: any) => item.id),
        ...formModal,
      };
      // return await auditInspectionReportManagement(params);
      await signRef.value.openSign(
        dataList.value.map((item: any) => ({
          reportId: item.id,
          reportName: t('血浆检验报告'),
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

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await auditInspectionReportManagement({
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
