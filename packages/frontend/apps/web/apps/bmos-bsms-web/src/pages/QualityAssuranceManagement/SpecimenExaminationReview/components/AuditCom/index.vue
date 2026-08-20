<!-- 请验审核 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('请验审核')"
    wrapClassName="modalSizeExtraLarge"
    :formProps="formProps"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';
  import { auditSpecimenExamination } from '@/services';

  // const props = defineProps({
  //   rows: {
  //     type: Array<any>,
  //     default: () => [],
  //   },
  // });

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

  const loadData: DataRequestFn = async (params: any, onChangeParams: any): Promise<any> => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: dataList.value,
          total: dataList.value.length,
        });
      }, 100);
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        ids: dataList.value.map((item: any) => item.id),
        ...formModal,
      };
      return await auditSpecimenExamination(params);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
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
