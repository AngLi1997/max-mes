<template>
  <div class="container">
    <!-- 1.主页面 -->
    <div class="">
      <Tabs v-model:activeKey="activeKey" @change="tabChange">
        <TabPane v-for="item in tabList" :key="item.key" :tab="item.label"></TabPane>
      </Tabs>
    </div>
    <div class="treeTable"><Table :activeKey="activeKey"></Table></div>
  </div>
</template>

<script setup lang="tsx">
  import { Tabs, TabPane } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import Table from './Table/index.vue';
  const activeKey = ref('PRODUCT_PLAN_BATCH_NO');

  const tabList = ref([
    {
      key: 'PRODUCT_PLAN_BATCH_NO',
      label: t('生产批号规则'),
    },
    {
      key: 'PRODUCT_PLAN_NO',
      label: t('指令单编号规则'),
    },
  ]);

  // tab切换
  const tabChange = async (val: any) => {
    activeKey.value = val;
  };
</script>

<style scoped lang="less">
  .container {
    background-color: #fff;
    height: 100%;
    width: 100%;
    padding: 16px 16px 0px 16px;
  }
  .main {
    height: 100%;
    width: 100%;
    .tabPane {
      padding-left: 0px;
    }
  }
  .tabPane {
    padding-left: 16px;
  }

  .treeTable {
    height: calc(100% - 56px);
  }
</style>
