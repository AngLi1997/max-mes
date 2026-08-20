<!-- 物料库存管理 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="materialInstanceIdentify"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      rowSelections: [rowSelection],
      formProps: [formFirstProps],
      columns: [columnsFirst],
      tableFields: [
        {
          default: { warehouseArea, existsInventory },
        },
      ],
    }"
    :tableLoadApi="getMaterialInventoryPage"
    :expandLoadApi="getMaterialInventorySecondPage"
    :expandFields="(record: any) => ({ materialInstanceIdentify: record.materialInstanceIdentify })"
    :expandProps="{
      rowKeys: ['sampleNo'],
      search: [false],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      formProps: [formFirstProps],
      columns: [columnsExpand],
    }">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="warehouseArea" :options="materialWarehouseAreaDict" />
    </template>
    <template #tableHeaderToolbar0>
      <div class="title-flex">
        <Switch v-model:checked="existsInventory"></Switch>
        <span>{{ t('只看库存大于0的数据') }}</span>
      </div>
      <Button
        v-if="hasPermission('210060005000002') && warehouseArea === 'PASS'"
        type="primary"
        :disabled="!selectedRow.materialInstanceIdentify"
        @click="() => openMaterial(MaterialModelTypeEnum.RECEIVE, selectedRow)">
        {{ t('物料领用') }}
      </Button>
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
  <DetailModal ref="detailModalRef" />
  <StorageCard ref="storageCardRef" />
</template>

<script setup lang="ts">
  import { getMaterialInventoryPage, getMaterialInventorySecondPage } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Segmented, Switch } from 'ant-design-vue';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { MaterialModal, DetailModal, StorageCard } from './components';
  import { t } from '@bmos/i18n';
  import { useRowSelection } from '@/hooks';
  import { MaterialModelTypeEnum, MaterialWarehouseAreaEnum } from '@/types';
  import { usePermissionStore } from '@/stores';

  defineOptions({
    name: 'MaterialInventoryManagement',
    inheritAttrs: false,
  });

  const { materialWarehouseAreaDict } = getDicts();

  const { hasPermission } = usePermissionStore();

  const warehouseArea = ref<keyof typeof MaterialWarehouseAreaEnum>('PASS');

  const existsInventory = ref(true);

  const { selectedRow, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: true,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record.availableStock <= 0,
      };
    },
  });

  // 详情弹窗
  const detailModalRef = ref<InstanceType<typeof DetailModal>>();

  // 货位卡弹窗
  const storageCardRef = ref<InstanceType<typeof StorageCard>>();

  // 领用/抽检/退货/报废弹窗
  const materialModalRef = ref<InstanceType<typeof MaterialModal>>();
  const openMaterial = (type: MaterialModelTypeEnum, row: any) => {
    materialModalRef.value?.openModal(type, row);
  };

  const { pageExpendRef, columnsFirst, formFirstProps, columnsExpand, remarkModalOpen, remarkDetails } = useTable(
    (record: any) => {
      detailModalRef.value?.openModal(record);
    },
    (record: any) => {
      storageCardRef.value?.openModal(record);
    },
    openMaterial,
    warehouseArea,
  );
</script>

<style lang="less" scoped>
  .title-flex {
    display: flex;
    align-items: center;
    gap: 8px;
  }
</style>
