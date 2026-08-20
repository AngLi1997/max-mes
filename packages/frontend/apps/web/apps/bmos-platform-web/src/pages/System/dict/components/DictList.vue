<template>
  <BMPageComponent
    ref="tableInstance"
    :hide-right-tree="true"
    :rowKeys="['id', 'id']"
    :tableFields="tableFields"
    :requests="[reqPlatformDictListGET, reqPlatformDictListDetail]"
    :columns="columns"
    :titles="titles"
    :search="[true, true]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 12,
        },
      },
      {},
    ]">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="100020009000001" type="primary" @click="addDict('add')">
        {{ t('新建字典') }}
      </Button>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button
        v-hasAuth="100020009000003"
        type="primary"
        :disabled="buttonDisabled(currentNodes)"
        @click="addVersionData()">
        {{ t('新增数据') }}
      </Button>
      <AddDetailModal
        v-model:open="addDetailModalOpen"
        :selectDictId="currentNodes[0]?.id"
        :selectDictName="currentNodes[0]?.dictName"
        :row-data="rowData"
        :status="modalStatus"
        @updateTable="updateTable" />
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { reqPlatformDictListGET, reqPlatformDictListDetailGET } from '@/api';
  import { useTables } from './hooks';
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { BMPageComponent } from '@bmos/components';
  import AddDetailModal from './components/AddDictListDataModal.vue';
  import { MODAL_STATUS } from '../types';
  const buttonDisabled = (currentNodes: any) => {
    return !(currentNodes && currentNodes[0] && currentNodes[0]?.id);
  };
  const emits = defineEmits<{
    (e: 'addDict', type: string): void;
    (e: 'eidtOrLook', row: any, type: string): void;
  }>();

  const addDict = (type: string) => {
    emits('addDict', type);
  };

  const addVersionData = () => {
    addDetailModalOpen.value = true;
    modalStatus.value = MODAL_STATUS.ADD;
  };

  const updateTable = () => {
    tableInstance.value?.fetchData(1);
  };

  const { columns, titles, tableInstance, addDetailModalOpen, modalStatus, rowData } = useTables({
    emits,
  });
  const tableFields = ref([
    {
      field: {
        id: 'id',
      },
    },
    {
      field: {
        dictId: 'id',
      },
    },
  ]);
  // 获取字典管理版本信息
  const reqPlatformDictListDetail = async (params: any) => {
    if (params.dictId) {
      return reqPlatformDictListDetailGET(params);
    }
    return Promise.resolve({
      data: [],
      total: 0,
    });
  };
</script>

<style scoped></style>
