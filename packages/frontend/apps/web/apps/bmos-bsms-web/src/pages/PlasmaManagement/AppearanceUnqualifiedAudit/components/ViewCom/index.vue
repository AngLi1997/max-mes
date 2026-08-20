<!-- 外观不合格审核 -- 血浆详情 -->
<template>
  <DetailView
    :title="t('外观不合格审核详情')"
    :headerTitle="t('血浆外观检验')"
    :tagItem="status"
    :stepItems="stepItems"
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
  import { getAppearanceAuditDetail } from '@/services';
  import { columns } from './data';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
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
          <p>{t('血浆信息')}</p>
          <Descriptions size='small' column={10} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('血浆编号')}>{info.value?.plasmaNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆箱/托盘号')}>{info.value?.containerNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('重量')}>{info.value?.weight ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆类型')}>{info.value?.plasmaType?.label ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('免疫类型')}>{info.value?.immunityType ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('采浆日期')}>{info.value?.slurryDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血浆外观')}>{info.value?.appearanceResult?.label ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('献浆者编号')}>{info.value?.plasmaDonorNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('献浆者姓名')}>{info.value?.plasmaDonorName ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('来源单位')}>{info.value?.originOrg ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{t('库存信息')}</p>
          <Descriptions size='small' column={6} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('入库批号')}>{info.value?.inWarehouseBatchNo ?? '-'}</DescriptionsItem>
            {getWarehouseConfigByCode.value ? (
              <DescriptionsItem label={t('入库仓库')}>{info.value?.warehouse?.label ?? '-'}</DescriptionsItem>
            ) : (
              <></>
            )}

            <DescriptionsItem label={t('入库人')}>{info.value?.inWarehouseBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('入库日期')}>{info.value?.inWarehouseDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('货位号')}>{info.value?.cargoSpaceNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('托盘号')}>{info.value?.bigContainerNo ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('申请信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            {/* <DescriptionsItem label={t('申请状态')}>{info.value?.applyStatus?.label ?? '-'}</DescriptionsItem> */}
            <DescriptionsItem label={t('申请人')}>{info.value?.applyBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>{info.value?.applyTime ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style={{ marginTop: '16px' }}>
            {t('备注')}: {info.value?.applyRemark ?? ''}
          </p>
        </>
      ),
    },
    {
      title: t('审核信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{info.value?.auditStatus?.label ?? '-'}</DescriptionsItem>
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

  onMounted(async () => {
    try {
      if (route.params?.plasmaOrgNo) {
        const { data } = await getAppearanceAuditDetail(route.params?.plasmaOrgNo as string);
        info.value = {
          ...data.baseInfo,
          ...data.inventoryInfo,
          ...data.applyInfo,
          ...data.auditInfo,
        };
        status.value = {
          key: data.auditStatus?.name,
          value: data.auditStatus?.value,
        };
        stepItems.value = data.processNodeList?.map((item: any) => {
          return {
            ...item,
            title: item.name,
            description: item.date,
            status: item.status?.value,
          };
        });
        tableData.value = data.operateInfoList;
      }
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped></style>
