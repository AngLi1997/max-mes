<!-- 实验包管理 -->
<template>
  <keep-alive>
    <Page
      v-if="currentComponent === Page"
      :data="data"
      :type="infoType"
      @watchEditInfo="watchEditInfo"
      @back="backPage"></Page>
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === InfoTable"
    :data="data"
    :type="infoType"
    @watchEditInfo="watchEditInfo"
    @back="backPage"></component>
</template>

<script setup lang="tsx">
  import { ref, shallowRef } from 'vue';
  import { Page, InfoTable } from './components';
  import { MODAL_STATUS } from './types';
  import { getExperimentalPackageInfo } from '@/services/index';
  import { message } from 'ant-design-vue';

  const currentComponent = shallowRef<any>(Page);

  const data = ref<any>({});
  const infoType = ref<MODAL_STATUS>();
  // 进入新增编辑查看页面
  const watchEditInfo = async (row: any, type: MODAL_STATUS) => {
    try {
      if (type !== MODAL_STATUS.ADD) {
        const res = await getExperimentalPackageInfo(row.id);
        data.value = res.data;
      } else {
        data.value = row;
      }
      infoType.value = type;
      currentComponent.value = InfoTable;
    } catch (error) {
      message.error(error?.message);
    }
  };

  // 返回page页
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
