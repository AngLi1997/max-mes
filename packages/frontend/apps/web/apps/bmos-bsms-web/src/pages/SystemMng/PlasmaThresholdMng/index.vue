<!-- 在库血浆阈值管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :titles="[t('阈值数据列表')]"
    :formProps="[formFirstProps]"
    :requests="[getPlasmaThresholdList as DataRequestFn]"
    :paginations="[paginationBig]"
    :columns="[columnsFirst]"></BMPageComponent>
  <EditModal ref="editModalRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getPlasmaThresholdList } from '@/services';
  import { useTable } from './hooks/useTable';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { paginationBig } from '@/utils/paginationConfig';
  import EditModal from './components/EditModal.vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'PlasmaThresholdMng',
  });

  const editModalRef = ref<InstanceType<typeof EditModal>>();

  const openEditModal = (row?: any, type: 'create' | 'edit' = 'create') => {
    editModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openEditModal);

  const submitSuccess = () => {
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
