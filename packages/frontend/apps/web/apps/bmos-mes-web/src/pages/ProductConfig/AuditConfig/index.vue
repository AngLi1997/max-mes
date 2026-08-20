<template>
  <BMPageComponent
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id', 'versionId']"
    :treeData="treeData"
    :search="[true, false]"
    :formProps="[formFirstProps, {}]"
    :fieldNames="{
      title: 'name',
      key: 'id',
      children: 'itemList',
    }"
    :treeField="{
      field: {
        categoryCode: 'id',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          id: 'id',
          categoryCode: 'categoryCode',
        },
      },
    ]"
    :requests="[reqFlowAuditListReq as DataRequestFn, reqFlowAuditVersionListReq as DataRequestFn]"
    :columns="[columnsFirst, columnsSecond]">
    <template #tableHeaderToolbar0="{ treeNode }">
      <BindProcess
        v-model:processOpen="bindProcessModalOpen"
        :checkIds="checkedProcessIds"
        :saveApi="reqFlowConfigBindProcessReq"
        :extraParams="{ id: firstRowData?.id }" />
      <Button v-hasAuth="120020008000001" type="primary" @click="() => addFlow(treeNode)">
        {{ t('新建流程') }}
      </Button>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button v-hasAuth="120020008000003" @click="() => updateVersion(currentNodes)">
        {{ t('升级版本') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('流程模型')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <HistoryModal v-model:historyOpen="historyOpen" :businessId="businessId" />
      <BMTableTitle :title="t('版本信息')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { reqFlowAuditList, reqGetFlowConfigTreeReq, reqFlowConfigBindProcessReq } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';
  import { useTable } from './hooks';
  import BindProcess from '@/components/BindProcess/index.vue';
  import HistoryModal from '@/components/History/index.vue';
  import { DataRequestFn, BMPageComponent, BMTableTitle } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { flow_STATE } from './enum';

  const router = useRouter();

  const reqFlowAuditListReq = async (params: any) => {
    if (!params.categoryCode || params.categoryCode === 'all') {
      return await reqFlowAuditList({
        ...params,
        categoryCode: 0,
      });
    }
    return await reqFlowAuditList(params);
  };
  const reqFlowAuditVersionListReq = async (params: any) => {
    if (!params.id) return Promise.resolve({ data: [] });
    return await reqFlowAuditList(params);
  };

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqGetFlowConfigTreeReq();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          key: 'all',
          code: 'all',
          itemList: data.map((item: any) => {
            return {
              ...item,
              itemList: item.itemList.map((it: any) => {
                return {
                  ...it,
                  isLeaf: true,
                };
              }),
            };
          }),
        },
      ];
    } catch (error) {
      //
    }
  };

  const {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    addFlow,
    firstRowData,
    bindProcessModalOpen,
    checkedProcessIds,
    businessId,
    historyOpen,
  } = useTable();

  const updateVersion = (currentNodes: any) => {
    if (!currentNodes?.[1]?.versionId) {
      message.warning(t('请先选择版本'));
      return;
    }
    router.push({
      name: 'audit-config-add-flow',
      query: {
        status: flow_STATE.updateVersion,
        versionId: currentNodes?.[1]?.versionId,
      },
    });
  };

  onMounted(() => {
    getTreeData();
  });
</script>
<style lang="less" scoped>
  :deep(.status-content) {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    .status-icon {
      width: 7px;
      height: 7px;
      border-radius: 50%;
      display: inline-block;
      margin-right: var(--bmos-margin-small);
    }
    &.edit {
      color: var(--bmos-success-color);
    }
    &.use {
      color: var(--bmos-warning-color-hover);
    }
    &.history {
      color: var(--bmos-fourth-level-text-color);
    }
    & .status-icon.edit {
      background-color: var(--bmos-success-color);
    }
    & .status-icon.use {
      background-color: var(--bmos-warning-color-hover);
    }
    & .status-icon.history {
      background-color: var(--bmos-fourth-level-text-color);
    }
  }
  .header-title {
    height: 20px;
    border-left: 3px solid var(--bmos-primary-color);
    display: flex;
    align-items: center;
    padding-left: var(--bmos-padding-small);
    color: #000000;
  }
</style>
