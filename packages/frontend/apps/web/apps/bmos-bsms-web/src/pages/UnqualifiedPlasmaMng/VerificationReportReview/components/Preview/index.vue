<!-- 内容预览 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格核查报告内容预览（非打印版）')"
    wrapClassName="modalSizeLarge"
    :show-ok-button="false">
    <template #formBefore>
      <Descriptions bordered :column="2">
        <DescriptionsItem
          :labelStyle="{ width: '150px' }"
          v-for="(item, index) in itemFields"
          :key="index"
          :span="item?.span || 1"
          :label="item.label">
          <img
            style="height: 38px; object-fit: cover"
            v-if="item.type === 'image' && info[item.key]"
            :src="`${url}/${info[item.key]}`" />
          <span v-else>{{ info[item.key] ?? '--' }}</span>
        </DescriptionsItem>
      </Descriptions>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useDescriptions } from './hooks';
  import { unqualifiedPlasmaReportAuditPreview } from '@/services';
  import { Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { BMModalForm } from '@bmos/components';

  const { itemFields } = useDescriptions();

  const url = ref<string>(window.location.origin);

  const info = ref<any>({});

  const open = ref<boolean>(false);

  const openModal = async (row: any) => {
    try {
      const { data } = await unqualifiedPlasmaReportAuditPreview(row.reportBillNo);
      info.value = data;
      open.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style lang="less" scoped>
  :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
    flex: 0;
  }
</style>
