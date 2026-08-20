<template>
  <BMConfigProvider prefixCls="test">
    <a-tabs v-model:activeKey="activeKey" tab-position="left" animated>
      <a-tab-pane key="3" tab="组件">
        <a-card title="可搜索树">
          <SearchTree></SearchTree>
        </a-card>
        <a-card title="弹窗表单">
          <ModalForm></ModalForm>
        </a-card>
        <a-card title="表格">
          <BMDemoTable></BMDemoTable>
        </a-card>
        <a-card title="表单">
          <Form></Form>
        </a-card>
      </a-tab-pane>
      <a-tab-pane key="1" tab="基础组件样式">
        <Basic></Basic>
      </a-tab-pane>
      <a-tab-pane key="2" tab="基础扩展组件样式">
        <a-card>
          <a-row>
            <a-col :span="8">
              <Tree></Tree>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <Table></Table>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="4">
              <ModalDemo></ModalDemo>
            </a-col>
            <a-col :span="20">
              <FormDemo></FormDemo>
            </a-col>
          </a-row>
        </a-card>
      </a-tab-pane>
      
    </a-tabs>
  </BMConfigProvider>
</template>
<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import Form from './extension/formDemo.vue';
  import ModalForm from './modalForm/index.vue';
  import SearchTree from './searchTree/SearchTree.vue';
  import BMDemoTable from './table/table.vue';
  import Tree from './extension/tree.vue';
  import Table from './extension/table.vue';
  import ModalDemo from './extension/modal.vue';
  import FormDemo from './extension/formDemo.vue';
  import Basic from './basic/basic.vue';
  // 测试基础组件样式
  import { ref } from 'vue';
  // i18n
  import { t, changeLanguage, init } from '@bmos/i18n';

  init({
    lng: 'zh-CN',
  });
  console.log(t('key'));
  changeLanguage('en');
  console.log(t('key'));

  const activeKey = ref('3'); //标签页当前页
</script>

<style>
  /* 修改样式例子
   .test-btn.test-btn-round {
      background-color: blue;
    } */
</style>
