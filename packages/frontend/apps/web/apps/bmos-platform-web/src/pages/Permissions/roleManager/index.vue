<!--角色管理-->
<template>
  <BMPageComponent
    ref="pageRoleManager"
    :selectedKeys="treeAllField.selectedKeys"
    :titles="[t('角色管理列表')]"
    :treeData="treeData"
    :fieldNames="fieldNames"
    :showAllAddIcon="hasPermission('100030003000001')"
    :actionList="actionList"
    :expandedKeys="treeAllField.expandedKeys"
    :defaultSelectedKeys="['all']"
    :columns="[columns as TableColumn[]]"
    :requests="[roleList as DataRequestFn]"
    :treeField="treeField"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: { span: 18 },
      },
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <BMModalForm
        ref="modalFormRef"
        :open="modalInstance.addStorage"
        :title="t(modelName[formModalType])"
        :formProps="{
          initialValues: formDefaultValue!,
          schemas: addFomUse.schemas as FormProps['schemas'],
          disabled: addFomUse.disabled,
          labelWidth: 120,
        }"
        :submit="handleModalSubmit"
        @cancelModal="() => (modalInstance.addStorage = false)" />
      <AllocationPerson v-model:openPeople="openPeople" :roleId="roleId" />
      <!-- type:'1'为菜单权限 '2'为权限授权 -->
      <MenuAllocation v-model:openMenu="openMenu" :roleId="roleId" :type="type" />
      <!-- 部门分配弹框 -->
      <DepartmentAllocation ref="DepartmentAllocationRef" :roleId="roleId"></DepartmentAllocation>
      <Button v-hasAuth="100030003000004" type="primary" @click="() => storageAdd(treeNode, modalStatus.Add)">
        {{ t('新增') }}
      </Button>
    </template>
  </BMPageComponent>
  <BMModalForm
    :title="modalTitle"
    :formProps="{
      initialValues: treeFormProps.initialValues,
      schemas: treeFormProps.schemas as FormProps['schemas'],
    }"
    :open="status"
    :submit="categorySubmit"
    @cancelModal="() => (status = false)" />
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { getRoleList } from '@/api/Permissions/roleManagement';
  import {
    BMPageComponent,
    ActionListItem,
    FormProps,
    BMModalForm,
    TableColumn,
    DataRequestFn,
  } from '@bmos/components';
  import AllocationPerson from './allocationPerson/allocationPerson.vue';
  import MenuAllocation from './menuAllocation/menuAllocation.vue';
  import DepartmentAllocation from './components/DepartmentAllocation.vue';
  import { useTree, useModalForm, useColumns } from './hooks';
  import { modelName, modalStatus } from './enum';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const TreeUser = useTree();
  const {
    status,
    treeData,
    fieldNames,
    actionList,
    modalTitle,
    treeAllField,
    treeFormProps,
    pageRoleManager,
    treeAction,
    categorySubmit,
  } = TreeUser;
  const UseModalForm = useModalForm(TreeUser);
  const { addFomUse, modalInstance, formModalType, formDefaultValue, storageAdd, handleModalSubmit } = UseModalForm;
  const UseColumns = useColumns(UseModalForm, TreeUser);
  const { openPeople, roleId, type, columns, treeField, openMenu, DepartmentAllocationRef } = UseColumns;
  //角色列表
  const roleList = async (params: any) => {
    const data = params?.roleTypeId == 'all' ? { ...params, roleTypeId: null } : params;
    return await getRoleList(data);
  };
  //树点击事件
  const handleTreeAction = (action: ActionListItem, node: any) => {
    treeAction(action, node);
  };
</script>
<style scoped lang="less"></style>
