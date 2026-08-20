<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验项目')"
    wrapClassName="modalSizeMedium"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"
    @cancelModal="closeModal">
    <template #formBefore>
      <Tabs v-model:activeKey="activeKey">
        <TabPane key="fixedInspectItems" :tab="t('固定项目')" />
        <TabPane key="specialInspectItems" :tab="t('特殊项目')" />
      </Tabs>
      <BMTable
        ref="tableRef"
        :search="false"
        :data-source="tableData[activeKey]"
        :columns="columns"
        row-key="id"
        headerTitle=""
        :scroll="{ x: 300, y: 400 }"
        :showRefresh="false"
        show-index
        :pagination="false"></BMTable>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { getInspectItems } from '@/services';
  import { t } from '@bmos/i18n';
  import { useTable } from './useTable';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { Tabs, TabPane, message } from 'ant-design-vue';

  const open = ref(false);

  const activeKey = ref('fixedInspectItems');

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();
  const tableData = ref<any>({});

  const openModal = async (row: any) => {
    try {
      const { data } = await getInspectItems({ sampleNo: row.sampleNo });
      tableData.value = data;
      open.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    // open.value = false;
    tableData.value = [];
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style scoped></style>
