<!-- 检验标准设置 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[postStaticDataConfigInspectPage as any]"
    :showToolBars="[false]"
    :columns="[columnsFirst]"></BMPageComponent>
  <EditModal v-model:modalOpen="editModalOpen" :rowData="firstRowData" @ok="updateTableData" />
</template>

<script setup lang="tsx">
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks';
  import { postStaticDataConfigInspectPage } from '@/services';
  import { paginationBig } from '@/utils';
  import EditModal from './components/EditModal.vue';

  defineOptions({
    name: 'StandardSettings',
    inheritAttrs: false,
  });

  const editModalOpen = ref<boolean>(false);

  const { columnsFirst, pageRef, formFirstProps, firstRowData, updateTableData } = useTable({
    editModalOpen,
  });
</script>
