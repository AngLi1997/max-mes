<!-- 报告审核 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :disabled="disabled"
      @openVerify="openVerify"
      @openAudit="openAudit"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === PleaseVerify || currentComponent === AuditCom"
    :data="data"
    :disabled="disabled"
    @openVerify="openVerify"
    @openAudit="openAudit"
    @back="backPage"></component>
</template>

<script setup lang="tsx">
  import { ref, shallowRef } from 'vue';
  import Page from './Page.vue';
  import { PleaseVerify } from '@/components/PleaseVerify';
  import { AuditCom } from '@/components/AuditCom';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const disabled = ref<boolean>(false);

  const openVerify = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    currentComponent.value = PleaseVerify;
  };

  const openAudit = (row: any) => {
    data.value = row;
    currentComponent.value = AuditCom;
  };

  const backPage = () => {
    disabled.value = false;
    currentComponent.value = Page;
  };
</script>

<style scoped lang="less"></style>
