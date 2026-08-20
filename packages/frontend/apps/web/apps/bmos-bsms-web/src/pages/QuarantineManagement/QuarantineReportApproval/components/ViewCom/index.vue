<!-- 详情 -->
<template>
  <DetailView title="检验单详情" :stepItems="stepItems"></DetailView>
</template>

<script setup lang="ts">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';

  const stepItems = ref([
    {
      title: `${t('申请人')}: 王跃桥`,
      description: '2024-05-07 15:00:00',
    },
    {
      title: `${t('审核人')}: 王跃桥`,
      description: '2024-05-14 15:00:00',
    },
  ]);
</script>

<style scoped></style>
