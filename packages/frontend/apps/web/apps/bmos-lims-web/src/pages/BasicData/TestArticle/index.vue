<!-- 检品管理 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :disabled="disabled"
      @watchEditInfo="watchEditInfo"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === InfoForm"
    :data="data"
    :disabled="disabled"
    @watchEditInfo="watchEditInfo"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref, shallowRef } from 'vue';
  import { Page, InfoForm } from './components';
  import { getCategoryInfo } from '@/services/index';
  import { message } from 'ant-design-vue';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const disabled = ref<boolean>(false);

  const watchEditInfo = async (row: any, flag: boolean) => {
    try {
      const res = await getCategoryInfo(row.id);
      data.value = res.data;
      disabled.value = flag;
      currentComponent.value = InfoForm;
    } catch (error) {
      message.error(error?.message);
    }
  };

  const backPage = () => {
    currentComponent.value = Page;
  };
</script>

<style scoped lang="less"></style>
