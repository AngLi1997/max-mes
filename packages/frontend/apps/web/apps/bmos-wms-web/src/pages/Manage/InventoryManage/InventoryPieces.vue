<!-- 货品件 -->
<template>
  <div class="inventory-pieces-manage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="returnInventoryManage">
            {{ t('货品管理') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t('货品件') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="returnInventoryManage">{{ t('返回') }}</Button>
        <Button type="primary" @click="addInventory">{{ t('新增货品件') }}</Button>
      </template>
      <BMPageComponent
        ref="pageRef"
        :rowKeys="['id']"
        :search="[true]"
        :hideRightTree="true"
        :showToolBars="[false]"
        :showHeader="[false]"
        :showSearchBorders="[false]"
        :formProps="[formFirstProps as Partial<FormProps>]"
        :requests="[getStorageCargoList as DataRequestFn]"
        :columns="[columnsFirst]">
        <template #tableTopHeaderTitle0>
          <BMTableTitle :title="t('货品库存')"></BMTableTitle>
        </template>
      </BMPageComponent>
    </BreadcrumbButton>
  </div>
  <AddInventoryModal v-model:open="addInventoryModalOpen" @updateTable="updateTable" />
</template>

<script lang="ts" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { reqStorageCargoPage } from '@/services';
  import { useInventoryPiecesTable } from './hooks';
  import { DataRequestFn, BMPageComponent, BMTableTitle, FormProps } from '@bmos/components';
  import AddInventoryModal from './components/AddInventoryModal.vue';

  const route = useRoute();
  const router = useRouter();
  const getStorageCargoList = async (params: any) => {
    const { id } = route.query;
    if (!id) return Promise.resolve({ data: [] });
    return await reqStorageCargoPage({
      ...params,
      inventoryBatchId: id,
    });
  };

  const { pageRef, updateTable, columnsFirst, formFirstProps } = useInventoryPiecesTable({});

  const returnInventoryManage = () => {
    // 返回货品管理
    router.push({
      name: 'InventoryManage',
    });
  };

  const addInventoryModalOpen = ref<boolean>(false);
  const addInventory = () => {
    addInventoryModalOpen.value = true;
  };
</script>

<style lang="less" scoped>
  .inventory-pieces-manage {
    width: 100%;
    height: 100%;
  }
  :deep(.content) {
    padding: 0;
  }
</style>
