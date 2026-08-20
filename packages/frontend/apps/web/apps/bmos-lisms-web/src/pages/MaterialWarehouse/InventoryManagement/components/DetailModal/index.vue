<!-- 详情弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('入库物料详情')"
    wrapClassName="modalSizeExtraLarge"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"
    @cancelModal="closeModal">
    <template #formBefore>
      <BMTable
        :search="false"
        :data-source="materialBaseInfo || []"
        :columns="materialBaseColumns"
        row-key="id"
        :headerTitle="t('物料基础信息')"
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"
        :pagination="false"></BMTable>
      <BMTable
        :search="false"
        :data-source="supplierBaseInfo || []"
        :columns="supplierBaseColumns"
        row-key="id"
        :headerTitle="t('供应商基础信息')"
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"
        :pagination="false"></BMTable>
      <BMTable
        :search="false"
        :data-source="materialInstanceDetail || []"
        :columns="materialBatchColumns"
        row-key="id"
        :headerTitle="t('物料批信息')"
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"
        :pagination="false"></BMTable>
      <BMTable
        ref="tableRef"
        :search="false"
        :data-request="loadData"
        :columns="columns"
        row-key="id"
        :headerTitle="t('入库记录')"
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"></BMTable>
    </template>
  </BMModalForm>
  <EditModal ref="editModalRef" @submitSuccess="openModal(rowData)" />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getMaterialInventoryDetail, getMaterialInventoryRecordPage } from '@/services';
  import { t } from '@bmos/i18n';
  import { useDes } from './hooks';
  import { BMModalForm, ModalFormInstance, BMTable } from '@bmos/components';
  import RemarkModal from '@/components/RemarkModal';
  import EditModal from './EditModal.vue';
  import { message } from 'ant-design-vue';

  const open = ref(false);

  const modalFormRef = ref<ModalFormInstance>();

  const rowData = ref<any>({});

  const materialBaseInfo = ref<any>([]);
  const supplierBaseInfo = ref<any>([]);
  const materialInstanceDetail = ref<any>([]);

  const loadData = async (params: any) => {
    const res = await getMaterialInventoryRecordPage({
      ...params,
      materialInstanceIdentify: rowData.value?.materialInstanceIdentify,
    });
    return res;
  };

  const openModal = async (row: any) => {
    try {
      rowData.value = { ...row };
      const { data } = await getMaterialInventoryDetail({
        materialInstanceIdentify: row?.materialInstanceIdentify,
        materialIdentify: row?.materialIdentify,
      });
      if (data) {
        materialBaseInfo.value = [data.materialBaseInfo];
        supplierBaseInfo.value = [data.supplierBaseInfo];
        materialInstanceDetail.value = [data.materialInstanceDetail];
      }
      open.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 编辑质控品含量
  const editModalRef = ref<InstanceType<typeof EditModal>>();
  const openEditModal = (row: any) => {
    editModalRef.value?.openModal(row);
  };

  const {
    materialBaseColumns,
    supplierBaseColumns,
    materialBatchColumns,
    tableRef,
    columns,
    remarkModalOpen,
    remarkDetails,
  } = useDes(openEditModal);

  const closeModal = () => {
    // open.value = false;
    // tableData.value = [];
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style scoped></style>
