<template>
  <div class="dynamic-report">
    <div class="dynamic-report-table">
      <BMTable
        :columns="columns"
        :dataSource="datasetDynamicReportDataList"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 400, y: 300 }" />
    </div>
    <Button v-if="!isView" type="link" @click="add">
      <BMIcons
        icon="Add"
        :style="{
          fontSize: '14px',
          width: '14px',
          height: '14px',
          marginRight: '5px',
        }" />
      {{ t('添加数据点') }}
    </Button>
  </div>
</template>
<script lang="tsx" setup>
  import { BMTable, TableColumn } from '@bmos/components';
  import { DatePicker, Input, InputNumber, Select } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { BMIcons } from '@bmos/icons';
  defineOptions({
    name: 'DynamicReportTable',
    inheritAttrs: false,
  });
  const datasetDynamicReportDataList = defineModel<Array<any>>('datasetDynamicReportDataList', {
    default: [
      {
        key: new Date().getTime(),
      },
    ],
  });

  const props = withDefaults(
    defineProps<{
      isView?: boolean;
    }>(),
    {
      isView: false,
    },
  );

  const add = () => {
    datasetDynamicReportDataList.value = [
      ...datasetDynamicReportDataList.value,
      {
        key: new Date().getTime(),
      },
    ];
  };

  const updateList = (key: string, value: any, id: number) => {
    datasetDynamicReportDataList.value = datasetDynamicReportDataList.value.map((item: any) => {
      if (item.id && item.id === id) {
        return {
          ...item,
          [key]: value,
        };
      }
      if (item.key === id) {
        return {
          ...item,
          [key]: value,
        };
      }
      return item;
    });
  };
  const updateLists = (obj: Array<{ key: string; value: any }>, id: number) => {
    datasetDynamicReportDataList.value = datasetDynamicReportDataList.value.map((item: any) => {
      if (item.id && item.id === id) {
        return {
          ...item,
          ...obj.reduce((acc: any, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {}),
        };
      }
      if (item.key === id) {
        return {
          ...item,
          ...obj.reduce((acc: any, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {}),
        };
      }
      return item;
    });
  };
  const columns: TableColumn[] = [
    {
      title: t('动态数据名称'),
      width: 100,
      resizable: true,
      dataIndex: 'dataName',
      fixed: 'left',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Input
              value={record.dataName}
              placeholder={t('动态数据名称')}
              allowClear
              maxlength={100}
              disabled={props.isView}
              onChange={(e: any) => {
                updateList('dataName', e.target.value, record.id || record.key);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('数据类型'),
      width: 50,
      resizable: true,
      dataIndex: 'dataType',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Select
              value={record.dataType}
              allowClear
              disabled={props.isView}
              placeholder={t('请选择')}
              options={[
                {
                  label: t('数值'),
                  value: 'NUMBER',
                },
                {
                  label: t('文本'),
                  value: 'TEXT',
                },
                {
                  label: t('日期'),
                  value: 'DATE',
                },
              ]}
              onChange={(value: SelectValue) => {
                updateLists(
                  [
                    {
                      key: 'dataType',
                      value,
                    },
                    {
                      key: 'defaultValue',
                      value: undefined,
                    },
                  ],
                  record.id || record.key,
                );
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('默认值'),
      width: 80,
      resizable: true,
      dataIndex: 'defaultValue',
      customRender: ({ record }) => {
        if (record.dataType === 'NUMBER') {
          return (
            <div class='editable-cell'>
              <InputNumber
                value={record.defaultValue}
                placeholder={t('默认值')}
                stringMode
                style={{ width: '100%' }}
                disabled={props.isView}
                onChange={(value: number | string) => {
                  updateList('defaultValue', value, record.id || record.key);
                }}
              />
            </div>
          );
        } else if (record.dataType === 'DATE') {
          return (
            <div class='editable-cell'>
              <DatePicker
                value={record.defaultValue}
                placeholder={t('默认值')}
                allowClear
                disabled={props.isView}
                showTime
                valueFormat='YYYY-MM-DD HH:mm:ss'
                onChange={(date: any) => {
                  updateList('defaultValue', date, record.id || record.key);
                }}
              />
            </div>
          );
        } else {
          return (
            <div class='editable-cell'>
              <Input
                value={record.defaultValue}
                placeholder={t('默认值')}
                allowClear
                maxlength={100}
                disabled={props.isView}
                onChange={(e: any) => {
                  updateList('defaultValue', e.target.value, record.id || record.key);
                }}
              />
            </div>
          );
        }
      },
    },
    {
      title: '',
      width: 20,
      dataIndex: 'delete',
      customRender: ({ record }) => {
        return props.isView ? null : (
          <BMIcons
            icon='CircleDelete'
            style={{
              fontSize: '16px',
              width: '16px',
              height: '16px',
            }}
            onClick={() => {
              datasetDynamicReportDataList.value = datasetDynamicReportDataList.value.filter((item: any) => {
                if (record.key) {
                  return item.key !== record.key;
                }
                return item.id !== record.id;
              });
            }}
          />
        );
      },
    },
  ];
</script>
