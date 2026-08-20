<!-- 检验录入 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :disabled="disabled"
      @openVerify="openVerify"
      @openInput="openInput"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent !== Page"
    :data="data"
    :disabled="disabled"
    @openVerify="openVerify"
    @openInput="openInput"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref, shallowRef } from 'vue';
  import Page from './Page.vue';
  import { PleaseVerify } from '@/components/PleaseVerify';
  import { InputCom } from '@/components/InputCom';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const disabled = ref<boolean>(false);

  const openVerify = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    currentComponent.value = PleaseVerify;
  };

  const openInput = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    currentComponent.value = InputCom;
  };

  const backPage = () => {
    disabled.value = false;
    currentComponent.value = Page;
  };
</script>

<style scoped lang="less"></style>
