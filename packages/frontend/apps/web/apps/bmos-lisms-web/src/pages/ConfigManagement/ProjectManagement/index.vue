<!-- 检验项目管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[postStaticDataConfigInspectPage as any]"
    :showToolBars="[false]"
    :columns="[columnsFirst]"></BMPageComponent>
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
  <EditModal v-model:modalOpen="editModalOpen" :rowData="firstRowData" @ok="updateTableData" />
</template>

<script setup lang="tsx">
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks';
  import { postStaticDataConfigInspectPage } from '@/services';
  import { paginationBig } from '@/utils';
  import RemarkModal from '@/components/RemarkModal';
  import EditModal from './components/EditModal.vue';

  defineOptions({
    name: 'ProjectManagement',
    inheritAttrs: false,
  });
  const editModalOpen = ref<boolean>(false);

  const { columnsFirst, pageRef, formFirstProps, remarkModalOpen, remarkDetails, firstRowData, updateTableData } =
    useTable({
      editModalOpen,
    });
</script>
