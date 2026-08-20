<!-- 批签发审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[false]"
    :formProps="[formFirstProps]"
    :requests="[reqLotReleaseManageQueryAuditPage as DataRequestFn]"
    :columns="[columnsFirst]"></BMPageComponent>
  <ApprovalModal
    v-model:approvalModalOpen="approvalModalOpen"
    :settings="rowData.payload?.settings"
    :taskId="rowData.taskId"
    :processInstanceId="rowData.processInstanceId"
    :nodeId="rowData.nodeId"
    :deploymentId="rowData.deploymentId"
    :executionId="rowData.executionId"
    @action="updateTableData" />
</template>

<script lang="ts" setup>
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import ApprovalModal from './components/ApprovalModal.vue';
  import { reqLotReleaseManageQueryAuditPage } from '@/services';

  const { pageRef, columnsFirst, formFirstProps, rowData, approvalModalOpen } = useTable({});
  const updateTableData = () => {
    pageRef.value?.fetchData();
  };
</script>
