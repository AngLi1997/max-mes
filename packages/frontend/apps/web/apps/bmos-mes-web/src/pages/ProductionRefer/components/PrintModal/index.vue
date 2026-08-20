<template>
  <BMPageTree
    v-model="selectedKeys"
    :tree-data="treeData"
    :fieldNames="fieldNames"
    :showAction="false"
    @tree-select="treeSelect">
    <div class="preview-container">
      <div class="preview-operation">
        <Button v-if="file" @click="print" type="primary">{{ commonModal.print }}</Button>
      </div>
      <!-- <div class="record-preview">
        <ShowPdf ref="ShowPdfRef" :file="file" :src="fileUrl"/>
      </div> -->
      <iframe v-if="file" :src="fileUrl + '#toolbar=0'" class="printIframe" frameborder="0"></iframe>
      <div v-else class="noPdf">
        <img :src="noPdf" alt="">
        {{ t('暂无记录') }}
      </div>
    </div>
  </BMPageTree>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { BMPageTree } from '@bmos/components';
  import { useTree } from '../hooks/useTree';
  import { commonModal } from '@/config';
  import { ShowPdf } from '@/components/Record';
  import { saveOperationHistory } from '../../utils';
  import { t } from '@bmos/i18n';
  import noPdf from '@/assets/images/noPdf.png';
  import { OPERATION } from '../../utils/enum';
  const props = withDefaults(
    defineProps<{
      planId: string;
      node?: any;
    }>(),
    {
      planId: '',
      node: () => ({}),
    },
  );
  const ShowPdfRef = ref()
  const { initData, selectedKeys, treeSelect, treeData, fieldNames, file, fileUrl } = useTree();
  onMounted(() => {
    initData(props.planId);
  });
  const print = () => {
    saveOperationHistory(props.node, OPERATION.P);
    // ShowPdfRef.value.printPdf();
    let iframe;
    let doc: any = null;
    iframe = document.createElement('iframe');
    iframe.setAttribute('id', 'print-iframe');
    iframe.setAttribute('src', fileUrl?.value);
    document.body.appendChild(iframe);
    doc = iframe.contentWindow?.document;
    //这里可以自定义样式
    iframe.onload = ()=>{
      iframe.contentWindow?.focus();
      iframe.contentWindow?.print();
    }
    doc = iframe.contentWindow?.document;
    doc.close();
  };
</script>

<style scoped lang="less">
  .preview-container {
    width: 100%;
    height: 100%;
    position: relative;
  }
  .printIframe{
    width: 100%;
    height: 100%;
  }
  .preview-operation {
    position: absolute;
    top: 10px;
    right: 30px;
  }
  .record-preview {
    height: calc(100% - 52px);
  }
  .noPdf{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .printIframe::-webkit-scrollbar {
    width: 8px;  /* 设置滚动条的宽度 */
  }
  .printIframe::-webkit-scrollbar-track {
    background-color: #f1f1f1;  /* 设置滚动条轨道的背景颜色 */
  }
  .printIframe::-webkit-scrollbar-thumb {
    background-color: #888;  /* 设置滚动条的颜色 */
  }
  .printIframe::-webkit-scrollbar-thumb:hover {
    background-color: #555;  /* 设置鼠标悬停时滚动条的颜色 */
  }
</style>
