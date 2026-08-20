<template>
  <Layout :list="list"></Layout>
</template>

<script setup lang="ts">
  import Layout from '../../components/Layout';
  import { RouteRecordRaw } from 'vue-router';
  import { getAsyncMenu } from '../../utils';

  const list = ref<Array<RouteRecordRaw>>([]);
  onMounted(async () => {
    const res = await getAsyncMenu();
    list.value = res;
  });
</script>
