<template>
  <div class="round-rule-table">
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
  import { useDict } from '@/stores/dictStore';
  import { SelectValue } from 'ant-design-vue/es/select';

  defineOptions({
    inheritAttrs: false,
  });
  const { getDict } = useDict();
  const dataSource = defineModel<any[]>('tableList', { default: [] });

  const roundValueOptions = ref<any>([]);

  const columns: TableColumn[] = [
    {
      title: t('判定字段名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('修约规则'),
      width: 100,
      dataIndex: 'roundValue',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.ruleCode}
            style='width: 100%'
            placeholder={t('请选择')}
            options={roundValueOptions.value}
            onSelect={(_value: SelectValue, option: any) => {
              record.roundValue = option.label;
            }}
          />
        );
      },
    },
    {
      title: t('保留位数'),
      width: 100,
      dataIndex: 'digits',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.digits}
            style='width: 100%'
            placeholder={t('请选择')}
            disabled={record.roundValue === 'NONE'}
            options={[
              { label: t('个位'), value: 0 },
              { label: `1${'位小数'}`, value: 1 },
              { label: `2${'位小数'}`, value: 2 },
              { label: `3${'位小数'}`, value: 3 },
            ]}
          />
        );
      },
    },
  ];

  onMounted(async () => {
    const data = await getDict('修约规则');
    roundValueOptions.value = data;
  });
</script>
<style lang="less" scoped>
  .round-rule-table {
    height: 100px;
  }
  :deep(.bmos-table .lisms-table-body) {
    border-bottom: none !important;
  }
</style>
