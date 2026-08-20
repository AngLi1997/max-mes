<!-- 不合格核查记录审核详情-->
<template>
  <DetailView
    :title="t('核查记录详情')"
    :headerTitle="t('不合格血浆核查记录')"
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
  import { BMTable, DataRequestFn } from '@bmos/components';
  import {
    unqualifiedCheckRecordDetail,
    getUnqualifiedPlasmaAffectedPlasma,
    unqualifiedCheckRecordLogList,
  } from '@/services';
  import { columns, columns2 } from './data';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    router.back();
  };

  const stepItems = ref<any[]>([]);

  const status = ref<any>({});

  const info = ref<any>({});

  const tableData = ref<any[]>([]);

  const loadData = async (params: any) => {
    const { data } = await getUnqualifiedPlasmaAffectedPlasma({
      ...params,

      unqualifiedPlasmaInfoId: route.params?.id,
    });

    return {
      data: data.detailList,
    };
  };

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
            <DescriptionsItem label={t('血型')}>{info.value?.bloodType?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('不合格项目')}>{info.value?.unqualifiedItems ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('阳性不合格')}>{info.value?.isPositive?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('拒绝日期')}>{info.value?.rejectDate ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('不合格来源')}>{info.value?.unqualifiedOrigin?.name ?? '-'}</DescriptionsItem>
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
          <Descriptions size='small' column={2} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('申请人')}>{info.value?.applyBy ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('申请日期')}>{info.value?.applyDate ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('审核信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={4} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{info.value?.auditStatus?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{info.value?.auditBy ?? '-'}</DescriptionsItem>
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

  onMounted(async () => {
    try {
      if (route.params?.id) {
        const { data } = await unqualifiedCheckRecordDetail(route.params?.id);
        const { data: tableList } = await unqualifiedCheckRecordLogList({ identify: route.params?.id });
        info.value = {
          ...data,
        };

        stepItems.value = [
          {
            title: `${t('申请人')}: ${data.applyBy}`,
            description: data.applyDate,
            status: 'finish',
          },
          {
            title: `${t('审核人')}: ${data.auditBy ?? '--'}`,
            description: data.auditDate,
            status: data.auditDate ? 'finish' : 'wait',
          },
        ];
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

<style scoped></style>
