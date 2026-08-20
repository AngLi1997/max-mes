<!--操作规程-->
<template>
  <BMPageComponent
    ref="pageProcedures"
    :titles="[t('规程列表'), t('版本信息')]"
    :selectedKeys="treeAllField.selectedKeys"
    :expandedKeys="treeAllField.expandedKeys"
    :treeData="treeData"
    :treeField="treeField"
    :fieldNames="fieldNames"
    :actionList="actionList"
    :showAllAddIcon="hasPermission('120020011000001')"
    :formProps="[formFirstProps, {}]"
    :tableFields="tableFields"
    :requests="[getRulesList as DataRequestFn, getVerList as DataRequestFn]"
    :columns="[rulesColumns, verColumns]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <Button v-hasAuth="120020011000004" type="primary" @click="addFiles(treeNode)">{{ t('新增文件') }}</Button>
      <BMModalForm
        :title="modalTitle"
        :formProps="{
          initialValues: treeFormProps.initialValues,
          schemas: treeFormProps.schemas as FormProps['schemas'],
        }"
        :open="status"
        :submit="categorySubmit"
        @cancelModal="() => (status = false)" />
      <BMModalForm
        :title="flowName"
        :formProps="{
          initialValues: flowProps.initialValues,
          schemas: flowProps.schemas as FormProps['schemas'],
        }"
        :open="flowStatus"
        :submit="flowSubmit"
        @cancelModal="() => (flowStatus = false)" />
      <PermissionModal
        v-model:permissionOpen="permissionModalOpen"
        :resourceId="firstRowData?.id"
        @ok="savePermission" />
      <HistoryModal v-model:historyOpen="historyOpen" :businessId="isVersionId?.id" />
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button v-hasAuth="120020011000005" @click="addVersion(currentNodes)">{{ t('新增版本') }}</Button>
    </template>
  </BMPageComponent>
</template>
<script lang="tsx" setup>
  import { getOperateRulePageList, getOperateRuleVersionPageList } from '@/services';
  import { t } from '@bmos/i18n';
  import PermissionModal from '@/components/PermissionDept/index.vue';
  import HistoryModal from '@/components/History/index.vue';
  import { BMPageComponent, BMModalForm, FormProps, DataRequestFn, ActionListItem } from '@bmos/components';
  import { useParams, useTree, useColumns } from './hooks';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const emits = defineEmits(['update:state', 'update:treeList', 'update:treeCutId', 'cutProceduresDetails']);
  //参数
  const UseParams = useParams();
  const {
    pageProcedures,
    historyOpen,
    flowName,
    permissionModalOpen,
    firstRowData,
    status,
    flowStatus,
    modalTitle,
    treeData,
    treeField,
    tableFields,
    treeAllField,
    fieldNames,
    formFirstProps,
    isVersionId,
  } = UseParams;
  const { treeFormProps, actionList, getTreeData, treeAction, categorySubmit } = useTree({ UseParams, emits });
  const { flowProps, rulesColumns, verColumns, addFiles, addVersion, savePermission, flowSubmit } = useColumns({
    UseParams,
    emits,
  });
  const getRulesList = async (params: any) => {
    emits('update:treeCutId', params?.categoryId && params.categoryId !== 'all' ? params.categoryId : '');
    if (!params.categoryId || params.categoryId === 'all') {
      return await getOperateRulePageList({
        ...params,
        categoryId: null,
      });
    }
    return await getOperateRulePageList(params);
  };
  const getVerList = async (params: any) => {
    if (!params.parentId) return Promise.resolve({ data: [] });
    return await getOperateRuleVersionPageList(params);
  };
  //树点击事件
  const handleTreeAction = (action: ActionListItem, node: any) => {
    treeAction(action, node);
  };
  onMounted(async () => {
    await getTreeData();
  });
</script>
