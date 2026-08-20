<!-- 分拣计划 -->
<template>
  <component
    :is="currentComponent"
    @enterDetail="enterDetail"
    @enterEdit="enterEdit"
    :planBatchNo="planBatchNo"
    :itemType="itemType"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, EditCom, ViewCom } from './components';

  defineOptions({
    name: 'SortingPlan',
    inheritAttrs: false,
  });

  const currentComponent = shallowRef<any>(Page);

  const planBatchNo = ref<any>('');
  const itemType = ref<any>(1);

  // 进入详情页
  const enterDetail = (batchNo: any, type: number) => {
    planBatchNo.value = batchNo;
    itemType.value = type;
    currentComponent.value = ViewCom;
  };

  // 进入编辑页
  const enterEdit = (batchNo: any, type: number) => {
    planBatchNo.value = batchNo;
    itemType.value = type;
    currentComponent.value = EditCom;
  };

  // 回到page页
  const backPage = () => {
    planBatchNo.value = '';
    currentComponent.value = Page;
  };
</script>

<style scoped></style>
