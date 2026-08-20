<!-- 文件模板管理 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="id"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      rowSelections: [rowSelection],
      formProps: [formFirstProps],
      columns: [columnsFirst],
    }"
    :tableLoadApi="postConfigFileTemplatePage"
    :expandLoadApi="getConfigFileTemplateChildPage"
    :expandFields="(record: any) => ({ templateNo: record.templateNo })"
    :expandProps="{
      rowKeys: ['standardNumber'],
      search: [false],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [false],
      columns: [columnsExpand],
    }">
    <template #tableHeaderTitle0>
      <Button v-hasAuth="210080005000001" :disabled="disCreated" type="primary" @click="add">
        {{ t('创建') }}
      </Button>
    </template>
  </PageExpandCom>
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
  <AddModal v-model:modalOpen="addModalOpen" :row-data="selectedRow" @ok="updateTableData" />
</template>

<script setup lang="ts">
  import { postConfigFileTemplatePage, postConfigFileTemplateChildPage } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { useTable } from './hooks';
  import { t } from '@bmos/i18n';
  import { useRowSelection } from '@/hooks';
  import RemarkModal from '@/components/RemarkModal';
  import AddModal from './components/AddModal.vue';

  defineOptions({
    name: 'FileTemplateManagement',
    inheritAttrs: false,
  });

  const { selectedRow, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: true,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  const getConfigFileTemplateChildPage = async (params: any) => {
    return await postConfigFileTemplateChildPage({
      ...params,
      auditResult: 'APPROVE',
    });
  };

  // 禁止创建
  const disCreated = computed(() => {
    return !selectedRow.value?.id;
  });

  const { pageExpendRef, columnsFirst, formFirstProps, columnsExpand, remarkModalOpen, remarkDetails } = useTable();

  const addModalOpen = ref<boolean>(false);
  const add = () => {
    addModalOpen.value = true;
  };

  const updateTableData = () => {
    clearSelect();
    pageExpendRef.value?.fetchData(0);
  };
</script>

<style scoped></style>
