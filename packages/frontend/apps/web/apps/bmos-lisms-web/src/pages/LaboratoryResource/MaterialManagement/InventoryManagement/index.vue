<!-- 领用库库存管理 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="storeNo"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      tableFields: [{ default: { zeroFlag, targetWarehouseId } }],
      rowSelections: [rowSelection],
      formProps: [formFirstProps],
      columns: [columnsFirst],
    }"
    :tableLoadApi="loadData"
    :expandLoadApi="getLaboratoryUseItemPage"
    :expandFields="(record: any) => ({ storeNo: record.storeNo })"
    :expandProps="{
      rowKeys: ['id'],
      search: [false],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      formProps: [formFirstProps],
      columns: [columnsExpand],
    }">
    <template #tableHeaderTitle0>
      <div class="title-flex">
        <Button
          v-hasAuth="210050001000001"
          :disabled="!selectedRow.storeNo"
          type="primary"
          @click="() => openMaterial('OUT_CONSUME', selectedRow)">
          {{ t('物料消耗') }}
        </Button>
        <Button
          v-hasAuth="210050001000002"
          :disabled="!selectedRow.storeNo"
          @click="() => openMaterial('OUT_SCRAP', selectedRow)">
          {{ t('物料报废') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <div class="title-flex">
        <Switch v-model:checked="zeroFlag" :checked-value="yesOrNoEnum.YES" :un-checked-value="yesOrNoEnum.NO"></Switch>
        <span>{{ t('只看库存大于0的数据') }}</span>
      </div>

      <Select v-model:value="targetWarehouseId" style="width: 250px" :options="targetWarehouseList" />
    </template>
  </PageExpandCom>
  <MaterialModal
    ref="materialModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getLaboratoryUsePage, getLaboratoryUseItemPage } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Switch } from 'ant-design-vue';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { MaterialModal } from './components';
  import { t } from '@bmos/i18n';
  import { useRowSelection } from '@/hooks';
  import { RecordSourceAuditTypeEnum, yesOrNoEnum } from '@/types';
  import { useDict } from '@/stores';

  defineOptions({
    name: 'InventoryManagement',
    inheritAttrs: false,
  });

  /**
   * @description: 加载列表数据
   * @return {Promise<void>}
   */
  const loadData = async (params: any) => {
    if (!params.zeroFlag || !params.targetWarehouseId) {
      return {
        data: {
          list: [],
          total: 0,
        },
      };
    }
    return await getLaboratoryUsePage(params);
  };

  const zeroFlag = ref<yesOrNoEnum>(yesOrNoEnum.YES);

  const { selectedRow, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: true,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  // 物料消耗/报废弹窗
  const materialModalRef = ref<InstanceType<typeof MaterialModal>>();
  const openMaterial = (type: RecordSourceAuditTypeEnum, row: any) => {
    materialModalRef.value?.openModal(type, row);
  };

  const { pageExpendRef, columnsFirst, formFirstProps, columnsExpand, remarkModalOpen, remarkDetails } = useTable();

  const targetWarehouseId = ref<string>('');

  const targetWarehouseList = ref<any[]>([]);

  const { getDict } = useDict();

  const initTargetWarehouseList = async () => {
    const data = await getDict('领用库');
    targetWarehouseList.value = data ?? [];
    targetWarehouseId.value = targetWarehouseList.value.length > 0 ? targetWarehouseList.value[0].value : '';
  };

  onBeforeMount(async () => {
    await initTargetWarehouseList();
  });

  onActivated(async () => {
    await initTargetWarehouseList();
  });
</script>

<style lang="less" scoped>
  .title-flex {
    display: flex;
    align-items: center;
    gap: 8px;
  }
</style>
