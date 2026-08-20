<template>
  <div class="container">
    <Descriptions bordered :column="1">
      <DescriptionsItem v-for="item in basicItems" :key="item.label" :label="item.label">
        {{ props.rowData[item?.field] ?? '' }}
      </DescriptionsItem>
    </Descriptions>
    <div style="margin-top: 10px; margin-bottom: 10px">
      <BMTableTitle :title="t('使用日志模板')" />
    </div>
    <div class="templateContent">
      <div class="infoTitle">
        <div>{{ t('操作名称') }}</div>
        <div>{{ t('模板内容') }}</div>
      </div>
      <div v-for="(item, index) in props.rowData?.useTemplateList" :key="index" class="infoContent">
        <div>{{ item.operateName }}</div>
        <div>{{ item.template }}</div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { Descriptions, DescriptionsItem } from 'ant-design-vue';
  import { BMTableTitle } from '@bmos/components';

  import { t } from '@bmos/i18n';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const basicItems = reactive<any>([
    {
      label: t('设备类型名称'),
      field: 'name',
    },
    {
      label: t('设备信息'),
      field: 'infoPropertyList',
    },
    {
      label: t('设备数据'),
      field: 'dataPropertyList',
    },
    {
      label: t('设备状态'),
      field: 'statusPropertyList',
    },
    {
      label: t('描述'),
      field: 'description',
    },
  ]);
</script>
<style lang="less" scoped>
  .container {
    width: 100%;
    padding: 16px;
    display: flex;
    flex-direction: column;
    .templateContent {
      width: calc(100vw - 520px);
      height: 55px;
      .infoTitle {
        display: flex;
        div {
          height: 55px;
          padding-left: 24px;
          line-height: 52px;
          background-color: rgba(0, 0, 0, 0.02);
          border: 1px solid rgba(5, 22, 38, 0.12);
        }
        > div:nth-child(1) {
          width: 25%;
          min-width: 135px;
          border-right: none;
        }
        > div:nth-child(2) {
          width: 75%;
        }
      }
    }
    .infoContent {
      display: flex;
      div {
        min-height: 55px;
        padding: 16px 24px;
        border: 1px solid rgba(5, 22, 38, 0.12);
        border-top: none;
        word-wrap: break-word;
      }
      > div:nth-child(1) {
        width: 25%;
        min-width: 135px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        border-right: none;
      }
      > div:nth-child(2) {
        width: 75%;
      }
    }
    :deep(.ems-descriptions .ems-descriptions-view) {
      border-radius: 2px;
    }
    :deep(.ems-descriptions-item-label) {
      width: 15%;
      min-width: 135px;
    }
  }
</style>
