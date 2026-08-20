<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="toProcessApproval">{{ title || t('异常管理') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('异常批次信息') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="toProcessApproval">{{ t('返回') }}</Button>
    </template>
    <BMTableTitle :title="t('生产批次信息')"></BMTableTitle>
    <BMDescriptions :list="descData" :column="4" :showBottomBorder="false" hasTitle></BMDescriptions>
    <div class="form_box">
      <BMForm ref="myFormRef" v-bind="formProps" @submit="searchClick" @reset="tableReset"></BMForm>
    </div>
    <div style="height: calc(100% - 230px)">
      <BMTable
        ref="tableInstance"
        :data-request="getDatasetPageList"
        :columns="columns"
        :show-tool-bar="false"
        :show-search-border="false"
        row-key="id"
        :search="false"
        showIndex
        :pagination="{
          pageSize: 10,
        }"></BMTable>
    </div>
  </BreadcrumbButton>
  <History
    v-model:historyOpen="historyOpen"
    :businessId="rowData?.id"
    :getApi="reqHistoryList"
    showDetail
    :detailLabel="detailLabel" />
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { onMounted, ref } from 'vue';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { BMForm, BMTable, BMDescriptions, BMTableTitle } from '@bmos/components';
  import { useData } from './hooks/useData';
  import History from '@/components/History/index.vue';
  import { reqHistoryList, reqPlanDetail } from '@/services';

  const detailLabel = ref({
    exceptionType: t('异常类型'),
    exceptionDescription: t('异常描述'),
    recordMode: t('记录方式'),
    recordTime: t('记录时间'),
    productFullName: t('产品名称'),
    reInvestigateReason: t('重新调查原因'),
    batchNo: t('生产批号'),
    processName: t('工艺名称'),
    processVersion: t('工艺版本'),
    procedureName: t('工序名称'),
    procedureStepName: t('工序步骤名称'),
    handleResult: t('处理结果'),
    handleTime: t('处理时间'),
    handleUserName: t('处理人名称'),
    cancelUserName: t('作废人名称'),
    cancelReason: t('作废原因'),
    cancelTime: t('作废时间'),
  }); // 操作历史label
  const {
    columns,
    historyOpen,
    formProps,
    descData,
    searchClick,
    tableReset,
    tableInstance,
    productPlanId,
    rowData,
    myFormRef,
    getDatasetPageList,
  } = useData();

  const router = useRouter();
  const route = useRoute();
  const title = ref();
  // 返回上一个页面
  const toProcessApproval = () => {
    router.back();
  };
  onMounted(async () => {
    title.value = route.query.title as string;
    productPlanId.value = route.query.productPlanId as string;
    const { data } = await reqPlanDetail(productPlanId.value);
    descData.value.map((item: any) => {
      item.value = data[item.value] || '';
    });
  });
</script>
<style scoped lang="less">
  .form_box {
    border-top: 5px solid #f5f7fa;
    border-bottom: 5px solid #f5f7fa;
    margin-bottom: 20px;
    padding-top: 10px;
  }
  .common-history-modal {
    .history-container {
      .mes-steps {
        .mes-steps-item-container .mes-steps-item-title {
          color: var(--bmos-fourth-level-text-color);
        }
      }
    }
  }
</style>
