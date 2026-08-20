<!-- 检验查询 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :disabled="disabled"
      @open="open"
      @openInput="openInput"
      @openAudit="openAudit"
      @openIssuance="openIssuance"
      @openGenerate="openGenerate"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="
      currentComponent === PleaseVerify ||
      currentComponent === InputCom ||
      currentComponent === AuditCom ||
      currentComponent === Issuance ||
      currentComponent === Generate ||
      currentComponent === Report
    "
    :data="data"
    :disabled="disabled"
    @open="open"
    @openInput="openInput"
    @openAudit="openAudit"
    @openIssuance="openIssuance"
    @openGenerate="openGenerate"
    @back="backPage"></component>
</template>

<script setup lang="tsx">
  import { ref, shallowRef } from 'vue';
  import { Page, Report } from './components';
  import { PleaseVerify } from '@/components/PleaseVerify';
  import { InputCom } from '@/components/InputCom';
  import { AuditCom } from '@/components/AuditCom';
  import { Issuance } from '@/components/Issuance';
  import { Generate } from '@/components/Generate';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const disabled = ref<boolean>(false);

  const open = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    if (flag) {
      currentComponent.value = PleaseVerify;
    } else {
      currentComponent.value = Report;
    }
  };

  // 生成
  const openGenerate = (row: any) => {
    data.value = row;
    currentComponent.value = Generate;
  };

  // 录入
  const openInput = (row: any, flag: boolean) => {
    data.value = row;
    disabled.value = flag;
    currentComponent.value = InputCom;
  };

  // 审核
  const openAudit = (row: any) => {
    data.value = row;
    currentComponent.value = AuditCom;
  };

  // 签发
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
