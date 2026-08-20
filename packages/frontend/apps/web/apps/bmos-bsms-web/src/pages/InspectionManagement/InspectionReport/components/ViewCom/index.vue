<!-- 检验报告详情 -->
<template>
  <DetailView
    :title="t('检验报告详情')"
    :headerTitle="`${t('检验报告')}: ${infoData?.reportNo}`"
    :showStep="false"
    :cardItems="cardItemCmp"
    @back="back">
    <template #header>
      <div style="width: 100%; padding: 0 16px">
        <Tabs v-model:activeKey="activeKey" @change="changeType">
          <TabPane :key="1" :tab="t('基础信息')"></TabPane>
          <TabPane :key="2" :tab="t('检验报告')"></TabPane>
        </Tabs>
      </div>
    </template>
  </DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { BMTable, BMForm } from '@bmos/components';
  import { Tabs, TabPane, DescriptionsItem, Descriptions, message } from 'ant-design-vue';
  import { descriptionItems, columns, formProps } from './data';
  import { getInspectionReportDetail, getInspectionReportDetailList } from '@/services';
  import { t } from '@bmos/i18n';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
  };

  const activeKey = ref<any>(1);

  const loadData = async (params: any): Promise<any> => {
    const data = {
      ...params,
      inspectionBatchNo: route.params.inspectionBatchNo,
    };
    return await getInspectionReportDetailList(data);
  };

  const infoData = ref<any>({});

  const changeType = async (val: any) => {
    if (val === 2) {
      await nextTick();
      setFormModels({
        fileNo: infoData.value?.fileNo,
        reportNo: infoData.value?.reportNo,
        checkBase: infoData.value?.basis,
        conclusion: infoData.value?.testConclusion,
      });
    }
  };

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={descriptionItems.length} bordered={true} layout={'vertical'}>
            {descriptionItems.map(item => (
              <DescriptionsItem label={item.label}>{infoData.value?.[item.field] || '-'}</DescriptionsItem>
            ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('检验不合格信息'),
      slot: () => (
        <>
          <BMTable
            ref='teamTable'
            row-key='id'
            columns={columns}
            search={false}
            showToolBar={false}
            dataRequest={loadData}
            showIndex={true}></BMTable>
        </>
      ),
    },
  ]);

  const formRef = ref<any>();

  const setFormModels = (values: any) => {
    try {
      formRef.value?.setFormModels(values);
    } catch (error: any) {
      console.error(error);
    }
  };

  // 检验报告
  const cardItems2 = ref([
    {
      slot: () => (
        <div style={{ margin: '20px 20vw' }}>
          <BMForm ref={formRef} {...formProps}></BMForm>
        </div>
      ),
    },
  ]);

  const cardItemCmp = computed(() => {
    if (activeKey.value == 1) {
      return cardItems.value;
    } else if (activeKey.value == 2) {
      return cardItems2.value;
    } else {
      return [];
    }
  });

  onMounted(async () => {
    try {
      const { data } = await getInspectionReportDetail({ inspectionBatchNo: route.params.inspectionBatchNo });
      infoData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped>
  :deep(.bsms-tabs-nav) {
    margin: 0;
  }
</style>
