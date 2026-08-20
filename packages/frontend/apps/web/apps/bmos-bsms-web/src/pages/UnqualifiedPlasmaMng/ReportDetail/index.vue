<!-- 不合格血浆核查报告-->
<template>
  <DetailView
    :title="t('不合格报告详情')"
    :headerTitle="t('不合格血浆核查报告')"
    :tagItem="status"
    :showStep="activeKey === '1'"
    :stepItems="stepItems"
    :cardItems="cardItemCmp"
    @back="back">
    <template #header>
      <div style="width: 100%">
        <Tabs v-model:activeKey="activeKey" @change="changeType">
          <TabPane v-for="item in tabItems" :key="item.key" :tab="item.name"></TabPane>
        </Tabs>
      </div>
    </template>
  </DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { Tabs, TabPane, Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { BMTable, DataRequestFn, BMForm } from '@bmos/components';
  import {
    getUnqualifiedPlasmaAffectedPlasma,
    unqualifiedPlasmaReportDetail,
    unqualifiedCheckRecordLogList,
  } from '@/services';
  import { columns, columns2, formProps } from './data';
  import { computed } from 'vue';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
  };

  const activeKey = ref<any>('1');

  const tabItems = ref<any>([
    {
      key: '1',
      name: t('基础信息'),
    },
    {
      key: '2',
      name: t('不合格核查报告'),
    },
  ]);

  const changeType = async (val: any) => {
    console.log(val);
  };

  const stepItems = ref<any[]>([]);

  const status = ref<any>({});

  const info = ref<any>({});

  const tableData = ref<any[]>([]);

  const loadData = async (params: any) => {
    const { data } = await getUnqualifiedPlasmaAffectedPlasma({
      ...params,
      unqualifiedPlasmaInfoId: route.query?.unqualifiedPlasmaInfoId,
    });

    return {
      data: data.detailList,
    };
  };

  const cardItemCmp = computed(() => {
    return activeKey.value === '1' ? cardItems.value : cardItems2.value;
  });

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <p>{t('核查信息')}</p>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('来源单位')}>{info.value?.originOrg ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('不合格编号')}>{info.value?.no ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('献浆者姓名')}>{info.value?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('献浆者编号')}>{info.value?.plasmaDonorNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('血型')}>{info.value?.bloodType ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('不合格项目')}>{info.value?.unqualifiedItems ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('阳性不合格')}>{info.value?.isPositive ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('拒绝日期')}>{info.value?.rejectDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('不合格来源')}>{info.value?.unqualifiedOrigin ?? '-'}</DescriptionsItem>
          </Descriptions>
          <p style='margin-top: 16px'>{t('受影响血浆')}</p>
          <div style={{ height: '300px' }}>
            <BMTable
              ref='teamTable'
              row-key='id'
              columns={columns2}
              search={false}
              showToolBar={false}
              dataRequest={loadData as DataRequestFn}></BMTable>
          </div>
        </>
      ),
    },
    {
      title: t('申请信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={4} bordered={true} layout={'vertical'}>
            {/* <DescriptionsItem label={t('申请状态')}>{info.value?.applyDate ?? '-'}</DescriptionsItem> */}
            <DescriptionsItem label={t('申请人')}>{info.value?.applyByName ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>{info.value?.applyDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('送审人')}>{info.value?.sendToAuditByName ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('送审日期')}>{info.value?.sendToAuditTime ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('审核信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={4} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{info.value?.auditStatus ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{info.value?.auditByName ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>{info.value?.auditDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('备注')}>{info.value?.auditRemark ?? '-'}</DescriptionsItem>
          </Descriptions>
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

  const formRef = ref<any>();

  // const setFormModels = (values: any) => {
  //   try {
  //     formRef.value?.setFormModels(values);
  //   } catch (error: any) {
  //     console.error(error);
  //   }
  // };

  // 不合格核查报告
  const cardItems2 = ref([
    {
      slot: () => (
        <div style={{ margin: '20px 20vw' }}>
          <BMForm ref={formRef} {...formProps}></BMForm>
        </div>
      ),
    },
  ]);

  onMounted(async () => {
    formProps.initialValues = {};
    try {
      if (route.query?.reportBillNo) {
        const { data } = await unqualifiedPlasmaReportDetail(route.query?.reportBillNo as string);
        const { data: tableList } = await unqualifiedCheckRecordLogList({
          identify: route.query?.unqualifiedPlasmaInfoId,
        });
        info.value = {
          ...data.checkInfo,
          ...data.applyInfo,
          ...data.auditInfo,
        };

        formProps.initialValues = data.baseInfo;

        stepItems.value =
          data.processNodeList?.map((item: any) => {
            return {
              ...item,
              title: item.name,
              description: item.date,
              status: item.status?.value,
            };
          }) ?? [];
        status.value = {
          key: data.auditStatus?.name,
          value: data.auditStatus?.value,
        };
        tableData.value = tableList;
      }
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
    }
  });
</script>

<style lang="less" scoped></style>
