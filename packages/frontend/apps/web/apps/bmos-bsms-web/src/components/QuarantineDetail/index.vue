<!-- 核查详情 -->
<template>
  <DetailView
    :key="route.params.id"
    :headerTitle="t('核查详情')"
    :showBreadcrumb="false"
    :stepItems="stepItems"
    :showStep="activeKey == '1'"
    :tagItem="{
      value: infoData?.auditStatus?.value,
      key: infoData?.auditStatus?.name,
      className: 'tag-1',
    }"
    :backFn="back"
    :cardItems="cardItemsCmp">
    <template #extra>
      <div>
        <Button
          v-if="infoData?.auditStatus?.value == 1"
          v-hasAuth="170050003000001"
          style="margin-right: 8px"
          type="primary"
          @click="openOperateModal('audit')">
          {{ t('送审') }}
        </Button>
        <Button
          v-if="infoData?.auditStatus?.value == 1"
          v-hasAuth="170050003000002"
          style="margin-right: 8px"
          @click="openOperateModal('cancel')">
          {{ t('撤销') }}
        </Button>
        <Button
          v-if="infoData?.auditStatus?.value == 2"
          v-hasAuth="170050004000002"
          style="margin-right: 8px"
          type="primary"
          @click="openOperateAuditModal('audit')">
          {{ t('审核') }}
        </Button>
        <Button
          v-if="infoData?.auditStatus?.value == 2"
          v-hasAuth="170050004000001"
          style="margin-right: 8px"
          @click="openOperateAuditModal('return')">
          {{ t('退回') }}
        </Button>
        <Button @click="back">{{ t('返回') }}</Button>
      </div>
    </template>
    <template #header>
      <div style="width: 100%">
        <Tabs v-model:activeKey="activeKey">
          <TabPane v-for="item in tabItems" :key="item.key" :tab="item.name"></TabPane>
        </Tabs>
      </div>
    </template>
  </DetailView>
  <OperateModal ref="operateModalRef" @submitSuccess="initFn" />
  <OperateAuditModal ref="operateAuditModalRef" @submitSuccess="initFn" />
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { formProps, columns } from './data';
  import { t } from '@bmos/i18n';
  import { Tabs, TabPane, Descriptions, DescriptionsItem, message, Button } from 'ant-design-vue';
  import { BMPageComponent, DataRequestFn, BMTable, BMForm } from '@bmos/components';
  import { useTable } from './hooks';
  import { getCheckQueryDetailPage, getCheckQueryDetail, exportCheckQueryDetail } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import OperateModal from '@/pages/QuarantineManagement/QuarantineReportApproval/components/OperateModal/index.vue';
  import OperateAuditModal from '@/pages/QuarantineManagement/QuarantineReportAudit/components/OperateModal/index.vue';
  import { fileStreamDownload } from '@bmos/utils';

  defineOptions({
    inheritAttrs: false,
  });

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const route = useRoute();
  const router = useRouter();

  const back = () => {
    router.back();
  };

  const stepItems = ref([]);

  const cardItemsCmp = computed(() => {
    if (activeKey.value == '1') {
      return cardItems.value;
    } else if (activeKey.value == '2') {
      return cardItems2.value;
    } else {
      return cardItems3.value;
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
      name: t('核查明细'),
    },
    {
      key: '3',
      name: t('核查报告'),
    },
  ]);

  const infoData = ref<any>({});

  // ===========送审/撤销============
  const operateModalRef = ref();

  const openOperateModal = (type: 'audit' | 'cancel') => {
    operateModalRef.value?.openModal([infoData.value], type);
  };

  // ===========审核============
  const operateAuditModalRef = ref();

  const openOperateAuditModal = (type: 'audit' | 'return') => {
    operateAuditModalRef.value?.openModal([infoData.value], type);
  };

  // 基础信息
  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <p>{t('核查信息')}</p>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('检品批号')}>{infoData.value?.inWarehouseBatchNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('报告编号')}>{infoData.value?.reportNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('核查批号')}>{infoData.value?.checkNo ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('来源单位')}>{infoData.value?.originOrgInfo?.originOrg ?? '-'}</DescriptionsItem>
            {getWarehouseConfigByCode.value ? (
              <DescriptionsItem label={t('所在仓库')}>{infoData.value?.warehouse?.name ?? '-'}</DescriptionsItem>
            ) : (
              <> </>
            )}
          </Descriptions>
          <p style={{ marginTop: '16px' }}>{t('核查数据')}</p>
          <Descriptions size='small' column={6} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('核查份数')}>{infoData.value?.checkNumber ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('重量')}>{infoData.value?.checkWeight ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('检疫期合格')}>{infoData.value?.passNum ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('检疫期未通过')}>{infoData.value?.unResNum ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('检疫期不合格')}>{infoData.value?.unPassNum ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('合格率')}>{infoData.value?.passRate ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('申请详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('报告状态')}>{infoData.value?.reportStatus?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('报告人')}>{infoData.value?.reportUser ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('报告日期')}>{infoData.value?.reportTime ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('送审人')}>{infoData.value?.sendUser ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('送审日期')}>{infoData.value?.sendTime ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('审核详情'),
      slot: () => (
        <>
          <Descriptions size='small' column={3} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('审核状态')}>{infoData.value?.auditStatus?.name ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核人')}>{infoData.value?.auditUser ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('审核日期')}>{infoData.value?.auditTime ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('操作记录'),
      slot: () => (
        <>
          <BMTable
            row-key='id'
            columns={columns}
            search={false}
            showToolBar={false}
            dataSource={infoData.value?.logList || []}></BMTable>
        </>
      ),
    },
  ]);

  // 核查明细

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const loading = ref(false);
  const exportExcel = async (data: any) => {
    try {
      loading.value = true;
      const res = await exportCheckQueryDetail(data);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  const cardItems2 = ref([
    {
      slot: () => (
        <>
          <BMPageComponent
            ref={pageRef.value}
            rowKeys={['id']}
            search={[true]}
            hideRightTree={true}
            showHeader={[false]}
            showToolBars={[true]}
            tableFields={[
              {
                default: {
                  checkNo: infoData.value?.checkNo,
                },
              },
            ]}
            formProps={[formFirstProps]}
            paginations={[paginationBig]}
            requests={[getCheckQueryDetailPage as DataRequestFn]}
            columns={[columnsFirst]}>
            {{
              tableHeaderToolbar0: ({ instance }: any) => {
                return (
                  <Button
                    type='primary'
                    loading={loading.value}
                    onClick={() =>
                      exportExcel({
                        ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
                        checkNo: infoData.value?.checkNo,
                      })
                    }>
                    {t('导出')}
                  </Button>
                );
              },
            }}
          </BMPageComponent>
        </>
      ),
    },
  ]);

  // 核查报告
  const cardItems3 = ref([
    {
      slot: () => (
        <>
          <BMForm {...formProps}></BMForm>
        </>
      ),
    },
  ]);

  const initFn = async () => {
    formProps.initialValues = {};
    try {
      const { data } = await getCheckQueryDetail(route.params.id);
      infoData.value = {
        ...data,
        ...data.reportVO,
      };
      formProps.initialValues = data.reportVO;
      stepItems.value = data.flows?.map((item: any) => {
        return {
          ...item,
          title: item.name,
          description: item.date,
          status: item.status?.value,
        };
      });
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
      // back();
    }
  };

  watch(
    () => router.currentRoute.value,
    async () => {
      await initFn();
    },
  );

  onMounted(async () => {
    activeKey.value = route.query.type ?? '1';
    await initFn();
  });
</script>

<style scoped></style>
