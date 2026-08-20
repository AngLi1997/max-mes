<!-- 献浆者详情 -->
<template>
  <DetailView :showBreadcrumb="false" :headerTitle="t('献浆者详情')" :showStep="false" :cardItems="cardItems">
    <template #extra>
      <Button @click="() => router.back()">{{ t('返回') }}</Button>
    </template>
  </DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { BMTable } from '@bmos/components';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { useDetail } from './data';
  import { useRouter } from 'vue-router';
  import { getBloodDonorDetail } from '@/services';

  // getBloodDonorDetail

  const router = useRouter();

  const { columns, columnsSecond } = useDetail();

  const info = ref<any>({});

  const tableData = ref<any[]>([]);
  const tableDataSecond = ref<any[]>([]);

  // 献浆者信息
  const cardItems = ref([
    {
      title: t('献浆者信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('献浆者编号')}>{info.value?.no}</DescriptionsItem>
            <DescriptionsItem label={t('姓名')}>{info.value?.name}</DescriptionsItem>
            <DescriptionsItem label={t('性别')}>{info.value?.sex?.name}</DescriptionsItem>
            <DescriptionsItem label={t('血型')}>{info.value?.bloodType?.name}</DescriptionsItem>
            <DescriptionsItem label={t('身份证地址')}>{info.value?.address}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('对应血浆信息'),
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
    {
      title: t('对应标本信息'),
      slot: () => (
        <div style={{ height: '300px' }}>
          <BMTable
            ref='teamTable'
            row-key='id'
            columns={columnsSecond}
            search={false}
            showToolBar={false}
            dataSource={tableDataSecond.value}></BMTable>
        </div>
      ),
    },
  ]);

  const initData = async () => {
    try {
      const { data } = await getBloodDonorDetail(router.currentRoute.value?.params?.id);
      info.value = data.plasmaDonorInfo;
      tableData.value = data.plasmaInfoList;
      tableDataSecond.value = data.sampleInfoList;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  watch(
    () => router.currentRoute.value,
    async () => {
      if (router.currentRoute.value?.params?.id) {
        await initData();
      } else {
        tableData.value = [];
        tableDataSecond.value = [];
        info.value = {};
      }
    },
  );

  onMounted(async () => {
    await initData();
  });
</script>

<style lang="less" scoped></style>
