<!-- 报告签发 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :disabled="disabled"
      @openVerify="openVerify"
      @openIssuance="openIssuance"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === Issuance || currentComponent === PleaseVerify"
    :data="data"
    :disabled="disabled"
    @openVerify="openVerify"
    @openIssuance="openIssuance"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref, shallowRef } from 'vue';
  import Page from './Page.vue';

  import { PleaseVerify } from '@/components/PleaseVerify';
  import { Issuance } from '@/components/Issuance';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const disabled = ref<boolean>(false);

  const openVerify = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    currentComponent.value = PleaseVerify;
  };

  const openIssuance = (row: any) => {
    data.value = row;
    currentComponent.value = Issuance;
  };

  const backPage = () => {
    disabled.value = false;
    currentComponent.value = Page;
  };
</script>

<style scoped lang="less"></style>
