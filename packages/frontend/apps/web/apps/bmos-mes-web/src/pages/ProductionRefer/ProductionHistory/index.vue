<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :showTollBar="false"
    :rowKeys="['id']"
    :columns="columns"
    :requests="requests"
    :treeData="TREE_DATA"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 12,
        },
      },
    ]"
    :titles="[t('生产历史')]"
    :fieldNames="fieldNames"
    :treeField="treeField"></BMPageComponent>
  <NormalModalForm
    :title="t('预览记录')"
    wrapClassName="modalSizeExtraLarge"
    :open="previewStatus"
    @cancelModal="() => (previewStatus = false)">
    <div class="record-content">
      <div v-if="file" class="operation">
        <Button type="primary" @click="print">{{ commonModal.print }}</Button>
      </div>
      <!-- <div class="record-preview">
        <ShowPdf ref="ShowPdfRef" :file="file" :src="fileUrl" />
      </div> -->
      <iframe
        v-if="file"
        :src="fileUrl + '#toolbar=0&navpanes=0&scrollbar=0&view=FitH'"
        class="printIframe"
        scrolling="no"
        frameborder="0"></iframe>
      <div v-else class="noPdf">
        <img :src="noPdf" alt="" />
        {{ t('暂无记录') }}
      </div>
    </div>
    <template #footer></template>
  </NormalModalForm>
  <HistoryModal
    v-model:historyOpen="historyOpen"
    :businessId="currentRecord?.id"
    :getApi="getProductHistoryOperationPageApi" />
</template>

<script setup lang="ts">
  import { NormalModalForm, BMPageComponent } from '@bmos/components';
  import { commonModal } from '@/config';
  import { t } from '@bmos/i18n';
  import { useColumns } from './hooks/useColumns';
  // import { ShowPdf } from '@/components/Record';
  import { useTree } from './hooks/useTree';
  import { saveOperationHistory } from '../utils';
  import { OPERATION } from '../utils/enum';
  import noPdf from '@/assets/images/noPdf.png';
  import HistoryModal from '@/components/History/index.vue';
  import { getProductHistoryOperationPageApi } from '@/services';

  const { columns, requests, currentRecord, previewStatus, historyOpen, file, fileUrl, pageRef } = useColumns();
  const { TREE_DATA, fieldNames, treeField } = useTree();
  // const ShowPdfRef = ref();
  const print = () => {
    saveOperationHistory(currentRecord.value, OPERATION.P);
    // ShowPdfRef.value.printPdf()
    let iframe;
    let doc: any = null;

    iframe = document.createElement('iframe');
    iframe.setAttribute('id', 'print-iframe');
    iframe.setAttribute('src', fileUrl?.value);
    document.body.appendChild(iframe);
    doc = iframe.contentWindow?.document;
    //这里可以自定义样式
    iframe.onload = () => {
      iframe.contentWindow?.focus();
      iframe.contentWindow?.print();
    };
    doc = iframe.contentWindow?.document;
    doc.close();
  };
</script>

<style scoped lang="less">
  .record-content {
    height: 55vh;
    position: relative;
    overflow: hidden;
  }
  .printIframe {
    width: 100%;
    height: 100%;
    border: none;
  }
  .recordPrint {
    width: 100%;
    height: 100%;
  }
  .operation {
    position: absolute;
    top: 10px;
    right: 30px;
    z-index: 9;
  }
  .record-preview {
    height: calc(100% - 52px);
  }
  .noPdf {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
</style>
