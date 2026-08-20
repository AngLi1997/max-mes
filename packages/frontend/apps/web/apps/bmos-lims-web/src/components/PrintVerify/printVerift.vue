<template>
  <div class="a4-size">
    <div id="content">
      <table class="mytable">
        <tr>
          <td class="title">{{ t('编号') }}</td>
          <td colspan="3">{{ dataItem.orderNo }}</td>
        </tr>
        <tr>
          <td style="text-align: center; font-size: 24px" colspan="4">{{ t('物料请验单') }}</td>
        </tr>
        <tr>
          <td class="title">{{ t('物料名称') }}</td>
          <td>{{ dataItem.productsName }}</td>
          <td class="title">{{ t('物料编码') }}</td>
          <td>{{ dataItem.productsCode }}</td>
        </tr>
        <tr>
          <td class="title">{{ t('批号') }}</td>
          <td>{{ dataItem.batchNo }}</td>
          <td class="title">{{ t('规格') }}</td>
          <td>{{ dataItem.specification }}</td>
        </tr>
        <tr>
          <td class="title">{{ t('数量') }}</td>
          <td>{{ dataItem.inspectNumber }}</td>
          <td class="title">{{ t('级别') }}</td>
          <td>{{ dataItem.level }}</td>
        </tr>
        <tr>
          <td class="title">{{ t('生产单位') }}</td>
          <td>{{ dataItem.productionUnit }}</td>
          <td class="title">{{ t('供货单位') }}</td>
          <td>{{ dataItem.supplier }}</td>
        </tr>
        <tr>
          <td class="title">{{ t('请验人') }}</td>
          <td>{{ dataItem.verifier }}</td>
          <td class="title">{{ t('请验时间') }}</td>
          <td>{{ dataItem.verifyTime }}</td>
        </tr>
        <tr>
          <td class="title">{{ t('请验部门') }}</td>
          <td>{{ dataItem.verifyDept }}</td>
          <td class="title">{{ t('备注') }}</td>
          <td>{{ dataItem.remark }}</td>
        </tr>
      </table>
    </div>
    
  </div>
</template>

<script setup lang="ts">
import {
  Descriptions,
  DescriptionsItem
} from 'ant-design-vue';
import { t } from '@bmos/i18n';
import {printPDF} from './utils'
import htmlPdf from './utils/html2paf'
import JsPDF from 'jspdf';
import PDFPlugin from './utils/PDFPlugin';
import { defaultConfig, pageEnum } from './utils/const';
import { nextTick, ref } from 'vue';

const dataItem = ref({
  orderNo: '',
  productsName: '',
  productsCode: '',
  batchNo: '',
  specification: '',
  inspectNumber: '',
  level: '',
  productionUnit: '',
  supplier: '',
  verifier: '',
  verifyTime: '',
  verifyDept: '',
  remark: ''
})

// 获取当前页面dom，并转换为pdf在新窗口打开
const printDom = async (data) => {
  dataItem.value = data
  await nextTick()
  let pdfP = new PDFPlugin('demo');
  const config = { pattern: 1 };
  const target = document.getElementById('content');
  target && await pdfP.add(target, config.pattern);
  pdfP.open();
};

defineExpose({
  printDom
})
</script>

<style lang="less" scoped>
.a4-size {
  width: 210mm;
  height: 297mm;
  background: #fff;
  padding: 16px;
  .mytable {
    width: 100%;
    margin-top: 8px;
    // table-layout:auto;
    td {
      border: 1px solid #000000;
      padding: 8px;
      font-size: 18px;
      font-weight: bold;
      width: 35%;
      // 换行
      word-break: break-all;
    }
    .title {
      width: 15%;
    }
  }
}
#content {
  width: 760px;
  height: 100%;
  overflow: auto;
}
</style>