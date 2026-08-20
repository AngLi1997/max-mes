<template>
  <BMPageComponent
    :showAllAddIcon="false"
    :showAction="false"
    :showTollBar="false"
    :columns="columns"
    :treeField="treeField"
    :requests="requests"
    :titles="[t('批记录打印')]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 18,
        },
      },
    ]"
    :tree-data="TREE_DATA"
    :fieldNames="fieldNames"></BMPageComponent>
  <NormalModalForm
    wrapClassName="modalSizeExtraLarge"
    :title="t('关联批次')"
    :open="previewStatus"
    @cancelModal="() => (previewStatus = false)">
    <div class="record-content">
      <PrintModal :planId="currentRecord.id" :node="currentRecord"></PrintModal>
    </div>
    <template #footer></template>
  </NormalModalForm>
  <HistoryModal v-model:historyOpen="historyOpen" :businessId="currentRecord?.id" />
</template>

<script setup lang="ts">
  import { NormalModalForm, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import PrintModal from '../components/PrintModal/index.vue';
  import { useTree } from './hooks/useTree';
  import { useColumns } from './hooks/useColumns';
  import HistoryModal from '@/components/History/index.vue';

  const { columns, requests, currentRecord, previewStatus, historyOpen } = useColumns();
  const { TREE_DATA, fieldNames, treeField } = useTree();
</script>

<style scoped lang="less">
  .record-content {
    height: 640px;
  }
</style>
