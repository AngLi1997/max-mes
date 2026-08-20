<template>
  <BMPageComponent
    ref="pageRef"
    :columns="columns"
    :requests="requests"
    :showAllAddIcon="hasPermission('120070001000001')"
    :showAction="
      hasPermission('120070001000001') || hasPermission('120070001000002') || hasPermission('120070001000003')
    "
    :actionList="actionList"
    :rowKeys="['id', 'id']"
    :search="[true, false]"
    :treeData="treeData"
    :formProps="[formFirstProps, {}]"
    :rowClick="handleRowClick"
    :fieldNames="{
      title: 'name',
      key: 'id',
    }"
    :treeField="{
      field: {
        datasetCategoryId: 'id',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          datasetId: 'id',
          datasetType: 'datasetType',
        },
      },
    ]"
    :selectedKeys="treeSelectedKeys"
    @treeSelect="selectTreeNode"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <Button v-hasAuth="120070001000004" type="primary" @click="() => handleAddDataset(treeNode)">
        {{ t('新增') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('数据集')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('数据点信息')"></BMTableTitle>
    </template>
  </BMPageComponent>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="treeModalOpen"
    :title="treeModalTitle"
    :formProps="treeModalFormProps"
    wrapClassName="modalSizeMedium"
    :submit="treeModalSubmit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMPageComponent, BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';
  import { useTables } from './hooks';
  import { usePermissionStore } from '@/stores/permission';
  import { OperationType } from './type';

  const router = useRouter();

  const { hasPermission } = usePermissionStore();

  const {
    requests,
    columns,
    treeData,
    pageRef,
    formFirstProps,
    handleRowClick,
    actionList,
    handleTreeAction,
    treeSelectedKeys,

    treeModalOpen,
    treeModalTitle,
    treeModalFormProps,
    treeModalSubmit,
    selectTreeNode,
  } = useTables();

  const handleAddDataset = (treeNode: any) => {
    router.push({
      name: 'data-set-manage-detail',
      query: {
        type: OperationType.Add,
        ...(treeNode.id && treeNode.id !== 'all' && { datasetCategoryId: treeNode.id }),
      },
    });
  };
</script>

<style scoped lang="less"></style>
