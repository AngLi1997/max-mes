<template>
  <div class="dynamic-table-content">
    <div class="operation">
      <Button type="primary" class="add-btn" @click="add">
        {{ t('新增数据') }}
      </Button>
    </div>

    <div class="dynamic-table">
      <BMTable
        :columns="columns"
        :dataSource="dataList"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 400, y: 300 }" />
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { BMTable, TableColumn } from '@bmos/components';
  import { Button, Input, InputNumber, TreeSelect } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { dictListDictCode } from '@/services';

  defineOptions({
    name: 'EDADynamicTable',
    inheritAttrs: false,
  });
  const emits = defineEmits(['validate']);
  const dataList = defineModel<Array<any>>('dataList', {
    default: [],
  });

  const add = () => {
    dataList.value = [
      ...dataList.value,
      {
        key: new Date().getTime(),
      },
    ];
  };

  const updateList = (key: string, value: any, id: number) => {
    dataList.value = dataList.value.map((item: any) => {
      if (item.key === id) {
        return {
          ...item,
          [key]: value,
        };
      }
      return item;
    });
  };

  const dataTreeData = ref<any[]>([]);
  const getDataOptions = async () => {
    try {
      const { data } = await dictListDictCode({
        code: 'DeviceDataFields',
      });
      dataTreeData.value = [
        ...(data
          ? [
              {
                label: t('设备数据自定义字段'),
                value: 'DeviceDataFields',
                selectable: false,
                children: data.map((item: any) => {
                  return {
                    label: `${item.label}-${item.value}`,
                    value: item.value,
                  };
                }),
              },
              {
                label: t('设备信息字段'),
                value: 'DeviceInfoFields',
                selectable: false,
                // 设备名称、设备编号、采集时间、采集人
                children: [
                  {
                    label: t('设备名称'),
                    value: 'equipmentName',
                  },
                  {
                    label: t('设备编号'),
                    value: 'equipmentCode',
                  },
                  {
                    label: t('采集时间'),
                    value: 'acquisitionTime',
                  },
                  {
                    label: t('采集人'),
                    value: 'acquisitionUser',
                  },
                ],
              },
            ]
          : []),
      ];
    } catch (error) {
      //
    }
  };

  const columns: TableColumn[] = [
    {
      title: t('列名'),
      width: 200,
      resizable: true,
      dataIndex: 'colName',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Input
              value={record.colName}
              placeholder={t('列名')}
              allowClear
              maxlength={100}
              onChange={(e: any) => {
                updateList('colName', e.target.value, record.key);
                emits('validate');
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('列数据'),
      width: 200,
      resizable: true,
      dataIndex: 'colData',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <TreeSelect
              value={record.colData}
              treeDefaultExpandAll
              allowClear
              placeholder={t('列数据')}
              treeData={dataTreeData.value}
              onChange={(value: string) => {
                updateList('colData', value, record.key);
                emits('validate');
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('列宽(px)'),
      width: 150,
      resizable: true,
      dataIndex: 'colWidth',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <InputNumber
              value={record.colWidth}
              placeholder={t('列宽')}
              min={1}
              style={{ width: '100%' }}
              onChange={(value: any) => {
                updateList('colWidth', value, record.key);
                emits('validate');
              }}
            />
          </div>
        );
      },
    },
    {
      title: '',
      width: 100,
      dataIndex: 'delete',
      customRender: ({ record }) => {
        return (
          <Button
            type='link'
            danger
            onClick={() => {
              dataList.value = dataList.value.filter((item: any) => {
                if (record.key) {
                  return item.key !== record.key;
                }
                return item.id !== record.id;
              });
              emits('validate');
            }}>
            {t('删除')}
          </Button>
        );
      },
    },
  ];

  onMounted(() => {
    getDataOptions();
  });
</script>
<style lang="less" scoped>
  .dynamic-table-content {
    .operation {
      display: flex;
      justify-content: flex-end;
    }
    .add-btn {
      margin-bottom: var(--bmos-margin-large);
    }
  }
</style>
