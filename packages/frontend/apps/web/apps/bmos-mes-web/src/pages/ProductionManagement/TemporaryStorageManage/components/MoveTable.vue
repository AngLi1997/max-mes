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
        :scroll="{ x: 800, y: 300 }" />
    </div>
    <Row class="total">
      <Col :span="4">
        <span>
          {{ `${t('已选择')}：` }}
          <span class="numbers">{{ `${selectionReactive.selectedRowKeys.length} ${t('件')}` }}</span>
        </span>
      </Col>
      <Col :span="20">
        <span>
          {{ `${t('总计')}：` }}
          <span class="numbers">{{ `${selectionReactive.selectedNumber} ${props.currentNodes[0]?.unit}` }}</span>
        </span>
      </Col>
    </Row>
  </div>
</template>
<script lang="tsx" setup>
  import { postUnitCalcSumAdapt } from '@/services';
  import { BMDescriptions, BMTable, DescriptionsItemProps, TableColumn, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { debounce } from '@bmos/utils';
  const emit = defineEmits<{
    (e: 'update:selects', selects: string[]): void;
  }>();

  const props = withDefaults(
    defineProps<{
      selects: string[];
      modalTableData?: Recordable[];
      isView?: boolean;
      currentNodes?: Recordable;
    }>(),
    {
      isView: false,
      modalTableData: () => [],
      currentNodes: () => ({}),
    },
  );

  const descData = ref<DescriptionsItemProps[]>([
    {
      label: t('物料编码'),
      value: props.currentNodes[0]?.mergeCode,
    },
    {
      label: t('物料名称'),
      value: props.currentNodes[0]?.materialName,
    },
    {
      label: t('物料规格'),
      value: props.currentNodes[0]?.materialSpecification,
    },
    {
      label: t('物料批号'),
      value: props.currentNodes[0]?.materialBatchNo,
    },
  ]);

  const columns: TableColumn[] = [
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
      fixed: 'left',
      width: 100,
      resizable: true,
      defaultSortOrder: 'ascend',
      sorter: (a: Recordable, b: Recordable) => a.materialNo - b.materialNo,
    },
    {
      title: t('物料量'),
      dataIndex: 'quantity',
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

  const rowSelectionChange = debounce(async (selectedRowKeys: any, selectedRows: any) => {
    try {
      const dataModel = {
        list: selectedRows.map((r: any) => {
          return { unitId: r.finalUnitId, value: r.quantity };
        }),
        targetUnitId: props.currentNodes[0]?.finalUnitId,
      };
      const res = await postUnitCalcSumAdapt(dataModel);
      selectionReactive.selectedRowKeys = selectedRowKeys;
      selectionReactive.selectedNumber = res.data?.value;
      emit('update:selects', selectedRowKeys);
    } catch (error: any) {
      console.log(error);
    }
  }, 800);

  watch(
    () => props.selects,
    val => {
      dataSource.value = props.modalTableData;
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
      height: 240px;
    }
    .total {
      margin-top: 20px;
      background-color: var(--bmos-primary-color-background);
      border-radius: 4px;
      padding: 10px;
    }
    .numbers {
      color: var(--bmos-primary-color);
    }
  }
</style>
