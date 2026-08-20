<template>
  <div class="out-materials">
    <BMDescriptions :column="2" :list="descData" />
    <div class="out-materials-table">
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
          <span class="numbers">{{ `${selectionReactive.selectedNumber}  ${props.currentNodes[0]?.unit}` }}</span>
        </span>
      </Col>
    </Row>
  </div>
</template>
<script lang="tsx" setup>
  import { postUnitCalcSumAdapt } from '@/services';
  import { BMDescriptions, BMTable, DescriptionsItemProps, TableColumn, Recordable } from '@bmos/components';
  import type { UnwrapRef } from 'vue';
  import { cloneDeep, debounce } from '@bmos/utils';
  import { t } from '@bmos/i18n';
  import { DataItem } from '../types';
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

  const dataSource = ref<any[]>(
    props.modalTableData.map((row: any, index: any) => {
      return { ...row, key: index };
    }),
  );

  const selectionReactive = reactive<{
    selectedRowKeys: string[];
    selectedAll: any[];
    selectedNumber: string;
  }>({
    selectedRowKeys: [],
    selectedAll: [],
    selectedNumber: '0',
  });
  const editableData: UnwrapRef<Record<string, DataItem>> = reactive({});

  const rowSelectionChange = (selectedRowKeys: any, selectedRows: any) => {
    selectedRowKeys.map((record: any) => {
      editableData[record] = cloneDeep(dataSource.value.filter(item => record === item.id)[0]);
    });
    selectionReactive.selectedRowKeys = selectedRowKeys;
    selectionReactive.selectedAll = selectedRows;
    amountTo();
    emit('update:selects', selectedRows);
  };
  //合计
  const amountTo = debounce(async () => {
    try {
      const dataModel = {
        list: selectionReactive.selectedAll.map((r: any) => {
          return { unitId: r.finalUnitId, value: r.quantity.toString() };
        }),
        targetUnitId: props.currentNodes[0]?.finalUnitId,
      };
      const res = await postUnitCalcSumAdapt(dataModel);
      selectionReactive.selectedNumber = res.data?.value;
    } catch (error) {
      console.error(error);
    }
  }, 800);
  watch(
    () => props.selects,
    val => {
      selectionReactive.selectedRowKeys = val as string[];
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>
<style lang="less" scoped>
  .out-materials {
    .out-materials-table {
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
    }
    .numbers {
      color: var(--bmos-primary-color);
    }
  }
</style>
