<!-- 标本出库计划详情 -->
<template>
  <DetailView
    :title="t('标本出库计划详情')"
    :headerTitle="t('流程进度')"
    :tagItem="{
      value: infoData?.sampleAuditDetailInfo?.auditResult?.value,
      key: infoData?.sampleAuditDetailInfo?.auditResult?.name,
      className: 'tag-1',
    }"
    :backFn="backFn"
    :showStep="activeKey != '2'"
    :stepItems="stepItemsCmp"
    :cardItems="cardItemCmp">
    <template #header>
      <div style="width: 100%">
        <Tabs v-model:activeKey="activeKey">
          <TabPane v-for="item in tabItems" :key="item.key" :tab="item.name"></TabPane>
        </Tabs>
      </div>
    </template>
  </DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn, BMTable } from '@bmos/components';
  import { Tabs, TabPane, DescriptionsItem, Descriptions, message } from 'ant-design-vue';
  import { getSampleDeliveryPlanDetailList, getSampleDeliveryPlanDetail } from '@/services';
  import { descriptionItems, columns } from './outboundInfo';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { computed } from 'vue';

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const router = useRouter();

  const backFn = () => {
    router.back();
  };

  const route = useRoute();
  const outPlanBatchNo = computed(() => {
    return route.params.outPlanBatchNo as string;
  });

  const activeKey = ref<any>('1');

  const tabItems = ref<any>([
    {
      key: '1',
      name: t('基础信息'),
    },
    {
      key: '2',
      name: t('标本明细'),
    },
    {
      key: '3',
      name: t('审核信息'),
    },
  ]);

  const stepItems1 = ref<any>([]);
  const stepItems2 = ref<any>([]);

  const stepItemsCmp = computed(() => {
    if (activeKey.value == '1') {
      return stepItems1.value;
    } else if (activeKey.value == '3') {
      return stepItems2.value;
    } else {
      return [];
    }
  });

  const infoData = ref<any>({});

  // 基础信息
  const cardItems = ref([
    {
      title: t('出库信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={descriptionItems.length} bordered={true} layout={'vertical'}>
            {descriptionItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn ? item.renderFn(infoData.value.basicInfo) : infoData.value.basicInfo?.[item.field]) ??
                    '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
          <div style={{ marginTop: '16px' }}>{`${t('出库备注')}: ${
            infoData.value.basicInfo?.outPlanRemark ?? '-'
          }`}</div>
        </>
      ),
    },
    {
      title: t('操作记录'),
      slot: () => (
        <>
          <BMTable
            ref='teamTable'
            row-key='id'
            columns={columns}
            search={false}
            showToolBar={false}
            dataSource={infoData.value?.bizOperationLogVOList}></BMTable>
        </>
      ),
    },
  ]);

  // 血浆明细

  const cardItems2 = ref([
    {
      title: t('已选标本'),
      slot: () => (
        <>
          <BMPageComponent
            ref={pageRef.value}
            rowKeys={['id']}
            search={[true]}
            hideRightTree={true}
            showHeader={[false]}
            showToolBars={[true]}
            formProps={[formFirstProps]}
            paginations={[paginationBig]}
            requests={[loadData as DataRequestFn]}
            columns={[columnsFirst]}></BMPageComponent>
        </>
      ),
    },
  ]);

  // 岗位审核
  const cardItems3 = ref([
    {
      title: t('申请详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('申请状态')}>
              {infoData.value.sampleAuditDetailInfo?.approveStatus?.name ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('申请人')}>
              {infoData.value.sampleAuditDetailInfo?.approveBy ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>
              {infoData.value.sampleAuditDetailInfo?.approveDate ?? '-'}
            </DescriptionsItem>
          </Descriptions>
          <div style={{ marginTop: '8px' }}>{`${t('申请备注')}: ${
            infoData.value.sampleAuditDetailInfo?.approveRemark ?? '-'
          }`}</div>
        </>
      ),
    },
    {
      title: t('审核详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>
              {infoData.value.sampleAuditDetailInfo?.auditResult?.name ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>
              {infoData.value.sampleAuditDetailInfo?.auditBy ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>
              {infoData.value.sampleAuditDetailInfo?.auditDate ?? '-'}
            </DescriptionsItem>
          </Descriptions>
          <div style={{ marginTop: '8px' }}>{`${t('审核备注')}: ${
            infoData.value.sampleAuditDetailInfo?.auditRemark ?? '-'
          }`}</div>
        </>
      ),
    },
  ]);

  const cardItemCmp = computed(() => {
    if (activeKey.value == '1') {
      return cardItems.value;
    } else if (activeKey.value == '2') {
      return cardItems2.value;
    } else {
      return cardItems3.value;
    }
  });

  const loadData = async (params: any) => {
    const datas = {
      ...params,
      outPlanBatchNo: outPlanBatchNo.value,
    };
    if (!datas?.outPlanBatchNo) {
      return {
        data: [],
      };
    }
    return await getSampleDeliveryPlanDetailList(datas);
  };

  onMounted(async () => {
    try {
      const { data } = await getSampleDeliveryPlanDetail({ outPlanBatchNo: outPlanBatchNo.value });
      infoData.value = data;
      stepItems1.value = data.processNodeVOList?.map((item: any) => {
        return {
          ...item,
          title: item.name,
          description: item.date,
          status: item.status?.value,
        };
      });
      stepItems2.value = [
        {
          title: `${t('申请人')}${
            data.sampleAuditDetailInfo?.approveBy ? ':' + data.sampleAuditDetailInfo?.approveBy : ''
          }`,
          description: data.sampleAuditDetailInfo?.approveDate,
          status: data.sampleAuditDetailInfo?.approveStatus?.value === 1 ? 2 : 0,
        },
        {
          title: `${t('审核人')}${
            data.sampleAuditDetailInfo?.auditBy ? ':' + data.sampleAuditDetailInfo?.auditBy : ''
          }`,
          description: data.sampleAuditDetailInfo?.auditDate,
          status: data.sampleAuditDetailInfo?.auditResult?.value === 1 ? 2 : 0,
        },
      ];
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped></style>
