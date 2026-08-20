<!-- 报告生成 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :disabled="disabled"
      @openVerify="openVerify"
      @openGenerate="openGenerate"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === PleaseVerify || currentComponent === Generate"
    :data="data"
    :disabled="disabled"
    @openVerify="openVerify"
    @openGenerate="openGenerate"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref, shallowRef } from 'vue';
  import Page from './Page.vue';
  import { PleaseVerify } from '@/components/PleaseVerify';
  import { Generate } from '@/components/Generate';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const disabled = ref<boolean>(false);

  const openVerify = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    currentComponent.value = PleaseVerify;
  };

  const openGenerate = (row: any) => {
    data.value = row;
    currentComponent.value = Generate;
  };

  const backPage = () => {
    disabled.value = false;
    currentComponent.value = Page;
  };
</script>

<style scoped lang="less"></style>
