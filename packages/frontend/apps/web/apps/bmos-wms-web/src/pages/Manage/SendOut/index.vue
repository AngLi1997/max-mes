<!-- 仓库发料 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[false]"
    :formProps="[formFirstProps]"
    :requests="[reqSendOutQueryPage as DataRequestFn]"
    :columns="[columnsFirst]"></BMPageComponent>
  <Sign
    v-model:open="signOpen"
    :signatureData="JSON.stringify(rowData)"
    :userList="permissionCodeUserList"
    :signatureAction="38"
    :labelList="labelList"
    showAlert
    :alertDesc="t('是否取消当前领料计划发料?')"
    :alertType="'warning'"
    @signSuccess="signSuccess"></Sign>
</template>

<script lang="ts" setup>
  import { reqSendOutQueryPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import Sign from '@/components/Sign';
  import { t } from '@bmos/i18n';

  const { pageRef, columnsFirst, formFirstProps, signOpen, permissionCodeUserList, labelList, signSuccess, rowData } =
    useTable({});
</script>
