<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('关键控制点检查')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
    :submit="validate"
    @cancelModal="closeModal">
    <template #formBefore>
      <div>
        <BMTable
          :search="false"
          :data-source="beforeList"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :showHeader="false"
          :scroll="{ x: 700, y: 400 }"
          bordered
          :pagination="false"
          :showRefresh="false">
          <template #headerTitle>
            <BMTableTitle :title="t('检测前关键控制点')" />
          </template>
        </BMTable>
        <BMTable
          :search="false"
          :data-source="inProgressList"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :showHeader="false"
          :scroll="{ x: 700, y: 400 }"
          bordered
          :pagination="false"
          :showRefresh="false">
          <template #headerTitle>
            <BMTableTitle :title="t('检测中关键控制点')" />
          </template>
        </BMTable>
        <BMTable
          :search="false"
          :data-source="afterList"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :showHeader="false"
          :scroll="{ x: 700, y: 400 }"
          bordered
          :pagination="false"
          :showRefresh="false">
          <template #headerTitle>
            <BMTableTitle :title="t('检测后关键控制点')" />
          </template>
        </BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="1010" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable, useForm, useCtrl } from './hooks';
  import { Sign } from '@/components/Sign';
  import { checkReport } from '@/services';
  import { BMModalForm, BMTableTitle, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { useWarn } from '@/hooks';
  import { YesOrNoEnum } from '@/types';

  const emits = defineEmits(['submitSuccess']);

  const signRef = ref<InstanceType<typeof Sign>>();

  const open = ref(false);

  const { modalFormRef, formProps } = useForm();
  const { columns } = useTable();
  const { beforeList, inProgressList, afterList, initList, handleChecked } = useCtrl();
  const record = ref<any>({});

  const openModal = async (row: any) => {
    record.value = row;
    open.value = true;
  };

  const submitParams = ref<any>({});

  const { warnModal } = useWarn();

  const validate = async (formModel: any) => {
    try {
      const checkRecordDetail = handleChecked();
      if (formModel.checkResult === 'RESULT_PASS') {
        for (let item in checkRecordDetail) {
          if (checkRecordDetail[item] === YesOrNoEnum.NO) {
            warnModal(t('存在未通过控制点，确认是否通过'), {
              async onOk() {
                await submit(formModel, checkRecordDetail);
              },
            });
            return;
          }
        }
      }
      await submit(formModel, checkRecordDetail);
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const submit = async (formModel: any, checkRecordDetail: any) => {
    try {
      submitParams.value = {
        id: record.value.id,
        sampleBatchNo: record.value.sampleBatchNo,
        checkRecordDetail,
        ...formModel,
      };
      await signRef.value?.openSign(submitParams.value);
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await checkReport({
        ...submitParams.value,
        signature: signUrl,
      });
      message.success(t('操作成功'));
      closeModal();
      emits('submitSuccess');
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
    initList();
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style lang="less" scoped>
  :deep(.lisms-table-tbody > tr:nth-child(2) > td) {
    border-top: 1px solid #e1e3e5;
  }
</style>
