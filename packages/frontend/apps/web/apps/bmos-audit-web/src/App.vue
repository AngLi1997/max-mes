<script setup lang="ts">
  import Layout from './components/Layout';
  import { BMConfigProvider } from '@bmos/components';
  import { onMounted } from 'vue';
  import { RouteRecordRaw } from 'vue-router';
  import { getAsyncMenu } from './utils/asyncMenu';
  import { getLang } from './utils/handleLang';
  import { ref } from 'vue';

  const list = ref<Array<RouteRecordRaw>>([]);
  onMounted(async () => {
    const res = await getAsyncMenu();
    list.value = res;
  });
</script>

<template>
  <BMConfigProvider prefixCls="audit" :lang="getLang()">
    <Layout :list="list"></Layout>
  </BMConfigProvider>
</template>

<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>
