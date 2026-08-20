<template>
  <div class="container">
    <!-- 1.主页面 -->
    <div v-if="showMain" class="main">
      <div class="tabPane">
        <Tabs v-model:activeKey="activeKey" @change="tabChange">
          <TabPane v-for="item in tabList" :key="item.key" :tab="item.label"></TabPane>
        </Tabs>
      </div>
      <div class="treeTable">
        <TreeAndTable @look="look" @getCategoryCode="getCategoryCode"></TreeAndTable>
      </div>
      <!-- :nowTab="activeKey" 如需加监听tab变化-->
    </div>
    <!-- 2.查看跳转的页面 -->
    <div v-else class="detailPage">
      <LookDetail
        :rowData="rowData"
        :titles="titles"
        :categoryCode="categoryCode"
        :parentId="parentId"
        @back="back"></LookDetail>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive } from 'vue';
  import { Tabs, TabPane } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import TreeAndTable from './TreeAndTable/index.vue';
  import LookDetail from './LookDetail/index.vue';
  const showMain = ref(true); // 默认展示主页 还有个查看页
  const activeKey = ref('mes');
  const rowData = ref();
  const titles = reactive({
    //传给详情页的前两列表头名称
    title1: '',
    title2: '',
  });
  const categoryCode = ref();
  const parentId = ref();

  const tabList = ref([
    {
      key: 'mes',
      label: t('制造执行系统'),
    },
  ]);

  // tab切换
  const tabChange = async () => {
    // console.log(activeKey, 'activeKey');
  };
  // 查看按钮(切换页面)
  const look = async (val: any, title1: string, title2: string) => {
    showMain.value = false;
    rowData.value = val;
    titles.title1 = title1;
    titles.title2 = title2;
  };

  // 存树节点id(传给详情页) 第一个是当前树节点id,第二个是父级树节点id
  const getCategoryCode = async (val: any, val2: any) => {
    categoryCode.value = val;
    parentId.value = val2;
  };

  // 详情页的返回按钮
  const back = async () => {
    showMain.value = true;
  };
</script>

<style scoped lang="less">
  .container {
    background-color: #fff;
    height: 100%;
    width: 100%;
    padding: 16px 0px 0px 0px;
  }
  .main {
    height: 100%;
    width: 100%;
    .tabPane {
      padding-left: 16px;
      height: 40px;
    }
  }
  .treeTable {
    height: calc(100% - 40px);
  }
  .detailPage {
    padding-right: 16px;
  }
</style>
