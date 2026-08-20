<!-- 检验项目管理 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :type="modelType"
      @watchEditInfo="watchEditInfo"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === InfoForm"
    :data="data"
    :type="modelType"
    @watchEditInfo="watchEditInfo"
    @back="backPage"></component>
</template>

<script setup lang="tsx">
  import { ref, shallowRef } from 'vue';
  import { Page, InfoForm } from './components';
  import { getInspectionItemInfo } from '@/services/index';
  import { message } from 'ant-design-vue';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});

  const modelType = ref<string>('add');

  const watchEditInfo = async (row: any, flag: string) => {
    try {
      if (flag !== 'add') {
        const res = await getInspectionItemInfo(row.id);
        data.value = res.data;
      } else {
        data.value = row;
      }

      modelType.value = flag;
      currentComponent.value = InfoForm;
    } catch (error) {
      message.error(error?.message);
    }
  };

  const backPage = () => {
    currentComponent.value = Page;
  };
</script>

<style scoped lang="less">
  .parameter-config-table {
    padding: var(--bmos-padding-small);
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    .bmos-table {
      height: 100%;
    }
  }
</style>
./components/hooks
