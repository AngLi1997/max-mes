<!-- 入库物料信息 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :titles="[t('入库物料信息')]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :scrolls="[{ x: 1600, y: 600 }]"
    :requests="[getIncomingMaterialList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="180050003000001" type="primary" @click="openModal({}, 'create')">
        {{ t('新增') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 新增/编辑弹框 -->
  <OperateModal ref="modalRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getIncomingMaterialList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { OperateModal } from './components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'IncomingMaterialInformation',
  });

  // 审核操作
  const modalRef = ref();

  const openModal = (data: any, type: 'create' | 'update') => {
    modalRef.value?.openModal(data, type);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openModal);

  const submitSuccess = () => {
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
