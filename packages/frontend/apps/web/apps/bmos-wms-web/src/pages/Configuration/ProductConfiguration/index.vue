<!--货位配置-->
<template>
  <BMPageComponent
    ref="pageStorages"
    :selectedKeys="treeAllField.selectedKeys"
    :titles="[t('货位列表')]"
    :columns="columns"
    :treeData="treeData"
    :showAllAddIcon="hasPermission('150010002000001')"
    :treeField="treeField"
    :defaultSelectedKeys="['all']"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: { span: 12 },
      },
    ]"
    :requests="[storageList as DataRequestFn]"
    :fieldNames="fieldNames"
    :actionList="actionList"
    :expandedKeys="treeAllField.expandedKeys"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <!-- 表格的编辑查看弹框 -->
      <BMModalForm
        ref="modalFormRef"
        :title="t(modelName[formModalType])"
        :open="modalInstance.addStorage"
        :formProps="{
          initialValues: formDefaultValue!,
          schemas: addFomUse.schemas as FormProps['schemas'],
          disabled: addFomUse.disabled,
          labelWidth: 120,
        }"
        :submit="handleModalSubmit"
        @cancelModal="() => (modalInstance.addStorage = false)">
        <template #DEPART="{ formModel }">
          <div class="depart-modal-tree">
            <ModalBtn :submit="() => departMentSubmit(formModel, 'deptIds')" :title="t('部门授权')">
              <DepartMent ref="departMent" :checks="formModel['deptIds']" :type="false" :isAdd="true"></DepartMent>
              <template #trigger>
                <Button :icon="departIcon(formModel, 'deptIds')" class="depart-btn" :disabled="addFomUse.disabled">
                  {{ t('选择部门') }}
                </Button>
              </template>
            </ModalBtn>
          </div>
        </template>
        <template #footer="{ formModel }">
          <Button v-if="formModalType !== modalStatus.View" @click="() => (modalInstance.addStorage = false)">
            {{ t('取消') }}
          </Button>
          <Button type="primary" @click="handleModalSubmit(formModel)">{{ t('确定') }}</Button>
        </template>
      </BMModalForm>
      <!-- 部门授权弹框 -->
      <PermissionModal
        v-model:permissionOpen="permissionModalOpen"
        :processId="firstRowData?.id"
        @ok="savePermission" />
      <Button v-hasAuth="150010002000004" type="primary" @click="() => storageAdd(treeNode, modalStatus.Add)">
        {{ t('新增') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 树节点的编辑查看弹框 -->
  <BMModalForm
    :title="modalTitle"
    :formProps="{
      initialValues: treeFormProps.initialValues,
      schemas: treeFormProps.schemas as FormProps['schemas'],
    }"
    :open="status"
    :submit="categorySubmit"
    @cancelModal="() => (status = false)"></BMModalForm>
</template>

<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { useTree, useColumns, useModalForm } from './hooks';
  import { modelName, modalStatus } from './enum';
  import { storageQueryList } from '@/services';
  import { BMIcons } from '@bmos/icons';
  import { BMModalForm, FormProps, ActionListItem } from '@bmos/components';
  import ModalBtn from '@/components/ModalBtn/index.vue';
  import PermissionModal from './components/PermissionModal.vue';
  import DepartMent from '@/components/DepartMent/index.vue';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const TreeUser = useTree();
  const {
    status,
    treeData,
    fieldNames,
    modalTitle,
    actionList,
    treeAllField,
    treeFormProps,
    pageStorages,
    treeAction,
    categorySubmit,
  } = TreeUser;
  const UseModalForm = useModalForm(TreeUser);
  const { modalInstance, addFomUse, formDefaultValue, formModalType, modalFormRef, storageAdd, handleModalSubmit } =
    UseModalForm;
  const UseColumns = useColumns(UseModalForm);
  const { columns, treeField, permissionModalOpen, firstRowData } = UseColumns;
  const departMent = ref();
  const storageList = async (params: any) => {
    const data = params?.storageId == 'all' ? { ...params, storageId: null } : params;
    const res = await storageQueryList(data);
    return await storageQueryList(data);
  };
  const departIcon = (model: any, field: string) => {
    const style_icon = {
      width: '15px',
      height: '15px',
      marginRight: '5px',
      marginBottom: '1px',
      verticalAlign: 'sub',
    };
    if (!model[field] || model[field]?.length === 0) {
      return h(BMIcons, {
        icon: 'Depart',
        style: style_icon,
      });
    } else {
      return h(BMIcons, {
        icon: 'Success',
        style: style_icon,
      });
    }
  };
  const departMentSubmit = (model: any, field: string) => {
    const keys = departMent.value.getSelectKeys();
    model[field] = keys;
    return Promise.resolve(true);
  };
  //树点击事件
  const handleTreeAction = (action: ActionListItem, node: any) => {
    treeAction(action, node);
  };
  // 刷新表格
  const savePermission = () => {
    pageStorages.value?.fetchData(0);
  };
</script>

<style scoped lang="less"></style>
