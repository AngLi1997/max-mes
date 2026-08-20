<template>
  <div class="report_form_container" id="report_form_loading">
    <div class="report_form_common report_form_header" v-if="edit">
      <Row style="flex: 1; padding: 0">
        <Col :span="12">
          <div class="title-container" v-if="!AREA_STATUS.SET_PRINT_AREA">
            <div class="title-content" v-if="showTitleContent">
              <Button
                type="primary"
                @click="uploadTemplate"
                :disabled="!STATUS.export">
                {{ t('导入模板') }}
              </Button>
              <Button @click="setPrintArea">{{ t('设置打印区域') }}</Button>
            </div>
            <div class="title-content title-content-right">
              <slot name="title"></slot>
            </div>
          </div>
        </Col>
        <Col :span="12">
          <div class="toolbar-container" v-if="!AREA_STATUS.SET_PRINT_AREA">
            <slot name="toolbar"></slot>
          </div>
          <div class="toolbar-container" v-else>
            <Button type="primary" danger @click="clearAllArea">
              {{ t('清除所有') }}
            </Button>
            <Button @click="cancelEditPrintArea">{{ t('取消') }}</Button>
            <Button @click="undoPreviousStep">{{ t('后退') }}</Button>
            <Button @click="redoPreviousStep">{{ t('前进') }}</Button>
            <Button type="primary" @click="saveRangeToPrintArea">
              {{ t('保存') }}
            </Button>
            <Button type="primary" @click="confirmEditPrintArea">
              {{ t('确定') }}
            </Button>
          </div>
        </Col>
      </Row>
    </div>
    <div id="luckysheet" class="report-form_luckysheet"></div>
  </div>
</template>
<script setup lang="ts">
  import { Row, Col, Button } from 'ant-design-vue';
  import { ReportProps } from './props/report';
  import { t } from '@bmos/i18n';
  import { printTemplate, saveTemplate } from './js/luckysheet';
  import { useSheet } from './hooks/useSheet';
  import { useManage } from './hooks/useManage';
  import { useFile } from './hooks/useFile';

  const props = defineProps(ReportProps);
  const manage = useManage();
  const sheet = useSheet(manage, props);
  const {
    getSheetData,
    setColumnWidth,
    initFile,
    init,
    loadSheetData,
    STATUS,
    loadFileToExcel,
    find
  } = sheet;
  const {
    downloadExcel,
    confirmEditPrintArea,
    cancelEditPrintArea,
    saveRangeToPrintArea,
    setPrintArea,
    AREA_STATUS,
    clearAllArea,
    getSheetDataFormStream,
    getPrintArea,
    undoPreviousStep,
    redoPreviousStep,
  } = manage;
  const { uploadTemplate } = useFile(props);
  onMounted(() => {
    window.luckysheet.create({
      container: 'luckysheet', //luckysheet is the container id
      showinfobar: false,
      showtoolbar: props.edit,
      data: [
        {
          // ...config,
          data: [],
          // name: config.name,
        },
      ],
    });
  });

  const getData = () => {
    return saveTemplate(getPrintArea());
  };

  defineExpose({
    export: downloadExcel,
    print: printTemplate,
    init,
    initFile,
    getData: getData,
    getAllsheet: getSheetData,
    setColumnWidth,
    refresh: loadSheetData,
    getSheetDataFormStream,
    loadFileToExcel,
    setPrintArea,
    find
  });
</script>

<style lang="less" scoped>
  .report_form_container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    background-color: #ffffff;
    padding-inline: var(--gap-component);
    padding-block-start: var(--gap-component);
  }
  .report_form_common {
    display: flex;
    justify-content: flex-end;
    :deep(.lims-col) {
      padding: 0 !important;
    }
  }
  .report_form_header {
    padding-bottom: var(--gap-component);
  }
  .report_form_footer {
    padding-bottom: 12px;
  }
  .report-form_luckysheet {
    height: 100%;
    width: 100%;
    position: relative;
    flex: 1;
  }
  .title-content {
    display: flex;
    column-gap: var(--gap-component);
  }
  .toolbar-container {
    display: flex;
    justify-content: end;
    column-gap: var(--gap-component);
  }
</style>
