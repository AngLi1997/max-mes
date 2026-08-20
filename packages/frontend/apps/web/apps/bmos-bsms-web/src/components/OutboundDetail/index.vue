<!-- 出库审核详情 -->
<template>
  <DetailView
    :title="props.title"
    :headerTitle="`${t('出库批号')}: ${props.rowData?.batchNo}`"
    :tagItem="{
      value: props.rowData.auditStatus?.value,
      key: props.rowData.auditStatus?.name,
      className: 'tag-1',
    }"
    :showStep="activeKey != '2'"
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
  import { BMPageComponent, DataRequestFn, BMTable } from '@bmos/components';
  import { Tabs, TabPane, DescriptionsItem, Descriptions, message } from 'ant-design-vue';
  import {
    getOutboundProcessInfo,
    getOutboundProcessLog,
    getOutboundProcessDetail,
    getDeliveryPlanSelectedList,
  } from '@/services';
  import { descriptionItems, columns } from './outboundInfo';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
  };

  const props = defineProps({
    title: {
      type: String,
      default: '',
    },
    rowData: {
      type: Object,
      default: () => {},
    },
    type: {
      type: Number,
      default: 1,
    },
  });

  const activeKey = ref<any>('1');

  const tabItems = ref<any>([
    {
      key: '1',
      name: t('基础信息'),
    },
    {
      key: '2',
      name: t('血浆明细'),
    },
  ]);

  const changeType = async (val: any) => {
    try {
      if (val === '1') {
        const res = await getOutboundProcessInfo({ batchNo: props.rowData?.batchNo });

        stepItems.value =
          res.data?.map((item: any) => {
            return {
              ...item,
              title: item.name,
              description: item.date,
              status: item.status?.value,
            };
          }) ?? [];

        const res2 = await getOutboundProcessLog({ id: props.rowData?.id, type: props.type });
        tableData.value = res2.data ?? [];
      } else if (val === '2') {
        stepItems.value = [];
        tableData.value = [];
      } else {
        const res3 = await getOutboundProcessDetail({
          batchNo: props.rowData?.batchNo,
          type: props.type,
          flowCode: val,
        });
        stepItems.value =
          res3.data.flows?.map((item: any) => {
            return {
              ...item,
              title: item.name,
              description: item.date,
              status: item.status?.value,
            };
          }) ?? [];
        approveInfo.value = res3.data.approveInfo;
        auditInfo.value = res3.data.auditInfo;
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const stepItems = ref<any>([]);

  // 基础信息
  const cardItems = ref([
    {
      title: t('出库信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={7} bordered={true} layout={'vertical'}>
            {descriptionItems
              .filter((item: any) => item.vIf ?? true)
              .map(item => (
                <DescriptionsItem label={item.label}>
                  {(item.renderFn ? item.renderFn(props.rowData) : props.rowData?.[item.field]) ?? '-'}
                </DescriptionsItem>
              ))}
          </Descriptions>
          <div style={{ marginTop: '16px' }}>{`${t('出库血浆类型')}: ${props.rowData?.deliveryPlasmaType ?? '-'}`}</div>
          <div style={{ marginTop: '8px' }}>{`${t('出库备注')}: ${props.rowData?.remark ?? '-'}`}</div>
        </>
      ),
    },
    {
      title: t('操作记录'),
      slot: () => (
        <>
          <BMTable
            ref='teamTable'
            row-key='id'
            columns={columns}
            search={false}
            showToolBar={false}
            dataSource={tableData.value}></BMTable>
        </>
      ),
    },
  ]);

  // 血浆明细
  const tableData = ref<any>([]);

  const cardItems2 = ref([
    {
      title: t('已选血浆'),
      slot: () => (
        <>
          <BMPageComponent
            ref={pageRef.value}
            rowKeys={['id']}
            search={[true]}
            hideRightTree={true}
            showHeader={[false]}
            showToolBars={[true]}
            formProps={[formFirstProps]}
            paginations={[paginationBig]}
            tableFields={[
              {
                default: { type: props.type },
              },
            ]}
            requests={[loadData as DataRequestFn]}
            columns={[columnsFirst]}></BMPageComponent>
        </>
      ),
    },
  ]);

  // 岗位审核
  const approveInfo = ref<any>({});
  const auditInfo = ref<any>({});

  const cardItems3 = ref([
    {
      title: t('申请详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={7} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('申请状态')}>{approveInfo.value?.auditStatus?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请人')}>{approveInfo.value?.auditBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>{approveInfo.value?.auditDate ?? '-'}</DescriptionsItem>
          </Descriptions>
          <div style={{ marginTop: '8px' }}>{`${t('申请备注')}: ${approveInfo.value?.remark ?? '-'}`}</div>
        </>
      ),
    },
    {
      title: t('审核详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={7} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{auditInfo.value?.auditStatus?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{auditInfo.value?.auditBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>{auditInfo.value?.auditDate ?? '-'}</DescriptionsItem>
          </Descriptions>
          <div style={{ marginTop: '8px' }}>{`${t('审核备注')}: ${auditInfo.value?.remark ?? '-'}`}</div>
        </>
      ),
    },
  ]);

  const cardItemCmp = computed(() => {
    if (activeKey.value == '1') {
      return cardItems.value;
    } else if (activeKey.value == '2') {
      return cardItems2.value;
    } else {
      return cardItems3.value;
    }
  });

  const loadData = async (params: any) => {
    const datas = {
      ...params,

      batchNo: props.rowData?.batchNo,
    };
    if (!datas?.batchNo) {
      return {
        data: [],
      };
    }
    return await getDeliveryPlanSelectedList(datas);
  };

  onMounted(async () => {
    console.log('route', route);
    const { data } = await getOutboundProcessInfo({ batchNo: props.rowData?.batchNo });

    data.forEach((item: any, index: number) => {
      if (index) {
        tabItems.value.push({
          key: item.code,
          name: item.name,
        });
      }
    });

    await changeType(activeKey.value);
  });
</script>

<style scoped></style>
