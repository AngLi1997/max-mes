<template>
  <div class="dynamic-report">
    <div
      :class="[
        'dynamic-report-table',
        datasetPointList.length >= 20 ? 'set-height' : '',
        datasetPointList.length >= 20 ? virtualizedClass : '',
      ]">
      <BMTable
        ref="tableRef"
        :columns="columns"
        :dataSource="renderList"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 400, y: 300 }" />
    </div>
    <Button v-if="!isView" type="link" class="add-btn" @click="add">
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
  import { BMTable, Recordable, TableColumn } from '@bmos/components';
  import { Button, Dropdown, Input, Menu, MenuItem, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  import RelationComponentItem from './RelationComponentItem.vue';
  import { ComponentNode, NODE_TYPE } from '@/components/Record';
  import { copyToClipboard, isArray, isEmpty } from '@bmos/utils';
  import { useVirtualized } from '../hooks/useVirtualized';

  defineOptions({
    name: 'PointTable',
    inheritAttrs: false,
  });

  const emit = defineEmits<{
    (e: 'nodeClick', target: ComponentNode): void;
    (e: 'deleteClick', row: Recordable): void;
    (e: 'addClick', row: Recordable): void;
  }>();

  const datasetPointList = defineModel<Array<any>>('datasetPointList', {
    default: [
      {
        key: new Date().getTime(),
      },
    ],
  });

  const { renderList, virtualizedClass, scrollTo } = useVirtualized(datasetPointList);

  const props = withDefaults(
    defineProps<{
      isView?: boolean;
      checkStatus: any;
      datasetKey?: string;
    }>(),
    {
      isView: false,
      checkStatus: () => ({
        status: false,
        behavior: void 0,
      }),
      datasetKey: '',
    },
  );

  const tableRef = ref<any>(null);

  const add = () => {
    datasetPointList.value = [
      ...(isArray(datasetPointList.value) ? datasetPointList.value : []),
      {
        key: new Date().getTime(),
      },
    ];
  };

  const updateList = (key: string, value: any, id: number) => {
    datasetPointList.value = datasetPointList.value.map((item: any) => {
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

  const getTarget = (str: string) => {
    try {
      return JSON.parse(str || '{}');
    } catch (error) {
      return {};
    }
  };
  const copyDataIndex = async (text: string) => {
    try {
      await copyToClipboard(text);
      message.success(`${text} ${t('复制成功')}`);
    } catch (error) {
      message.error(`${text} ${t('复制失败')}`);
    }
  };

  const columns: Ref<TableColumn[]> = ref([
    {
      title: t('数据点名称'),
      width: 100,
      resizable: true,
      dataIndex: 'name',
      fixed: 'left',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Input
              value={record.name}
              placeholder={t('数据点名称')}
              allowClear
              maxlength={100}
              disabled={props.isView}
              onChange={(e: any) => {
                updateList('name', e.target.value, record.id || record.key);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('关联组件'),
      width: 100,
      dataIndex: 'relationComponent',
      customRender: ({ record }) => {
        return (
          <RelationComponentItem
            target={getTarget(record?.extra)}
            hasAdd={props.isView ? false : isEmpty(record.extra)}
            show={!props.isView}
            row={record}
            checkStatus={props.checkStatus}
            onDeleteClick={() => {
              emit('deleteClick', record);
            }}
            onNodeClick={(target: ComponentNode) => {
              emit('nodeClick', target);
            }}
            onAdd={() => {
              emit('addClick', record);
            }}
          />
        );
      },
    },
    {
      title: t('索引'),
      dataIndex: 'datasetPointKey',
      width: 40,
      hideInTable: !props.isView,
      customRender: ({ record }) => {
        return (
          <Dropdown trigger={['contextmenu']}>
            {{
              default: () => (
                <Button
                  type='link'
                  style={{ padding: 0 }}
                  onClick={e => {
                    e?.preventDefault();
                    try {
                      if (
                        JSON.parse(record.extra)?.componentType === NODE_TYPE.RADIO ||
                        JSON.parse(record.extra)?.componentType === NODE_TYPE.CHECKBOX
                      ) {
                        copyDataIndex('${(' + props.datasetKey + '.' + record.datasetPointKey + ')[][][][][]}');
                      } else {
                        copyDataIndex('${(' + props.datasetKey + '.' + record.datasetPointKey + ')[][][][]}');
                      }
                    } catch (error) {
                      copyDataIndex('${(' + props.datasetKey + '.' + record.datasetPointKey + ')[][][][]}');
                    }
                  }}>
                  {record.datasetPointKey}
                </Button>
              ),
              overlay: () => {
                return (
                  <Menu>
                    <MenuItem onClick={() => copyDataIndex(props.datasetKey + '.' + record.datasetPointKey)}>
                      {props.datasetKey + '.' + record.datasetPointKey}
                    </MenuItem>
                    <MenuItem onClick={() => copyDataIndex(',' + props.datasetKey + '.' + record.datasetPointKey)}>
                      {',' + props.datasetKey + '.' + record.datasetPointKey}
                    </MenuItem>
                  </Menu>
                );
              },
            }}
          </Dropdown>
        );
      },
    },
    {
      title: '',
      width: 30,
      dataIndex: 'delete',
      hideInTable: props.isView,
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
              emit('deleteClick', record);
              datasetPointList.value = datasetPointList.value.filter((item: any) => {
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
  ]);

  defineExpose({
    scrollTo,
  });
</script>

<style lang="less" scoped>
  :deep(.mes-table-wrapper .mes-table-tbody tr td) {
    height: 58px;
  }
  .set-height {
    height: 660px;
  }
</style>
