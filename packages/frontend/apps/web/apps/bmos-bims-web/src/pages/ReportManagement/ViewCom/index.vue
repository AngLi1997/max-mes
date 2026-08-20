<template>
  <DetailView :title="t('检验报告管理详情')" :headerTitle="t('检验报告')" :showStep="false" :cardItems="cardItemsCmp">
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
  import { descriptionItems, formProps, columns } from './data';
  import {
    getInspectionReportManagementInfo,
    getInspectionReportManagementReportInfo,
    getInspectionReportManagementNegativeInfo,
  } from '@/services';
  import { t } from '@bmos/i18n';
  import { Tabs, TabPane, Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { BMTable, BMForm } from '@bmos/components';

  const route = useRoute();

  const cardItemsCmp = computed(() => {
    if (activeKey.value == '1') {
      return cardItems.value;
    } else {
      return cardItems2.value;
    }
  });

  const activeKey = ref<any>('1');

  const tabItems = ref<any>([
    {
      key: '1',
      name: t('基础信息'),
    },
    {
      key: '2',
      name: t('检验报告'),
    },
  ]);

  const loadData = async (params: any) => {
    return await getInspectionReportManagementNegativeInfo({
      ...params,
      id: route.params.id,
    });
  };

  const baseInfo = ref<any>({});

  // 基础信息
  const cardItems = ref([
    {
      title: t('出库信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            {descriptionItems.map(item => (
              <DescriptionsItem label={item.label}>
                {item.renderFn ? item.renderFn(baseInfo.value) : baseInfo.value?.[item.field]}
              </DescriptionsItem>
            ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('不合格信息'),
      slot: () => (
        <>
          <BMTable row-key='id' columns={columns} search={false} showToolBar={false} dataRequest={loadData}></BMTable>
        </>
      ),
    },
  ]);

  // 检验报告
  const cardItems2 = ref([
    {
      slot: () => (
        <>
          {/* @ts-ignore */}
          <BMForm {...formProps}></BMForm>
        </>
      ),
    },
  ]);

  onMounted(async () => {
    formProps.initialValues = {};
    try {
      const { data } = await getInspectionReportManagementInfo(route.params.id as string);
      const { data: data2 } = await getInspectionReportManagementReportInfo(route.params.id as string);
      baseInfo.value = data;

      formProps.initialValues = data2;
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
      // back();
    }
  });
</script>

<style scoped></style>
