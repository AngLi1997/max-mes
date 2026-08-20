<template>
  <div class="pass-standard-table">
    <Tabs v-model:activeKey="tabKey">
      <TabPane :key="PassStandardTypeEnum.NUMBER" :tab="t('数值型')">
        <BMTable
          :columns="numberColumns"
          :dataSource
          :pagination="false"
          :search="false"
          :showToolBar="false"
          :scroll="{ x: 800, y: 100 }" />
      </TabPane>
      <TabPane :key="PassStandardTypeEnum.TEXT" :tab="t('文本型')">
        <BMTable
          :columns="textColumns"
          :dataSource
          :pagination="false"
          :search="false"
          :showToolBar="false"
          :scroll="{ x: 800, y: 100 }" />
      </TabPane>
    </Tabs>
  </div>
</template>
<script lang="tsx" setup>
  import { BMTable, TableColumn } from '@bmos/components';
  import { TabPane, Select, Tabs, InputNumber, Input } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { PassStandardTypeEnum } from '@/types';

  defineOptions({
    inheritAttrs: false,
  });
  const { operatorDict } = getDicts();

  const dataSource = defineModel<any[]>('tableList', { default: [] });
  const tabKey = defineModel<PassStandardTypeEnum>('tabKey', { default: PassStandardTypeEnum.NUMBER });

  const numberColumns: TableColumn[] = [
    {
      title: t('判定字段名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 100,
    },
    {
      title: `${t('操作符')}1`,
      width: 100,
      dataIndex: 'labelOne',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.labelOne}
            style='width: 100%'
            placeholder={t('请选择')}
            options={operatorDict}
          />
        );
      },
    },
    {
      title: `${t('数值')}1`,
      width: 100,
      dataIndex: 'valueOne',
      customRender: ({ record }) => {
        return (
          <InputNumber
            v-model:value={record.valueOne}
            style='width: 100%'
            placeholder={t('请输入')}
            max={1000000}
            min={0}
            precision={6}
          />
        );
      },
    },
    {
      title: `${t('操作符')}2`,
      width: 100,
      dataIndex: 'labelTwo',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.labelTwo}
            style='width: 100%'
            placeholder={t('请选择')}
            options={operatorDict}
          />
        );
      },
    },
    {
      title: `${t('数值')}2`,
      width: 100,
      dataIndex: 'valueTwo',
      customRender: ({ record }) => {
        return (
          <InputNumber
            v-model:value={record.valueTwo}
            style='width: 100%'
            placeholder={t('请输入')}
            max={1000000}
            min={0}
            precision={6}
          />
        );
      },
    },
  ];
  const textColumns: TableColumn[] = [
    {
      title: t('判定字段名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 100,
    },
    {
      title: `${t('操作符')}`,
      width: 100,
      dataIndex: 'textOne',
      customRender: ({ record }) => {
        return (
          <Select
            v-model:value={record.textOne}
            style='width: 100%'
            placeholder={t('请选择')}
            options={[
              { label: t('包含'), value: '包含' },
              { label: t('不包含'), value: '不包含' },
            ]}
          />
        );
      },
    },
    {
      title: `${t('数值')}`,
      width: 100,
      dataIndex: 'textValueOne',
      customRender: ({ record }) => {
        return (
          <Input v-model:value={record.textValueOne} style='width: 100%' placeholder={t('请输入')} maxlength={10} />
        );
      },
    },
  ];
</script>
<style lang="less" scoped>
  .pass-standard-table {
    height: 200px;
  }
  :deep(.bmos-table .lisms-table-body) {
    border-bottom: none !important;
  }
</style>
