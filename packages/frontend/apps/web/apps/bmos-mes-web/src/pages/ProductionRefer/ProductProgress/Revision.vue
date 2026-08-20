<!-- 修订记录 -->
<template>
  <Revision :title="t('生产进度')" returnRouteName="product-progress" :productPlanId="productPlanId" />
</template>

<script lang="tsx" setup>
  import Revision from '../components/Revision/index.vue';
  import { t } from '@bmos/i18n';

  const route = useRoute();
  const productPlanId = ref(route.query.productPlanId as string);
</script>
