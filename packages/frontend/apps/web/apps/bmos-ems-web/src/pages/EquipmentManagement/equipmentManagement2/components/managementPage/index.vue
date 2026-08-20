<!--设备管理-->
<template>
  <BMPageComponent
    ref="pageManagement"
    :selectedKeys="treeAllField.selectedKeys"
    :titles="[t('设备列表')]"
    :treeData="treeData"
    :fieldNames="fieldNames"
    :actionList="actionList"
    :showAllAddIcon="hasPermission('160010002000001')"
    :expandedKeys="treeAllField.expandedKeys"
    :defaultSelectedKeys="['all']"
    :treeField="treeField"
    :columns="[columns as TableColumn[]]"
    :formProps="[formFirstProps as any]"
    :requests="[storageList as DataRequestFn]"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0>
      <!-- 打印标签弹框 -->
      <BMPrint
        v-model:open="printOpen"
        :getPrinter="reqGetPrintEquipment"
        sceneId="160004001"
        @printConfirm="printConfirm"></BMPrint>
      <Button v-hasAuth="160010002000004" type="primary" @click="addManagementPage">{{ t('新增设备') }}</Button>
      <Button v-hasAuth="160010002000007" @click="print">{{ t('打印标签') }}</Button>
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
  </BMPageComponent>
</template>
<script lang="tsx" setup>
  import { getPlatformEquipmentPage, reqGetPrintEquipment } from '@/services';
  import { t } from '@bmos/i18n';
  import {
    BMPageComponent,
    BMModalForm,
    TableColumn,
    FormProps,
    DataRequestFn,
    ActionListItem,
    BMPrint,
  } from '@bmos/components';
  import { useTree, useColumns } from './hooks';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const emits = defineEmits(['addManagementPage', 'treeData', 'selectTree', 'equipmentTag', 'matchPoint']);

  const TreeUser = useTree({ emits });
  const {
    status,
    treeData,
    modalTitle,
    fieldNames,
    actionList,
    treeAllField,
    treeFormProps,
    treeAction,
    categorySubmit,
    getTreeData,
  } = TreeUser;
  const UseColumns = useColumns({ emits });
  const {
    columns,
    treeField,
    formFirstProps,
    addManagementPage,
    rowSelections,
    printOpen,
    printConfirm,
    print,
    selectionReactive,
  } = UseColumns;

  //设备列表
  const storageList = async (params: any) => {
    const data = params?.categoryId == 'all' ? { ...params, categoryId: null } : params;
    emits('selectTree', data?.categoryId);
    rowSelections[0].selectedRowKeys = [];
    selectionReactive.selectedRowKeys = [];
    return await getPlatformEquipmentPage(data);
  };
  //树点击事件
  const handleTreeAction = (action: ActionListItem, node: any) => {
    treeAction(action, node);
  };
  onMounted(async () => {
    await getTreeData();
  });
</script>
<style lang="less" scoped>
  :deep(.hide) {
    width: 100%;
    overflow: hidden;

    text-overflow: ellipsis;
  }
</style>
