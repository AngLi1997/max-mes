<!--公式配置-->
<template>
  <BMPageComponent
    ref="pageExpression"
    :selectedKeys="treeAllField.selectedKeys"
    :titles="[t('公式配置列表')]"
    :treeData="treeData"
    :actionList="actionList"
    :showAllAddIcon="hasPermission('100020006000001')"
    :expandedKeys="treeAllField.expandedKeys"
    :fieldNames="fieldNames"
    :columns="[columns as TableColumn[]]"
    :treeField="treeField"
    :requests="[storageList as DataRequestFn]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: { span: 18 },
      },
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <CalculateModal v-model:open="calculateModalOpen" :rowData="rowData" @submit="expressionVerify" />
      <BMModalForm
        ref="modalFormRef"
        wrapClassName="modalSizeMedium add-expression-modal"
        :open="modalInstance.addStorage"
        :title="t(modelName[formModalType])"
        :formProps="{
          initialValues: formDefaultValue!,
          schemas: addFomUse.schemas as FormProps['schemas'],
          disabled: addFomUse.disabled,
          labelWidth: 90,
        }"
        :submit="handleModalSubmit"
        :okButtonProps="isDisabled"
        @cancelModal="() => (modalInstance.addStorage = false)" />
      <Button v-hasAuth="100020006000004" type="primary" @click="() => storageAdd(treeNode, modalStatus.Add)">
        {{ t('新建公式') }}
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
  <!-- 绑定记录 -->
  <BindRecord v-if="rowData.id" v-model:open="bindRecordOpen" :rowData="rowData" />
</template>

<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { reqPageUsingGET } from '@/api';
  import {
    BMPageComponent,
    DataRequestFn,
    BMModalForm,
    TableColumn,
    FormProps,
    ActionListItem,
  } from '@bmos/components';
  import { useTree, useColumns, useModalForm } from './hooks';
  import { modelName, modalStatus } from './enum';
  import { usePermissionStore } from '@/stores/permission';
  import CalculateModal from './components/CalculateModal.vue';
  import BindRecord from './components/BindRecord.vue';

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
    pageExpression,
    treeAction,
    categorySubmit,
  } = TreeUser;
  const UseModalForm = useModalForm(TreeUser);
  const {
    addFomUse,
    modalInstance,
    formDefaultValue,
    formModalType,
    modalFormRef,
    isDisabled,
    storageAdd,
    handleModalSubmit,
  } = UseModalForm;
  const UseColumns = useColumns(UseModalForm, TreeUser);
  const { columns, treeField, calculateModalOpen, rowData, bindRecordOpen, expressionVerify } = UseColumns;
  //公式配置列表
  const storageList = async (params: any) => {
    const data = params?.expressionCategoryId == 'all' ? { ...params, expressionCategoryId: '0' } : params;
    return await reqPageUsingGET(data);
  };

  //树点击事件
  const handleTreeAction = (action: ActionListItem, node: any) => {
    treeAction(action, node);
  };
</script>
<style lang="less">
  .add-expression-modal {
    .expression-help-icon {
      cursor: pointer;
    }
  }
  .expressionTable {
    .plat-table-thead {
      th {
        font-weight: 200;
        font-size: 13px;
      }
    }
  }

  .expression-help-popover {
    .container {
      padding-left: var(--bmos-padding-small);
      padding-right: var(--bmos-padding-small);
      .item-title {
        color: #000;
        margin-bottom: var(--bmos-margin-large);
        margin-top: var(--bmos-margin-large);
      }
      .plat-tag {
        background-color: var(--bmos-primary-color-tab);
        color: var(--bmos-primary-color);
        border: none;
        font-size: 14px;
        margin-inline-end: 9px;
        border-radius: 3px;
        line-height: 20px;
        padding: 2px 8px 2px 8px;
        margin-bottom: 5px;
      }
    }
  }
</style>
