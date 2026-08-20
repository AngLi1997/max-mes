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
          <span :style="{ color: 'var(--bmos-primary-color)' }">
            {{ `${selectionReactive.selectedRowKeys.length} ${t('件')}` }}
          </span>
        </span>
      </Col>
      <Col :span="20">
        <span>
          {{ `${t('总计')}：` }}
          <span :style="{ color: 'var(--bmos-primary-color)' }">
            {{ `${selectionReactive.selectedNumber}  ${props.currentNodes[0]?.unit}` }}
          </span>
        </span>
      </Col>
    </Row>
  </div>
</template>
<script lang="tsx" setup>
  import { BMDescriptions, BMTable, DescriptionsItemProps, TableColumn, Recordable } from '@bmos/components';
  import type { UnwrapRef } from 'vue';
  import { cloneDeep, debounce } from '@bmos/utils';
  import { InputNumber } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { DataItem } from '../types';
  import { accSub } from '../utils';
  import { reqWmsUnitCalcSumAdapt } from '@/services';
  const emit = defineEmits<{
    (e: 'update:selects', selects: string[]): void;
  }>();

  const props = withDefaults(
    defineProps<{
      selects: string[];
      inventoryList?: any;
      isView?: boolean;
      currentNodes?: Recordable[number];
      errorInput?: Recordable[];
    }>(),
    {
      isView: false,
      inventoryList: () => [],
      currentNodes: () => [],
      errorInput: () => [],
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
      title: t('出库量'),
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            {selectionReactive.selectedRowKeys.includes(record.id) ? (
              <InputNumber
                v-model:value={editableData[record.id].quantity}
                min={0}
                max={Number(record.availableQuantity)}
                stringMode={true}
                onBlur={() => changeEit(record.id)}
                status={
                  props.errorInput?.findIndex((item: any) => item.inventoryNo === record.inventoryNo) !== -1
                    ? 'error'
                    : ''
                }
              />
            ) : (
              ''
            )}
          </div>
        );
      },
    },
    {
      title: t('剩余量'),
      width: 100,
      resizable: true,
      customRender: ({ record }) => (
        <div>
          {selectionReactive.selectedRowKeys.includes(record.id)
            ? accSub(record.availableQuantity, record.quantity)
            : ''}
        </div>
      ),
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      resizable: true,
    },
  ];
  const dataSource = ref<any[]>(
    props.inventoryList?.map((row: any, index: any) => {
      return { ...row, key: index, quantity: row.availableQuantity };
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

  const getUnitNumber = debounce(async (targetUnitId: string, selectedRows: any) => {
    try {
      const list: Array<{ unitId: string; value: string }> = selectedRows?.map((item: any) => {
        const quantity = editableData[item.id]?.quantity || item.availableQuantity;
        return {
          unitId: item.unitId,
          value: quantity,
        };
      });
      const { data } = await reqWmsUnitCalcSumAdapt(targetUnitId, list);
      selectionReactive.selectedNumber = data.value || '0';
    } catch (error) {
      console.error(error);
    }
  }, 500);

  const rowSelectionChange = (selectedRowKeys: any, selectedRows: any) => {
    selectedRowKeys.map((record: any) => {
      editableData[record] = cloneDeep(dataSource.value.filter(item => record === item.id)[0]);
    });
    selectionReactive.selectedRowKeys = selectedRowKeys;
    selectionReactive.selectedAll = selectedRows;
    getUnitNumber(props.currentNodes[0]?.unitId, selectedRows);
    emit('update:selects', selectedRows);
  };
  //修改
  const changeEit = (id: string) => {
    const sum = editableData[id];
    if (editableData[id].quantity == '0') {
      sum.quantity = sum.availableQuantity;
    }
    Object.assign(dataSource.value.filter(item => id === item.id)[0], sum);
    Object.assign(selectionReactive.selectedAll.filter(item => id === item?.id)[0], sum);
    getUnitNumber(props.currentNodes[0]?.unitId, selectionReactive.selectedAll);
    emit('update:selects', selectionReactive.selectedAll);
  };
  watch(
    () => props.selects,
    val => {
      selectionReactive.selectedRowKeys = val.map((item: any) => item.id);
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
  }
</style>
