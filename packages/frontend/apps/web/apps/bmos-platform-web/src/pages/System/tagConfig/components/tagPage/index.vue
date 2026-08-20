<!--标签配置-->
<template>
  <BMPageComponent
    ref="pageTagConfig"
    :titles="[t('标签管理列表')]"
    :treeData="treeData"
    :fieldNames="fieldNames"
    :defaultSelectedKeys="['all']"
    :showAllAddIcon="false"
    :showAction="false"
    :treeField="treeField"
    :columns="[columns as TableColumn[]]"
    :requests="[storageList as DataRequestFn]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: { span: 12 },
      },
    ]">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="100020007000001" type="primary" @click="addTagPage">{{ t('新增') }}</Button>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMPageComponent, TableColumn, DataRequestFn } from '@bmos/components';
  import { useTree, useColumns } from './hooks';
  import { reqPlatformTagInstanceQueryPageGET } from '@/api';
  const emits = defineEmits(['addTagPage', 'treeData', 'viewTagPage', 'editTagPage', 'selectTree']);
  const TreeUser = useTree({ emits });
  const { treeData, fieldNames } = TreeUser;
  const UseColumns = useColumns({ emits });
  const { columns, treeField } = UseColumns;

  const addTagPage = () => {
    emits('addTagPage');
  };
  const storageList = async (params: any) => {
    const data = params?.tagTypeId == 'all' ? { ...params, tagTypeId: null } : params;
    emits('selectTree', data?.tagTypeId);
    return await reqPlatformTagInstanceQueryPageGET(data);
  };
</script>

<style lang="less" scoped></style>
