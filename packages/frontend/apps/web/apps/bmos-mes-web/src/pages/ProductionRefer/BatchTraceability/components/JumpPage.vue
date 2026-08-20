<template>
  <div class="details-container">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('批次追溯') }}</breadcrumb-item>
          <breadcrumb-item>
            {{ t('查看详情') }}
          </breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="action">
        <Space>
          <Button @click="back">{{ t('返回') }}</Button>
        </Space>
      </Col>
    </Row>
    <div class="productionInfo">
      <BMTableTitle style="margin-bottom: 10px" :title="t('生产信息')"></BMTableTitle>
      <Descriptions :column="4">
        <DescriptionsItem v-for="item in basicItems" :key="item.label" :label="item.label">
          {{ props.rowData[item?.field] ?? '-' }}
        </DescriptionsItem>
      </Descriptions>
    </div>
    <div class="processNodes">
      <Tabs v-model:activeKey="activeKey" @change="tabChange">
        <TabPane v-for="item in tabList" :key="item.key" :tab="item.label"></TabPane>
      </Tabs>
      <div v-if="activeKey !== '7'" class="table">
        <Table :activeKey="activeKey" :rowData="rowData"></Table>
      </div>
      <div v-else class="batchRecord">
        <!-- 批记录展示页面 -->
        <BatchRecord :rowData="rowData"></BatchRecord>
      </div>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import {
    Row,
    Col,
    Breadcrumb,
    BreadcrumbItem,
    Space,
    Button,
    Descriptions,
    DescriptionsItem,
    Tabs,
    TabPane,
  } from 'ant-design-vue';
  import { BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import Table from './Table.vue';
  import BatchRecord from './BatchRecord.vue';

  const emit = defineEmits(['back']);
  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const activeKey = ref('1');
  const tabList = ref([
    {
      key: '1',
      label: t('执行信息'),
    },
    {
      key: '2',
      label: t('物料追溯'),
    },
    {
      key: '3',
      label: t('设备使用'),
    },
    {
      key: '4',
      label: t('房间清场信息'),
    },
    {
      key: '5',
      label: t('检验信息'),
    },
    {
      key: '6',
      label: t('异常信息'),
    },
    {
      key: '7',
      label: t('批记录'),
    },
  ]);
  const basicItems = reactive<any>([
    {
      label: t('产品名称'),
      field: 'productName',
    },
    {
      label: t('产品编码'),
      field: 'productMergeCode',
    },
    {
      label: t('产品规格'),
      field: 'productSpecification',
    },
    {
      label: t('工艺名称'),
      field: 'processName',
    },
    {
      label: t('生产批号'),
      field: 'batchNo',
    },
    {
      label: t('生产产线'),
      field: 'productionLineName',
    },
    {
      label: t('生产开始时间'),
      field: 'startTime',
    },
    {
      label: t('生产结束时间'),
      field: 'endTime',
    },
  ]);
  // 返回
  const back = () => {
    emit('back');
  };
  // tab切换
  const tabChange = async (val: any) => {
    activeKey.value = val;
  };
</script>
<style lang="less" scoped>
  .details-container {
    width: 100%;
    height: 100%;
    position: relative;
    .header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
      }
    }
    .action {
      text-align: right;
    }
    .productionInfo {
      width: 100%;
      height: 120px;
      background-color: white;
      padding: 2px 16px;
      margin-bottom: 10px;
    }
    .processNodes {
      width: 100%;
      height: calc(100% - 190px);
      background-color: white;
      padding: 5px 16px 0 16px;
      display: flex;
      flex-direction: column;
      .table {
        flex: 1;
        overflow-y: scroll;
      }
      .batchRecord {
        height: calc(100% - 60px);
      }
    }
  }
</style>
