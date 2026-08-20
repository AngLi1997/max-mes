<!-- 免疫类型管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[false]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[false]"
    :requests="[getImmunetypeList as DataRequestFn]"
    :paginations="[paginationBig]"
    :columns="[columnsFirst]"></BMPageComponent>
  <EditModal ref="editModalRef" @submitSuccess="() => pageRef.fetchData()" />
</template>

<script setup lang="ts">
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import EditModal from './components/EditModal/index.vue';
  import { getImmunetypeList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';

  defineOptions({
    name: 'ImmunetypeMng',
  });

  const editModalRef = ref();

  const openModal = (data: any) => {
    editModalRef.value.openModal(data);
  };

  const { pageRef, columnsFirst } = useTable(openModal);
</script>

<style scoped></style>
