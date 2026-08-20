<template>
  <div class="transfer-materials">
    <BMDescriptions :column="2" :list="descData" />
    <div class="transfer-materials-table">
      <BMTable
        :columns="columns"
        :dataSource="dataSource"
        :rowSelection="{
          selectedRowKeys: selectionReactive.selectedRowKeys,
          onChange: rowSelectionChange,
        }"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 800, y: 280 }" />
    </div>

    <Row class="total">
      <Col :span="4">
        <span>
          {{ `${t('已选择')}：` }}
          <span :style="{ color: 'var(--bmos-primary-color)' }">
            {{ `${selectionReactive.selectedRowKeys.length} ${t('件')}` }}
          </span>
        </span>
      </Col>
      <Col :span="20">
        <span>
          {{ `${t('总计')}：` }}
          <span :style="{ color: 'var(--bmos-primary-color)' }">
            {{ `${selectionReactive.selectedNumber} ${props.currentNodes[0]?.unit}` }}
          </span>
        </span>
      </Col>
    </Row>
  </div>
</template>
<script lang="tsx" setup>
  import { BMDescriptions, BMTable, DescriptionsItemProps, TableColumn, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reqWmsUnitCalcSumAdapt } from '@/services';
  import { debounce } from '@bmos/utils';
  const emit = defineEmits<{
    (e: 'update:selects', selects: string[]): void;
  }>();

  const props = withDefaults(
    defineProps<{
      selects: string[];
      inventoryList?: any;
      isView?: boolean;
      currentNodes?: Recordable[number];
    }>(),
    {
      isView: false,
      inventoryList: () => [],
      currentNodes: () => [],
    },
  );

  const descData = ref<DescriptionsItemProps[]>([
    {
      label: t('货品编码'),
      value: props.currentNodes[0]?.mergeCode,
    },
    {
      label: t('货品名称'),
      value: props.currentNodes[0]?.cargoName,
    },
    {
      label: t('货品规格'),
      value: props.currentNodes[0]?.specification,
    },
    {
      label: t('货品批号'),
      value: props.currentNodes[0]?.inventoryBatchNo,
    },
  ]);

  const columns: TableColumn[] = [
    {
      title: t('货品件号'),
      dataIndex: 'inventoryNo',
      fixed: 'left',
      width: 100,
      resizable: true,
      defaultSortOrder: 'ascend',
      sorter: (a: Recordable, b: Recordable) => a.inventoryNo - b.inventoryNo,
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      width: 100,
      resizable: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      resizable: true,
    },
  ];

  const dataSource = ref<any[]>([]);

  const selectionReactive = reactive<{
    selectedRowKeys: string[];
    selectedNumber: number;
  }>({
    selectedRowKeys: [],
    selectedNumber: 0,
  });

  const getUnitNumber = debounce(async (targetUnitId: string, selectedRows: any) => {
    try {
      const list: Array<{ unitId: string; value: string }> = selectedRows?.map((item: any) => {
        return {
          unitId: item.unitId,
          value: item.availableQuantity,
        };
      });
      const { data } = await reqWmsUnitCalcSumAdapt(targetUnitId, list);
      selectionReactive.selectedNumber = data.value || '0';
    } catch (error) {
      console.error(error);
    }
  }, 300);

  const rowSelectionChange = (selectedRowKeys: any, selectedRows: any) => {
    selectionReactive.selectedRowKeys = selectedRowKeys;
    getUnitNumber(props.currentNodes[0]?.unitId, selectedRows);
    emit('update:selects', selectedRowKeys);
  };

  watch(
    () => props.selects,
    val => {
      dataSource.value = props.inventoryList;
      selectionReactive.selectedRowKeys = val as string[];
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>
<style lang="less" scoped>
  .transfer-materials {
    .transfer-materials-table {
      height: 220px;
    }
    .total {
      margin-top: 20px;
      background-color: var(--bmos-primary-color-background);
      border-radius: 4px;
      padding: 10px;
    }
  }
</style>
