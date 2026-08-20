<template>
  <DetailView
    :title="t('待入库血浆管理详情')"
    :headerTitle="t('血浆接收')"
    :tagItem="status"
    :showStep="false"
    :cardItems="cardItems"
    @back="back">
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
  import { getPlasmaToStorageDetail } from '@/services';
  import { columns } from './data';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
  };

  const status = ref<any>({});

  const info = ref<any>({});

  const tableData = ref<any[]>([]);

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <p>{t('血浆信息')}</p>
          <Descriptions size='small' column={8} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('浆站出库批号')}>{info.value?.syncBatchNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('来源单位')}>{info.value?.originOrg ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆编号起')}>{info.value?.plasmaNoUp ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆编号止')}>{info.value?.plasmaNoDown ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆托盘号起')}>{info.value?.containerNoUp ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆托盘号止')}>{info.value?.containerNoDown ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期起')}>{info.value?.slurryDateBegin ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期止')}>{info.value?.slurryDateEnd ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{t('运输信息')}</p>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('起运时间')}>{info.value?.beginTime ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('运输时间')}>{info.value?.transitTime ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('运输温度')}>{info.value?.temperature ?? '-'}</DescriptionsItem>
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
              <DescriptionsItem label={t('入库仓库')}>{info.value?.warehouse?.label ?? '-'}</DescriptionsItem>
            ) : (
              <></>
            )}
            <DescriptionsItem label={t('验收状态')}>{info.value?.acceptanceStatus?.label ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('验收人')}>{info.value?.acceptanceByName ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('验收日期')}>{info.value?.acceptanceDate ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{`${t('验收备注')}: ${info.value?.acceptanceRemark ?? '-'}`}</p>
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

  onMounted(async () => {
    try {
      if (route.params?.syncBatchNo) {
        const { data } = await getPlasmaToStorageDetail(route.params.syncBatchNo as string);
        info.value = {
          ...data.baseInfo,
          ...data.transportInfo,
          ...data.acceptanceInfo,
        };
        status.value = {
          key: data.acceptanceStatus?.name,
          value: data.acceptanceStatus?.value,
        };
        tableData.value = data.operateInfoList;
      }
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped></style>
