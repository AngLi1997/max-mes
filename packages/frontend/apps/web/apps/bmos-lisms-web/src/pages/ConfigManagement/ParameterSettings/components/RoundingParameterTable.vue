<template>
  <div class="out-materials-table">
    <BMTable
      :columns
      :dataSource
      :pagination="false"
      :search="false"
      :showToolBar="false"
      :scroll="{ x: 800, y: 100 }" />
  </div>
</template>
<script lang="tsx" setup>
  import { BMTable, TableColumn } from '@bmos/components';
  import { Select } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { RoundingRuleEnum } from '@/types';
  import { SelectValue } from 'ant-design-vue/es/select';

  defineOptions({
    inheritAttrs: false,
  });

  const { decimalUnitDict } = getDicts();

  const dataSource = defineModel<any[]>('tableList', { default: [] });

  const columns: TableColumn[] = [
    {
      title: t('判定字段名称'),
      dataIndex: 'description',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('修约规则'),
      width: 100,
      dataIndex: 'label',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.label}
            style='width: 100%'
            placeholder={t('请选择')}
            options={Object.keys(RoundingRuleEnum).map(key => ({
              label: RoundingRuleEnum[key as keyof typeof RoundingRuleEnum],
              value: key,
            }))}
            onSelect={(value: SelectValue) => {
              if (value === 'RR001') {
                record.value = undefined;
              }
            }}
          />
        );
      },
    },
    {
      title: t('保留位数'),
      width: 100,
      dataIndex: 'value',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.value}
            style='width: 100%'
            placeholder={t('请选择')}
            disabled={record.label === 'RR001'}
            options={decimalUnitDict}
          />
        );
      },
    },
  ];
</script>
<style lang="less" scoped>
  .out-materials-table {
    height: 100px;
  }
  :deep(.bmos-table .lisms-table-body) {
    border-bottom: none !important;
  }
</style>
