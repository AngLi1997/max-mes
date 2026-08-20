<!-- 详情 -->
<template>
  <DetailView
    :title="t('分拣任务详情')"
    :headerTitle="t('分拣任务详情')"
    :tagItem="{
      value: rowData.status?.value,
      key: rowData.status?.label,
      className: 'tag-1',
    }"
    :showStep="false"
    :cardItems="cardItems"
    @back="back"></DetailView>
</template>

<script setup lang="ts">
  import DetailView from '@/components/DetailView/index.vue';
  import { t } from '@bmos/i18n';
  import { useDetail } from './hooks/useDetail';

  const router = useRouter();
  const route = useRoute();

  const rowData = computed<any>(() => {
    return JSON.parse(route.query.rowData as string);
  });

  const back = () => {
    router.back();
  };

  const { cardItems } = useDetail(rowData.value);
</script>

<style scoped></style>
