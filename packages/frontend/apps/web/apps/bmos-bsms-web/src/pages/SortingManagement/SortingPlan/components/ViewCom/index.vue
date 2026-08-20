<!-- 详情 -->
<template>
  <DetailView
    :title="t('分拣计划详情')"
    :showHeader="false"
    :showStep="false"
    :cardItems="cardItems"
    @back="back"></DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { useDescriptions, useTable } from './hooks';
  import { getSortingPlanDetail } from '@/services';
  import { t } from '@bmos/i18n';

  const router = useRouter();
  const route = useRoute();

  const query = computed(() => {
    return {
      planBatchNo: route.query.planBatchNo as string,
      itemType: parseInt(route.query.itemType as string) as 1 | 2,
    };
  });

  const back = () => {
    router.back();
  };

  const { descriptionItems } = useDescriptions();

  const { pageRef, columnsFirst, sampleColumns, formFirstProps, paginationFirst, loadData } = useTable(
    query.value.itemType,
  );

  const info = ref<any>({});

  const cardItems = ref([
    {
      title: t('分拣信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={7} bordered={true} layout={'vertical'}>
            {descriptionItems
              .filter(item => (item.vIf ?? true) && (item.showFn ? item.showFn(query.value.itemType) : true))
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {item.render ? item.render(info.value) : info.value?.[item.field]}
                </DescriptionsItem>
              ))}
          </Descriptions>
          <div style={{ marginTop: '16px' }}>{`${t('备注')}: ${info.value?.remark ?? ''}`}</div>
        </>
      ),
    },
    {
      title: query.value.itemType === 1 ? t('已选择血浆') : t('已选择标本'),
      slot: () => (
        // @ts-ignore
        <BMPageComponent
          ref={pageRef}
          rowKeys={['id']}
          search={[true]}
          hideRightTree={true}
          showAction={false}
          showHeader={[false]}
          showToolBars={[false]}
          tableFields={[{ default: { planBatchNo: query.value.planBatchNo } }]}
          formProps={[formFirstProps]}
          paginations={[paginationFirst]}
          scrolls={[{ x: 1000, y: 220 }]}
          requests={[loadData as DataRequestFn]}
          columns={[query.value.itemType === 1 ? columnsFirst : sampleColumns]}></BMPageComponent>
      ),
    },
  ]);

  onMounted(async () => {
    try {
      const { data } = await getSortingPlanDetail(query.value.planBatchNo);
      info.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style lang="less" scoped>
  :deep(.bsms-descriptions .bsms-descriptions-view) {
    border-radius: 0;
  }
  :deep(.bmos-page-component-container) {
    max-height: 520px;
  }
</style>
