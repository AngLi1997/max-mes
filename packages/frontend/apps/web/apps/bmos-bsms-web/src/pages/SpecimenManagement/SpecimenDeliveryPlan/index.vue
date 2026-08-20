<!-- 标本出库计划 -->
<template>
  <component
    :is="currentComponent"
    @enterDetail="enterDetail"
    @enterEdit="enterEdit"
    :outPlanBatchNo="outPlanBatchNo"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, EditCom, ViewCom } from './components';

  defineOptions({
    name: 'specimen-delivery-plan',
    inheritAttrs: false,
  });

  // const ImportModalRef = ref();

  // const batchImport = () => {
  //   ImportModalRef.value.openModal();
  // };

  const currentComponent = shallowRef<any>(Page);

  const outPlanBatchNo = ref<any>(''); // 出库计划批次号

  // 进入详情页
  const enterDetail = (batchNo: any) => {
    outPlanBatchNo.value = batchNo;
    currentComponent.value = ViewCom;
  };

  // 进入编辑页
  const enterEdit = (batchNo: any) => {
    outPlanBatchNo.value = batchNo;
    currentComponent.value = EditCom;
  };

  // 回到page页
  const backPage = () => {
    outPlanBatchNo.value = '';
    currentComponent.value = Page;
  };
</script>

<style scoped></style>
