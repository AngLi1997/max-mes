<!-- 放行单详情 -->
<template>
  <DetailView
    :headerTitle="t('流程进度')"
    :tagItem="{
      value: infoData.auditStatus?.value,
      key: infoData.auditStatus?.name,
      className: 'tag-1',
    }"
    :showBreadcrumb="false"
    :stepItems="stepItems"
    :cardItems="cardItems">
    <template #extra>
      <Button @click="back">{{ t('返回') }}</Button>
    </template>
  </DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { BMTable, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { onMounted } from 'vue';
  import { getQualityGuaranteeReleaseDetail, getQualityGuaranteeReleaseOperationLog } from '@/services';

  defineOptions({
    name: 'ViewCom',
    inheritAttrs: false,
  });

  const router = useRouter();

  const back = () => {
    router.back();
  };

  const columns: TableColumn[] = [
    {
      title: t('操作人'),
      dataIndex: 'createBy',
    },
    {
      title: t('操作日期'),
      dataIndex: 'createTime',
    },
    {
      title: t('操作事项'),
      dataIndex: 'content',
    },
    {
      title: t('操作备注'),
      dataIndex: 'remark',
    },
  ];

  const stepItems = ref([
    {
      title: t('创建人'),
      description: '',
    },
    {
      title: t('审核人'),
      description: '',
    },
  ]);

  const infoData = ref<any>({});
  const tableData = ref<any>([]);
  // 基础信息
  const cardItems = ref([
    {
      title: t('放行单详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={10} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('文件编号')}>{infoData.value?.fileNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('放行单编号')}>{infoData.value?.passNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('单采血浆站名称')}>{infoData.value?.originOrg ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('检品批号')}>{infoData.value?.inWarehouseBatchNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('核查批号')}>{infoData.value?.checkNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('报告单编号')}>{infoData.value?.reportNo ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{t('放行单数据')}</p>
          <Descriptions size='small' column={6} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('核查份数')}>{infoData.value?.checkNumber ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('合格份数')}>{infoData.value?.passNum ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('不合格份数')}>{infoData.value?.unPassNum ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('再次核查份数')}>{infoData.value?.unResNum ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('审核信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{infoData.value?.auditStatus?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{infoData.value?.auditUser ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>{infoData.value?.auditTime ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('操作记录'),
      slot: () => (
        <BMTable
          ref='teamTable'
          row-key='id'
          columns={columns}
          search={false}
          showToolBar={false}
          dataSource={tableData.value}></BMTable>
      ),
    },
  ]);

  const route = useRoute();

  const initData = async () => {
    const { params } = route;
    try {
      if (params.id) {
        const { data } = await getQualityGuaranteeReleaseDetail(params.id);
        infoData.value = {
          ...data,
          ...data.quarantineSummaryRecord,
          quarantineSummaryRecord: undefined,
        };
        stepItems.value = data.flows?.map((item: any) => {
          return {
            ...item,
            title: item.name,
            description: item.date,
            status: item.status?.value,
          };
        });
        const { data: table } = await getQualityGuaranteeReleaseOperationLog(params.id);
        tableData.value = table;
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  onMounted(async () => {
    await initData();
  });
</script>

<style scoped></style>
