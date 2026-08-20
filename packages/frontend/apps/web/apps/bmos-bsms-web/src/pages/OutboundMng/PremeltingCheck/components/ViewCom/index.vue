<!-- 详情 -->
<template>
  <DetailView
    :title="t('预融核对详情')"
    :headerTitle="t('预融核对详情')"
    :tagItem="{
      value: infoData.status?.value,
      key: infoData.status?.name,
      className: 'tag-1',
    }"
    :showStep="false"
    :cardItems="cardItems"
    @back="back"></DetailView>
</template>

<script setup lang="tsx">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { getPrethawingInfo } from '@/services';
  import { BMTable } from '@bmos/components';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { useTable } from './hooks/useTable';

  const router = useRouter();
  const route = useRoute();

  const back = () => {
    console.log('back');
    router.back();
  };

  const { tableRef, columns, loadData, pagination } = useTable(route.params.batchNo as string);

  const descriptionItems = [
    {
      label: t('出库批号'),
      field: 'batchNo',
    },
    {
      label: t('出库类型'),
      field: 'type',
      render: (text: any) => {
        return text?.name;
      },
    },
    {
      label: t('质量状态'),
      field: 'qualityStatus',
      render: (text: any) => {
        return text?.name;
      },
    },
    {
      label: t('数量'),
      field: 'num',
    },
    {
      label: t('总重量'),
      field: 'weight',
    },
  ];

  const infoData = ref<any>({});

  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={5} bordered={true} layout={'vertical'}>
            {descriptionItems.map(item => (
              <DescriptionsItem label={item.label}>
                {item.render ? item.render(infoData.value?.[item.field]) : infoData.value?.[item.field] ?? '-'}
              </DescriptionsItem>
            ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('核对结果'),
      slot: () => (
        <>
          <Descriptions size='small' column={2} bordered={true} layout={'vertical'}>
            <DescriptionsItem label={t('总数量')}>{infoData.value?.checkNum ?? '-'}</DescriptionsItem>
            <DescriptionsItem label={t('异常数量')}>{infoData.value?.errorNum ?? '-'}</DescriptionsItem>
          </Descriptions>
        </>
      ),
    },
    {
      title: t('异常明细'),
      slot: () => (
        // <div style={{ height: '300px' }}>
        <BMTable
          ref={tableRef}
          row-key='id'
          columns={columns}
          search={false}
          showToolBar={false}
          dataRequest={loadData}
          pagination={pagination}></BMTable>
        // </div>
      ),
    },
  ]);

  onMounted(async () => {
    try {
      const { data } = await getPrethawingInfo({ batchNo: route.params?.batchNo });
      infoData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped></style>
