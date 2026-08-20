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
       <!--  <a-card title="正常弹窗表单">
          <NormalModalForm></NormalModalForm>
          <BMDescriptions :list="[
            {label: '1', value: '1'},
            {label: '2', value: '2', span: 2},
            {label: '1', value: '1'},
          ]" />
        </a-card> -->
        <!-- <a-card title="表格"> -->
          <BMDemoTable></BMDemoTable>
        <!-- </a-card> -->
         <!--  <a-card title="表单">
          <Form></Form>
        </a-card>
       <a-card title="抽屉">
          <a-button type="primary" @click="open = true">Open</a-button>
          <a-drawer
            v-model:open="open"
            class="custom-class"
            root-class-name="root-class-name"
            :root-style="{ color: 'blue' }"
            style="color: red;"
            title="Basic Drawer"
            placement="right">
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
          </a-drawer>
        </a-card>
        <a-card title="pageComponent">
          <a-table style="margin-top: 8px;" :columns="columns" :data-source="[]" />
           <pageComponent></pageComponent>
        </a-card> -->
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
  import { BMConfigProvider, BMIcon, BMDescriptions } from '@bmos/components';
  import Form from './form/form.vue';
  import ModalForm from './modalForm/index.vue';
  import NormalModalForm from './normalModalForm/index.vue'
  import SearchTree from './searchTree/SearchTree.vue';
  import BMDemoTable from './table/table.vue';
  import Tree from './extension/tree.vue';
  import Table from './extension/table.vue';
  import ModalDemo from './extension/modal.vue';
  import FormDemo from './extension/formDemo.vue';
  import Basic from './basic/basic.vue';
  import pageComponent from './pageComponent/pageComponent.vue';
  // 测试基础组件样式
  import { ref } from 'vue';
  // i18n
  import { t, changeLanguage, init, I18nLanguageEnum } from '@bmos/i18n';

  init({
    lng: 'zh-CN',
  });
  const columns = [{ title: 'Name' }, { title: 'Age' }];
  console.log(t('key11111'));
  // changeLanguage('en');
  // console.log(t('key'));
  // changeLanguage('zh-CN');

  const activeKey = ref('3'); //标签页当前页

  const open = ref(false);
</script>

<style>
  /* 修改样式例子
   .test-btn.test-btn-round {
      background-color: blue;
    } */
    .test-tabs-tabpane {
      height: 400px;
      border: 1px solid #e8e8e8;
    }
</style>
