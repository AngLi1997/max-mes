<!-- 已入库标本详情 -->
<template>
  <DetailView
    ref="detail"
    :showBreadcrumb="false"
    :headerTitle="t('已入库标本详情')"
    :menuItems="menuItems"
    :stepItems="stepItems"
    :cardItems="cardItems">
    <!-- <template #contentLeft="{ cardRefs }">
      <Tabs v-model:activeKey="activeKey" tabPosition="left" @change="val => changeType(val, cardRefs)">
        <TabPane v-for="item in tabItems" :tab="item.label" :key="item.key"></TabPane>
      </Tabs>
    </template> -->
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
  import { getSampleInWarehouseDetail } from '@/services';
  import { BMTable } from '@bmos/components';

  const route = useRoute();
  const router = useRouter();

  const {
    basicItems,
    donorItems,
    storageCheckItems,
    storageItems,
    inspectionItems,
    inspectionResultItems,
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
      label: t('入库验收信息'),
      key: 2,
    },
    {
      label: t('库存信息'),
      key: 3,
    },
    {
      label: t('检验信息'),
      key: 4,
    },
    {
      label: t('检验结果'),
      key: 5,
    },
    {
      label: t('维护信息'),
      key: 6,
    },
    {
      label: t('操作信息'),
      key: 7,
    },
  ]);

  const detail = ref<any>(null);

  const stepItems = ref([]);

  const infoData = ref<any>({});

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            {basicItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.sampleBasicInfo)
                    : infoData.value?.sampleBasicInfo?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('献浆者信息'),
      slot: () => (
        <div style='width: 100%;'>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            {donorItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.plasmaDonorInfo)
                    : infoData.value?.plasmaDonorInfo?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
        </div>
      ),
    },
    {
      title: t('入库验收信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
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
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
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
      title: t('检验信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            {inspectionItems
              .filter(item => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn
                    ? item.renderFn(infoData.value?.inspectionInfo)
                    : infoData.value?.inspectionInfo?.[item.field]) ?? '-'}
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
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
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
      title: t('维护信息'),
      slot: () => (
        <>
          {/* @ts-ignore */}
          <BMTable
            row-key='id'
            columns={maintainColumns}
            search={false}
            showToolBar={false}
            dataSource={infoData.value?.maintainInfo}></BMTable>
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
            dataSource={infoData.value?.bizOperationLogVOList}></BMTable>
        </>
      ),
    },
  ]);

  const back = () => {
    router.back();
  };

  watch(
    () => router.currentRoute.value,
    async () => {
      await initData();
    },
  );

  const initData = async () => {
    try {
      const { data } = await getSampleInWarehouseDetail(route.params);
      infoData.value = data;
      stepItems.value = data.processNodeVOList?.map((item: any) => {
        return {
          ...item,
          title: item.name,
          description: (
            <div style={{ width: item.endDate ? '300px' : '150px' }}>{`${item.date ?? ''}${
              item.endDate ? ' - ' + item.endDate : ''
            }`}</div>
          ),
          status: item.status?.value,
        };
      });
    } catch (error: any) {
      error.message && message.error(error.message);
      // back();
    }
  };

  onMounted(async () => {
    await initData();
  });
</script>

<style lang="less" scoped>
  :deep(.bsms-descriptions-view) {
    overflow: auto;
  }
</style>
