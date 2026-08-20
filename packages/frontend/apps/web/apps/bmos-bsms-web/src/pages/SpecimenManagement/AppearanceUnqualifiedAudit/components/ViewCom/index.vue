<!-- 外观不合格审核 -- 标本详情 -->
<template>
  <DetailView
    :title="t('外观不合格审核详情')"
    :headerTitle="t('标本外观检验')"
    :tagItem="{
      key: info?.auditResult?.name,
      value: info?.auditResult?.value,
      className: 'tag-1',
    }"
    :backFn="backFn"
    :stepItems="stepItems"
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
  import { getAppearanceUnqualifiedDetail } from '@/services';
  import { columns } from './data';

  const router = useRouter();

  const backFn = () => {
    router.push({ name: 'AppearanceUnqualifiedAudit' });
  };

  const stepItems = ref<any[]>([]);

  const status = ref<any>({});

  const info = ref<any>({});

  const tableData = ref<any[]>([]);

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <p>{t('标本信息')}</p>
          <Descriptions size='small' column={10} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('标本编号')}>{info.value?.sampleNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本箱号')}>{info.value?.boxId ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本类型')}>{info.value?.sampleType?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期')}>{info.value?.slurryDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('标本外观')}>{info.value?.sampleAppearance?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('献浆者编号')}>{info.value?.no ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('献浆者姓名')}>{info.value?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('来源单位')}>{info.value?.originOrg ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{t('库存信息')}</p>
          <Descriptions size='small' column={6} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('入库批号')}>{info.value?.inWarehouseBatchNo ?? '-'}</DescriptionsItem>
            {getWarehouseConfigByCode.value ? (
              <DescriptionsItem label={t('入库仓库')}>{info.value?.warehouse?.name ?? '-'}</DescriptionsItem>
            ) : (
              <></>
            )}
            <DescriptionsItem label={t('入库人')}>{info.value?.inWarehouseBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('入库日期')}>{info.value?.inWarehouseDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('货位号')}>{info.value?.cargoSpaceNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('托盘号')}>{info.value?.palletNo ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('申请信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            {/* <DescriptionsItem label={t('申请状态')}>{info.value?.applyStatus ?? '-'}</DescriptionsItem> */}
            <DescriptionsItem label={t('申请人')}>{info.value?.approveBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>{info.value?.approveDate ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>
            {t('备注')}: {info.value?.approveRemark ?? ''}
          </p>
        </>
      ),
    },
    {
      title: t('审核信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{info.value?.auditResult?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{info.value?.auditBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>{info.value?.auditDate ?? '-'}</DescriptionsItem>
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

  onMounted(async () => {
    try {
      if (route.params?.orgSampleNo) {
        const { data } = await getAppearanceUnqualifiedDetail({ orgSampleNo: route.params.orgSampleNo });
        info.value = {
          ...data.sampleBasicInfo,
          ...data.plasmaDonorInfo,
          ...data.inventoryInfo,
          ...data.auditInfo,
        };
        status.value = {
          key: data.auditStatus?.name,
          value: data.auditStatus?.value,
        };
        stepItems.value = data.processNodeVOList?.map((item: any) => {
          return {
            ...item,
            title: item.name,
            description: item.date,
            status: item.status?.value,
          };
        });
        tableData.value = data.bizOperationLogVOList;
      }
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped></style>
