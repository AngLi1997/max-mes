<template>
  <BMPageComponent
    ref="pageRef"
    :columns="columns"
    :requests="requests"
    :showAllAddIcon="hasPermission('120080001000001')"
    :showAction="
      hasPermission('120080001000001') || hasPermission('120080001000002') || hasPermission('120080001000003')
    "
    :actionList="actionList"
    :rowKeys="['id', 'id']"
    :search="[true, false]"
    :treeData="treeData"
    :formProps="[formFirstProps, {}]"
    :fieldNames="{
      title: 'name',
      key: 'id',
    }"
    :treeField="{
      field: {
        categoryId: 'id',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          templateInfoId: 'id',
        },
      },
    ]"
    :selectedKeys="treeSelectedKeys"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <PermissionModal
        v-model:permissionOpen="permissionModalOpen"
        :resourceId="firstRowData?.id"
        @ok="updateFirstTable" />
      <BindProcess
        v-model:processOpen="bindProcessModalOpen"
        :checkIds="checkedProcessIds"
        :saveApi="reqBatchRecordsTemplateBindProcess"
        :extraParams="{ templateInfoId: firstRowData?.id }"
        @ok="updateFirstTable" />
      <AddTemplateModal
        v-model:templateModalOpen="addTemplateModalOpen"
        :treeData="treeData[0]?.children"
        :treeNode="treeNode"
        @ok="updateFirstTable" />
      <Button v-hasAuth="120080001000004" type="primary" @click="addTemplate">
        {{ t('新增模板') }}
      </Button>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <span>
        <AddVersionModal
          v-model:versionModalOpen="addVersionModalOpen"
          :firstRowData="firstRowData"
          @ok="updateSecondTable" />
        <UploadTemplateModal
          v-model:uploadModalOpen="uploadTemplateModalOpen"
          :secondRowData="secondRowData"
          @ok="updateSecondTable" />
        <Step
          v-model:open="stepOpen"
          :formValue="{
            lotRecordsTemplateId: currentNodes?.[0]?.id,
            name: currentNodes?.[0]?.name,
            lotRecordsVersion: secondRowData?.version,
            lotRecordsVersionId: secondRowData?.id,
          }" />
        <History
          v-model:historyOpen="historyOpen"
          :businessId="secondRowData?.id"
          :getApi="reqBatchRecordsTemplateHistory"
          :downFileApi="reqBatchRecordsTemplatePathDownload"
          downFileType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
      </span>
      <Button v-hasAuth="120080001000007" type="default" @click="() => addVersion(currentNodes)">
        {{ t('新增版本') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('模板信息')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('版本信息')"></BMTableTitle>
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
  import { BMModalForm, BMPageComponent, BMTableTitle, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Button, message } from 'ant-design-vue';
  import { useTables } from './hooks';
  import { usePermissionStore } from '@/stores/permission';
  import PermissionModal from '@/components/PermissionDept/index.vue';
  import BindProcess from '@/components/BindProcess/index.vue';
  import { reqBatchRecordsTemplateBindProcess } from '@/services';
  import AddTemplateModal from './components/AddTemplateModal.vue';
  import AddVersionModal from './components/AddVersionModal.vue';
  import UploadTemplateModal from './components/UploadTemplateModal.vue';
  import Step from '../components/step/index.vue';
  import { reqBatchRecordsTemplateHistory, reqBatchRecordsTemplatePathDownload } from '@/services';
  import History from '@/components/History/index.vue';

  const { hasPermission } = usePermissionStore();

  const {
    requests,
    columns,
    treeData,
    pageRef,
    formFirstProps,
    actionList,
    handleTreeAction,
    treeSelectedKeys,
    treeModalOpen,
    treeModalTitle,
    treeModalFormProps,
    treeModalSubmit,
    permissionModalOpen,
    updateFirstTable,
    firstRowData,
    bindProcessModalOpen,
    checkedProcessIds,
    uploadTemplateModalOpen,
    secondRowData,
    stepOpen,
    historyOpen,
    updateSecondTable,
  } = useTables();

  const addTemplateModalOpen = ref<boolean>(false);
  const addTemplate = () => {
    addTemplateModalOpen.value = true;
  };

  const addVersionModalOpen = ref<boolean>(false);
  const addVersion = (currentNodes: Recordable) => {
    if (!currentNodes?.[0]?.id) {
      message.error(t('请选择模板'));
      return;
    }
    firstRowData.value = currentNodes?.[0];
    addVersionModalOpen.value = true;
  };
</script>

<style scoped lang="less"></style>
