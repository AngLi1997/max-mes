<template>
  <div class="container">
    <Tabs v-if="!type" v-model:activeKey="activeKey" @change="typeChange">
      <TabPane v-if="treeSelectData?.materialCategoryType?.value != 2" key="1" :tab="t('消耗信息')"></TabPane>
      <TabPane v-if="treeSelectData?.materialCategoryType?.value != 0" key="2" :tab="t('产出信息')"></TabPane>
    </Tabs>
    <BMDescriptions :list="descData" :column="2" :showBottomBorder="false"></BMDescriptions>
    <div class="table_box">
      <BMTable
        ref="tableInstance"
        :columns="columns"
        :dataSource="tableData"
        :show-tool-bar="false"
        :show-search-border="false"
        row-key="id"
        :pagination="false"
        :search="false"
        :scroll="{ x: 800, y: 500 }"></BMTable>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMDescriptions, BMTable, TableColumn } from '@bmos/components';
  import { Tabs, TabPane } from 'ant-design-vue';
  const props = withDefaults(defineProps<{ type: string; treeSelectData: any }>(), { type: '' });

  const tableInstance = ref<any>(null);
  const activeKey = ref('1');
  const descData = ref<any>([
    {
      label: t('物料批号'),
      value: 'storageMaterialBatchNo',
      key: 'storageMaterialBatchNo',
    },
    {
      label: t('消耗总量'),
      value: 'consumeQuantity',
      key: 'consumeQuantity',
    },
  ]);
  const typeChange = () => {
    if (activeKey.value == '1') {
      descData.value[0] = {
        label: t('物料批号'),
        value: `${props.treeSelectData.storageMaterialBatchNo || ''}`,
        key: 'storageMaterialBatchNo',
      };
      descData.value[1] = {
        label: t('消耗总量'),
        value: `${props.treeSelectData.consumeQuantity || ''}${props.treeSelectData.unit || ''}`,
        key: 'consumeQuantity',
      };
      if (descData.value.length > 2) {
        descData.value.splice(-(descData.value.length - 2));
      }
      tableInstance.value.updateColumn([
        {
          dataIndex: 'batchNo', // 生产批号
          hideInTable: false,
        },
        {
          dataIndex: 'processName', // 工艺
          hideInTable: false,
        },
        {
          dataIndex: 'materialNo', // 物料件号
          hideInTable: false,
        },
      ]);
      tableData.value = props.treeSelectData.consumeList;
    } else {
      descData.value[0] = {
        label: props.treeSelectData.materialCategoryType.value == 1 ? t('物料批号') : t('成品批号'),
        value: `${props.treeSelectData.storageMaterialBatchNo || ''}`,
        key: 'storageMaterialBatchNo',
      };
      descData.value[1] = {
        label: t('产出总量'),
        value: `${props.treeSelectData.outputQuantity || ''}${props.treeSelectData.unit || ''}`,
        key: 'consumeQuantity',
      };
      tableInstance.value.updateColumn([
        {
          dataIndex: 'batchNo', // 生产批号
          hideInTable: true,
        },
        {
          dataIndex: 'processName', // 工艺
          hideInTable: true,
        },
        {
          dataIndex: 'materialNo', // 物料件号
          hideInTable: props.treeSelectData.materialCategoryType.value == 2,
        },
      ]);
      if (descData.value.length < 4) {
        descData.value.push(
          {
            label: t('产出生产批号'),
            value: props.treeSelectData.sourceBatchNo || '',
            key: 'sourceBatchNo',
          },
          {
            label: t('工艺名称'),
            value: props.treeSelectData.sourceProcessName || '',
            key: 'sourceProcessName',
          },
        );
      }
      tableData.value = props.treeSelectData.outputList;
    }
  };

  watch(
    () => props.treeSelectData,
    () => {
      descData.value = descData.value.map((item: any) => {
        item.value = props.treeSelectData[item.key];
        return item;
      });
      if (!props.type && props.treeSelectData.materialCategoryType.value == 2) {
        activeKey.value = '2';
      } else {
        activeKey.value = '1';
      }
      if (props.type == 'input') {
        activeKey.value = '1';
      } else if (props.type == 'out') {
        activeKey.value = '2';
      }
      typeChange();
    },
  );

  const tableData = ref([]);
  const columns: TableColumn[] = [
    {
      title: t('消耗生产批号'),
      dataIndex: 'batchNo',
      width: 160,
      hideInTable: false,
    },
    {
      title: t('工艺'),
      dataIndex: 'processName',
      width: 160,
      hideInTable: false,
    },
    {
      title: t('工序'),
      dataIndex: 'procedureName',
      width: 160,
      hideInTable: false,
    },
    {
      title: t('步骤/任务'),
      dataIndex: 'procedureStepName',
      width: 160,
      hideInTable: false,
    },
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
      width: 160,
      hideInTable: false,
    },
    {
      title: t('物料量'),
      dataIndex: 'quantity',
      width: 160,
      hideInTable: false,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 160,
      hideInTable: false,
    },
  ];
  onMounted(() => {
    if (props.type == 'input') {
      activeKey.value = '1';
    } else if (props.type == 'out') {
      activeKey.value = '2';
    }
    descData.value = descData.value.map((item: any) => {
      item.value = props.treeSelectData[item.key];
      return item;
    });
    if (!props.type && props.treeSelectData.materialCategoryType.value == 2) {
      activeKey.value = '2';
    } else if (!props.type) {
      activeKey.value = '1';
    }
    typeChange();
  });
</script>
<style lang="less">
  .container {
    height: 100%;
    .table_box {
      height: calc(100% - 130px);
    }
  }
</style>
