<!-- 发布/核对 弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="dialogType == 'publish' ? t('破伤风抗体效价发布') : t('破伤风抗体效价核对')"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { publishTiter, checkTiter } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef } = useForm();
  const { tableRef, columns } = useTable();

  const dialogType = ref<'publish' | 'check'>('publish');

  const dataList = ref<any>([]);

  const openModal = async (rows: any, type: 'publish' | 'check') => {
    dataList.value = [...rows];
    dialogType.value = type;
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (): Promise<any> => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: [...dataList.value],
          total: dataList.value?.length || 0,
        });
      }, 100);
    });
  };

  const cancel = () => {
    open.value = false;
    dataList.value = [];
    // tableRef.value?.fetchData();
  };

  // 提交
  const submit = async () => {
    try {
      const params = {
        idList: dataList.value.map((item: any) => item.id),
        titerType: 4,
      };
      if (dialogType.value === 'publish') {
        await publishTiter(params);
      } else {
        await checkTiter(params);
      }
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
