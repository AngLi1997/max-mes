<!-- 数量 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[dialogType]"
    wrapClassName="modalSizeLarge"
    :showOkButton="false">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :data-request="loadData"
          :columns="tableColumns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :formProps="formProps"
          :pagination="paginationSmall">
          <template #toolbar>
            <Button type="primary">{{ t('导出') }}</Button>
          </template>
        </BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const modalFormRef = ref();
  const { tableRef, columns, columnsUnqualified, columnsExtra, formProps } = useTable();

  const dataList = ref<any>([]);
  const dialogType = ref<'hg' | 'bhg' | 'wtg'>('hg');

  const titleMap = {
    hg: t('检疫期合格数据'),
    bhg: t('检疫期不合格数据'),
    wtg: t('检疫期未通过数据'),
  };

  const tableColumns = computed(() => {
    if (dialogType.value === 'bhg') {
      return [...columns, ...columnsUnqualified];
    } else {
      return [...columns, ...columnsExtra];
    }
  });

  const openModal = async (rows: any, type: 'hg' | 'bhg' | 'wtg') => {
    dataList.value = rows || [];
    dialogType.value = type;
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (): Promise<any> => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: dataList.value,
          total: dataList.value?.length || 0,
        });
      }, 100);
    });
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
