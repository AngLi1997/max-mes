<!-- 血浆详情 -->
<template>
  <DetailView
    ref="detail"
    :showBreadcrumb="false"
    :headerTitle="t('血浆详情')"
    :menuItems="menuItems"
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
  import { t } from '@bmos/i18n';
  import { useDetail } from './data';
  import { getPlasmaInventoryDetail, getPlasmaInventoryOperation } from '@/services';
  import { BMTable } from '@bmos/components';

  const route = useRoute();
  const router = useRouter();

  const {
    basicItems,
    donorItems,
    storageCheckItems,
    storageItems,
    inspectionResultItems,
    quarantinePeriodItems,
    sortingItems,
    maintainColumns,
    operationColumns,
  } = useDetail();

  const menuItems = ref([
    {
      label: t('基础信息'),
      key: 0,
    },
    {
      label: t('献浆者信息'),
      key: 1,
    },
    {
      label: t('验收信息'),
      key: 2,
    },
    {
      label: t('库存信息'),
      key: 3,
    },
    {
      label: t('检验结果'),
      key: 4,
    },
    {
      label: t('检疫期信息'),
      key: 5,
    },
    {
      label: t('分拣信息'),
      key: 6,
    },
    {
      label: t('维护信息'),
      key: 7,
    },
    {
      label: t('操作信息'),
      key: 8,
    },
  ]);

  const detail = ref<any>(null);

  const stepItems = ref([]);

  const infoData = ref<any>({});

  const logList = ref<any>([]);

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={8} bordered={true} layout={'vertical'}>
            {basicItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn ? item.renderFn(infoData.value?.baseInfo) : infoData.value?.baseInfo?.[item.field]) ??
                    '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('献浆者信息'),
      slot: () => (
        <div style='width: 100%'>
          <Descriptions size='small' column={8} bordered={true} layout={'vertical'}>
            {donorItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label} {...(item.componentProps ?? {})}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.donorInfo)
                    : infoData.value?.donorInfo?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </div>
      ),
    },
    {
      title: t('验收信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={8} bordered={true} layout={'vertical'}>
            {storageCheckItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.acceptanceInfo)
                    : infoData.value?.acceptanceInfo?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('库存信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={9} bordered={true} layout={'vertical'}>
            {storageItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.inventoryInfo)
                    : infoData.value?.inventoryInfo?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('检验结果'),
      slot: () => (
        <>
          <Descriptions size='small' column={12} bordered={true} layout={'vertical'}>
            {inspectionResultItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.examinationResultInfo)
                    : infoData.value?.examinationResultInfo?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('检疫期信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={7} bordered={true} layout={'vertical'}>
            {quarantinePeriodItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.quarantineInfo)
                    : infoData.value?.quarantineInfo?.[item.field]) || '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('分拣信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={6} bordered={true} layout={'vertical'}>
            {sortingItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.sortingInfo)
                    : infoData.value?.sortingInfo?.[item.field]) || '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('维护信息'),
      slot: () => (
        <>
          {/* @ts-ignore */}
          <BMTable
            row-key='id'
            columns={maintainColumns}
            search={false}
            showToolBar={false}
            dataSource={infoData.value?.maintainInfos}></BMTable>
        </>
      ),
    },
    {
      title: t('操作记录'),
      slot: () => (
        <>
          {/* @ts-ignore */}
          <BMTable
            row-key='id'
            columns={operationColumns}
            search={false}
            showToolBar={false}
            dataSource={logList.value}></BMTable>
        </>
      ),
    },
  ]);

  const back = () => {
    router.back();
  };

  const initData = async () => {
    try {
      const { data } = await getPlasmaInventoryDetail(route.params);
      const { data: tableList } = await getPlasmaInventoryOperation(route.params);
      stepItems.value = data?.processNodeList?.map((item: any) => {
        return {
          ...item,
          title: item.name,
          description: item.date,
          status: item.status?.value,
        };
      });
      infoData.value = {
        ...data,
        quarantineInfo: {
          ...data.quarantineInfo,
          plasmaNo: data.baseInfo.plasmaNo,
        },
      };
      logList.value = tableList;
    } catch (error: any) {
      error.message && message.error(error.message);
      // back();
    }
  };

  watch(
    () => router.currentRoute.value,
    async () => {
      await initData();
    },
  );

  onMounted(async () => {
    await initData();
  });
</script>

<style lang="less" scoped>
  :deep(.bsms-descriptions-view) {
    overflow: auto;
  }
</style>
