<!-- 检验报告 -->
<template>
  <div style="height: 100%;">
    <Row class="header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('检验管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ t('检验查询') }}</breadcrumb-item>
          <breadcrumb-item>{{ t('报告详细') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="header-btn">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
        </Space>
      </Col>
    </Row>
    <LimsCard style="height: calc(100% - 64px - 182px)" :title="t('检验信息')" >
      <div class="table">
        <Descriptions
          size="small"
          :column="3"
        >
          <DescriptionsItem :label="t('检品名称')">{{ infoData.productsName }}</DescriptionsItem>
          <DescriptionsItem :label="t('规格')">{{ infoData.specification }}</DescriptionsItem>
          <DescriptionsItem :label="t('批号')">{{ infoData.batchNo }}</DescriptionsItem>
        </Descriptions>
        <BMTable
          ref="tableRef"
          :data-request="loadData"
          :columns="columns"
          :formProps="formProps"
          :scroll="{ y: 200}"
          :showRefresh="false"
          :search="false"
          :showIndex="false"
          :pagination="false"
        >
        </BMTable>
      </div>
    </LimsCard>
    <LimsCard :title="t('检验结论')" >
      <Descriptions
        size="small"
        :column="3"
      >
        <DescriptionsItem :span="3" :label="t('检验结论')">{{ reportResultInfo.result }}</DescriptionsItem>
        <DescriptionsItem :label="t('报告人')">{{ reportResultInfo.reportName }}</DescriptionsItem>
        <DescriptionsItem :label="t('审核人')">{{ reportResultInfo.auditName }}</DescriptionsItem>
        <DescriptionsItem :label="t('签发人')">{{ reportResultInfo.signName }}</DescriptionsItem>
      </Descriptions>
    </LimsCard>
  </div>
</template>

<script setup lang="tsx">
import { t } from '@bmos/i18n';
import {
  BMTableTitle,
  BMTable,
  BMForm,
  RenderCallbackParams
} from '@bmos/components';
import { onMounted, reactive, ref } from 'vue';
import {
  Descriptions,
  DescriptionsItem,
  RadioGroup,
  Radio,
  message
} from 'ant-design-vue';
import {
  LimsCard
} from '@/components/Card';
import { 
  getCheckOrderReportInfo,
  signCheckOrderReport
} from '@/services/index';
import {
  useTable
} from './hooks/useTable';

const props = defineProps({
  data: {
    type: Object,
    default: () => {
      return {}
    }
  }
})

const infoData = ref<any>({})
const tableData = ref<any>([])
const reportResultInfo = ref<any>({})
const tableRef = ref<any>(null)

const emit = defineEmits(['back'])

const back = () => {
  emit('back')
}

const loadData = async (params: any) => {
  return new Promise(resolve => {
    resolve({
      data: [...tableData.value]
    });
  });
};

const { columns, formProps, rowData } = useTable();

onMounted(async () => {
  try {
    const res = await getCheckOrderReportInfo(props.data.orderNo)
    infoData.value = {
      productsName: res.data.productsName,
      specification: res.data.specification,
      batchNo: res.data.batchNo
    }
    res.data.reportInspectVOList.forEach((item: any) => {
      const reportName = item.reportName
      item.reportAnalyzeVOList.forEach((i: any, index) => {
        tableData.value.push({
          ...i,
          reportName: index === 0 ? `【${reportName}】` : ' ',
          length: item.reportAnalyzeVOList?.length ?? 1
        })
      })
    })
    reportResultInfo.value = res.data.reportResultInfoVO
    tableRef.value?.fetchData()
  } catch(error: any) {
    message.error(error?.message);
  }
})
</script>

<style lang="less" scoped>
.mr-16 {
  margin-right: 16px
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  // background-color: #fff;
  flex-grow: 0;
  width: 100% !important;
  padding-bottom: 12px;
  // margin-bottom: var(--bmos-margin-small);
  .crumb {
    line-height: 36px;
  }
  &-btn {
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
}

.table {
  // background-color: #fff;
  height: 100%;
  padding: var(--bmos-padding-small);
  // overflow: auto;
  .bmos-table {
    height: 94%;
    overflow: auto;
  }
}
</style>