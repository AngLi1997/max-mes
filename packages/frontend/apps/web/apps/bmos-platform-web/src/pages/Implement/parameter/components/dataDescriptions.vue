<template>
  <div class="out-materials">
    <BMDescriptions :showBottomBorder="false" :column="1" :list="descData" :labelStyle="lbStyle" />
  </div>
</template>
<script lang="tsx" setup>
  import { BMDescriptions, DescriptionsItemProps } from '@bmos/components';
  import { t } from '@bmos/i18n';

  const props = withDefaults(
    defineProps<{
      rowData?: any;
    }>(),
    {
      rowData: '',
    },
  );
  const descData = ref<DescriptionsItemProps[]>([
    {
      label: t('参数名称'),
      value: props.rowData?.name,
    },
    {
      label: t('代码'),
      value: props.rowData?.code,
    },
    {
      label: t('值类型'),
      value: props.rowData?.valueType?.label,
    },
    {
      label: t('参数业务类型'),
      value: props.rowData?.businessType?.label,
    },
    {
      label: t('所属应用'),
      value: props.rowData?.belong,
    },
    {
      label: t('描述'),
      value: props.rowData?.description,
    },
  ]);
  const lbStyle = ref({
    width: '100px',
    display: 'flex',
    justifyContent: 'flex-end',
  });
  watch(
    () => props.rowData,
    async () => {
      await nextTick();
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>
<style lang="less" scoped>
  .out-materials {
    :deep(.plat-descriptions) {
      background-color: var(--bmos-background-color);
    }
  }
</style>
