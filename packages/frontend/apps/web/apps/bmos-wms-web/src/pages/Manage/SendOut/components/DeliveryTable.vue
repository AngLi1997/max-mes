<template>
  <div class="delivery-materials">
    <BMDescriptions :column="2" :list="descData" />
    <div class="delivery-materials-table">
      <BMTable
        :columns="columns"
        :dataSource="productList"
        :rowSelection="{
          selectedRowKeys: selectionReactive.selectedRowKeys,
          onChange: rowSelectionChange,
        }"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 800, y: 300 }" />
    </div>
    <Row class="total">
      <Col :span="4">
        <span>
          {{ `${t('已选择')}：` }}
          <span class="unit">{{ `${selectionReactive.selectedRowKeys?.length || 0} ${t('件')}` }}</span>
        </span>
      </Col>
      <Col :span="20">
        <span>
          {{ `${t('总计')}：` }}
          <span class="unit">{{ `${selectionReactive.selectedNumber || 0}` }}{{ rowData?.unit }}</span>
        </span>
      </Col>
    </Row>
  </div>
</template>
<script lang="tsx" setup>
  import { reqWmsUnitCalcSumAdapt } from '@/services';
  import { BMDescriptions, BMTable, DescriptionsItemProps, TableColumn, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { debounce } from '@bmos/utils';

  const emit = defineEmits<{
    (e: 'update:selects', selects: string[], realQuantity: string): void;
  }>();

  const props = withDefaults(
    defineProps<{
      selects: string[];
      rowData?: any;
      productList?: any[];
    }>(),
    {
      rowData: () => [],
      productList: () => [],
    },
  );

  const descData = ref<DescriptionsItemProps[]>([
    {
      label: t('货品名称'),
      value: props.rowData?.cargoName,
    },
    {
      label: t('货品编码'),
      value: props.rowData?.cargoCode,
    },
    {
      label: t('货品批号'),
      value: props.rowData?.batchNo,
      vIf: () => !!props.rowData?.batchNo,
    },
    {
      label: t('计划量'),
      value: props.rowData?.targetQuantity + props.rowData?.unit,
    },
  ]);

  const columns: TableColumn[] = [
    {
      title: t('货品批号'),
      dataIndex: 'inventoryBatchNo',
      fixed: 'left',
      width: 150,
    },
    {
      title: t('货品件号'),
      dataIndex: 'inventoryNo',
      defaultSortOrder: 'ascend',
      sorter: (a: Recordable, b: Recordable) => a.inventoryNo - b.inventoryNo,
      width: 100,
    },
    {
      title: t('物料量'),
      dataIndex: 'quantity',
      width: 80,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 80,
    },
    {
      title: t('货位'),
      dataIndex: 'position',
      width: 150,
    },
    {
      title: t('有效期至'),
      dataIndex: 'expiredDate',
      width: 150,
    },
  ];

  const selectionReactive = reactive<{
    selectedRowKeys: string[];
    selectedAll: any[];
    selectedNumber: string;
  }>({
    selectedRowKeys: [],
    selectedAll: [],
    selectedNumber: '0',
  });

  const getUnitNumber = debounce(async (targetUnitId: string, selectedRows: any) => {
    try {
      const list: Array<{ unitId: string; value: string }> = selectedRows?.map((item: any) => {
        return {
          unitId: item.unitId,
          value: item.quantity,
        };
      });
      const { data } = await reqWmsUnitCalcSumAdapt(targetUnitId, list);
      selectionReactive.selectedNumber = data.value || '0';
      emit('update:selects', selectedRows, selectionReactive.selectedNumber);
    } catch (error) {}
  }, 500);

  const rowSelectionChange = (selectedRowKeys: any, selectedRows: any) => {
    selectionReactive.selectedRowKeys = selectedRowKeys;
    selectionReactive.selectedAll = selectedRows;
    getUnitNumber(props.rowData?.unitId, selectedRows);
  };
  let flag = ref<boolean>(true);
  watch(
    () => props.selects,
    val => {
      selectionReactive.selectedRowKeys = val as string[];
      if (val && val.length && flag.value) {
        register();
        flag.value = false;
      }
    },
    {
      immediate: true,
      deep: true,
    },
  );

  const register = () => {
    const rows = props.productList?.filter((item: any) => props.selects.includes(item.id));
    rows?.length && getUnitNumber(props.rowData?.unitId, rows);
  };
</script>
<style lang="less" scoped>
  .delivery-materials {
    .delivery-materials-table {
      height: 230px;
      .editable-cell {
        position: relative;
        .editable-cell-icon,
        .editable-cell-icon-check {
          position: absolute;
          right: 0;
          width: 20px;
          cursor: pointer;
        }
      }
    }
    .total {
      margin-top: 20px;
      background-color: var(--bmos-primary-color-background);
      border-radius: 4px;
      padding: 10px;
      .unit {
        color: var(--bmos-primary-color);
      }
    }
  }
</style>
