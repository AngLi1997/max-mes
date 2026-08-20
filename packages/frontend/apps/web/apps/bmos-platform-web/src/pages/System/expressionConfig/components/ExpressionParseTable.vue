<template>
  <BMTable
    ref="tableInstance"
    :columns="columns"
    :data-source="tableData"
    row-key="key"
    :search="false"
    :scroll="{ x: 500 }"
    :pagination="false"
    :show-tool-bar="false"></BMTable>
</template>

<script lang="tsx" setup>
  import { Form, Input } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMTable, TableColumn } from '@bmos/components';

  interface ExpressionParse {
    key: string;
    value: string;
    result: string;
  }

  const emit = defineEmits<{
    (e: 'update:expressionParse', value: ExpressionParse[]): void;
  }>();

  const props = withDefaults(
    defineProps<{
      expressionParse: ExpressionParse[];
    }>(),
    {
      expressionParse: () => [],
    },
  );

  const columns: TableColumn[] = [
    {
      title: t('参数'),
      dataIndex: 'key',
      width: 50,
    },
    {
      title: t('参数命名'),
      dataIndex: 'value',
      width: 50,
    },
    {
      title: t('参数值'),
      dataIndex: 'result',
      width: 50,
      customRender: ({ record }) => {
        return (
          <Input
            v-model:value={record.result}
            placeholder={t('参数值')}
            onChange={() => {
              onValueChange(record as ExpressionParse);
            }}></Input>
        );
      },
    },
  ];

  const tableData = ref([] as ExpressionParse[]);

  watch(
    () => props.expressionParse,
    val => {
      tableData.value = val.map(item => {
        return {
          key: item.key,
          value: item.value,
          result: item.result || undefined,
        } as ExpressionParse;
      });
    },
  );

  const formItemContext = Form.useInjectFormItemContext();
  const triggerChange = (changedValue: ExpressionParse) => {
    emit(
      'update:expressionParse',
      props.expressionParse?.map(item => {
        if (item.key === changedValue.key) {
          return {
            ...item,
            ...changedValue,
          };
        }
        return item;
      }),
    );
    formItemContext.onFieldChange();
  };
  const onValueChange = (record: ExpressionParse) => {
    triggerChange(record);
  };
</script>
