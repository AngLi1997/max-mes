<template>
  <Layout :list="list"></Layout>
</template>

<script setup lang="ts">
  import { RouteRecordRaw } from 'vue-router';
  import { getAsyncMenu } from '../../utils';
  import Layout from '@/components/Layout';

  const list = ref<Array<RouteRecordRaw>>([]);
  onMounted(async () => {
    const res = await getAsyncMenu();
    list.value = res;
  });
</script>

<style scoped lang="less">
  @media print {
    @page {
      /* 纵向打印 */
      //size: portrait;
      /* 横向打印 */
      size: landscape;

      /* 去掉页眉页脚*/
      margin-top: 0;
      margin-bottom: 0;
    }
    /* 浏览器在渲染时不要对框进行颜色或样式调整 */
    * {
      -webkit-print-color-adjust: exact !important;
      -moz-print-color-adjust: exact !important;
      -ms-print-color-adjust: exact !important;
      print-color-adjust: exact !important;
    }

    .print-layout__view-entity {
      display: none !important;
    }
    .print-layout__print-entity {
      //display: block !important;

      visibility: visible !important;
      height: 100%;
    }
    textarea {
      border: none;
      border-bottom: 1px solid #000000 !important;
    }
  }
</style>
