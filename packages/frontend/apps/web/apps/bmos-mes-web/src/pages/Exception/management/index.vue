<!-- 异常管理 -->
<template>
  <div class="exception_main">
    <Tabs v-model:activeKey="activeKey" @change="typeChange">
      <TabPane key="1" :tab="t('调查中')"></TabPane>
      <TabPane key="2" :tab="t('已关闭')"></TabPane>
    </Tabs>
    <div class="table_box">
      <BMPageComponent
        ref="pageRef"
        :showAllAddIcon="false"
        :showAction="false"
        :rowKeys="['id', 'id']"
        hideRightTree
        :search="[true, false]"
        :formProps="[formFirstProps, {}]"
        :requests="[reqMaterialListReq as DataRequestFn]"
        :columns="[columnsFirst]">
        <template #tableHeaderToolbar0>
          <Button v-hasAuth="120090001000001" type="primary" @click="addExceptionOpen">
            {{ t('新增') }}
          </Button>
        </template>
      </BMPageComponent>
    </div>
    <AddModal ref="addRef" :is-update="isUpdate" @submit="pageRef.fetchData()" />
    <HandleModal :rowData="rowData" @submit="pageRef.fetchData()" />
    <ToVoid :rowData="rowData" @submit="pageRef.fetchData()" />
    <Investigation :rowData="rowData" @submit="pageRef.fetchData()" />
    <!-- <History v-model:open-history-modal="openHistoryModal" :rowData="rowData" :history-list="historyList" /> -->
    <History
      v-model:historyOpen="openHistoryModal"
      :businessId="rowData?.id"
      :getApi="reqHistoryList"
      showDetail
      :detailLabel="detailLabel" />
  </div>
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import { getExceptionPage } from '@/services';
  import { Tabs, TabPane } from 'ant-design-vue';
  import AddModal from './component/addModal.vue';
  import HandleModal from './component/handle.vue';
  import ToVoid from './component/toVoid.vue';
  import Investigation from './component/investigation.vue';
  import History from '@/components/History/index.vue';
  import { reqHistoryList } from '@/services';

  const reqMaterialListReq = async (params: any) => {
    return await getExceptionPage({ ...params, investigating: activeKey.value === '1' });
  };
  const {
    columnsFirst,
    pageRef,
    formFirstProps,
    activeKey,
    typeChange,
    addExceptionOpen,
    openHistoryModal,
    detailLabel,
    isUpdate,
    addRef,
    rowData,
  } = useTable();
</script>
<style scoped lang="less">
  .exception_main {
    height: 100%;
    .table_box {
      height: calc(100% - 40px);
    }
    :deep(.mes-tabs) {
      background-color: white;
      padding-left: 20px;
    }
    :deep(.mes-tabs-nav) {
      margin: 0;
    }
  }
</style>
