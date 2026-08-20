<!-- 内容预览 -->
<template>
  <Drawer
    v-model:open="open"
    :title="t('检疫期报告内容预览（非打印版）')"
    placement="right"
    width="900px"
    @after-open-change="afterOpenChange">
    <Descriptions bordered :column="2">
      <DescriptionsItem v-for="(item, index) in itemFields" :key="index" :span="item?.span || 1" :label="item.label">
        <img
          style="height: 38px; object-fit: cover"
          v-if="item.img && infoData?.[item.key]"
          :src="infoData?.[item.key]" />
        <span v-else>{{ infoData?.[item.key] ?? '--' }}</span>
      </DescriptionsItem>
    </Descriptions>
    <div>
      <BMTable
        ref="tableRef"
        :search="false"
        :data-request="loadData"
        :columns="columns"
        row-key="id"
        headerTitle=""
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"
        :pagination="paginationSmall"></BMTable>
    </div>
  </Drawer>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useDescriptions, useTable } from './hooks';
  import { paginationSmall } from '@/utils/paginationConfig';
  import { BMTable } from '@bmos/components';
  import { previewQuarantineReportAudit, getCheckQueryDetailList } from '@/services';
  import { message } from 'ant-design-vue';

  const { itemFields } = useDescriptions();
  const { tableRef, columns } = useTable();

  // const url = ref<string>(window.location.origin);

  const rowData = ref<any>({});

  const loadData = async (params: any) => {
    return await getCheckQueryDetailList({
      ...params,
      checkNo: rowData.value.checkNo,
    });
  };

  const open = ref<boolean>(false);

  const afterOpenChange = (bool: boolean) => {
    console.log('open', bool);
  };

  const infoData = ref<any>();

  const showDrawer = async (row: any) => {
    try {
      rowData.value = row;
      const { data } = await previewQuarantineReportAudit({ checkNo: row.checkNo });
      infoData.value = {
        ...data,
        jpName: t('原料血浆'),
      };
      open.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  defineExpose({ showDrawer });
</script>

<style lang="less" scoped>
  :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
    flex: 0;
  }
</style>
