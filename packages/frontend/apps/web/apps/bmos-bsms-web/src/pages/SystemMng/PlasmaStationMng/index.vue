<!-- 单采血浆站管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :requests="[getPlasmaStationList as DataRequestFn]"
    :paginations="[paginationBig]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button v-hasAuth="170110002000001" type="primary" @click="openAddDialog('create')">
        {{ t('新增') }}
      </Button>
    </template>
  </BMPageComponent>
  <AddDialog ref="addDialogRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getPlasmaStationList } from '@/services';
  import { useTable } from './hooks/useTable';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { paginationBig } from '@/utils/paginationConfig';
  import AddDialog from './components/AddDialog.vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'PlasmaStationMng',
  });

  const addDialogRef = ref<InstanceType<typeof AddDialog>>();

  const openAddDialog = (type: 'create' | 'edit', row?: any) => {
    addDialogRef.value?.openModal(type, row);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openAddDialog);

  const submitSuccess = () => {
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
