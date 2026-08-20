<!-- 详情 -->
<template>
  <DetailView
    :title="t('验收审核详情')"
    :headerTitle="t('标本验收')"
    :tagItem="{
      key: infoData?.acceptanceInfo?.sampleAcceptanceStatus?.name,
      value: infoData?.acceptanceInfo?.sampleAcceptanceStatus?.value,
      className: 'tag-1',
    }"
    :backFn="backFn"
    :showStep="false"
    :cardItems="cardItems">
    <!-- <template #extra>
      <Button type="primary">{{ t('标本验收') }}</Button>
    </template> -->
  </DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { BMTable } from '@bmos/components';
  import { getSampleAcceptanceDetail } from '@/services';
  import { columns } from './data';

  const router = useRouter();

  const backFn = () => {
    router.push({ name: 'AcceptanceAudit' });
  };

  const infoData = ref<any>({});

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <p>{t('标本信息')}</p>
          <Descriptions size='small' column={8} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('浆站出库批号')}>
              {infoData.value.basicInfo?.syncBatchNo ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('来源单位')}>{infoData.value.basicInfo?.originOrg ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本编号起')}>{infoData.value.basicInfo?.sampleNoUp ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本编号止')}>{infoData.value.basicInfo?.sampleNoDown ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本箱号起')}>{infoData.value.basicInfo?.boxIdUp ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本箱号止')}>{infoData.value.basicInfo?.boxIdDown ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期起')}>{infoData.value.basicInfo?.slurryDateUp ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期止')}>
              {infoData.value.basicInfo?.slurryDateDown ?? '-'}
            </DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{t('运输信息')}</p>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('起运时间')}>{infoData.value.transportInfo?.beginTime ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('运输时间')}>
              {infoData.value.transportInfo?.transitTime ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('运输温度')}>
              {infoData.value.transportInfo?.temperature ?? '-'}
            </DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('验收详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={8} bordered={true} layout={'vertical'}>
            {getWarehouseConfigByCode.value ? (
              <DescriptionsItem label={t('入库仓库')}>
                {infoData.value.acceptanceInfo?.warehouse?.name ?? '-'}
              </DescriptionsItem>
            ) : (
              <></>
            )}
            <DescriptionsItem label={t('验收状态')}>
              {infoData.value.acceptanceInfo?.sampleAcceptanceStatus?.name ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('验收人')}>
              {infoData.value.acceptanceInfo?.acceptanceBy ?? '-'}
            </DescriptionsItem>
            <DescriptionsItem label={t('验收日期')}>
              {infoData.value.acceptanceInfo?.acceptanceDate ?? '-'}
            </DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{`${t('验收备注')}: ${
            infoData.value.acceptanceInfo?.acceptanceRemark ?? '-'
          }`}</p>
          <p>{`${t('审核备注')}: ${infoData.value.acceptanceInfo?.acceptanceAuditRemark ?? '-'}`}</p>
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
          dataSource={infoData.value?.bizOperationLogVOList}></BMTable>
      ),
    },
  ]);

  const route = useRoute();

  onMounted(async () => {
    try {
      const { data } = await getSampleAcceptanceDetail({ syncBatchNo: route.params.syncBatchNo });
      infoData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped></style>
