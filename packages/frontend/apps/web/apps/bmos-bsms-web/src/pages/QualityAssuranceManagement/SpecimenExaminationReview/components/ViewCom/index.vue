<template>
  <DetailView
    :title="t('标本请验审核详情')"
    :headerTitle="t('流程进度')"
    :tagItem="{
      key: infoData?.auditResult?.name,
      value: infoData?.auditResult?.value,
      className: 'tag-1',
    }"
    :stepItems="stepItems"
    :cardItems="cardItems"
    @back="back"></DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { BMTable } from '@bmos/components';
  import { getExaminationDetail } from '@/services';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { columns } from './data';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
  };

  // 请验操作
  // const pleaseVerifyRef = ref();

  // const openPleaseVerify = (data: any) => {
  //   pleaseVerifyRef.value?.openModal(data);
  // };

  const tableData = ref<any[]>([]);

  const infoData = ref<any>({});

  const stepItems = ref<any>([]);

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <p>{t('标本信息')}</p>
          <Descriptions size='small' column={10} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('入库批号')}>{infoData.value?.inWarehouseBatchNo || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('来源单位')}>{infoData.value?.originOrg || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('数量')}>{infoData.value?.inspectionNum || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本类型')}>{infoData.value?.sampleType?.name || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本编号起')}>{infoData.value?.sampleNoUp || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本编号止')}>{infoData.value?.sampleNoDown || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本箱号起')}>{infoData.value?.boxIdUp || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本箱号止')}>{infoData.value?.boxIdDown || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期起')}>{infoData.value?.slurryDateUp || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期止')}>{infoData.value?.slurryDateDown || '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('申请信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={2} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('申请人')}>{infoData.value?.approveBy || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>{infoData.value?.approveDate || '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{`${t('备注')}: ${infoData.value?.approveRemark || '-'}`}</p>
        </>
      ),
    },
    {
      title: t('审核信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={4} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{infoData.value?.auditResult?.name || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{infoData.value?.auditBy || '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>{infoData.value?.auditDate || '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{`${t('备注')}: ${infoData.value?.auditRemark || '-'}`}</p>
        </>
      ),
    },
    {
      title: t('操作记录'),
      slot: () => (
        <div style={{ height: '300px' }}>
          <BMTable
            ref='teamTable'
            row-key='id'
            columns={columns}
            search={false}
            showToolBar={false}
            dataSource={tableData.value}></BMTable>
        </div>
      ),
    },
  ]);

  const initFn = async () => {
    try {
      const { data } = await getExaminationDetail({ inspectionBatchNo: route.params.inspectionBatchNo });
      infoData.value = {
        ...data.sampleAuditDetailInfo,
        ...data.sampleBatchBasicInfo,
      };
      tableData.value = data.bizOperationLogVOList;
      stepItems.value = data.processNodeVOList?.map((item: any) => {
        return {
          ...item,
          title: item.name,
          description: item.date,
          status: item.status?.value,
        };
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  onMounted(async () => {
    await initFn();
  });
</script>

<style lang="less" scoped></style>
