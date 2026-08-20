<!-- 创建放行单 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('创建放行单')"
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
          :showToolBar="false"
          row-key="id"
          :pagination="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="905" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { Sign } from '@/components/Sign';
  import { message } from 'ant-design-vue';
  import { getQualityGuaranteeReleasePermitNumber, createReleaseNote } from '@/services';
  import { BMModalForm, BMTable } from '@bmos/components';

  const signRef = ref();

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref<any>([]);

  const openModal = async (row: any) => {
    const { data } = await getQualityGuaranteeReleasePermitNumber();
    dataList.value = [row];
    open.value = true;
    await nextTick();
    setFormModels({
      quarantineId: row.quarantineId,
      fileNo: data,
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
        checkNo: dataList.value?.[0]?.checkNo,
      };
      // return await createReleaseNote(params);
      await signRef.value.openSign(
        dataList.value.map((item: any) => ({
          reportId: item.quarantineId,
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
      await createReleaseNote({
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
